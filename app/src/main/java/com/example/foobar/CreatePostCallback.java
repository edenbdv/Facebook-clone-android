package com.example.foobar;

import com.example.foobar.entities.Post_Item;

public interface CreatePostCallback {
    void onSuccess(Post_Item postItem);
    void onPermissionDenied();
    void onFailure(String errorMessage);
}