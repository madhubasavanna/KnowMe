package com.madhubasavanna.knowme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;
import com.madhubasavanna.knowme.MainActivity;
import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.youtubedata.LoadVideoAsyncTask;
import com.madhubasavanna.knowme.youtubedata.VideoDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements VideoListAdapter.VideoClickListener, MainActivity.OnAboutDataReceivedListener {

    private RecyclerView videoListRecyclerView;
    private List<SearchResult> videoListData;
    private String searchText = null;
    public MainActivity mActivity;
    private List<Integer> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list_view, container, false);

        videoListRecyclerView = view.findViewById(R.id.video_list_recyclerview);
        mActivity = (MainActivity) getActivity();
        mActivity.setAboutDataListener(this);
        videoListData = getVideoList();
        //check weather data is obtained from youtube api or not, if not available set "no data retrieved message"
        if(videoListData != null){
            VideoListAdapter videoListAdapter = new VideoListAdapter(videoListData,this, getContext());
            videoListRecyclerView.setAdapter(videoListAdapter);
            videoListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else {
            videoListRecyclerView.setVisibility(View.GONE);
            TextView textView = view.findViewById(R.id.nodata);
            textView.setText("No data retrieved");
            textView.setVisibility(View.VISIBLE);
        }
        
        return view;
    }

    @Override
    public void onDataReceived(String searchtext) {
        this.searchText = searchtext;
    }

    private List<SearchResult> getVideoList() {
        LoadVideoAsyncTask task = new LoadVideoAsyncTask();
        task.execute("searchByKeyword",searchText);
        try{
            return task.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public void onVideoClick(VideoDetails videoDetails) {
        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
        intent.putExtra("videoId", videoDetails.getVideoId());
        startActivity(intent);
    }
}
