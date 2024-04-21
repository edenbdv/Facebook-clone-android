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

    private String token;

    private UserDao userDao;

    private TokenLiveData tokenLive;

    private UserAPI userAPI;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    //private MutableLiveData<User_Item> userLiveData;

    private MutableLiveData<User_Item> userLiveData;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_prefs";


    public UsersRepository(Context context,String profile_username,String username, String password) {

        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        userDao = AppDB.getInstance(context).userDao();
        tokenLive = new TokenLiveData(username, password);

        userLiveData = new UserLiveData(profile_username);

        userAPI = new UserAPI(tokenLive, userDao,userLiveData);
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

    }

    public LiveData<String> getToken() {
        return tokenLive;
    }


    public LiveData<User_Item> getUserLiveData() {
        return userLiveData;
    }

    // Method to validate user credentials
    public int validateUser(String username, String password) {
        return userDao.validateUser(username, password);
    }



//    public void getUser(String username) {
//        String token = sharedPreferences.getString("token", "");
//        String authToken =  "Bearer "+ token;
//        userAPI.getUser(username,authToken);
//    }


    public void deleteUser(String profile_username) {
        String token = sharedPreferences.getString("token", "");
        String authToken = "Bearer "+ token;
        userAPI.deleteUser(profile_username,authToken);
    }

    public void createUser(User_Item user) {
        new Thread(() -> userDao.createUser(user)).start();
        userAPI.createUser(user);
        Log.d("created user", user.getUsername());
    }


    public void  updateUser(String profile_username, String fieldName, String fieldValue) {
        String token = sharedPreferences.getString("token", "");
        String authToken = "Bearer "+ token;
        userAPI.updateUser(profile_username,fieldName,fieldValue, authToken);
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


    class UserLiveData extends  MutableLiveData<User_Item> {

        private  String profile_username;

        public UserLiveData(String profile_username) {
            super();
            this.profile_username = profile_username;
        }

        @Override
        protected  void  onActive() {
            super.onActive();
            new Thread(()->{

            }).start();

            String token = sharedPreferences.getString("token", "");
            userAPI.getUser(profile_username, "Bearer "+ token);
        }



    }


    }
