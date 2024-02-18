package com.example.foobar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements AddPostWindow.PostIdUpdater{

    Adapter_Feed adapterFeed;
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private Button addPost;
    private boolean isMenuVisible = false;
    private int nextPostId = 11; // Starting ID for posts


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        // Retrieve username from intent extras
        String username = getIntent().getStringExtra("username");
        // Get user details from UsersData
        HashMap<String, String> userDetails = UsersData.getUserDetails(username);
        String picture = userDetails.get("profilePictureUri");
        Uri pictureUri = Uri.parse(picture);
        ImageView imageView = findViewById(R.id.profilePic);
        // Load the picture into the ImageView
        imageView.setImageURI(pictureUri);



        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        adapterFeed = new Adapter_Feed(this);
        lstPosts.setAdapter(adapterFeed);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // Parse JSON data and set it to adapter
        List<Post_Item> posts = JsonParser.parseJsonData(this);
        ArrayList<Post_Item> first10Posts = new ArrayList<>(posts.subList(0, Math.min(posts.size(), 10)));
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
            Intent intent = new Intent(FeedActivity.this, AddPostWindow.class);
            intent.putExtra("username", username);
            intent.putExtra("profilePictureUri", picture);
            intent.putExtra("nextPostId", nextPostId); // Pass next available ID to AddPostWindow
            //intent.putExtra("FeedActivityReference", this); // Pass instance of PostIdUpdater
            startActivity(intent);
        });

    }

    @Override
    public void updateNextPostId() {
        incrementNextPostId();
    }

    public Adapter_Feed getAdapter() {
        return adapterFeed;
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