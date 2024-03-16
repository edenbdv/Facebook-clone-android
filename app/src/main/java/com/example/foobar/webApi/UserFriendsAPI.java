package com.example.foobar.webApi;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFriendsAPI {

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;

    public UserFriendsAPI() {

        retrofit = new Retrofit.Builder()
                //.baseUrl(MyApplication.context.getString(R.string.BaseUrl))  //we need to change it later to be save in R string
                .baseUrl("http://192.168.1.23:12345/api/")  //we need to change it later to be save in R string

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public void getUserFriends(String username, String authToken) {
        Call<JsonObject> call = webServiceAPI.getUserFriends(username, authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject responseBody = response.body();
                    if (responseBody != null) {
                        // Process the response based on its structure
                        // For example, if the response contains an array under a specific key:
                        JsonArray friendsArray = responseBody.getAsJsonArray("friends");
                        if (friendsArray != null && friendsArray.size() > 0) {
                            List<String> friends = new ArrayList<>();
                            for (JsonElement element : friendsArray) {
                                friends.add(element.getAsString());
                            }
                            Log.d("UserAPI", username + " Friends: " + friends);
                        } else {
                            Log.d("UserAPI", "User has no friends or response format is unexpected.");
                        }
                    } else {
                        Log.d("UserAPI", "Empty response body.");
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("UserAPI", "Failed to get user Friends. Response code: " + response.code() + ", Error message: " + errorMessage);
                    } catch (IOException e) {
                        Log.e("UserAPI", "Error reading error message: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("UserAPI", "Connection to server failed with error: " + t.getMessage());
            }
        });
    }


    public void addFriendRequest(String receiverUsername, String authToken) {
        Call<Void> call = webServiceAPI.addFriendReq(receiverUsername, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "Friend request sent successfully");
                    // Handle success if needed
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("UserAPI", "Failed to send friend request. Response code: " + response.code() + ", Error message: " + errorMessage);
                        // Handle failure if needed
                    } catch (IOException e) {
                        Log.e("UserAPI", "Error reading error message: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Connection to server failed with error: " + t.getMessage());
            }
        });
    }


    public void acceptReq(String senderId , String receiverId, String authToken) {
        Call<Void> call = webServiceAPI.acceptReq(senderId, receiverId, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "Friend request accepted successfully");
                    // Optionally, you can reload the friend list or perform any other action
                } else {
                    Log.e("UserAPI", "Failed to accept friend request. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Failed to accept friend request. Error: " + t.getMessage());
            }
        });


    }

    public void deleteFriend(String user, String friend, String authToken) {
        Call<Void> call = webServiceAPI.deleteFriend(user, friend, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "Friend deleted successfully");
                    // Optionally, you can update the UI or perform any other action
                } else {
                    Log.e("UserAPI", "Failed to delete friend. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Failed to delete friend. Error: " + t.getMessage());
            }
        });
    }

}