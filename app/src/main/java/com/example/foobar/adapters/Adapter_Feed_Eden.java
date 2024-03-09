package com.example.foobar.adapters;

import android.content.Context;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.foobar.CommentActivity;
import com.example.foobar.R;
import com.example.foobar.entities.Post_Item;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
public class Adapter_Feed_Eden extends RecyclerView.Adapter<Adapter_Feed_Eden.PostViewHolder> {

    private final Context context;
    private ArrayList<Post_Item> posts;

    public Adapter_Feed_Eden(Context context) {
        this.context = context;
        this.posts = new ArrayList<>();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post_Item post = posts.get(position);
        holder.bind(post);


        // Set click listener for delete button
        holder.btnTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete post action
                deletePostById(post.getId());
            }
        });


        // Set click listener for edit button
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit post action
                editPostById(post.getId());
            }
        });



    }

    public void SetPosts(ArrayList<Post_Item> l) {
        this.posts = l;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (posts != null) {
            return posts.size();
        } else return 0;
    }

    public ArrayList<Post_Item> getPosts() {
        return posts;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        ImageButton btnEdit, btnTrash;
        TextView tv_name, tv_time, tv_status;
        ImageView imgView_proPic, imgView_postPic;
        ImageButton btnLike;
        LinearLayout likeContainer;
        LinearLayout commentContainer;
        TextView tvLike;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
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


            // Setup like button
            setupLikeButton();

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


            public void bind (Post_Item post){

                // Load text data
                tv_name.setText(post.getName());
                tv_time.setText(post.getTime());
                tv_status.setText(post.getText());

                // Load images based on post ID range
                loadImagesBasedOnPostId(post, imgView_proPic, imgView_postPic);

                // Update like button state
                updateLikeButtonState(post);
            }


            private void setupLikeButton () {
                // Handle like button click
                btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toggle like status and update UI accordingly
                        boolean isLiked = !posts.get(getAdapterPosition()).isLiked();
                        posts.get(getAdapterPosition()).setLiked(isLiked);
                        updateLikeButtonState(posts.get(getAdapterPosition()));
                    }
                });
            }

            private void updateLikeButtonState (Post_Item post){
                // Set initial like button color based on post's liked status
                int likeButtonColor = post.isLiked() ? R.color.colorPrimary : R.color.grey;
                btnLike.setColorFilter(ContextCompat.getColor(context, likeButtonColor));
                tvLike.setTextColor(ContextCompat.getColor(context, likeButtonColor)); // Update text color
            }
        }


    // Method to delete a post by its ID
    public void deletePostById(int postId) {
        int len = posts.size();
        for (int i = 0; i < len; i++) {
            if (posts.get(i).getId() == postId) {
                posts.remove(i);
                notifyItemRemoved(i);
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
                        notifyItemChanged(position);

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



    private void loadImagesBasedOnPostId(Post_Item post, ImageView proPicImageView, ImageView postPicImageView) {
        int postId = post.getId();
        if (postId >= 1 && postId <= 10) {
            // Convert profile picture path to Uri and load into ImageView from assets
            loadImageFromAssets(post.getPropic(), proPicImageView);
            loadImageFromAssets(post.getPostpic(), postPicImageView);
        } else {
            // Load profile picture and post picture from URIs
            loadImageFromUri(post.getPropic(), proPicImageView);
            loadImageFromUri(post.getPostpic(), postPicImageView);
        }
    }

    private void loadImageFromAssets(String path, ImageView imageView) {
        try {
            InputStream inputStream = context.getAssets().open(path.substring(1));
            SVG svg = SVG.getFromInputStream(inputStream);
            imageView.setImageDrawable(new PictureDrawable(svg.renderToPicture()));
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromUri(String uriString, ImageView imageView) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            imageView.setImageURI(uri);
            imageView.setVisibility(View.VISIBLE); // Make ImageView visible
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
