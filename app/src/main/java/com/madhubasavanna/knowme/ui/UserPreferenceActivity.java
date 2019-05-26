package com.madhubasavanna.knowme.ui;

import android.os.AsyncTask;
import android.os.Bundle;

import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.userdata.KnowMeDatabase;
import com.madhubasavanna.knowme.userdata.UserPreferenceViewModel;
import com.madhubasavanna.knowme.userdata.UserPreferences;
import com.madhubasavanna.knowme.wikipediadata.WikiUserData;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPreferenceActivity extends AppCompatActivity implements UserPreferenceAdapter.UserPreferenceListener {
    @BindView(R.id.recycler_view_user_preference)
    RecyclerView recyclerView;
    List<WikiUserData> dataList = new ArrayList<>();
    UserPreferences removeData;
    UserPreferenceViewModel model;
    KnowMeDatabase db;
    UserPreferenceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle(R.string.preference_activity_title);
        db = KnowMeDatabase.getInstance(getBaseContext());
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //dataList.add(new WikiUserData("Empty","no url"));
        getPreferencesData();
        adapter = new UserPreferenceAdapter(this, dataList,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    public void getPreferencesData(){
        //new UserPreferenceAsyncTask().execute("load");
        model = ViewModelProviders.of(this).get(UserPreferenceViewModel.class);
        model.getPreferencesList().observe(this, listData->{
            List<WikiUserData> list = new ArrayList<>();
            for(UserPreferences a:listData){
                list.add(new WikiUserData(a.getTitle(),a.getThumbnailUrl(),a.getId(),a.convertByteToBitmap()));
            }
            adapter.setData(list);
        });
    }

    public class UserPreferenceAsyncTask extends AsyncTask<String,Void, List<WikiUserData>>{
        public UserPreferenceAsyncTask(){}
        @Override
        protected void onPostExecute(List<WikiUserData> userPreferences) {
            dataList.clear();
            getPreferencesData();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected List<WikiUserData> doInBackground(String... c) {
            switch (c[0]){
//                case "load":List<UserPreferences> preferenceList = db.userPreferencesDao().loadPreferences();
//                    List<WikiUserData> list = new ArrayList<>();
//                    for(UserPreferences a:preferenceList){
//                        list.add(new WikiUserData(a.getTitle(),a.getThumbnailUrl(),a.getId(),a.convertByteToBitmap()));
//                    }
//                    return list;
                case "remove": db.userPreferencesDao().removeFromPreference(removeData);
                return null;

                default: return null;
            }
        }
    }

    @Override
    public void OnUserPreferenceChange(UserPreferences data) {
        removeData = data;
        new UserPreferenceAsyncTask().execute("remove");
    }
}
