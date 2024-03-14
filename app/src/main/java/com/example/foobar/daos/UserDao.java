package com.example.foobar.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foobar.entities.User_Item;

@Dao
public interface UserDao {

    //according to  http://foo.com/api/users/:id


    // get user- data (username,displayName , profile pic) according to his username
    // need to change according to the token!!
    @Query("SELECT username, displayName, profilePic FROM User_Item WHERE username = :username")
    User_Item getPartUserData(String username);


    // only for the user itself ? (according to token)
    @Query("SELECT * FROM User_Item WHERE username = :username")
    User_Item getFullUserData(String username);


    // create new user
    @Insert
    void createUser(User_Item user);


    // update new user by specific field
    @Query("UPDATE User_Item SET username = :newUsername WHERE username = :username")
    void updateUsername(String username, String newUsername);

    @Query("UPDATE User_Item SET password = :newPassword WHERE username = :username")
    void updatePassword(String username, String newPassword);

    @Query("UPDATE User_Item SET displayName = :newDisplayName WHERE username = :username")
    void updateDisplayName(String username, String newDisplayName);

    @Query("UPDATE User_Item SET profilePic = :newProfilePic WHERE username = :username")
    void updateProfilePic(String username, String newProfilePic);

    //deleteUser
    @Query("DELETE FROM User_Item WHERE username = :username")
    void deleteUser(String username);


}