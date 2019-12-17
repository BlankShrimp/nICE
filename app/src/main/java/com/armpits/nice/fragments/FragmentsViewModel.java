package com.armpits.nice.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Log;
import com.armpits.nice.models.Module;

import java.util.Date;
import java.util.List;


public class FragmentsViewModel extends ViewModel {
    LiveData<List<Module>> modules;
    LiveData<List<Deadline>> deadlines;
    LiveData<List<Log>> logs;

    public FragmentsViewModel() {
        modules = NiceDatabase.getAllModules();
        deadlines = NiceDatabase.getAllDeadlines();
        logs = NiceDatabase.getAllLogs();
    }

    void addLog(String message) {
        NiceDatabase.insert(new Log(new Date(), message));
    }
}