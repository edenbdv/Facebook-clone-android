package com.example.foobar.webApi;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.foobar.MyApplication;
import com.example.foobar.AddFriendRequestListener ;
import com.example.foobar.R;
import com.example.foobar.daos.FriendRequestDao;
import com.example.foobar.daos.FriendshipDao;
import com.example.foobar.entities.FriendRequest;
import com.example.foobar.entities.Friendship;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
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

    private FriendshipDao friendshipDao;

    private FriendRequestDao friendRequestDao;

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private MutableLiveData<List<String>> friendsListData;
    private MutableLiveData<List<String>> friendsRequestsData;

    public UserFriendsAPI(FriendshipDao friendshipDao, FriendRequestDao friendRequestDao,
                          MutableLiveData<List<String>> friendsListData, MutableLiveData<List<String>> friendsRequestData) {

        this.friendshipDao = friendshipDao;
        this.friendRequestDao = friendRequestDao;
        this.friendsListData = friendsListData;
        this.friendsRequestsData = friendsRequestData;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.base_url))
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
                        JsonArray friendsArray = responseBody.getAsJsonArray("friends");

                        if (friendsArray != null && friendsArray.size() > 0) {
                            //Perform database operations asynchronously
                            new Thread(() -> {
                                friendshipDao.clear(); // Clear existing data in the table
                                List<String> friends = new ArrayList<>();
                                Friendship friendship;
                                for (JsonElement element : friendsArray) {
                                    String friendUsername = element.getAsString();
                                    friendship = new Friendship(username,friendUsername);
                                    friendshipDao.insert(friendship);
                                    friends.add(friendUsername);
                                }
                                friendsListData.postValue(friends);
                            }).start();

                            Log.d("UserAPI", "got user friends");
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


    public void getFriendRequests(String username, String authToken) {
        Log.d("API", "getFriendRequests");
        Call<JsonObject> call = webServiceAPI.getFriendRequests(username, authToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("UserFriendsAPI", "trying to send ");

                if (response.isSuccessful()) {
                    JsonObject responseBody = response.body();
                    if (responseBody != null) {
                        Log.d("UserFriendsAPI", "Response body: " + responseBody.toString());
                        JsonArray requestsArray = responseBody.getAsJsonArray("friendReqs");

                        if (requestsArray != null && requestsArray.size() > 0) {
                            // Perform database operations asynchronously
                            new Thread(() -> {
                                friendRequestDao.clear(); // Clear existing data in the table
                                List<String> requests = new ArrayList<>();
                                for (JsonElement element : requestsArray) {
                                    String senderUsername = element.getAsString();
                                    Log.d("API", senderUsername);
                                    requests.add(senderUsername);
                                }
                                // Update LiveData with the list of friend requests
                                friendsRequestsData.postValue(requests);
                            }).start();
                            Log.d("UserAPI", "got user friend requests");
                        } else {
                            Log.d("UserAPI", "User has no friend requests or response format is unexpected.");
                        }
                    } else {
                        Log.d("UserAPI", "Empty response body.");
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("UserAPI", "Failed to get user friend requests. Response code: " + response.code() + ", Error message: " + errorMessage);
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


    public void addFriendRequest(String receiverUsername, String authToken,final AddFriendRequestListener  listener) {
        Call<Void> call = webServiceAPI.addFriendReq(receiverUsername, authToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("UserAPI", "Friend request sent successfully");
                    listener.onFriendRequestSent();
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.d("UserAPI", "Failed to send friend request. Response code: " + response.code() + ", Error message: " + errorMessage);
                        listener.onFriendRequestFailed(errorMessage);
                    } catch (IOException e) {
                        Log.e("UserAPI", "Error reading error message: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserAPI", "Connection to server failed with error: " + t.getMessage());
                listener.onFriendRequestFailed(t.getMessage());

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