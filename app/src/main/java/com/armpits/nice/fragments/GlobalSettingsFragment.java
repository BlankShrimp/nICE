package com.armpits.nice.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.armpits.nice.R;
import com.armpits.nice.models.Module;

import java.util.ArrayList;
import java.util.List;

public class GlobalSettingsFragment extends Fragment {
    private FragmentsViewModel viewModel;
    private List<Module> modules;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_global_settings, container, false);

        modules = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);
        viewModel.modules.observe(this, newModules -> {
            modules.clear();
            modules.addAll(newModules);
        });

        return root;
    }
}