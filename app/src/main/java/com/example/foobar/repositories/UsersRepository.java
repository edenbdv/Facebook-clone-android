package com.example.foobar.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.daos.FriendRequestDao;
import com.example.foobar.daos.FriendshipDao;
import com.example.foobar.daos.UserDao;
import com.example.foobar.AppDB;
import com.example.foobar.entities.User_Item;
import com.example.foobar.webApi.UserAPI;
import com.example.foobar.webApi.UserFriendsAPI;
import com.example.foobar.webApi.UserPostsAPI;
import java.util.List;

public class UsersRepository {

    private UserDao userDao;
    private FriendshipDao friendshipDao;
    private FriendRequestDao friendRequestDao;
    private UserAPI userAPI;
    private UserFriendsAPI userFriendsAPI;
    private  UserPostsAPI userPostsAPI;
    private TokenLiveData token;
    private String username;
    private String password;

    public UsersRepository(Context context, String username, String password) {
        userDao = AppDB.getInstance(context).userDao();
        token= new TokenLiveData(username, password);
        userAPI = new UserAPI(token, userDao);
        //userFriendsAPI = new UserFriendsAPI(friendshipDao,friendRequestDao); //maybe need to add live data here??
    }

    public LiveData<String> getToken() {
        return token;
    }

    // Method to validate user credentials
    public int validateUser(String username, String password) {
        return userDao.validateUser(username, password);
    }



    public void deleteUser(String username) {
        //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ik5vZ2EiLCJpYXQiOjE3MTA3OTY0MTQsImV4cCI6MTcxMDg4MjgxNH0.cXi8MYbiPqvV8W0KvZdNZJcpSDi1Zp2dU1FsUmthV_Q"; //for example if roey is logged in
        userAPI.deleteUser(username,authToken);
        //userDao.deleteUser(username);
    }

    public void  updateUser(String username, String fieldName, String fieldValue) {
        //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA3MDcwNjIsImV4cCI6MTcxMDc5MzQ2Mn0.TtcFArEMg70hESXCCBVc2-XFuF-jASrrqc-ZNWvkr3o";
        String authToken =  "Bearer "+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ik5vZ2EiLCJpYXQiOjE3MTA3OTY0MTQsImV4cCI6MTcxMDg4MjgxNH0.cXi8MYbiPqvV8W0KvZdNZJcpSDi1Zp2dU1FsUmthV_Q"; //for example if roey is logged in
        userAPI.updateUser(username,fieldName,fieldValue, authToken);
    }

    public void createUser(User_Item user) {
        new Thread(() -> userDao.createUser(user)).start();
        userAPI.createUser(user);
        Log.d("created user", user.getUsername());
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
