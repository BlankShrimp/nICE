package com.armpits.nice.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Module;
import com.armpits.nice.networking.Parser;
import com.armpits.nice.ui.ModulesAdapter;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModuleSettingsFragment extends Fragment {
    private View mContainer;
    private RecyclerView recyclerView;
    private ModulesAdapter adapter;
    private LiveData<List<Module>> modulesLiveData;
    private List<Module> modules;

    private String username;
    private String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.fragment_module_settings, container, false);
        recyclerView = mContainer.findViewById(R.id.recycler_view_modules);

        modulesLiveData = NiceDatabase.getAllModules();
        modules = new ArrayList<>(); // keep it empty for now, load data on view created
        adapter = new ModulesAdapter(mContainer.getContext(), modules);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContainer.getContext()));
        recyclerView.setAdapter(adapter);

        modulesLiveData.observe(this, updatedModules -> {
            // cannot just reassign, otherwise the adapter will lose the point
            Log.d("FRAG", "new modules: " + updatedModules);
            modules.clear();
            modules.addAll(updatedModules);
            adapter.notifyDataSetChanged();
        });

        return mContainer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = SharedPreferencesManager.get(Const.SP_USERNAME, mContainer.getContext());
        password = SharedPreferencesManager.get(Const.SP_PASSWORD, mContainer.getContext());

        // if the DB is empty, download modules from ICE and save to DB
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (modules.isEmpty()) {
                Log.d("MODULES EMPTY", new Date().toString());
                List<String[]> onlineModules = Parser.getCoursesList(username, password);

                for (String[] moduleInfo : onlineModules)
                    NiceDatabase.insert(new Module(moduleInfo[0], moduleInfo[1], new Date(),
                            false, false, false));
            }
        }).start();
    }
}