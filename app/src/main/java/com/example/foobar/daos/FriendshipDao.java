package com.example.foobar.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foobar.entities.Friendship;

import java.util.List;

@Dao
public interface FriendshipDao {

    // add new friendship
    @Insert
    void insert(Friendship friendship);

    // delete friendship
    @Delete
    void delete(Friendship friendship);


    // get all friends of specific user
    @Query("SELECT CASE WHEN user1_username = :username THEN user2_username ELSE user1_username END AS friendUsername " +
            "FROM friendships WHERE user1_username = :username OR user2_username = :username")
    List<String> getUserFriends(String username);


}
