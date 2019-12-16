package com.armpits.nice.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Log;
import com.armpits.nice.models.Module;

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

//    private MediatorLiveData<List<Log>> logsMediator = new MediatorLiveData<>();
//    private LiveData<List<Log>> logsLiveData;
//    public Observable<List<Log>> logsList;
//
//    public FragmentsViewModel() {
//        logsMediator.addSource(logsLiveData, logsList -> logsMediator.setValue(logsList));
//    }
//
//    public void startLoading(LifecycleOwner owner) {
//        logsMediator.observe(owner, logsList -> {
//            logsList.set(logsList)
//        });
//    }
}