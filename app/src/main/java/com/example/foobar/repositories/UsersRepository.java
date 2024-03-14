package com.example.foobar.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foobar.daos.UserDao;
import com.example.foobar.AppDB;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.webApi.UserAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;


import java.util.LinkedList;
import java.util.List;

public class UsersRepository {
    private UserDao userDao;
    private UserAPI userAPI;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    //private PostListData userPosts;

    public UsersRepository(Context context) {
        AppDB userDatabase = AppDB.getInstance(context);
        userDao = userDatabase.userDao();
        userAPI = new UserAPI();

        //userPosts = new PostListData(postDao);
        //userData = new UserData(userDao);
    }

    public LiveData<User_Item> getUser(String username) {
        MutableLiveData<User_Item> userData = new MutableLiveData<>();

        // Perform data fetching asynchronously
        Executors.newSingleThreadExecutor().execute(() -> {
            // Access the DAO to fetch user data
            User_Item user = userDao.getFullUserData(username);

            // Post the fetched user data to the LiveData
            userData.postValue(user);
        });

        return userData;
    }


//    public void createUser(User_Item user) {
//        userDao.createUser(user);
//        userAPI.createUser(user);
//    }

    public void createUser(User_Item user) {
        executor.execute(() -> {
            // Perform database operation asynchronously
            userDao.createUser(user);
            userAPI.createUser(user);
        });
    }

//    public void deleteUser(String username) {
//        userDao.deleteUser(username);
//        //userAPI.deleteUser(username);
//    }

    public void deleteUser(String username) {
        executor.execute(() -> {
            // Perform database operation asynchronously
            userDao.deleteUser(username);
            //userAPI.deleteUser(username);
        });
    }

}
