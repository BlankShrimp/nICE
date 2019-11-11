package com.armpits.nice.fragments.logs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LogsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Logs are raining like cats and dogs!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}