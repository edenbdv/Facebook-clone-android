package com.example.foobar.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.repositories.UsersRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User_Item> userItemLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Post_Item>> postsLiveData = new MutableLiveData<>();
    private UsersRepository userRepository;
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UsersRepository(application);
    }

    // Method to create a new user
    public void createUser(User_Item user) {
        userRepository.createUser(user);
    }

    // Method to delete a user
    public void deleteUser(String username) {
        userRepository.deleteUser(username);
    }

    // Method to get the current user data
    public LiveData<User_Item> getCurrentUser(String username) {
        return userRepository.getUser(username);
    }

     //Method to get all posts of the current user
//    public LiveData<List<Post_Item>> getUserPosts(String username) {
//        return userRepository.getUserPosts(username);
//    }


    // Method to create a new post
//    public void createPost(String username, String text, String picture, String authToken) {
//        userRepository.createPost(username, text, picture, authToken);
//    }

    // Method to set user data
    public void setUserItem(User_Item userItem) {
        userItemLiveData.setValue(userItem);
    }

    // Method to set post data
    public void setPosts(List<Post_Item> posts) {
        postsLiveData.setValue(posts);
    }


}
