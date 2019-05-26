package com.madhubasavanna.knowme.userdata;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserPreferences.class}, version = 1, exportSchema = false)
public abstract class KnowMeDatabase extends RoomDatabase {
    private static final String TAG = KnowMeDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "KnowMeDatabase";
    private static KnowMeDatabase sInstance;

    public static KnowMeDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(TAG,"Creating new database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        KnowMeDatabase.class, KnowMeDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG,"Getting the database instance");
        return sInstance;
    }

    public abstract UserPreferencesDao userPreferencesDao();
}
