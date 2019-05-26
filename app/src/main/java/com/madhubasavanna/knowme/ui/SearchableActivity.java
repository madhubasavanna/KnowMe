package com.madhubasavanna.knowme.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.SearchViewQueryTextEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.madhubasavanna.knowme.AppExecutors;
import com.madhubasavanna.knowme.R;
import com.madhubasavanna.knowme.userdata.KnowMeDatabase;
import com.madhubasavanna.knowme.userdata.UserPreferences;
import com.madhubasavanna.knowme.wikipediadata.WikiUserData;
import com.madhubasavanna.wikipediadatalibrary.ApiClient;
import com.madhubasavanna.wikipediadatalibrary.WikipediaDataSearch;
import com.madhubasavanna.wikipediadatalibrary.jsonimageurlclasses.DataNamingStrategy;
import com.madhubasavanna.wikipediadatalibrary.jsonimageurlclasses.ImageDetails;
import com.madhubasavanna.wikipediadatalibrary.jsonimageurlclasses.ImageUrlResponse;
import com.madhubasavanna.wikipediadatalibrary.jsonsearchclass.Search;
import com.madhubasavanna.wikipediadatalibrary.jsonsearchclass.SearchResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity implements WikiAdapter.WikiAdapterListener {
    private static final String TAG = SearchableActivity.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private WikiAdapter mAdapter;
    private List<WikiUserData> wikiUserList = new ArrayList<>();
    @BindView(R.id.suggestion_recyclerview)
    RecyclerView recyclerView;
//    @BindView(R.id.search_view)
//    SearchView searchView;
    @BindView(R.id.search_edit_text)
    EditText editText;
    Unbinder unbind;
    List<Search> searchList;
    WikipediaDataSearch.ApiService apiService;
    KnowMeDatabase mKnowMeDatabase;
    Bitmap thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        unbind = ButterKnife.bind(this);

        //searchView.setIconified(false);
        mKnowMeDatabase = KnowMeDatabase.getInstance(getApplicationContext());

        editText.setSelected(true);

        mAdapter = new WikiAdapter(this, wikiUserList, this);
        apiService = ApiClient.getClient().create(WikipediaDataSearch.ApiService.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        DisposableObserver<SearchResponse> disposableObserver = getSearchObserver();

        disposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle(new Function<String, Single<SearchResponse>>() {
                    @Override
                    public Single<SearchResponse> apply(String s) throws Exception {
                        Single<SearchResponse> searchResponse = apiService.getSearchList(s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());

                        return searchResponse;
                    }
                })
                .subscribeWith(disposableObserver));

        disposable.add(RxTextView.textChangeEvents(editText)
                .skipInitialValue()
                .filter(charSequence -> charSequence.text().length() > 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchTextWatcher()));

        disposable.add(disposableObserver);

    }

    private DisposableObserver<SearchResponse> getSearchObserver(){
        return new DisposableObserver<SearchResponse>(){
            @Override
            public void onNext(SearchResponse response) {
                SearchResponse list = response;
                searchList = list.getQuery().getSearch();
                List<WikiUserData> demoList = new ArrayList<>();
                for(Search a:searchList){
                    demoList.add(new WikiUserData(a.getTitle(), a.getPageid()));
                }
                wikiUserList.clear();
                wikiUserList.addAll(demoList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<SearchViewQueryTextEvent> searchPersonSearchWatcher() {
        return new DisposableObserver<SearchViewQueryTextEvent>() {
            @Override
            public void onNext(SearchViewQueryTextEvent searchViewQueryTextEvent) {
                Log.d(TAG, "Search query: " + searchViewQueryTextEvent.queryText().toString());
                publishSubject.onNext(searchViewQueryTextEvent.queryText().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    private DisposableObserver<TextViewTextChangeEvent> searchTextWatcher() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.d(TAG, "Search query: " + textViewTextChangeEvent.text());
                publishSubject.onNext(textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onWikiDataSelected(WikiUserData wikiUserData) {
        editText.setText(wikiUserData.getName());
    }

    @Override
    public void onFollowBtnClicked(WikiUserData wikiUserData) {
        Call<String> a =  WikipediaDataSearch.getService().getImageUrl(wikiUserData.getName());
        a.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Get the json response as string, then set the naming strategy, remove @jsonproperty annotation in projo file
                if (response.isSuccessful()) {
                    String responseString = response.body();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setPropertyNamingStrategy(new DataNamingStrategy(String.valueOf(wikiUserData.getPageId())));
                    ImageUrlResponse u = new ImageUrlResponse();
                    try {
                        u = mapper.readValue(responseString, ImageUrlResponse.class);
                    }catch (com.fasterxml.jackson.core.JsonParseException | com.fasterxml.jackson.databind.JsonMappingException e){}
                    catch (IOException e){}
                    //set the image url
                    wikiUserData.setImageUrl(u.getQuery().getPages().getImageDetails().getThumbnail().getSource());
                    //convert the bitmap image to byte and set userpreference to save in database
                    UserPreferences userPreferences = new UserPreferences(wikiUserData.getPageId()
                            ,wikiUserData.getName(),null,wikiUserData.getImageUrl());
                    //get the image from url
                    Picasso.with(getBaseContext()).load(userPreferences.getThumbnailUrl()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            //Load the thumbnail and store it in database as byte
                            userPreferences.setThumbnail(UserPreferences.convertBitmapTobyte(Bitmap.createBitmap(bitmap)));
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mKnowMeDatabase.userPreferencesDao().addPreference(userPreferences);
                                }
                            });
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.e(TAG, errorDrawable.toString());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) { }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        unbind.unbind();
        super.onDestroy();
    }
}
