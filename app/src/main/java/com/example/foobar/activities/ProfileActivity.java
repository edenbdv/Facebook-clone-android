package com.example.foobar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.AddFriendRequestListener;
import com.example.foobar.ImageLoader;
import com.example.foobar.PostViewHolder;
import com.example.foobar.R;
import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.adapters.Adapter_FriendRequest;
import com.example.foobar.viewModels.FriendsViewModel;
import com.example.foobar.viewModels.UserPostsViewModel;
import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements PostViewHolder.OnPostActionListener, AddFriendRequestListener {

    private Adapter_FriendRequest friendRequestAdapter;
    private UserPostsViewModel userPostsViewModel;
    private UserViewModel userViewModel;
    private Adapter_Feed profileAdapter;

    private FriendsViewModel friendsViewModel;

    private String profile_username;

    private ImageView profileImageView;


    private String current_username;

    private String authToken;

    private ImageLoader imageLoader;


    private static final String SHARED_PREF_NAME = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageLoader = new ImageLoader();



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
        initViewModel();


        // handle profile picture
        userViewModel.getUserLiveData();
        profileImageView = findViewById(R.id.profile_picture);
        loadProfilePic();



        // Fetch friend list data from FriendsViewModel
        friendsViewModel.setListener(this);
        friendsViewModel.getFriendList().observe(ProfileActivity.this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> friendList) {
                // Determine if the current user is a friend of the profile user
                boolean isFriend = friendList != null && friendList.contains(current_username);
                Log.d("profile", "isFriend ? " + isFriend);

                // Determine if the current user is viewing their own profile
                boolean isCurrentUser = current_username.equals(profile_username);
                Log.d("profile", "is current user ? " + isCurrentUser);


                // Update UI based on user relationships
                handleUIBasedOnRelationship(isFriend, isCurrentUser);
            }
        });


        // Initialize adapter
        profileAdapter = new Adapter_Feed(this, this);


        RecyclerView postList = findViewById(R.id.post_list);
        postList.setAdapter(profileAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));


        // Observe changes to user posts data
        userPostsViewModel.getUserPosts(profile_username).observe(this, new Observer<List<Post_Item>>() {
            @Override
            public void onChanged(List<Post_Item> posts) {
                profileAdapter.SetPosts(posts);
            }
        });


        // Add Friend button click listener
        Button addFriendButton = findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle adding a friend
                onAddFriendClick(profile_username);
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


        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("profile_username", profile_username);
                intent.putExtra("current_username", current_username);

                startActivity(intent);
            }
        });


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

        // Set the profile username
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


    // Method to display friend requests in a dialog
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


    // Method to handle UI based on user relationships
    private void handleUIBasedOnRelationship(boolean isFriend, boolean isCurrentUser) {
        Button addFriendButton = findViewById(R.id.add_friend_button);

        if (isCurrentUser) {
            // only the user itself should see edit/delete profile, the friendReq list and his posts.
            findViewById(R.id.edit_profile_button).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_profile_button).setVisibility(View.VISIBLE);
            findViewById(R.id.see_friend_requests_button).setVisibility(View.VISIBLE);
            findViewById(R.id.add_friend_button).setVisibility(View.GONE);

            loadPosts();

        } else {

            findViewById(R.id.delete_profile_button).setVisibility(View.GONE);
            findViewById(R.id.edit_profile_button).setVisibility(View.GONE);
            findViewById(R.id.see_friend_requests_button).setVisibility(View.GONE);

            if (isFriend) {
                // friends should be able to see friend's list and his posts
                findViewById(R.id.view_friends_button).setVisibility(View.VISIBLE);
                findViewById(R.id.add_friend_button).setVisibility(View.GONE);

                loadPosts();

            } else {
                // strangers should be able to see add friend button
                findViewById(R.id.add_friend_button).setVisibility(View.VISIBLE);
                findViewById(R.id.view_friends_button).setVisibility(View.GONE);

                // Hide post list for strangers
                RecyclerView postList = findViewById(R.id.post_list);
                if (postList != null) {
                    postList.setVisibility(View.GONE);
                }

            }
        }


    }


    // Method to load posts
    private void loadPosts() {
        // Observe changes to user posts data

        userPostsViewModel.getUserPosts(profile_username).observe(ProfileActivity.this, new Observer<List<Post_Item>>() {
            @Override
            public void onChanged(List<Post_Item> posts) {
                // Update the RecyclerView with the posts
                RecyclerView postList = findViewById(R.id.post_list);
                if (postList != null) {
                    postList.setVisibility(View.VISIBLE);
                    profileAdapter.SetPosts(posts);
                }
            }
        });
    }

    // Implement methods from OnPostActionListener interface as needed
    @Override
    public void onPostDeleted(Post_Item delPost) {

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        delPost.setCreatedBy(username);
        userPostsViewModel.deletePost(delPost);
        profileAdapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    @Override
    public void onPostUpdatedText(Post_Item updatedPost) {

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        updatedPost.setCreatedBy(username);
        String fieldVal = updatedPost.getText();

        // Handle post text update
        userPostsViewModel.updatePost(updatedPost,"text",fieldVal);
        profileAdapter.notifyDataSetChanged();

    }

    private void onAddFriendClick(String profileUsername) {
        // Call the ViewModel method to handle sending a friend request
        friendsViewModel.addFriendRequest(profileUsername);
    }



    @Override
    public void onFriendRequestSent() {
        // Handle success, for example, show a toast
        Toast.makeText(this, "Friend request sent successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFriendRequestFailed(String errorMessage) {
        // Handle failure, for example, show an error message
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Initialize ViewModel
    public  void initViewModel() {
        userPostsViewModel = new ViewModelProvider(this).get(UserPostsViewModel.class);
        userPostsViewModel.initRepo(this, profile_username);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.initRepo(this, profile_username,current_username ,"");
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        friendsViewModel.initRepo(this, profile_username);
    }


    // load profile picture in the profile page
    public void  loadProfilePic() {

        userViewModel.getUserLiveData().observe(this, new Observer<User_Item>() {
            @Override
            public void onChanged(User_Item userItem) {
                Log.d("profile", "hiiii");


                // Update UI with user data
                if (userItem != null) {
                    String profilePicture = userItem.getProfilePic();
                    Log.d("profile", "pic: " + profilePicture);

                    Bitmap profileBitmap = null;

                     profileBitmap = imageLoader.decodeBase64ToBitmap(profilePicture);
                     if (profileBitmap != null) {
                        profileImageView.setImageBitmap(profileBitmap);

                     } else {
                        // Load profile picture from URI using the image loader
                        imageLoader.loadImageFromUri(profilePicture, profileImageView);
                    }
                }
            }
        });

    }

}