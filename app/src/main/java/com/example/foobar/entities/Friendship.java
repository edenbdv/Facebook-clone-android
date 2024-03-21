package com.example.foobar.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "friendships",
      primaryKeys = {"user1_username", "user2_username"},
      indices = {@Index(value = {"user1_username", "user2_username"}, unique = true)})

public class Friendship {
    @NonNull
    private String user1_username;
    @NonNull
    private String user2_username;

    public Friendship(@NonNull String user1_username, @NonNull  String user2_username) {
        this.user1_username = user1_username;
        this.user2_username = user2_username;
    }

    @NonNull
    public String getUser1_username() {
        return user1_username;
    }

    public void setUser1_username(@NonNull String user1_username) {
        this.user1_username = user1_username;
    }

    @NonNull
    public String getUser2_username() {
        return user2_username;
    }

    public void setUser2_username(@NonNull String user2_username) {
        this.user2_username = user2_username;
    }
}
