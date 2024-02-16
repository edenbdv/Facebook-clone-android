package com.example.foobar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;

import java.util.ArrayList;
public class FeedActivity extends AppCompatActivity {

    Adapter_Feed adapterFeed;
    private DrawerLayout drawerLayout;
    private ImageButton menuButton;
    private boolean isMenuVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final Adapter_Feed adapter = new Adapter_Feed(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Post_Item> posts = new ArrayList<>();
        posts.add(new Post_Item(1, 9, 2, R.drawable.post1, R.drawable.post1,
                "John Doe", "2 hrs", "The cars we drive say a lot about us.", false));

        adapter.SetPosts(posts);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
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

}