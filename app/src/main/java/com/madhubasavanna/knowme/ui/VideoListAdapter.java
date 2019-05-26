package com.madhubasavanna.knowme.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.youtubedata.VideoDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.NumberViewHolder> {
    private Context context;
    private List<SearchResult> videoList;
    private VideoDetails videoDetails;
    private final VideoClickListener videoClickListener;

    public interface VideoClickListener{
        void onVideoClick(String  videoId);
    }

    VideoListAdapter(List<SearchResult> videoslist,VideoClickListener videoClickListener, Context context){
        this.videoList = videoslist;
        this.context = context;
        this.videoClickListener = videoClickListener;
        videoDetails = new VideoDetails();
    }
    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.video_item_view,parent,false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //YouTubePlayerView videolist;
        final TextView textView;
        ImageView image;
        NumberViewHolder(@NonNull View itemView) {
            super(itemView);
            //videolist = itemView.findViewById(R.id.youtube_player_view);
            image = itemView.findViewById(R.id.thumbnail);
            textView = itemView.findViewById(R.id.title);
        }

        void bind(int position){
            SearchResult searchResult = videoList.get(position);
            ResourceId resourceId = searchResult.getId();
            videoDetails.setVideoId(resourceId.getVideoId());
            videoDetails.setTitle(searchResult.getSnippet().getTitle());
            videoDetails.setThumbnail(searchResult.getSnippet().getThumbnails().getHigh().getUrl());

            textView.setText(videoDetails.getTitle());

            //loading thumbnail using picasso library
            Picasso.with(context).load(videoDetails.getThumbnail()).into(image);

            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ResourceId resourceId = videoList.get(getAdapterPosition()).getId();
            videoClickListener.onVideoClick(resourceId.getVideoId());
        }
    }
}
