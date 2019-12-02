package com.armpits.nice.fragments.module_settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import java.util.Objects;

public class ModuleSettingsFragment extends Fragment {
    private View mContainer;
    private RecyclerView recyclerView;
    private ModulesAdapter adapter;
    private List<Module> modules;

    private String username;
    private String password;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.fragment_module_settings, container, false);
        recyclerView = mContainer.findViewById(R.id.recycler_view);

        modules = new ArrayList<>();    // keep it empty for now, load data on attach is created
        adapter = new ModulesAdapter(mContainer.getContext(), modules);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContainer.getContext()));
        recyclerView.setAdapter(adapter);

        return mContainer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = SharedPreferencesManager.get(Const.SP_USERNAME, mContainer.getContext());
        password = SharedPreferencesManager.get(Const.SP_PASSWORD, mContainer.getContext());

        // get the modules from the DB or from online
        List<Module> localModules = NiceDatabase.getAllModules();
        if (localModules == null)
            new Thread(() -> {
                List<String[]> onlineModules = Parser.getCoursesList(username, password);

                for (String[] moduleInfo : onlineModules)
                    modules.add(new Module(moduleInfo[0], moduleInfo[1], new Date(),
                            false, false, false));

                // the update of the data of the recyclerview must be done from the UI thread
                Objects.requireNonNull(getActivity())
                        .runOnUiThread(() -> adapter.notifyDataSetChanged());
            }).start();

        else {
            modules.addAll(localModules);
            adapter.notifyDataSetChanged();
        }
    }
}