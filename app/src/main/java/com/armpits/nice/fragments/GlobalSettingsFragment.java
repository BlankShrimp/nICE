package com.armpits.nice.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.armpits.nice.R;
import com.armpits.nice.activities.EntryPointActivity;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Module;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalSettingsFragment extends Fragment {
    private FragmentsViewModel viewModel;
    private List<Module> modules;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_global_settings, container, false);
        viewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);
        viewModel.modules.observe(this, newModules -> {
            modules.clear();
            modules.addAll(newModules);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Profile picture, force update button

        // EMAIL
        TextView txtEmail = root.findViewById(R.id.txtEmail);
        txtEmail.setText(SharedPreferencesManager.get(Const.SP_USERNAME, getContext()));

        // LAST UPDATE MESSAGE
        TextView txtLasUpdate = root.findViewById(R.id.txtLastUpdate);
        viewModel.logs.observe(this, newLogs -> {
            String lastUpdateMessage = "Last update:" + newLogs.get(0).date.toString().substring(0, 20);
            txtLasUpdate.setText(lastUpdateMessage);
        });

        // UPDATE FREQUENCY
        Spinner frequencySpinner = root.findViewById(R.id.spinUpdateFrequency);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, Const.UPDATE_FREQUENCIES);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        frequencySpinner.setAdapter(adapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferencesManager.set(
                        Const.SP_UPDATE_FREQUENCY, Const.UPDATE_FREQUENCIES.get(i),
                        getActivity());
                viewModel.addLog("Changed update frequency to " + Const.UPDATE_FREQUENCIES.get(i));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // LOGOUT
        Button btnLogout = root.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferencesManager.set(Const.SP_LOGGED_IN, "false", getActivity());
            viewModel.addLog(SharedPreferencesManager.get(Const.SP_USERNAME, getContext())+" logged out");
            startActivity(new Intent(getActivity(), EntryPointActivity.class));
        });
    }
}