package com.example.foobar.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foobar.entities.FriendRequest;

import java.util.List;

@Dao
public interface FriendRequestDao {

    // add new friend request
    @Insert
    void addFriendReq(FriendRequest friendRequest);


    // delete friend request
    @Delete
    void deleteFriendReq(FriendRequest friendRequest);


    // get all friends of specific user
    @Query("SELECT senderUsername FROM friend_requests WHERE receiverUsername = :username" )
    List<String> getFriendRequests(String username);



    // Retrieve a specific friend request by sender and receiver usernames
    @Query("SELECT * FROM friend_requests WHERE senderUsername = :sender AND receiverUsername = :receiver LIMIT 1")
    FriendRequest getFriendRequest(String sender, String receiver);

}
