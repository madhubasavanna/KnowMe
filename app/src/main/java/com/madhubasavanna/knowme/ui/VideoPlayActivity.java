package com.madhubasavanna.knowme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.model.SearchResult;
import com.madhubasavanna.knowme.MainActivity;
import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.youtubedata.LoadVideoAsyncTask;
import com.madhubasavanna.knowme.youtubedata.VideoDetails;
import com.madhubasavanna.youtubedatalibrary.Authentication.ApiKeys;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoPlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, VideoListAdapter.VideoClickListener {
    Button button;
    YouTubePlayerView youTubePlayerView;
    String videoId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_screen);

        recyclerView = findViewById(R.id.related_video_recycler_view);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youTubePlayerView.initialize(ApiKeys.youTubeApiKey, this);
        videoId = getIntent().getStringExtra("videoId");

        recyclerView.setAdapter(new VideoListAdapter(getRelatedVideoList(), this, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<SearchResult> getRelatedVideoList() {
        LoadVideoAsyncTask task = new LoadVideoAsyncTask();
        task.execute("relatedVideos",videoId);
        try{
            return task.get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public void onVideoClick(String videoId) {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra("videoId", videoId);
        startActivity(intent);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        if(!b){
            youTubePlayer.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
