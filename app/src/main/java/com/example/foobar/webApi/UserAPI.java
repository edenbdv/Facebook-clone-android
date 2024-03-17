package com.example.foobar.webApi;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.UserDao;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.TokenRes;
import com.example.foobar.entities.User_Item;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {

    private MutableLiveData<String> tokenLiveData;
    private UserDao userDao;

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public UserAPI(MutableLiveData<String> tokenLiveData, UserDao userDao) {

        this.tokenLiveData = tokenLiveData;
        this.userDao = userDao;

        retrofit = new Retrofit.Builder()
                //.baseUrl(MyApplication.context.getString(R.string.BaseUrl))  //we need to change it later to be save in R string
                .baseUrl("http://192.168.0.103:12345/api/")  //we need to change it later to be save in R string

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
                    TokenRes tokenRes = response.body();
                    String token = tokenRes.getToken();
                    Log.d("Token", token); // Print the token to the logcat
                    tokenLiveData.postValue(token); // Update LiveData with the generated token
                } else {
                    Log.e("UserAPI", "Failed to generate token. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenRes> call, Throwable t) {
                Log.e("UserAPI", "Failed to generate token: " + t.getMessage());
            }
        });
    }


//    public void createToken(String username, String password) {
//        // return webServiceAPI.createToken(username, password);
//        Call<TokenRes> call = webServiceAPI.createToken(username, password);
//        call.enqueue(new Callback<TokenRes>() {
//            @Override
//            public void onResponse(Call<TokenRes> call, Response<TokenRes> response) {
//                if (response.isSuccessful()) {
//                    TokenRes token = response.body();
//                    Log.d("UserAPI", "Token received: " + token.getToken());
//                    // Handle token response here
//                } else {
//                    try {
//                        String errorMessage = response.errorBody().string();
//                        Log.d("UserAPI", "Failed to create token. Response code: " + response.code() + ", Error message: " + errorMessage);
//
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    // Handle unsuccessful response
//                }
//            }
//            @Override
//            public void onFailure(Call<TokenRes> call, Throwable t) {
//                Log.e("UserAPI", "Connection to server failed with error: " + t.getMessage());
//
//            }
//        });
//    }


    // ask noga how handle if the user itself/other user asked
    public void getUser(String username, String authToken) {
        Call<User_Item> call = webServiceAPI.getUser(username, authToken);
        call.enqueue(new Callback<User_Item>() {
            @Override
            public void onResponse(Call<User_Item> call, Response<User_Item> response) {
                if (response.isSuccessful()) {

                    Log.d("UserAPI","username: "+ response.body().getUsername());
                    Log.d("UserAPI","password: "+response.body().getPassword());
                    Log.d("UserAPI","display name: "+response.body().getDisplayName());
                    Log.d("UserAPI","profile pic: "+response.body().getProfilePic());
                    Log.d("UserAPI","friendRequests: "+response.body().getFriendRequests());


                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("UserAPI", "Failed to get user. Response code: " + response.code() + ", Error message: " + errorMessage);
                    } catch (IOException e) {
                    }
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
                    Log.d("UserAPI", "User updated successfully: " +updatedUser);
                    // Handle updated user data here
                } else {
                    Log.d("UserAPI", "Failed to update user. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User_Item> call, Throwable t) {
                Log.e("UserAPI", "Failed to updated user: " + t.getMessage());
            }
        });
    }


    public void deleteUser(String username, String authToken) {
        Call<Void> call = webServiceAPI.deleteUser(username, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful deletion
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
