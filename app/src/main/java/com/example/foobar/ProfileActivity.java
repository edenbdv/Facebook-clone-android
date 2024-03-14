package com.example.foobar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    private Adapter_Profile profileAdapter;
    private UserViewModel profileViewModel;
    private FriendsViewModel friendsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize ViewModel
        profileViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        // Initialize adapter
        profileAdapter = new Adapter_Profile();

        RecyclerView postList = findViewById(R.id.post_list);
        postList.setAdapter(profileAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));

        // Observe changes to user data
//        profileViewModel.getUserItem().observe(this, new Observer<User_Item>() {
//            @Override
//            public void onChanged(User_Item userItem) {
//            }
//        });

        // Observe changes to post data
//        profileViewModel.getPosts().observe(this, new Observer<List<Post_Item>>() {
//            @Override
//            public void onChanged(List<Post_Item> posts) {
//                profileAdapter.setPosts(posts);
//            }
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

    // Method to display friend list in a dialog
    private void showFriendListDialog(List<String> friendList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Friends");
        builder.setItems(friendList.toArray(new String[0]), null);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
