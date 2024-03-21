package com.example.foobar.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foobar.entities.Friendship;
import com.example.foobar.entities.Post_Item;

import java.util.List;

@Dao
public interface FriendshipDao {


    // delete friendship
    @Delete
    void delete(Friendship friendship);


    // get all friends of specific user
    @Query("SELECT CASE WHEN user1_username = :username THEN user2_username ELSE user1_username END AS friendUsername " +
            "FROM friendships WHERE user1_username = :username OR user2_username = :username")
    List<String> getUserFriends(String username);



    @Query("DELETE FROM friendships")
    void clear();


    //inset list of friendships
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Friendship> friendships);


    // insert one friendship
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Friendship friendship);

}
