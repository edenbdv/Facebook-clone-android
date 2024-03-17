package com.example.foobar.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.repositories.FeedRepository;

import java.util.List;

public class FeedViewModel extends ViewModel {

   private LiveData<List<Post_Item>> posts;

    private FeedRepository feedRepository;

    public FeedViewModel() {

    }

    public void initRepo(Context context) {
        feedRepository = new FeedRepository(context);
        // Initialize LiveData from repository
        posts = feedRepository.getAll();

    }



    // Expose LiveData to observe posts
    public LiveData<List<Post_Item>> getPostsLiveData() {
        return posts;
    }


    public  void  createPost(Post_Item postItem)  {
        String username = postItem.getCreatedBy();
        String text = postItem.getText();
        String picture = postItem.getPicture();

        feedRepository.createPost(username, text, picture);
    }

    public  void  deletePost(Post_Item postItem)  {
        String username = postItem.getCreatedBy();
        String postId = postItem.get_id();
        int localId = postItem.getId();

       feedRepository.deletePost(localId, username, postId);
    }


}
