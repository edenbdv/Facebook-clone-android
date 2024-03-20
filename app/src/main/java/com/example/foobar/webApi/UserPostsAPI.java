package com.example.foobar.webApi;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.entities.Post_Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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



    public UserPostsAPI(MutableLiveData<List<Post_Item>> postListData,PostDao postDao) {

        this.postListData = postListData;
        this.postDao = postDao;

        retrofit = new Retrofit.Builder()
                //.baseUrl(MyApplication.context.getString(R.string.BaseUrl))  //we need to change it later to be save in R string
                .baseUrl("http://172.20.10.3:12345/api/")  //we need to change it later to be save in R string

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }




    public void createPost(String username, String text, String picture, String authToken) {

        Call<Post_Item> call = webServiceAPI.createPost(username, text, picture, authToken);
        call.enqueue(new Callback<Post_Item>() {
            @Override
            public void onResponse(Call<Post_Item> call, Response<Post_Item> response) {

                new Thread(() -> {
                    // Insert the newly created post into the local database

                    Post_Item postItem = response.body();
                    Log.d("PostAPI", " local id:"+ postItem.getId());


                    String text =  postItem.getText();
                    String picture = postItem.getPicture();
                    String username = postItem.getCreatedBy();

                    Post_Item localPost = new Post_Item(text,picture,username,false);
                    Log.d("PostAPI", "  fixed local id:"+ localPost.getId());


                    // Manually assign an ID using the counter
//                    postItem.setId(postIdCounter++);
//                    Log.d("PostAPI", " fixed id:"+ postItem.getId());

                    postDao.createPost(postItem);
                    List<Post_Item> updatedPosts =  new ArrayList<>(postListData.getValue());
                    updatedPosts.add(postItem);

                    // Update the LiveData with the updated list of posts

                    postListData.postValue(updatedPosts);
                }).start();

            }

            @Override
            public void onFailure(Call<Post_Item> call, Throwable t) {
                Log.e("PostAPI", "Failed to create post. Error: " + t.getMessage());
            }
        });
    }



    public void getUserPosts(String username, String authToken) {
        Call<List<Post_Item>> call = webServiceAPI.getUserPosts(username, authToken);
        call.enqueue(new Callback <List<Post_Item>>() {
            @Override
            public void onResponse(Call<List<Post_Item>> call, Response<List<Post_Item>> response) {
                if (response.isSuccessful()) {
                    List<Post_Item> posts = response.body();
                    if (posts != null && !posts.isEmpty()) {
                        for (Post_Item post : posts) {
                            // Process each post
                            Log.d("PostAPI", "_id: " + post.get_id());
                            Log.d("PostAPI", "Text: " + post.getText());
                            Log.d("PostAPI", "Picture: " + post.getPicture());
                        }
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Log.d("PostAPI", "Failed to show posts. Response code: " + response.code() + ", Error message: " + errorMessage);
                        } catch (IOException e) {
                            Log.e("PostAPI", "Error reading error message: " + e.getMessage());
                        }
                    }
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
//                            String idMongo = deletedPost.get_id();
//                            postDao.deletePost2(idMongo);

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
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("PostAPI", "Failed to update post. Response code: " + response.code() + ", Error message: " + errorMessage);
                    } catch (IOException e) {
                        Log.e("PostAPI", "Error reading error message: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Post_Item> call, Throwable t) {
                Log.e("PostAPI", "Failed to update post: " + t.getMessage());
            }
        });
    }


}
