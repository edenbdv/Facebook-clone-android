package com.example.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.AppDB;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.entities.Post_Item;

import java.util.LinkedList;
import java.util.List;

public class FeedRepository {

    private final FeedDao feedDao;
    private final PostListData postListData;

    public FeedRepository(Context context) {
        AppDB Userdatabase = AppDB.getInstance(context);
        feedDao = Userdatabase.feedDao();
        postListData = new PostListData();
    }

    public LiveData<List<Post_Item>> getAll() {
        return postListData;
    }

    public LiveData<List<Post_Item>> getPosts(String username) {
        fetchPosts(username);
        return postListData;
    }

    private void fetchPosts(String username) {
        new Thread(() -> {
            // Fetch posts from the database using FeedDao
            List<Post_Item> friendPosts = feedDao.getPostsFromFriends(username);
            List<Post_Item> nonFriendPosts = feedDao.getPostsFromNonFriends(username);

            // Combine the two lists
            List<Post_Item> allPosts = new LinkedList<>();
            allPosts.addAll(friendPosts);
            allPosts.addAll(nonFriendPosts);

            // Update LiveData with the fetched posts
            postListData.postValue(allPosts);
        }).start();
    }

    class PostListData extends MutableLiveData<List<Post_Item>> {
        public PostListData() {
            super();
        }
    }
}
