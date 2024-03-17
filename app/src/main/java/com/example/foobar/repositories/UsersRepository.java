package com.example.foobar.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foobar.daos.UserDao;
import com.example.foobar.AppDB;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.webApi.UserAPI;
import com.example.foobar.webApi.UserPostsAPI;


import java.util.List;

public class UsersRepository {

    private UserDao userDao;
    private UserAPI userAPI;
    private TokenLiveData token;

    public UsersRepository(Context context) {
        userDao = AppDB.getInstance(context).userDao();
        token= new TokenLiveData("Noga", "Noga1234");
        userAPI = new UserAPI(token, userDao);
    }

    public LiveData<String> getToken() {
        return token;
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
