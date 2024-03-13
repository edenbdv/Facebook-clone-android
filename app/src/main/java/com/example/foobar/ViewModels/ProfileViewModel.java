package com.example.foobar.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<User_Item> userItemLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Post_Item>> postsLiveData = new MutableLiveData<>();

    // Method to set user data
    public void setUserItem(User_Item userItem) {
        userItemLiveData.setValue(userItem);
    }

    // Method to set post data
    public void setPosts(List<Post_Item> posts) {
        postsLiveData.setValue(posts);
    }

    // LiveData to observe user data changes
    public LiveData<User_Item> getUserItem() {
        return userItemLiveData;
    }

    // LiveData to observe post data changes
    public LiveData<List<Post_Item>> getPosts() {
        return postsLiveData;
    }
}
