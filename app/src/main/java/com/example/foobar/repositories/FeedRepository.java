package com.example.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.AppDB;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.webApi.PostsAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.LinkedList;
import java.util.List;

public class FeedRepository {

    private  FeedDao feedDao;

    private PostDao postDao;

    private  PostListData postListData;  // is a mutable live data, that extended live data

    private PostsAPI postsAPI;

    private UserPostsAPI userPostsAPI;


    public FeedRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        feedDao = db.feedDao();
        postDao = db.postDao();
        postListData = new PostListData();
        postsAPI = new PostsAPI(postListData, feedDao);
        userPostsAPI = new UserPostsAPI(postListData, postDao);
    }



    // upcasting postListData to LiveData

    public LiveData<List<Post_Item>> getAll() {
        return postListData;
    }
    // Call createPost method in FeedViewModel to handle creating the post

    public void  createPost(String username,String text,String picture) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userPostsAPI.createPost(username,text,picture,authToken);
    }


    public void  deletePost(int localId,String username, String postId) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userPostsAPI.deletePost(localId, username, postId, authToken);
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

                //after api updated room , extract the data from the local db and update live data
                //postListData.postValue(feedDao.getPostsFromFriends("Eden"));
                //postListData.postValue(feedDao.getPostsFromNonFriends("Eden"));
            }).start();

            String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
            postsAPI.getPosts( "Bearer "+ jwtTokenRoey); //for example if roey is logged in
        }


        public LiveData<List<Post_Item>> getAll() {
            return  postListData;
        }



    }
}
