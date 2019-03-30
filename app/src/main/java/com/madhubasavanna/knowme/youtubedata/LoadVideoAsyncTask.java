package com.madhubasavanna.knowme.youtubedata;

import android.os.AsyncTask;

import com.google.api.services.youtube.model.SearchResult;
import com.madhubasavanna.youtubedatalibrary.YouTubeDataSearch;

import java.util.List;

public class LoadVideoAsyncTask extends AsyncTask<String,Void, List<SearchResult>> {
    private List<SearchResult> videoListData;
    @Override
    protected List<SearchResult> doInBackground(String... searchquery) {
        switch(searchquery[0]){
            case "searchByKeyword": videoListData = YouTubeDataSearch.getSearchResults(searchquery[1]);
                        break;
            case "relatedVideos": videoListData = YouTubeDataSearch.getRelatedVideoResult(searchquery[1]);
                        break;
        }

        return videoListData;
    }

    @Override
    protected void onPostExecute(List<SearchResult> searchResults) {
        super.onPostExecute(searchResults);
    }
}
