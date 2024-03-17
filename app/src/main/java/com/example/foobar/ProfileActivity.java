package com.example.foobar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.viewModels.FriendsViewModel;
import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.adapters.Adapter_Profile;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private Adapter_Profile profileAdapter;
    private FriendsViewModel friendsViewModel;

    private String username;
    private String authToken;

    private static final String SHARED_PREF_NAME = "user_prefs";
    // Constant key for username extra
    public static final String EXTRA_USERNAME = "extra_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Retrieve the username and token from SharedPreferences
        retrieveUserInfo();

        // Retrieve the username from intent extras
        if (getIntent().hasExtra(EXTRA_USERNAME)) {
            username = getIntent().getStringExtra(EXTRA_USERNAME);
        } else {
            // Handle case where username is not provided
            finish(); // Close the activity if username is not provided
            return;
        }

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        // Initialize adapter
        profileAdapter = new Adapter_Profile();

        RecyclerView postList = findViewById(R.id.post_list);
        postList.setAdapter(profileAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));

        // Observe changes to user data
//        userViewModel.getCurrentUser(username).observe(this, user -> {
//            // Update UI with user data
//            updateUI(user);
//        });

        // Observe changes to user posts
//        userViewModel.getUserPosts(username, "Bearer "+ authToken).observe(this, posts -> {
//            // Update RecyclerView with user posts
//            profileAdapter.setPosts(posts);
//        });


        // Add click listener to the "View Friends" button
        Button viewFriendsButton = findViewById(R.id.view_friends_button);
        viewFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch friend list data from FriendsViewModel
                friendsViewModel.fetchFriendList();
            }
        });

        // Observe changes to friend list data
        friendsViewModel.getFriendList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> friendList) {
                // Display friend list in a dialog or popup
                showFriendListDialog(friendList);
            }
        });
    }

    // Method to retrieve username and token from SharedPreferences
    private void retrieveUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        authToken = sharedPreferences.getString("token", "");
    }

    // Method to display friend list in a dialog
    private void showFriendListDialog(List<String> friendList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Friends");
        builder.setItems(friendList.toArray(new String[0]), null);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private void updateUI(User_Item user) {
        // Update TextViews with user data
        TextView usernameTextView = findViewById(R.id.user_name);
        ImageView profilePicImageView = findViewById(R.id.profile_picture);

        usernameTextView.setText(user.getUsername());
        // Set profile picture using user.getProfilePic()

        // You can use an image loading library like Glide or Picasso to load the image from URL
        // Glide.with(this).load(user.getProfilePic()).into(profilePicImageView);
    }
}
