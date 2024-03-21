package com.example.foobar.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(tableName = "friend_requests",
        primaryKeys = {"senderUsername", "receiverUsername"},
        indices = {@Index(value = {"senderUsername", "receiverUsername"}, unique = true)})
public class FriendRequest {

    @NonNull
    private String senderUsername;

    @NonNull
    private String receiverUsername;

    public FriendRequest(@NonNull String senderUsername, @NonNull String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    @NonNull
    public String getSenderUsername() {
        return senderUsername;
    }

    @NonNull
    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setSenderUsername(@NonNull String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setReceiverUsername(@NonNull String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
}
