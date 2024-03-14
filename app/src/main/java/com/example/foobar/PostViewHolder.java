package com.example.foobar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;

import com.example.foobar.ImageLoader;
import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostViewHolder extends RecyclerView.ViewHolder {

    ImageButton btnEdit, btnTrash;
    TextView tv_name, tv_time, tv_status;
    ImageView imgView_proPic, imgView_postPic;
    ImageButton btnLike;
    LinearLayout likeContainer;
    LinearLayout commentContainer;
    TextView tvLike;

    private Context context;
    private List<Post_Item> posts;
    private Adapter_Feed adapter;

    public PostViewHolder(@NonNull View itemView, Context context, Adapter_Feed adapter) {
        super(itemView);
        this.context = context;
        this.adapter=adapter;

        // Initialize other views
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnTrash = itemView.findViewById(R.id.btnTrash);

        imgView_proPic = itemView.findViewById(R.id.proPic);
        imgView_postPic = itemView.findViewById(R.id.postPic);

        tv_name = itemView.findViewById(R.id.name);
        tv_time = itemView.findViewById(R.id.post_time);
        tv_status = itemView.findViewById(R.id.post_text);

        btnLike = itemView.findViewById(R.id.btnLike);
        likeContainer = itemView.findViewById(R.id.likeContainer);
        tvLike = itemView.findViewById(R.id.tvLike);

        commentContainer = itemView.findViewById(R.id.commentContainer);
    }

    public void bind(Post_Item post, List<Post_Item> posts) {
        this.posts = posts;
        // Load text data
        tv_name.setText(post.getCreatedBy());
        //tv_time.setText((CharSequence) post.getCreatedAt());
        Date createdAt = post.getCreatedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Customize the format as needed
        String formattedDate = sdf.format(createdAt);
        tv_time.setText(formattedDate); // Set the formatted date string to the TextView
        tv_status.setText(post.getText());

        ImageLoader ImageHandler = new ImageLoader();
        // Load images based on post ID range
        ImageHandler.loadImagesBasedOnPostId(context, post, imgView_proPic, imgView_postPic);

        // Update like button state
        updateLikeButtonState(post);


        // Handle like button click
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle like status and update UI accordingly
                boolean isLiked = !post.isLiked();
                post.setLiked(isLiked);
                updateLikeButtonState(post);
            }
        });

        // Set click listener for the name TextView
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ProfileActivity with dummy parameters
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("username", "dummy_username"); // Dummy username
                intent.putExtra("profilePicture", "dummy_profile_picture_uri"); // Dummy profile picture URI
                context.startActivity(intent);
            }
        });

        // Set click listener for delete button
        btnTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete post action
                deletePostById(post.getId());
            }
        });

        // Set click listener for edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit post action
                editPostById(post.getId());
            }
        });
        // Setup click listener for comment container
        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CommentActivity
                Intent intent = new Intent(context, CommentActivity.class);
                context.startActivity(intent);
            }
        });
    }

    // Method to delete a post by its ID
    public void deletePostById(int postId) {
        int len = posts.size();
        for (int i = 0; i < len; i++) {
            if (posts.get(i).getId() == postId) {
                posts.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    // Method to edit a post by its ID
    public void editPostById(int postId) {
        // Find the post by its ID
        for (int i = 0; i < posts.size(); i++) {
            Post_Item post = posts.get(i);
            if (post.getId() == postId) {

                // Create an EditText to get the new post text
                final EditText input = new EditText(context);
                input.setText(post.getText()); // Set the original post text as a placeholder


                final int position = i;

                // Create an AlertDialog with the EditText
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Post");
                builder.setView(input);

                // Set up the buttons for positive (Save) and negative (Cancel) actions
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editedPostText = input.getText().toString();
                        // Update the post text
                        Post_Item post = posts.get(position);
                        post.setText(editedPostText);
                        // Notify adapter that the item has changed at the specified position
                        adapter.notifyItemChanged(getAdapterPosition());

                        // Handle picture editing

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel editing
                        dialog.cancel();
                    }
                });

                // Show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                break; // Exit the loop once the post is found
            }
        }
    }

    private void updateLikeButtonState (Post_Item post){
        // Set initial like button color based on post's liked status
        int likeButtonColor = post.isLiked() ? R.color.colorPrimary : R.color.grey;
        btnLike.setColorFilter(ContextCompat.getColor(context, likeButtonColor));
        tvLike.setTextColor(ContextCompat.getColor(context, likeButtonColor)); // Update text color
    }



}