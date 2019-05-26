package com.madhubasavanna.knowme.userdata;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserPreferencesDao {
    @Insert
    void addPreference(UserPreferences userPreferences);

    @Query("SELECT * FROM UserPreferences")
    List<UserPreferences> loadPreferences();

    @Query("SELECT * FROM UserPreferences")
    LiveData<List<UserPreferences>> loadPreferencesLive();

    @Query("SELECT * FROM UserPreferences WHERE id=:pid")
    LiveData<UserPreferences> loadPreferences(int pid);

    @Delete
    void removeFromPreference(UserPreferences userPreferences);

    @Query("DELETE FROM UserPreferences WHERE id=:pid;")
    void removeFromPreference(int pid);

}
