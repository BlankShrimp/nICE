package com.armpits.nice.activities;

import android.os.Bundle;

import com.armpits.nice.R;
import com.armpits.nice.db.DbHolder;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;
import com.armpits.nice.networking.Parser;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DbHolder {
    private NiceDatabase db;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_module_settings, R.id.navigation_logs, R.id.navigation_global_settings).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // instantiate the database and get the credentials
        db = Room.databaseBuilder(getApplicationContext(), NiceDatabase.class, "nice_db").build();
        username = SharedPreferencesManager.get(Const.SP_USERNAME, this);
        password = SharedPreferencesManager.get(Const.SP_PASSWORD, this);
    }

    @Override
    public List<Module> getAllModules() {
        // get modules from DB, but if these are empty then get them from online and save them to DB
        List<Module> modules = db.moduleDao().getAll().getValue();

        if (modules == null || modules.isEmpty()) {
            modules = new ArrayList<>();
            List<String[]> modulesOnline = Parser.getCoursesList(username, password);

            for (String[] moduleInfo : modulesOnline)
                modules.add(new Module(moduleInfo[0], moduleInfo[1], new Date(),
                false, false, false));

            db.moduleDao().insertAll(modules.toArray(new Module[]{}));
        }

        return modules;
    }

    @Override
    public List<Deadline> getAllMDeadlines() {
        return db.deadlineDao().getAll().getValue();
    }

    @Override
    public List<Material> getAllMaterial() {
        return db.materialDao().getAll().getValue();
    }
}
