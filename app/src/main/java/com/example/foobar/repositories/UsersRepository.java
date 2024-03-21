package com.example.foobar.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

    private TokenLiveData tokenLive;

    private UserAPI userAPI;

    private UserFriendsAPI userFriendsAPI;

    private  UserPostsAPI userPostsAPI;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<User_Item> userLiveData = new MutableLiveData<>();

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_prefs";


    //private PostListData userPosts;

    public UsersRepository(Context context, String username, String password) {
        userDao = AppDB.getInstance(context).userDao();
        tokenLive = new TokenLiveData(username, password);
        userAPI = new UserAPI(tokenLive, userDao);
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //userFriendsAPI = new UserFriendsAPI(friendshipDao,friendRequestDao); //maybe need to add live data here??
    }

    public LiveData<String> getToken() {
        return tokenLive;
    }

    // Method to validate user credentials
    public int validateUser(String username, String password) {
        return userDao.validateUser(username, password);
    }



    public void getUser(String username) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTEwMTY2ODcsImV4cCI6MTcxMTEwMzA4N30.UzWtDcftRb9H9F3cfa0hZcDQa_KRQNDfFwBsSHQpUSw";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userAPI.getUser(username,authToken);
    }


    public void deleteUser(String username) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTEwMTY2ODcsImV4cCI6MTcxMTEwMzA4N30.UzWtDcftRb9H9F3cfa0hZcDQa_KRQNDfFwBsSHQpUSw";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userAPI.deleteUser(username,authToken);
    }

    public void createUser(User_Item user) {
        new Thread(() -> userDao.createUser(user)).start();
        userAPI.createUser(user);
        Log.d("created user", user.getUsername());
    }



    public void  updateUser(String username, String fieldName, String fieldValue) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTEwMTY2ODcsImV4cCI6MTcxMTEwMzA4N30.UzWtDcftRb9H9F3cfa0hZcDQa_KRQNDfFwBsSHQpUSw";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userAPI.updateUser(username,fieldName,fieldValue, authToken);
    }

    public void  getUserFriends(String username) {
        String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTEwMTY2ODcsImV4cCI6MTcxMTEwMzA4N30.UzWtDcftRb9H9F3cfa0hZcDQa_KRQNDfFwBsSHQpUSw";
        String authToken =  "Bearer "+ jwtTokenRoey; //for example if roey is logged in
        userFriendsAPI.getUserFriends(username, authToken);
    }

    class TokenLiveData extends MutableLiveData<String> {
        private final String username;
        private final String password;

        public TokenLiveData(String username, String password) {
            super();
            this.username = username;
            this.password = password;
            setValue("");
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(()-> {
                // Logic to generate token when LiveData becomes active
                userAPI.createToken(username, password);
            }).start();
        }

    }



    }
