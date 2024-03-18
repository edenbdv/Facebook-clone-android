package com.example.foobar.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foobar.daos.UserDao;
import com.example.foobar.AppDB;
import com.example.foobar.entities.User_Item;
import com.example.foobar.webApi.UserAPI;
import com.example.foobar.webApi.UserPostsAPI;
import java.util.List;

public class UsersRepository {

    private UserDao userDao;
    private UserAPI userAPI;
    private TokenLiveData token;
    private String username;
    private String password;

    public UsersRepository(Context context, String username, String password) {
        userDao = AppDB.getInstance(context).userDao();
        token= new TokenLiveData(username, password);
        userAPI = new UserAPI(token, userDao);
    }

    public LiveData<String> getToken() {
        return token;
    }

    // Method to validate user credentials
    public int validateUser(String username, String password) {
        return userDao.validateUser(username, password);
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
