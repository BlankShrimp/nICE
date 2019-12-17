package com.armpits.nice.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Module;
import com.armpits.nice.networking.Parser;
import com.armpits.nice.service.ScheduleService;
import com.armpits.nice.ui.ModulesAdapter;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModuleSettingsFragment extends Fragment {
    private View mContainer;

    private FragmentsViewModel viewModel;
    private ModulesAdapter adapter;
    private List<Module> modules;

    private String username;
    private String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);
        mContainer = inflater.inflate(R.layout.fragment_module_settings, container, false);
        RecyclerView recyclerView = mContainer.findViewById(R.id.recycler_view_modules);

        modules = new ArrayList<>(); // keep it empty for now, load data on view created
        adapter = new ModulesAdapter(mContainer.getContext(), modules);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContainer.getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.modules.observe(this, updatedModules -> {
            // cannot just reassign, otherwise the adapter will lose the point
            modules.clear();
            modules.addAll(updatedModules);
            adapter.notifyDataSetChanged();
        });

        Intent intent = new Intent(mContainer.getContext(), ScheduleService.class);
        getActivity().startService(intent);
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
            } catch (InterruptedException ignore) {}

            if (modules.isEmpty()) {
                List<String[]> onlineModules = Parser.getCoursesList(username, password);

                for (String[] moduleInfo : onlineModules) {
                    NiceDatabase.insert(new Module(moduleInfo[0], moduleInfo[1], new Date(),
                            false, false, false, false));
                    viewModel.addLog("New module found on ICE: " + moduleInfo[0]);
                }
            }
        }).start();
    }
}