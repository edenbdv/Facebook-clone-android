package com.example.foobar.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.repositories.FeedRepository;

import java.util.List;

public class FeedViewModel extends ViewModel {

   private LiveData<List<Post_Item>> posts;

    private FeedRepository feedRepository;

    public FeedViewModel() {

    }

    public void initRepo(Context context) {
        feedRepository = new FeedRepository(context);
        // Initialize LiveData from repository
        posts = feedRepository.getAll();

    }

    // Method to set post data
//    public void setPosts(List<Post_Item> posts) {
//        postsLiveData.setValue(posts);
//    }


    // Expose LiveData to observe posts
    public LiveData<List<Post_Item>> getPostsLiveData() {
        return posts;
    }

    // Method to fetch posts from the repository
//    public LiveData<List<Post_Item>> getPosts(String username) {
//        return feedRepository.getPosts(username);
//    }




}
