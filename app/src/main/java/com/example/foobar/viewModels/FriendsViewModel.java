package com.example.foobar.viewModels;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.repositories.FeedRepository;
import com.example.foobar.repositories.FriendsRepository;
import com.example.foobar.repositories.UsersRepository;

import java.util.List;

public class FriendsViewModel extends ViewModel {
    private LiveData<List<User_Item>> friends;
    private String username;
    private FriendsRepository friendsRepository;

    public FriendsViewModel() {
    }

    public void initRepo(Context context, String username) {
        friendsRepository = new FriendsRepository(context, username);
        this.username = username;
        friends = friendsRepository.getAll();

    }

    // LiveData to observe friend list data changes
    public LiveData<List<User_Item>> getFriendList() {
        return friends;
    }

}
