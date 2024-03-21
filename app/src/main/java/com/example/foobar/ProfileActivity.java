package com.example.foobar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.adapters.Adapter_FriendRequest;
import com.example.foobar.viewModels.FriendsViewModel;
import com.example.foobar.viewModels.UserPostsViewModel;
import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.adapters.Adapter_Profile;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.webApi.UserFriendsAPI;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Adapter_FriendRequest friendRequestAdapter; // Declare FriendRequestAdapter
    private UserPostsViewModel userPostsViewModel;
    private UserViewModel userViewModel;
    private Adapter_Profile profileAdapter;
    private FriendsViewModel friendsViewModel;

    private String profile_username;

    private String current_username;
    private String authToken;

    private static final String SHARED_PREF_NAME = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Retrieve the username from intent extras
        if (getIntent().hasExtra("username")) {
            profile_username = getIntent().getStringExtra("username");
            // Use the username as needed
            Log.d("ProfileActivity", "Username: " + profile_username);
        } else {
            // Handle case where username extra is missing
            // For example, show an error message and finish the activity
            Toast.makeText(this, "Username not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Retrieve the username and token from SharedPreferences
        retrieveUserInfo();

        // Initialize ViewModel
        userPostsViewModel = new ViewModelProvider(this).get(UserPostsViewModel.class);
        userPostsViewModel.initRepo(this, profile_username);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.initRepo(this, current_username, "");
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        friendsViewModel.initRepo(this, profile_username); // Initialize FriendsViewModel

        // Initialize adapter
        profileAdapter = new Adapter_Profile();

        // Observe changes to user posts data
        userPostsViewModel.getUserPosts(profile_username).observe(this, new Observer<List<Post_Item>>() {
            @Override
            public void onChanged(List<Post_Item> posts) {
                profileAdapter.setPosts(posts);
            }
        });

        // Find the delete profile button
        Button deleteProfileButton = findViewById(R.id.delete_profile_button);
        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the deleteUser method of the UserViewModel
                userViewModel.deleteUser(profile_username);
                // Start the login activity
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
///////////////////////////////////////////////////////////////////////////
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the deleteUser method of the UserViewModel
                userViewModel.updateUser(profile_username, "username", "pipi");
            }
        });
//////////////////////////////////////////////////////////////////////////
        // Fetch user profile data
//        userViewModel.getUser(username).observe(this, new Observer<User_Item>() {
//            @Override
//            public void onChanged(User_Item user) {
//                updateUI(user);
//            }
//        });

        RecyclerView postList = findViewById(R.id.post_list);
        postList.setAdapter(profileAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));

// Add click listener to the "View Friends" button
        Button viewFriendsButton = findViewById(R.id.view_friends_button);
        viewFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch friend list data from FriendsViewModel
                friendsViewModel.getFriendList().observe(ProfileActivity.this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> friendList) {
                        // Display friend list in a dialog
                        showFriendListDialog(friendList);
                    }
                });
            }
        });

        // Add click listener to the "View Friend Requests" button
//        Button viewFriendRequestsButton = findViewById(R.id.see_friend_requests_button);
//        viewFriendRequestsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                friendsViewModel.getFriendRequests().observe(ProfileActivity.this, new Observer<List<String>>() {
//                    @Override
//                    public void onChanged(List<String> friendRequests) {
//                        if (friendRequests != null && !friendRequests.isEmpty()) {
//                            showFriendRequestsDialog(friendRequests);
//                        }
//                    }
//                });
//            }
//        });

        // Add click listener to the "View Friend Requests" button
        Button viewFriendRequestsButton = findViewById(R.id.see_friend_requests_button);
        viewFriendRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch friend requests data from FriendsViewModel
                friendsViewModel.getFriendRequests().observe(ProfileActivity.this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> friendRequests) {
                        //if (friendRequests != null && !friendRequests.isEmpty()) {
                        if (friendRequests != null) {
                            showFriendRequestsDialog(friendRequests);
                        } else {
                            // If there are no friend requests, show a toast message
                            Toast.makeText(ProfileActivity.this, "No friend requests found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Add an observer to the friend requests LiveData
//        friendsViewModel.getFriendRequests().observe(this, new Observer<List<String>>() {
//            @Override
//            public void onChanged(List<String> friendRequests) {
//                // Update the UI based on changes in the friend requests list
//                if (friendRequests != null && !friendRequests.isEmpty()) {
//                    // If friend requests list is not empty, show the dialog
//                    showFriendRequestsDialog(friendRequests);
//                } else {
//                    // If friend requests list is empty, display a message or handle it as needed
//                    // For example, show a toast message
//                    Toast.makeText(ProfileActivity.this, "No friend requests found.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        TextView usernameTextView = findViewById(R.id.user_name);
        usernameTextView.setText(profile_username);
    }

    // Method to retrieve username and token from SharedPreferences
    private void retrieveUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        current_username = sharedPreferences.getString("username", "");
        Log.d("username", current_username);
        authToken = sharedPreferences.getString("token", "");
        Log.d("token", authToken);
    }

    // Method to display friend list in a dialog
    private void showFriendListDialog(List<String> friendList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Friends");

        if (friendList.isEmpty()) {
            // Show a message indicating no friends to show
            builder.setMessage("No friends to show");
        } else {
            String[] usernames = friendList.toArray(new String[0]);
            // Show the friend list
            builder.setItems(usernames, null);
        }

        builder.setPositiveButton("OK", null);
        builder.create().show();
    }


    private void showFriendRequestsDialog(List<String> friendRequests) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Friend Requests");

        if (friendRequests.isEmpty()) {
            // If the list is empty, set the message to "No friend requests found."
            builder.setMessage("No friend requests found.");
            builder.create().show();
        } else {
            // Inflate the layout for friend request items
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_request, null);
            RecyclerView friendRequestsRecyclerView = dialogView.findViewById(R.id.friend_requests_list);

            // Create a layout manager for the RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            friendRequestsRecyclerView.setLayoutManager(layoutManager);

            // Create and set adapter for friend requests RecyclerView
            friendRequestAdapter = new Adapter_FriendRequest(friendRequests, new Adapter_FriendRequest.OnButtonClickListener() {
                @Override
                public void onAcceptButtonClick(String senderUsername) {
                    // Handle accept button click
                    acceptFriendRequest(senderUsername);
                }

                @Override
                public void onCancelButtonClick(String senderUsername) {
                    cancelFriendRequest(senderUsername);
                }
            });
            friendRequestsRecyclerView.setAdapter(friendRequestAdapter);

            builder.setView(dialogView);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    // Method to accept a friend request
    private void acceptFriendRequest(String senderUsername) {
        // Method to accept a friend request
        friendsViewModel.acceptFriendRequest(senderUsername);
        friendsViewModel.removeFriendRequest(senderUsername);
        showToast("Friend request from " + senderUsername + " accepted successfully");
    }

    private void cancelFriendRequest(String senderUsername) {
        // Method to accept a friend request
        friendsViewModel.removeFriendRequest(senderUsername);
        showToast("Friend request from " + senderUsername + " deleted successfully");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateUI(User_Item user) {
        // Update TextViews with user data
        TextView usernameTextView = findViewById(R.id.user_name);
        ImageView profilePicImageView = findViewById(R.id.profile_picture);

        usernameTextView.setText(profile_username);
    }
}