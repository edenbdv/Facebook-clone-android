package com.example.foobar;

public interface AddFriendRequestListener  {
    void onFriendRequestSent();
    void onFriendRequestFailed(String errorMessage);

}
