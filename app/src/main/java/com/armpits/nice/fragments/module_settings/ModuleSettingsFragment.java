package com.armpits.nice.fragments.module_settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armpits.nice.R;
import com.armpits.nice.models.Module;
import com.armpits.nice.ui.ModulesAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModuleSettingsFragment extends Fragment {

    private ModuleSettingsViewModel moduleSettingsViewModel;
    private RecyclerView recyclerView;
    private ModulesAdapter adapter;
    private List<Module> modules;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moduleSettingsViewModel =
                ViewModelProviders.of(this).get(ModuleSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_module_settings, container, false);
        moduleSettingsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });

        recyclerView = root.findViewById(R.id.recycler_view);
        modules = generateSampleModules();
        adapter = new ModulesAdapter(root.getContext(), modules);

        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    private List<Module> generateSampleModules() {
        List<Module> modules = new ArrayList<>(5);

        modules.add(new Module(
                "Machine Learning", "CSE315", new Date(),
                true, false, true));
        modules.add(new Module(
                "Big Data Analytics", "CSE313", new Date(),
                false, false, true));
        modules.add(new Module(
                "Mobile Computing", "CSE311", new Date(),
                true, true, false));
        modules.add(new Module(
                "English for Academic Purposes", "EAP121", new Date(),
                true, false, true));
        modules.add(new Module(
                "Math Will Kill Me", "MTH101", new Date(),
                true, true, true));

        return modules;
    }
}