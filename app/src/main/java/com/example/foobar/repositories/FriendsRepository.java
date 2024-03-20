package com.example.foobar.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobar.AppDB;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.FriendRequestDao;
import com.example.foobar.daos.FriendshipDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.daos.UserDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.webApi.PostsAPI;
import com.example.foobar.webApi.UserAPI;
import com.example.foobar.webApi.UserFriendsAPI;
import com.example.foobar.webApi.UserPostsAPI;

import java.util.LinkedList;
import java.util.List;

public class FriendsRepository {

    private String token;
    private FriendshipDao friendDao;
    private FriendRequestDao friendRequestDao;
    UserFriendsAPI friendsAPI;
    private FriendsRepository.FriendsListData friendsListData;  // is a mutable live data, that extended live data
    private FriendsRepository.FriendsRequestData friendsRequestData;  // is a mutable live data, that extended live data
    private SharedPreferences sharedPreferences;
    private String username;

    public FriendsRepository(Context context, String username) {
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        friendDao = AppDB.getInstance(context).friendshipDao();
        friendRequestDao = AppDB.getInstance(context).friendRequestDao(); // Initialize friendRequestDao
        friendsListData = new FriendsListData(); // Initialize friendsListData
        friendsRequestData = new FriendsRequestData(); // Initialize friendsListData
        friendsAPI = new UserFriendsAPI(friendDao,friendRequestDao, friendsListData, friendsRequestData); //maybe need to add live data here??
        this.username=username;
    }

    public LiveData<List<String>> getAll() {
        return friendsListData;
    }

    public LiveData<List<String>> getFriendsRequest() {
        return friendsRequestData;
    }


    public void acceptFriendRequest(String senderUsername) {
        friendsAPI.acceptReq(senderUsername, username, "Bearer " + token);
        List<String> friendRequests = friendsRequestData.getValue();
        if (friendRequests != null) {
            friendRequests.remove(senderUsername);
            friendsRequestData.setValue(friendRequests);
        }

    }

    public void removeFriendRequest(String friendRequestUsername) {
        List<String> friendRequests = friendsRequestData.getValue();
        if (friendRequests != null) {
            friendRequests.remove(friendRequestUsername);
            friendsRequestData.setValue(friendRequests);
        }
    }

    class FriendsListData extends MutableLiveData<List<String>> {
        public FriendsListData() {
            super();
            List<String> friends = new LinkedList<>();
            setValue(friends) ;
        }

        @Override
        protected void onActive() { //extract the data from the local dbm and update live data
            super.onActive();
            new Thread(()->{
            }).start();

            String token = sharedPreferences.getString("token", "");
            friendsAPI.getUserFriends(username, "Bearer "+ token);
        }

    }


    class FriendsRequestData extends MutableLiveData<List<String>> {
        public FriendsRequestData() {
            super();
            List<String> friendsRequest = new LinkedList<>();
            setValue(friendsRequest) ;
        }

        @Override
        protected  void  onActive() { //extract the data from the local dbm and update live data
            super.onActive();
            new Thread(()->{
            }).start();

            String token = sharedPreferences.getString("token", "");
            friendsAPI.getFriendRequests(username, "Bearer "+ token);
        }

    }

}
