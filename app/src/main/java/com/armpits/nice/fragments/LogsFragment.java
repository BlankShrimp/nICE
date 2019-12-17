package com.armpits.nice.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Log;
import com.armpits.nice.ui.LogsAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogsFragment extends Fragment {
    private View mContainer;

    private FragmentsViewModel viewModel;
    private LogsAdapter adapter;
    private List<Log> logs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(FragmentsViewModel.class);
        mContainer = inflater.inflate(R.layout.fragment_logs, container, false);
        RecyclerView recyclerView = mContainer.findViewById(R.id.recycler_view_logs);

        logs = new ArrayList<>();
        adapter = new LogsAdapter(mContainer.getContext(), logs);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContainer.getContext()));
        recyclerView.setAdapter(adapter);

        viewModel.logs.observe(this, updatedLogs -> {
            logs.clear();
            logs.addAll(updatedLogs);
            adapter.notifyDataSetChanged();
        });

        return mContainer;
    }
}