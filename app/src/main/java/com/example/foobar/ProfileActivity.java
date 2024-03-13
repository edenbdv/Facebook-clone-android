package com.example.foobar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foobar.ViewModels.ProfileViewModel;
import com.example.foobar.adapters.Adapter_Profile;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;
import com.example.foobar.databinding.Profile_Binding;
import com.example.foobar.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Profile_Binding binding;
    private Adapter_Profile profileAdapter;
    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_activity);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Observe changes to user data
        viewModel.getUserItem().observe(this, new Observer<User_Item>() {
            @Override
            public void onChanged(User_Item userItem) {
                binding.setUserItem(userItem);
            }
        });

        // Observe changes to post data
        viewModel.getPosts().observe(this, new Observer<List<Post_Item>>() {
            @Override
            public void onChanged(List<Post_Item> posts) {
                profileAdapter.setPosts(posts);
            }
        });

    }
}
