package com.example.foobar.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foobar.entities.Post_Item;

import java.util.List;

@Dao
public interface FeedDao {


    @Query("SELECT * FROM Post_Item WHERE createdBy IN " +
            "(SELECT CASE WHEN user1_username = :username THEN user2_username ELSE user1_username END FROM friendships WHERE user1_username = :username OR user2_username = :username) " +
            "ORDER BY createdAt DESC LIMIT 20")
    List<Post_Item> getPostsFromFriends(String username);

    @Query("SELECT * FROM Post_Item WHERE createdBy NOT IN " +
            "(SELECT CASE WHEN user1_username = :username THEN user2_username ELSE user1_username END FROM friendships WHERE user1_username = :username OR user2_username = :username) AND " +
            "createdBy != :username ORDER BY createdAt DESC LIMIT 5")
    List<Post_Item> getPostsFromNonFriends(String username);


    @Query("DELETE FROM Post_Item")
    void clear();

    //inset list of posts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Post_Item> postItems);

    // insert one post
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post_Item postItem);


}