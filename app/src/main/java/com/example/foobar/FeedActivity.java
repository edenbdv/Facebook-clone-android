package com.example.foobar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements AddPostWindow.PostIdUpdater, AddPostWindow.OnPostAddedListener{

    Adapter_Feed adapterFeed;
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private Button addPost;
    private boolean isMenuVisible = false;
    String picture;
    private int nextPostId = 11; // Starting ID for posts


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        // Retrieve username from intent extras
        String username = getIntent().getStringExtra("username");
        // Get user details from UsersData
        HashMap<String, String> userDetails = UsersData.getUserDetails(username);

        if (userDetails != null) {
            String picture = userDetails.get("profilePictureUri");
            if (picture != null) {
                Uri pictureUri = Uri.parse(picture);
                ImageView imageView = findViewById(R.id.profilePic);
                // Load the picture into the ImageView
                imageView.setImageURI(pictureUri);
            } else {
                Toast.makeText(this, "Error: Profile picture URI is null", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: User details not found", Toast.LENGTH_SHORT).show();
        }

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        adapterFeed = new Adapter_Feed(this);
        lstPosts.setAdapter(adapterFeed);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // Parse JSON data and set it to adapter
        List<Post_Item> posts = JsonParser.parseJsonData(this);
        ArrayList<Post_Item> first10Posts = new ArrayList<>(posts.subList(0, Math.min(posts.size(), 10)));

        // load the 10  posts
        adapterFeed.SetPosts(first10Posts);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menuButton);
        addPost = findViewById(R.id.postEditText);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });

        // Set an OnClickListener to the form container
        addPost.setOnClickListener(v-> {
            AddPostWindow.listener = this; // Set the listener as a static variable
            Intent intent = new Intent(FeedActivity.this, AddPostWindow.class);
            intent.putExtra("username", username);
            intent.putExtra("profilePicture", picture);
            intent.putExtra("nextPostId", nextPostId); // Pass next available ID to AddPostWindow
            startActivity(intent);
        });

    }


    @Override
    public void onPostAdded(Post_Item newPost) {
        // Handle the newly added post here
        // You can update your RecyclerView or perform any other necessary actions
        adapterFeed.getPosts().add(0, newPost); // Add the new post to the beginning of the list
        adapterFeed.notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }





    public Adapter_Feed getAdapter() {
        return adapterFeed;
    }

    @Override
    public void updateNextPostId() {
        incrementNextPostId();
    }

    private void incrementNextPostId() {
        nextPostId++;
    }

    private void toggleMenu() {
        if (isMenuVisible) {
            drawerLayout.closeDrawer(GravityCompat.START);
            isMenuVisible = false;
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
            isMenuVisible = true;
        }
    }


}