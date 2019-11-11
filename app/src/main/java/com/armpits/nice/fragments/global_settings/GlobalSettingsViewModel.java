package com.armpits.nice.fragments.global_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GlobalSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GlobalSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Here you will see module-level settings");
    }

    public LiveData<String> getText() {
        return mText;
    }
}