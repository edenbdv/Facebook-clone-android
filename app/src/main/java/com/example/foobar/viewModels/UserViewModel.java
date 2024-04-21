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

    private LiveData<User_Item> userLiveData ;

    private LiveData<String> tokenLiveData;
    private UsersRepository userRepository;



    public UserViewModel(@NonNull Application application) {
        super(application);
    }


    public void initRepo(Context context,String profile_username, String username, String password) {
        userRepository = new UsersRepository(context, profile_username,username, password);

        tokenLiveData = userRepository.getToken();

        userLiveData = userRepository.getUserLiveData();

    }

    // Method to retrieve token LiveData
    public LiveData<String> getTokenLiveData() {
        return tokenLiveData;
    }


    // Expose LiveData to observe user

    public LiveData<User_Item> getUserLiveData() {
        return userLiveData;
    }

    public void createUser(User_Item user) {
        userRepository.createUser(user);
    }

    // Method to validate user credentials
    public int validateUser(String username, String password) {
        return userRepository.validateUser(username, password);
    }


     //Method to delete a user
    public void deleteUser(String username) {
        userRepository.deleteUser(username);
    }


    public  void  updateUser(String username, String fieldName, String fieldValue)  {
        userRepository.updateUser(username,fieldName,fieldValue);
    }


}
