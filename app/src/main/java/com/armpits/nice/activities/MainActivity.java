package com.armpits.nice.activities;

import android.os.Bundle;
import android.os.StrictMode;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.notifications.Notify;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

    public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // allow internet
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_module_settings, R.id.navigation_logs, R.id.navigation_global_settings).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // insert your download here
        // instantiate the database
        NiceDatabase db = NiceDatabase.getDatabase(this);

        // Should create notification channels as soon as app starts.
        // It's safe to call this repeatedly because creating an existing notification channel
        // performs no operation.
        Notify.createNotificationChannels(this);

        if (SharedPreferencesManager.get(Const.SP_UPDATE_FREQUENCY, this).equals(Const.SP_ERROR))
            SharedPreferencesManager.set(Const.SP_UPDATE_FREQUENCY, "hour", this);
    }
}
