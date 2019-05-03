package com.madhubasavanna.knowme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;
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

public class LatestFragment extends Fragment implements VideoListAdapter.VideoClickListener {

    private RecyclerView videoListRecyclerView;
    private List<SearchResult> videoListData;
    private List<Integer> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list_view, container, false);

        videoListRecyclerView = view.findViewById(R.id.video_list_recyclerview);
        videoListData = getVideolist();
        if(videoListData != null){
            VideoListAdapter videoListAdapter = new VideoListAdapter(videoListData,this, getContext());
            videoListRecyclerView.setAdapter(videoListAdapter);
            videoListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }else {
            videoListRecyclerView.setVisibility(View.GONE);
            TextView textView = view.findViewById(R.id.nodata);
            textView.setText("No data retrived");
            textView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private List<SearchResult> getVideolist() {
        LoadVideoAsyncTask task = new LoadVideoAsyncTask();
        task.execute("searchByKeyword","latest celebrity interviews");
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
