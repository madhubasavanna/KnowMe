package com.madhubasavanna.knowme.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.madhubasavanna.knowme.R;

import androidx.annotation.Nullable;

public class SearchResultsActivity   extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_view);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Intent intent1 = new Intent(this, VideoPlayActivity.class);
            intent.putExtra("videoId", "js");
            startActivity(intent);
        }
    }
}
