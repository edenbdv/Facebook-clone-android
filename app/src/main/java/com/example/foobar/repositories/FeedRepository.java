package com.example.foobar.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.AppDB;
import com.example.foobar.PermissionDeniedCallback;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.webApi.PostsAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.LinkedList;
import java.util.List;

public class FeedRepository {

    private String token;

    private  FeedDao feedDao;

    private PostDao postDao;

    private  PostListData postListData;  // is a mutable live data, that extended live data

    private PostsAPI postsAPI;

    private UserPostsAPI userPostsAPI;
    private SharedPreferences sharedPreferences;



    public FeedRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        AppDB db = AppDB.getInstance(context);
        feedDao = db.feedDao();
        postDao = db.postDao();
        postListData = new PostListData();
        postsAPI = new PostsAPI(postListData, feedDao);
        userPostsAPI = new UserPostsAPI(postListData, postDao);
    }




    public LiveData<List<Post_Item>> getAll() {
        return postListData;
    }
    // Call createPost method in FeedViewModel to handle creating the post

    public void  createPost(String username,String text,String picture, PermissionDeniedCallback callback) {
        String token = sharedPreferences.getString("token", "");
        String authToken = "Bearer "+ token;
        userPostsAPI.createPost(username, text, picture, authToken, callback);
    }



    public void  deletePost(int localId,String username, String postId) {
        String token = sharedPreferences.getString("token", "");
        String authToken = "Bearer "+ token;
        userPostsAPI.deletePost(localId, username, postId, authToken);
    }


    public void  updatePost(String username, String postId, String fieldName, String fieldValue , PermissionDeniedCallback callback) {
        String token = sharedPreferences.getString("token", "");
        String authToken = "Bearer "+ token;
        userPostsAPI.updatePost(username, postId, fieldName,fieldValue,authToken, callback);
    }

    public void reload() {
        // Call the appropriate method to fetch the latest posts from your data source
        String token = sharedPreferences.getString("token", "");
        String authToken = "Bearer "+ token;
        postsAPI.getPosts(authToken);
    }



    //inner class:
    class PostListData extends MutableLiveData<List<Post_Item>> {
        public PostListData() {
            super();
            List<Post_Item> posts = new LinkedList<Post_Item>();
            setValue(posts) ;
        }


        @Override
        protected  void  onActive() {
            super.onActive();
            new Thread(()->{

            }).start();

            String token = sharedPreferences.getString("token", "");
            postsAPI.getPosts( "Bearer "+ token); //for example if roey is logged in
        }





        public LiveData<List<Post_Item>> getAll() {
            return  postListData;
        }



    }
}
