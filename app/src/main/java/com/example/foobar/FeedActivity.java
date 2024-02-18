package com.example.foobar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    Adapter_Feed adapterFeed;
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private Button addPost;
    private boolean isMenuVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final Adapter_Feed adapter = new Adapter_Feed(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        // Parse JSON data and set it to adapter
        List<Post_Item> posts = JsonParser.parseJsonData(this);
        ArrayList<Post_Item> first10Posts = new ArrayList<>(posts.subList(0, Math.min(posts.size(), 10)));
        adapter.SetPosts(first10Posts);

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
        addPost.setOnClickListener(v -> {
            Intent i = new Intent(this, AddPostWindow.class);
            startActivity(i);
        });

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


//    public void showPopupWindow() {
////        AddPostWindow popupWindow = new AddPostWindow(this);
////        popupWindow.show(findViewById(R.id.postEditText));
//        AddPostWindow popupWindow = new AddPostWindow(this);
//        View rootView = getWindow().getDecorView().getRootView(); // Get the root view of the activity
//        popupWindow.show(rootView); // Show the popup window using the root view as the anchor
//    }

}