package com.example.foobar.webApi;

import android.util.Log;

import com.example.foobar.entities.Post_Item;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostsAPI {

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public PostsAPI() {

        retrofit = new Retrofit.Builder()
                //.baseUrl(MyApplication.context.getString(R.string.BaseUrl))  //we need to change it later to be save in R string
                .baseUrl("http://192.168.0.103:12345/api/")  //we need to change it later to be save in R string

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getPosts(String authToken) {
        Call<List<Post_Item>> call = webServiceAPI.getPosts(authToken);
        call.enqueue(new Callback<List<Post_Item>>() {
            @Override
            public void onResponse(Call<List<Post_Item>> call, Response<List<Post_Item>> response) {
                if (response.isSuccessful()) {
                    List<Post_Item> posts = response.body();
                    if (posts != null && !posts.isEmpty()) {
                        Log.d("PostsAPI", "Feed of user");  //can see content in debug, not in log!!!!
                        for (Post_Item post : posts) {
                            // Process each post
                            Log.d("PostAPI", post.toString());
                        }
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Log.d("PostsAPI", "Failed to show posts. Response code: " + response.code() + ", Error message: " + errorMessage);
                        } catch (IOException e) {
                            Log.e("PostsAPI", "Error reading error message: " + e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post_Item>> call, Throwable t) {
                Log.e("PostsAPI", "Failed to get posts: " + t.getMessage());
            }
        });
    }
}
