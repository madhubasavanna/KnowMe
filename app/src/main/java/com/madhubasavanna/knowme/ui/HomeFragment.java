package com.madhubasavanna.knowme.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.SearchResult;
import com.madhubasavanna.knowme.AppExecutors;
import com.madhubasavanna.knowme.MainActivity;
import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.userdata.KnowMeDatabase;
import com.madhubasavanna.knowme.userdata.UserPreferenceViewModel;
import com.madhubasavanna.knowme.userdata.UserPreferences;
import com.madhubasavanna.knowme.youtubedata.LoadVideoAsyncTask;
import com.madhubasavanna.knowme.youtubedata.VideoDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements VideoListAdapter.VideoClickListener {

    private RecyclerView videoListRecyclerView;
    private List<SearchResult> videoListData;
    public MainActivity mActivity;
    private UserPreferenceViewModel model;
    private List<Integer> list = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    int titlesDisplayedPosition = 0;
    boolean isScrolling = false;
    int currentItems;
    int totalItems;
    int scrolledOutItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list_view, container, false);

        videoListRecyclerView = view.findViewById(R.id.video_list_recyclerview);
        mActivity = (MainActivity) getActivity();
        videoListData = getVideoList();
        //check weather data is obtained from youtube api or not, if not available set "no data retrieved message"
        if (videoListData != null) {
            VideoListAdapter videoListAdapter = new VideoListAdapter(videoListData, this, getContext());
            videoListRecyclerView.setAdapter(videoListAdapter);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            videoListRecyclerView.setLayoutManager(manager);
            videoListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrolledOutItems = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();

                    if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                        isScrolling = false;
                        if (titles.size() != 0 && (titlesDisplayedPosition < titles.size())) {
                            LoadVideoAsyncTask loadVideoAsyncTask = new LoadVideoAsyncTask();
                            loadVideoAsyncTask.execute("searchByKeyword", titles.get(titlesDisplayedPosition));
                            titlesDisplayedPosition++;
                            try {
                                videoListData.addAll(loadVideoAsyncTask.get());
                                videoListAdapter.notifyDataSetChanged();
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            //Toast.makeText(getContext(),"No data", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {
            videoListRecyclerView.setVisibility(View.GONE);
            TextView textView = view.findViewById(R.id.nodata);
            textView.setText("No data retrieved");
            textView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public List<SearchResult> loadMoreData() {
        LoadVideoAsyncTask task = new LoadVideoAsyncTask();
        LoadVideoAsyncTask task1 = new LoadVideoAsyncTask();
        try {
            if (titles.size() != 0) {
                for (String a : titles) {
                    task.execute("searchByKeyword", a);
                    return task.get();
                }

            } else {
                task1.execute("searchByKeyword", "interviews of great personalities");
                return task1.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private List<SearchResult> getVideoList() {
        final Object LOCK = new Object();
        //synchronized (LOCK){
        GetTitlesAsyncTask titlesAsyncTask = new GetTitlesAsyncTask();
        titlesAsyncTask.execute();
        try {
            titles = titlesAsyncTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        LoadVideoAsyncTask task = new LoadVideoAsyncTask();
        if (titles.size() != 0) {
            task.execute("searchByKeyword", titles.get(titlesDisplayedPosition));
            titlesDisplayedPosition = (titlesDisplayedPosition != (titles.size() - 1)) ? ++titlesDisplayedPosition : 0;
        } else {
            task.execute("searchByKeyword", "interviews of great personalities");
        }

        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        //}
    }

    @Override
    public void onVideoClick(String videoId) {
        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
        intent.putExtra("videoId", videoId);
        startActivity(intent);
    }

    public class GetTitlesAsyncTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected void onPostExecute(List<String> list) {
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            KnowMeDatabase db = KnowMeDatabase.getInstance(getContext());
            List<UserPreferences> searchList = db.userPreferencesDao().loadPreferences();
            ArrayList<String> titles = new ArrayList<>();
            for (UserPreferences a : searchList) {
                titles.add(a.getTitle());
            }
            return titles;
        }
    }
}