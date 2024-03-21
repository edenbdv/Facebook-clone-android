package com.example.foobar;

import static android.app.PendingIntent.getActivity;
import com.example.foobar.ImageLoader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import com.example.foobar.ImageLoader;
import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.viewModels.FeedViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class PostViewHolder extends RecyclerView.ViewHolder {

    public interface OnPostActionListener {
        void onPostDeleted(Post_Item post);
        void onPostUpdatedText(Post_Item post);
    }

     private OnPostActionListener onPostActionListener;
    private static final String SHARED_PREF_NAME = "user_prefs";


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





    public PostViewHolder(@NonNull View itemView, Context context, Adapter_Feed adapter,OnPostActionListener listener) {
        super(itemView);
        this.context = context;
        this.adapter=adapter;
        this.onPostActionListener = listener;





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

        // Set  listener
        if (context instanceof OnPostActionListener) {
            onPostActionListener = (OnPostActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPostDeleteListener");
        }

    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        if (base64String == null) {
            return null;
        }
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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

        ImageLoader imageLoader = new ImageLoader();
        imageLoader.loadProfilePicture(post.getCreatedBy());


        // Create an instance of ImageLoader
        //ImageLoader imageLoader = new ImageLoader(profilePictureLoadedListener);

        // Load images based on post ID range
        imageLoader.loadImagesBasedOnPostId(context, post, imgView_proPic, imgView_postPic);


        // Decode base64 string to Bitmap and set it to ImageView
        Bitmap bitmap = decodeBase64ToBitmap(post.getPicture());
        imgView_postPic.setImageBitmap(bitmap);

        // Load profile picture
       // ImageHandler.loadProfilePicture(post.getCreatedBy());


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
                intent.putExtra("username", post.getCreatedBy()); // Dummy username
                intent.putExtra("profilePicture", "dummy_profile_picture_uri"); // Dummy profile picture URI
                context.startActivity(intent);
            }
        });

        // Set click listener for delete button
        btnTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete post action
                deletePostById(post, post.getId());
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
    public void deletePostById(Post_Item post,int postId) {
        int len = posts.size();
        for (int i = 0; i < len; i++) {
            if (posts.get(i).getId() == postId) {
                posts.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }

        if (onPostActionListener != null) {
            onPostActionListener.onPostDeleted(post);
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
                        // Update the post text (need to add update to photo also!!!!!!!!!!!)
                        Post_Item post = posts.get(position);
                        post.setText(editedPostText);
                        if (onPostActionListener != null) {
                            onPostActionListener.onPostUpdatedText(post);
                        }

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

    // Method to compress and encode the image
    private String compressAndEncodeImage(String base64Image) {
        // Convert base64 string to byte array
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);

        // Decode byte array to Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        // Compress the Bitmap (adjust compression quality as needed)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        // Encode the compressed Bitmap to base64 string
        byte[] compressedBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(compressedBytes, Base64.DEFAULT);
    }

    private void updateLikeButtonState (Post_Item post){
        // Set initial like button color based on post's liked status
        int likeButtonColor = post.isLiked() ? R.color.colorPrimary : R.color.grey;
        btnLike.setColorFilter(ContextCompat.getColor(context, likeButtonColor));
        tvLike.setTextColor(ContextCompat.getColor(context, likeButtonColor)); // Update text color
    }

    // Method to show the edit and delete icons
    public void showOwnershipIcon() {
        btnEdit.setVisibility(View.VISIBLE);
        btnTrash.setVisibility(View.VISIBLE);
    }

    // Method to hide the edit and delete icons
    public void hideOwnershipIcon() {
        btnEdit.setVisibility(View.GONE);
        btnTrash.setVisibility(View.GONE);
    }


}