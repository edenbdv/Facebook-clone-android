package com.example.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.daos.FriendRequestDao;
import com.example.foobar.daos.FriendshipDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.daos.UserDao;
import com.example.foobar.AppDB;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.webApi.PostsAPI;
import com.example.foobar.webApi.UserAPI;
import com.example.foobar.webApi.UserFriendsAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;


import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    private UserDao userDao;

    private FriendshipDao friendshipDao;

    private FriendRequestDao friendRequestDao;

    private UserAPI userAPI;

    private UserFriendsAPI userFriendsAPI;

    private  UserPostsAPI userPostsAPI;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<User_Item> userLiveData = new MutableLiveData<>();

    //private PostListData userPosts;

    public UsersRepository(Context context) {
        AppDB userDatabase = AppDB.getInstance(context);
        userDao = userDatabase.userDao();
        userAPI = new UserAPI(userLiveData, userDao);  //maybe need to add live data here??
        userFriendsAPI = new UserFriendsAPI(friendshipDao,friendRequestDao); //maybe need to add live data here??
    }

    public void getUser(String username) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA5NjIxNjEsImV4cCI6MTcxMTA0ODU2MX0.Sjok6qVTsOZFDzfEonvfWwfjDB9jfLxwdNpPEsJ-RSE";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userAPI.getUser(username,authToken);
    }


    public void deleteUser(String username) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA5NjIxNjEsImV4cCI6MTcxMTA0ODU2MX0.Sjok6qVTsOZFDzfEonvfWwfjDB9jfLxwdNpPEsJ-RSE";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userAPI.deleteUser(username,authToken);
    }


    public void  updateUser(String username, String fieldName, String fieldValue) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA5NjIxNjEsImV4cCI6MTcxMTA0ODU2MX0.Sjok6qVTsOZFDzfEonvfWwfjDB9jfLxwdNpPEsJ-RSE";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userAPI.updateUser(username,fieldName,fieldValue, authToken);
    }

    public void  getUserFriends(String username) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA5NjIxNjEsImV4cCI6MTcxMTA0ODU2MX0.Sjok6qVTsOZFDzfEonvfWwfjDB9jfLxwdNpPEsJ-RSE";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userFriendsAPI.getUserFriends(username, authToken);
    }



    }
