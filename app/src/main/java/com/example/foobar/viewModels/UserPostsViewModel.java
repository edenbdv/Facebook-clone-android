package com.example.foobar.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.repositories.UserPostsRepository;

import java.util.List;

public class UserPostsViewModel extends ViewModel {

    private LiveData<List<Post_Item>> userPostsLive;
    private UserPostsRepository userPostsRepository;

    public UserPostsViewModel() {
    }

    public void initRepo(Context context, String username) {
        userPostsRepository = new UserPostsRepository(context, username);
        Log.d("VM username", username);
        userPostsLive = userPostsRepository.getUserPosts();
    }

    public LiveData<List<Post_Item>> getUserPosts(String username) {
        Log.d("VM", "Got user posts");
        return userPostsLive;
    }

    public  void  deletePost(Post_Item postItem)  {
        String username = postItem.getCreatedBy();
        String postId = postItem.get_id();
        int localId = postItem.getId();

        userPostsRepository.deletePost(localId, username, postId);
    }


    public  void  updatePost(Post_Item postItem, String fieldName, String fieldValue)  {
        String username = postItem.getCreatedBy();
        String postId = postItem.get_id();

        userPostsRepository.updatePost(username,postId,fieldName,fieldValue);
    }

}