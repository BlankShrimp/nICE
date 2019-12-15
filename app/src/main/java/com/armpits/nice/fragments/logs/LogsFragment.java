package com.armpits.nice.fragments.logs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Log;
import com.armpits.nice.models.Module;
import com.armpits.nice.ui.LogsAdapter;
import com.armpits.nice.ui.ModulesAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogsFragment extends Fragment {
    private View mContainer;
    private RecyclerView recyclerView;
    private LogsAdapter adapter;
    private LiveData<List<Log>> logsLiveData;
    private List<Log> logs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.fragment_logs, container, false);
        recyclerView = mContainer.findViewById(R.id.recycler_view_logs);

        logsLiveData = NiceDatabase.getAllLogs();
        logs = new ArrayList<>();
        adapter = new LogsAdapter(mContainer.getContext(), logs);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContainer.getContext()));
        recyclerView.setAdapter(adapter);

        logsLiveData.observe(this, updatedLogs -> {
            logs.clear();
            logs.addAll(updatedLogs);
            adapter.notifyDataSetChanged();
        });

        return mContainer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // add some logs if empty
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (logs.isEmpty())
                for (int i=0; i<100; i++)
                    NiceDatabase.insert(new Log(new Date(System.currentTimeMillis()-i*100000),
                            "log number "+i+": this is a sample message. For displaying data in lists, we use what is called a RecyclerView. It uses an Adapter to control the display of each item in the list by asking you to first create a view for each position, and then define a way to update these views for the data at each position."
                    .substring(0, (int) (200*Math.random()))));
        }).start();
    }
}