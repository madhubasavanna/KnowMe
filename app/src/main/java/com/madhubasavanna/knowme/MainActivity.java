package com.madhubasavanna.knowme;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubePlayerView;
import com.madhubasavanna.knowme.ui.SearchResultsActivity;
import com.madhubasavanna.knowme.ui.UserProfileActivity;
import com.madhubasavanna.knowme.ui.ViewPagerAdapter;
import com.madhubasavanna.wikipediadatalibrary.WikipediaDataSearch;
import com.madhubasavanna.wikipediadatalibrary.jsonsearchclass.Query;
import com.madhubasavanna.wikipediadatalibrary.jsonsearchclass.Search;
import com.madhubasavanna.wikipediadatalibrary.jsonsearchclass.SearchResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    YouTubePlayerView videoPlayer;
    SearchView searchView;
    List<Search> searchList;
    String searchText = "Bill Gates";

    private OnAboutDataReceivedListener mAboutDataListener;

    public interface OnAboutDataReceivedListener {
        void onDataReceived(String searchtext);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorPrimaryLight)
                , getResources().getColor(android.R.color.white));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        viewPager.setAdapter(viewPagerAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        break;
                    case R.id.action_preference:
                        getData();
                        Toast.makeText(MainActivity.this, "Preference", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_profile:
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        viewPager.setAdapter(viewPagerAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAboutDataListener.onDataReceived(searchText);
                getData();
                searchText = searchList.get(0).getTitle();
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void getData(){
        Call<SearchResponse> searchListCall = WikipediaDataSearch.getService().getSearchList(searchText);
        searchListCall.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse list = response.body();
                searchList = list.getQuery().getSearch();
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable throwable) {

            }
        });
    }
}
