package com.example.foobar.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.AppDB;
import com.example.foobar.daos.PostDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.webApi.PostsAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.LinkedList;
import java.util.List;

public class UserPostsRepository {

    private PostDao postDao;
    private UserPostsAPI userPostsAPI;
    private LiveUserPosts userPostsLive;
    private String token;
    //private String username;

    public UserPostsRepository(Context context, String username) {
        AppDB db = AppDB.getInstance(context);
        postDao = db.postDao();
        userPostsLive = new LiveUserPosts(username);
        userPostsAPI = new UserPostsAPI(userPostsLive, postDao);

        //this.username = username;

        // Extract token from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
    }

    public LiveData<List<Post_Item>> getUserPosts() {
        Log.d("Repo", "Got user posts");
        return userPostsLive;

    }

    class LiveUserPosts extends MutableLiveData<List<Post_Item>> {
        private final String username;
        public LiveUserPosts(String username) {
            super();
            List<Post_Item> posts = new LinkedList<Post_Item>();
            setValue(posts);
            this.username=username;
        }

        @Override
        protected  void  onActive() { //extract the data from the local dbm and update live data
            super.onActive();
            new Thread(()->{
                userPostsLive.postValue(postDao.getUserPosts(username));
            }).start();
            token = "Bearer "+ token;
            Log.d("repo token", token);
            Log.d("repo user", username);
            userPostsAPI.getUserPosts(username, token);
            Log.d("msg", "Sent Request to get user posts");
        }
        public LiveData<List<Post_Item>> getAll() {
            return userPostsLive;
        }

    }}