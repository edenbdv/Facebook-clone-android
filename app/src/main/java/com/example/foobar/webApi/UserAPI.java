package com.example.foobar.webApi;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar.daos.UserDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.TokenRes;
import com.example.foobar.entities.User_Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    private  UserDao userDao;

    private MutableLiveData<User_Item> userData;


    public UserAPI( MutableLiveData<User_Item> userData,UserDao userDao) {

        this.userDao = userDao;
        this.userData =userData;

        retrofit = new Retrofit.Builder()
                //.baseUrl(MyApplication.context.getString(R.string.BaseUrl))  //we need to change it later to be save in R string
                .baseUrl("http://172.20.10.3:12345/api/")  //we need to change it later to be save in R string

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void createUser(User_Item user) {
        Call<User_Item> call = webServiceAPI.createUser(user);
        call.enqueue(new Callback<User_Item>() {
            @Override
            public void onResponse(Call<User_Item> call, Response<User_Item> response) {
                if (response.isSuccessful()) {
                    User_Item createdUser = response.body();
                    Log.d("UserAPI", "user created ");
                } else {
                    Log.d("UserAPI", "oops.....");
                }
            }

            @Override
            public void onFailure(Call<User_Item> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void createToken(String username, String password) {
        Call<TokenRes> call = webServiceAPI.createToken(username, password);
        call.enqueue(new Callback<TokenRes>() {
            @Override
            public void onResponse(Call<TokenRes> call, Response<TokenRes> response) {
                if (response.isSuccessful()) {
                    TokenRes token = response.body();
                    Log.d("UserAPI", "Token received: " + token.getToken());
                    // Handle token response here
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("UserAPI", "Failed to create token. Response code: " + response.code() + ", Error message: " + errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Handle unsuccessful response
                }
            }
            @Override
            public void onFailure(Call<TokenRes> call, Throwable t) {
                Log.e("UserAPI", "Connection to server failed with error: " + t.getMessage());

            }
        });
    }


    public void getUser(String username, String authToken) {
        Call<User_Item> call = webServiceAPI.getUser(username, authToken);
        call.enqueue(new Callback<User_Item>() {
            @Override
            public void onResponse(Call<User_Item> call, Response<User_Item> response) {
                if (response.isSuccessful()) {
                    User_Item userItem = response.body();

                    if (userItem != null) {
                        Log.d("UserAPI", "Got user successfully: " + userItem);
                        new Thread(() -> {
                            userDao.createUser(userItem);
                        }).start();
                        userData.setValue(userItem);
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            Log.d("UserAPI", "Failed to get user. Response code: " + response.code() + ", Error message: " + errorMessage);
                        } catch (IOException e) {
                            Log.e("UserAPI", "Error reading error message: " + e.getMessage());
                        }
                    }
                } else {
                    Log.d("UserAPI", "Failed to get user. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User_Item> call, Throwable t) {
                Log.e("UserAPI", "Connection to server failed with error: " + t.getMessage());
            }
        });
    }




    public void updateUser(String username,String fieldName, String fieldValue, String authToken) {
        Call<User_Item> call = webServiceAPI.updateUser(username, fieldName,fieldValue, authToken);
        call.enqueue(new Callback<User_Item>() {
            @Override
            public void onResponse(Call<User_Item> call, Response<User_Item> response) {
                if (response.isSuccessful()) {
                    User_Item updatedUser = response.body();

                    if (updatedUser != null) {
                        Log.d("UserAPI", "User updated successfully: " + updatedUser);

                        new Thread(() -> {
                            userDao.updateUser(updatedUser);
                        }).start();

                    } else {
                        Log.d("UserAPI", "Failed to update user. Response code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<User_Item> call, Throwable t) {
                Log.e("UserAPI", "Failed to updated user: " + t.getMessage());
            }
        });
    }



//
//    public void updatePost(String username, String postId, String fieldName, String fieldValue, String authToken) {
//        Call<Post_Item> call = webServiceAPI.updatePost(username, postId, fieldName, fieldValue, authToken);
//        call.enqueue(new Callback<Post_Item>() {
//            @Override
//            public void onResponse(Call<Post_Item> call, Response<Post_Item> response) {
//
//
//                if (response.isSuccessful()) {
//                    Post_Item updatedPost = response.body();
//                    if (updatedPost != null) {
//                        Log.d("PostAPI", "Post updated successfully: " + updatedPost.toString());
//
//                        new Thread(() -> {
//                            // Insert the newly updated post into the local database
//                            Post_Item postItem = response.body();
//                            postDao.updatePost(postItem);
//
//                            List<Post_Item> updatedPosts =  new ArrayList<>(postListData.getValue());
//                            updatedPosts.add(postItem);
//
//                            // Update the LiveData with the updated list of posts
//                            postListData.postValue(updatedPosts);
//                        }).start();
//
//
//
//                    } else {
//                        Log.e("PostAPI", "Received null response body");
//                    }
//                } else {
//                    try {
//                        String errorMessage = response.errorBody().string();
//                        Log.d("PostAPI", "Failed to update post. Response code: " + response.code() + ", Error message: " + errorMessage);
//                    } catch (IOException e) {
//                        Log.e("PostAPI", "Error reading error message: " + e.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Post_Item> call, Throwable t) {
//                Log.e("PostAPI", "Failed to update post: " + t.getMessage());
//            }
//        });
//    }


    public void deleteUser(String username, String authToken) {
        Call<Void> call = webServiceAPI.deleteUser(username, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        userDao.deleteUser(username);
                    }).start();

                    Log.d("UserAPI", "User deleted successfully");
                } else {
                    // Handle unsuccessful response
                    Log.d("UserAPI", "Failed to delete user. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("UserAPI", "Failed to delete user: " + t.getMessage());
            }
        });
    }

}

