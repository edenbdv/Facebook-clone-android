package com.example.foobar.viewModels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.repositories.FeedRepository;
import com.example.foobar.repositories.UsersRepository;

import java.util.List;

public class FriendsViewModel extends ViewModel {
    private MutableLiveData<List<String>> friendListLiveData = new MutableLiveData<>();

    private UsersRepository usersRepository;


    // Method to fetch the friend list
    public void fetchFriendList() {
        // Fetch friend list data from your data source (e.g., database or API)
        //List<String> friendList = yourDataSource.getFriendList();

        // Update LiveData with the new friend list
        //friendListLiveData.setValue(friendList);
    }

    // LiveData to observe friend list data changes
    public LiveData<List<String>> getFriendList() {
        return friendListLiveData;
    }


    public void  getUserFriends(String username) {
        usersRepository.getUserFriends(username);
    }
}

