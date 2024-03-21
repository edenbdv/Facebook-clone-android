package com.example.foobar.viewModels;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.foobar.entities.User_Item;
import com.example.foobar.repositories.UsersRepository;

import java.util.List;

public class UserViewModel extends ViewModel {
    private LiveData<String> tokenLiveData;
    private UsersRepository userRepository;

    public UserViewModel() {
    }

    public void initRepo(Context context, String username, String password) {
        userRepository = new UsersRepository(context, username, password);
        // Initialize LiveData from repository
        tokenLiveData = userRepository.getToken();

    }
    // Method to retrieve token LiveData
    public LiveData<String> getTokenLiveData() {
        return tokenLiveData;
    }

    // Method to validate user credentials
    public int validateUser(String username, String password) {
        return userRepository.validateUser(username, password);
    }

    public void createUser(User_Item user) {
        userRepository.createUser(user);
    }

    //Method to delete a user
    public void deleteUser(String username) {
        userRepository.deleteUser(username);
    }

    public void updateUser(String username, String fieldName, String fieldValue)  {
        userRepository.updateUser(username,fieldName,fieldValue);
    }

    private  void  getUser(String username) {
        userRepository.getUser(username);
    }

}
