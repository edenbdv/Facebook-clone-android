package com.example.foobar.repositories;

import android.content.Context;
import android.content.SharedPreferences;

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

    private String token;
    private FeedDao feedDao;
    private PostDao postDao;
    private PostListData postListData;  // is a mutable live data, that extended live data
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

    // upcasting postListData to LiveData

    public LiveData<List<Post_Item>> getAll() {
        return postListData;
    }

    public void  createPost(String username,String text,String picture) {


        //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ token; //for example if roey is logged in
        userPostsAPI.createPost(username,text,picture,authToken);

        // need to add dao !!!!!!!!!!!!! (in api..)
    }

    public void  deletePost(int localId,String username, String postId) {
        //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ token; //for example if roey is logged in
        userPostsAPI.deletePost(localId, username, postId, authToken);
    }


    public void  updatePost(String username, String postId, String fieldName, String fieldValue) {
        //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ token; //for example if roey is logged in
        userPostsAPI.updatePost(username, postId, fieldName,fieldValue,authToken);
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
                //postListData.postValue(feedDao.getPostsFromFriends("Eden"));
                //postListData.postValue(feedDao.getPostsFromNonFriends("Eden"));
            }).start();

            String token = sharedPreferences.getString("token", "");
            //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA2MTA4NDEsImV4cCI6MTcxMDY5NzI0MX0.UafMDeAaOFAfGGbsfTA2ugWlEuLHB1Yqg1Z8yYeFoew";
            postsAPI.getPosts( "Bearer "+ token);
        }


        public LiveData<List<Post_Item>> getAll() {
            return  postListData;
        }

    }
}