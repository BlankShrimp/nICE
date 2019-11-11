package com.armpits.nice.fragments.module_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ModuleSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ModuleSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Can change global settings and see your ugly face here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}