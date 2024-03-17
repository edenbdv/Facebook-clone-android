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
import com.example.foobar.repositories.FeedRepository;
import com.example.foobar.repositories.UsersRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private LiveData<String> tokenLiveData;
    private UsersRepository userRepository;

    public UserViewModel() {
    }

    public void initRepo(Context context) {
        userRepository = new UsersRepository(context);
        // Initialize LiveData from repository
        tokenLiveData = userRepository.getToken();

    }
    // Method to retrieve token LiveData
    public LiveData<String> getTokenLiveData() {
        return tokenLiveData;
    }



}
