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

    private MutableLiveData<User_Item> user ;

    private MutableLiveData<User_Item> userItemLiveData = new MutableLiveData<>();
    private UsersRepository userRepository;



    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UsersRepository(application);

        user = new MutableLiveData<User_Item>();
    }



     //Method to delete a user
    public void deleteUser(String username) {
        userRepository.deleteUser(username);
    }


    public  void  updateUser(String username, String fieldName, String fieldValue)  {
        userRepository.updateUser(username,fieldName,fieldValue);
    }

    private  void  getUser(String username) {
        userRepository.getUser(username);
    }



}
