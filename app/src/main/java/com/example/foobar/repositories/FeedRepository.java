package com.example.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.AppDB;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.webApi.PostsAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.LinkedList;
import java.util.List;

public class FeedRepository {

    private  FeedDao feedDao;
    private  PostListData postListData;  // is a mutable live data, that extended live data
    private PostsAPI postsAPI;

    public FeedRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        feedDao = db.feedDao();
        postListData = new PostListData();
        postsAPI = new PostsAPI(postListData, feedDao);
    }

    // upcasting postListData to LiveData

    public LiveData<List<Post_Item>> getAll() {
        return postListData;
    }


    //inner class:
    class PostListData extends MutableLiveData<List<Post_Item>> {
        public PostListData() {
            super();
            List<Post_Item> posts = new LinkedList<Post_Item>();
//            posts.add(new Post_Item("im testing noga coda","wow.png","Eden",true));
//            posts.add(new Post_Item("make america great again","eagle.png","Tramp",false));

            setValue(posts) ;
        }


        @Override
        protected  void  onActive() { //extract the data from the local dbm and update live data
            super.onActive();
            new Thread(()->{
                postListData.postValue(feedDao.getPostsFromFriends("Eden"));
                postListData.postValue(feedDao.getPostsFromNonFriends("Eden"));
            }).start();


            String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA2MTA4NDEsImV4cCI6MTcxMDY5NzI0MX0.UafMDeAaOFAfGGbsfTA2ugWlEuLHB1Yqg1Z8yYeFoew";
            postsAPI.getPosts( "Bearer "+ jwtTokenRoey); //for example if roey is logged in
        }


        public LiveData<List<Post_Item>> getAll() {
            return  postListData;
        }

    }
}