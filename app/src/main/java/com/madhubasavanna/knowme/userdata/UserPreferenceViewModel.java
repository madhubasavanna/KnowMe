package com.madhubasavanna.knowme.userdata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

public class UserPreferenceViewModel extends AndroidViewModel {
    private MediatorLiveData<List<UserPreferences>> preferenceList;
    private LiveData<List<UserPreferences>> listLiveData;
    public UserPreferenceViewModel(@NonNull Application application){
        super(application);
        this.preferenceList = new MediatorLiveData<>();
    }

    public LiveData<List<UserPreferences>> getPreferencesList(){
        KnowMeDatabase db = KnowMeDatabase.getInstance(this.getApplication());
        listLiveData = db.userPreferencesDao().loadPreferencesLive();
        preferenceList.addSource(listLiveData, value -> {
            preferenceList.setValue(value);
        });
        return preferenceList;
    }
}
