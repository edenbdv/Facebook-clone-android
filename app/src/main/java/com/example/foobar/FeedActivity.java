package com.example.foobar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.viewModels.FeedViewModel;
import com.example.foobar.viewModels.UserViewModel;

import java.util.List;

public class FeedActivity extends AppCompatActivity implements AddPostWindow.PostIdUpdater, AddPostWindow.OnPostAddedListener {

    private Adapter_Feed adapterFeed;
    private FeedViewModel feedViewModel;
    private DrawerLayout drawerLayout;
    private Button addPost;
    private int nextPostId = 11; // Starting ID for posts

    private static final String SHARED_PREF_NAME = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        // Initialize views
        initializeViews();

        // Initialize ViewModel
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedViewModel.initRepo(this);

        // Observe changes to the list of posts
        feedViewModel.getPostsLiveData().observe(this, new Observer<List<Post_Item>>() {
            @Override
            public void onChanged(List<Post_Item> postItems) {
                adapterFeed.SetPosts(postItems); // Update RecyclerView with new data
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the username from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String currentUsername = sharedPreferences.getString("username", "");
                // Handle the click event to navigate to the profile activity of the current user
                Intent intent = new Intent(FeedActivity.this, ProfileActivity.class);
                // Pass the current user's username to the profile activity if needed
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });
    }

    private void initializeViews() {
        // Initialize RecyclerView
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        adapterFeed = new Adapter_Feed(this);
        lstPosts.setAdapter(adapterFeed);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // Set click listeners
        Button addPost = findViewById(R.id.postEditText);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPostWindow();
            }
        });
    }


    private void openAddPostWindow() {
        AddPostWindow.listener = this; // Set the listener as a static variable
        Intent intent = new Intent(FeedActivity.this, AddPostWindow.class);
        intent.putExtra("username", "username"); // Pass the username
        intent.putExtra("profilePicture", "picture");
        intent.putExtra("nextPostId", nextPostId); // Pass next available ID to AddPostWindow
        startActivity(intent);
    }

    @Override
    public void onPostAdded(Post_Item newPost) {
        //adapterFeed.addPost(newPost); // Add the new post to the RecyclerView
        //adapterFeed.getPosts().add(0, newPost); // Add the new post to the beginning of the list

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        newPost.setCreatedBy(username);
        feedViewModel.createPost(newPost);
        adapterFeed.notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    @Override
    public void updateNextPostId() {
        incrementNextPostId();
    }

    private void incrementNextPostId() {
        nextPostId++;
    }

    public Adapter_Feed getAdapter() {
        return adapterFeed;
    }
}
