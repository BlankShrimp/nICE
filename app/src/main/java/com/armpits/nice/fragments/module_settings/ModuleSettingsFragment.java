package com.armpits.nice.fragments.module_settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.armpits.nice.R;

public class ModuleSettingsFragment extends Fragment {

    private ModuleSettingsViewModel moduleSettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moduleSettingsViewModel =
                ViewModelProviders.of(this).get(ModuleSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_module_settings, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        moduleSettingsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}