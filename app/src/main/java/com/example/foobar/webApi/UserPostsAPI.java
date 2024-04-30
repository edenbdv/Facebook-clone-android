package com.example.foobar.webApi;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar.CreatePostCallback;
import com.example.foobar.MyApplication;
import com.example.foobar.Post_IDGenerator;
import com.example.foobar.R;
import com.example.foobar.activities.AddPostWindow;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.entities.Post_Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPostsAPI {

    private MutableLiveData<List<Post_Item>> postListData;


    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private MutableLiveData<Post_Item> postData;

    private PostDao postDao;


    public UserPostsAPI(MutableLiveData<List<Post_Item>> postListData, PostDao postDao) {

        this.postListData = postListData;
        this.postDao = postDao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public void createPost(String username, String text, String picture, String authToken, CreatePostCallback callback) {

        Call<Post_Item> call = webServiceAPI.createPost(username, text, picture, authToken);
        call.enqueue(new Callback<Post_Item>() {
            @Override
            public void onResponse(Call<Post_Item> call, Response<Post_Item> response) {

                if (response.isSuccessful()) {
                    new Thread(() -> {
                        // Generate a numerical ID for the newly created post
                        int postId = Post_IDGenerator.getNextId();

                        // Insert the newly created post with the generated numerical ID into the local database
                        Post_Item postItem = response.body();
                        Post_Item localPost = new Post_Item(text, picture, username, false);
                        Log.d("PostAPI", " local id:" + postItem.getId());

                        localPost.setId(postId);
                        postDao.createPost(postItem);

                        // Update the LiveData with the updated list of posts
                        List<Post_Item> updatedPosts = new ArrayList<>(postListData.getValue());
                        updatedPosts.add(postItem);
                        postListData.postValue(updatedPosts);

                        // Invoke onSuccess method of the callback
                        callback.onSuccess(postItem);
                    }).start();
                } else {
                    if (response.code() == 403) {
                       // Log.e("UserPostsAPI","You do not have permission to perform this action");

                        // Invoke onPermissionDenied method of the callback
                        callback.onPermissionDenied();
                    } else {
                        //Log.e("UserPostsAPI", "Failed to create post: " + response.message());

                        // Invoke onFailure method of the callback with error message
                        callback.onFailure("Failed to create post: " + response.message());
                    }
                }

            }

            @Override
            public void onFailure(Call<Post_Item> call, Throwable t) {
                //Log.e("PostAPI", "Failed to create post. Error: " + t.getMessage());

                // Invoke onFailure method of the callback with error message
                callback.onFailure("Failed to create post. Error: " + t.getMessage());

            }
        });
    }

    public void getUserPosts(String username, String authToken) {
        //return webServiceAPI.getUserPosts(username, authToken);
        Call<List<Post_Item>> call = webServiceAPI.getUserPosts(username, authToken);
        call.enqueue(new Callback <List<Post_Item>>() {
            @Override
            public void onResponse(Call<List<Post_Item>> call, Response<List<Post_Item>> response) {
                if (response.isSuccessful()) {
                    Log.e("PostsAPI", "Posts retrieved successfully");

                    List<Post_Item> userPosts = response.body();
                    if (userPosts != null) {
                        // Perform database operations asynchronously
                        new Thread(() -> {
                            postDao.clear(); // Clear existing data in the table
                            postDao.insertList(userPosts); // Insert new data into the table
                        }).start();
                        postListData.setValue(userPosts); // Update LiveData with new data
                    } else {
                        Log.e("PostsAPI", "Response body is null");
                    }
                } else {
                    Log.e("PostsAPI", "Failed to get posts: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Post_Item>> call, Throwable t) {
                Log.e("PostAPI", "Failed to create post. Error: " + t.getMessage());
            }
        });
    }

    public void deletePost(int localId, String username, String postId, String authToken) {
        Call<Post_Item> call = webServiceAPI.deletePost(username, postId, authToken);
        call.enqueue(new Callback<Post_Item>() {
            @Override
            public void onResponse(Call<Post_Item> call, Response<Post_Item> response) {
                if (response.isSuccessful()) {
                    Post_Item deletedPost = response.body();
                    if (deletedPost != null) {
                        new Thread(() -> {
                             //Delete the post from local database

                            postDao.deletePost(localId);

                            // Update LiveData with updated list of posts
                            List<Post_Item> updatedPosts = new ArrayList<>(postListData.getValue());
                            updatedPosts.remove(deletedPost);
                            postListData.postValue(updatedPosts);
                        }).start();

                        Log.d("PostAPI", "Post deleted successfully");
                    } else {
                        Log.d("PostAPI", "Failed to delete post. No deleted post in response.");
                    }
                } else {
                    Log.d("PostAPI", "Failed to delete post. Response code: " + response.code());
                    // Handle unsuccessful response, if needed
                }
            }

            @Override
            public void onFailure(Call<Post_Item> call, Throwable t) {
                Log.e("PostAPI", "Failed to delete post: " + t.getMessage());
                // Handle the failure scenario, such as displaying an error message to the user
            }
        });
    }



    public void updatePost(String username, String postId, String fieldName, String fieldValue, String authToken) {
        Call<Post_Item> call = webServiceAPI.updatePost(username, postId, fieldName, fieldValue, authToken);
        call.enqueue(new Callback<Post_Item>() {
            @Override
            public void onResponse(Call<Post_Item> call, Response<Post_Item> response) {
                if (response.isSuccessful()) {
                    Post_Item updatedPost = response.body();
                    if (updatedPost != null) {
                        Log.d("PostAPI", "Post updated successfully: " + updatedPost.toString());

                        new Thread(() -> {
                            // Insert the newly updated post into the local database
                            Post_Item postItem = response.body();
                            postDao.updatePost(postItem);

                            List<Post_Item> updatedPosts =  new ArrayList<>(postListData.getValue());
                            updatedPosts.add(postItem);

                            // Update the LiveData with the updated list of posts
                            postListData.postValue(updatedPosts);
                        }).start();
                    } else {
                        Log.e("PostAPI", "Received null response body");
                    }
                } else {
                    if (response.code() == 403) {
                        //Log.e("UserPostsAPI", "You do not have permission to perform this action");
                        Log.e("UserPostsAPI", "You do not have permission to perform this action");
                    }
//                    try {
//                        String errorMessage = response.errorBody().string();
//                        Log.d("PostAPI", "Failed to update post. Response code: " + response.code() + ", Error message: " + errorMessage);
//                    } catch (IOException e) {
//                        Log.e("PostAPI", "Error reading error message: " + e.getMessage());
//                    }
                }
            }

            @Override
            public void onFailure(Call<Post_Item> call, Throwable t) {
                Log.e("PostAPI", "Failed to update post: " + t.getMessage());
            }
        });
    }


}

