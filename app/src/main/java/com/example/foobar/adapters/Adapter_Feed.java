package com.example.foobar.adapters;

import android.content.Context;

import com.example.foobar.PostViewHolder;
import com.example.foobar.R;
import com.example.foobar.entities.Post_Item;


import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Feed extends RecyclerView.Adapter<PostViewHolder> {

    private static final String SHARED_PREF_NAME = "user_prefs";

    private final Context context;
    private List<Post_Item> posts;

    private final PostViewHolder.OnPostActionListener listener; // Declare listener variable


    public Adapter_Feed(Context context, PostViewHolder.OnPostActionListener listener) {
        this.context = context;
        this.posts = new ArrayList<>();
        this.listener = listener; // Initialize listener

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        //return new PostViewHolder(view, context, this, (FeedActivity) context); // Pass the profile picture loaded listener
        return new PostViewHolder(view, context, this, listener); // Pass the profile picture loaded listener

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post_Item post = posts.get(position);
        //String username = post.getCreatedBy();
        holder.bind(post, posts);


        // Retrieve current username from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", "");

        // Check if the post was created by the current user
        if (post.getCreatedBy().equals(currentUsername)) {
            // Show the icon indicating ownership
            holder.showOwnershipIcon();
        } else {
            // Hide the icon
            holder.hideOwnershipIcon();
        }
    }

    public void SetPosts(List<Post_Item> l) {
        this.posts = l;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (posts != null) {
            return posts.size();
        } else return 0;
    }

    // Method to get the list of posts
    public List<Post_Item> getPosts() {
        return posts;
    }



}
