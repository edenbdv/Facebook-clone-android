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
    private SharedPreferences sharedPreferences;
    private String username;

    public FriendsRepository(Context context, String username) {
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        friendDao = AppDB.getInstance(context).friendshipDao();
        friendRequestDao = AppDB.getInstance(context).friendRequestDao(); // Initialize friendRequestDao
        friendsAPI = new UserFriendsAPI(friendDao,friendRequestDao); //maybe need to add live data here??
        this.username=username;
        friendsListData = new FriendsListData(); // Initialize friendsListData

    }

    public LiveData<List<User_Item>> getAll() {
        return friendsListData;
    }


    class FriendsListData extends MutableLiveData<List<User_Item>> {
        public FriendsListData() {
            super();
            List<User_Item> friends = new LinkedList<User_Item>();
            setValue(friends) ;
        }

        @Override
        protected  void  onActive() { //extract the data from the local dbm and update live data
            super.onActive();
            new Thread(()->{
                //postListData.postValue(feedDao.getPostsFromFriends("Eden"));
                //postListData.postValue(feedDao.getPostsFromNonFriends("Eden"));
            }).start();

            String token = sharedPreferences.getString("token", "");
            //String jwtTokenRoey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IlJvZXkiLCJpYXQiOjE3MTA2MTA4NDEsImV4cCI6MTcxMDY5NzI0MX0.UafMDeAaOFAfGGbsfTA2ugWlEuLHB1Yqg1Z8yYeFoew";
            friendsAPI.getUserFriends(username, "Bearer "+ token);
        }

    }
}
