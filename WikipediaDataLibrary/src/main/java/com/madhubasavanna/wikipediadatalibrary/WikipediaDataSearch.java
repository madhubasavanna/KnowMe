package com.madhubasavanna.wikipediadatalibrary;

import com.madhubasavanna.wikipediadatalibrary.jsonimageurlclasses.ImageUrlResponse;
import com.madhubasavanna.wikipediadatalibrary.jsonpagecontentclasses.ContentResponse;
import com.madhubasavanna.wikipediadatalibrary.jsonsearchclass.SearchResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WikipediaDataSearch {

    private static final String url = "https://en.wikipedia.org/w/";
    private static final String contentUrl = "action=query&format=json&prop=extracts&exintro&explaintext&redirects=1";
    private static final String imageUrl = "action=query&prop=pageimages&format=json&pithumbsize=300";
    private static final String searchList = "action=query&list=search&utf=8&format=json";

    public static PostService postService = null;

    public static PostService getService(){
        if (postService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

    public interface PostService{
        @GET("api.php?" + searchList)
        Call<SearchResponse> getSearchList(@Query("srsearch") String name);

        @GET("api.php?" + imageUrl)
        Call<String> getImageUrl(@Query("titles") String title);

        @GET("api.php?" + contentUrl)
        Call<ContentResponse> getContent(@Query("titles") String title);
    }

    public interface ApiService{
        @GET("api.php?" + searchList)
        Single<SearchResponse> getSearchList(@Query("srsearch") String name);

        @GET("api.php?" + imageUrl)
        Call<String> getImageUrl(@Query("titles") String title);
    }
}
