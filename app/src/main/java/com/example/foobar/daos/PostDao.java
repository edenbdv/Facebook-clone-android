package com.example.foobar.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foobar.entities.Post_Item;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void createPost(Post_Item post);


    @Query("SELECT * FROM Post_Item WHERE createdBy = :username ORDER BY createdAt DESC")
    List<Post_Item> getUserPosts(String username);

    // update new user by specific field
    @Query("UPDATE Post_Item SET text = :newText WHERE id = :id")
    void updateText(int id, String newText);

    @Query("UPDATE Post_Item SET picture = :newPicture WHERE id = :id")
    void updatePicture(int id,String newPicture);


    // in case someone changing his username
    @Query("UPDATE Post_Item SET createdBy = :newUsername WHERE id = :id")
    void updateUsername(int id, String newUsername);


    // regular delete (specific post )
    @Query("DELETE FROM Post_Item WHERE id = :id")
    void deletePost(int id);

    // use in case the user was deleted from the system
    @Query("DELETE FROM Post_Item WHERE createdBy = :userName")

    void deletePostByUser(String userName);

    @Query("DELETE FROM Post_Item")
    void clear();

    //inset list of posts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Post_Item> postItems);

    // insert one post
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post_Item postItem);


}
