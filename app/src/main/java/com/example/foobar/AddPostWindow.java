package com.example.foobar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.adapters.Adapter_Feed_Eden;
import com.example.foobar.entities.Post_Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.io.File;


public class AddPostWindow extends AppCompatActivity  {

    public interface PostIdUpdater {
        void updateNextPostId();
    }

    public interface OnPostAddedListener {
        void onPostAdded(Post_Item newPost);
        Adapter_Feed_Eden getAdapter(); // New method to retrieve the adapter

    }


    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText postContentEditText;
    private Button submitButton;
    private ImageView attachedImageView;
    private ImageButton attachImageButton;

    private Uri selectedImageUri;
    public static OnPostAddedListener listener;
    private FeedActivity feedActivity; // Reference to FeedActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_popup);

        int nextPostId = getIntent().getIntExtra("nextPostId", 0);

        listener = AddPostWindow.listener; // Retrieve the listener from the static variable
        if (listener == null) {
            Toast.makeText(this, "Error: Listener not passed properly", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if listener is not provided
            return;
        }

        // Find the EditText for post content and the submit button
        postContentEditText = findViewById(R.id.editTextMessage);
        submitButton = findViewById(R.id.buttonPost);
        attachImageButton = findViewById(R.id.imageViewAttach);
        attachedImageView = findViewById(R.id.attachedImageView);

        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the content of the post from the EditText
                String postContent = postContentEditText.getText().toString();
                String currentTime = getCurrentTime();
                // Retrieve username and profile picture URI from intent extras
                String username = getIntent().getStringExtra("username");
                String profilePicture = getIntent().getStringExtra("profilePicture");

                // Check if the user has provided both content and an image
                if (postContent.isEmpty() && selectedImageUri == null) {
                    // Show an error message to the user
                    Toast.makeText(AddPostWindow.this, "Please enter post content and attach an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                Post_Item newPost;
                if (selectedImageUri != null) {
                    // If an image is attached, create a post with both text and image
                    newPost = new Post_Item(nextPostId, 0, 0, profilePicture,
                            selectedImageUri.toString(), username, currentTime, postContent, false);
                } else {
                    // If no image is attached, create a post with only text
                    newPost = new Post_Item(nextPostId, 0, 0, profilePicture,
                            "", username, currentTime, postContent, false);
                }


                // Create a new Post_Item object with the post content and any other necessary data
//                Post_Item newPost = new Post_Item(nextPostId, 0, 0, profilePicture,
//                        selectedImageUri.toString(), username, currentTime, postContent, false);

                // Notify the listener that a new post has been added
                if (listener != null) {
                    listener.onPostAdded(newPost);
                }

                // Increment the next post ID for the next post
                if (feedActivity != null) {
                    feedActivity.updateNextPostId();
                }

                // Finish the activity
                finish();
            }
        });
        ImageButton closeButton = findViewById(R.id.buttonClose);
        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the current activity and go back to the previous one
                finish();
            }
        });
    }

    // Default constructor
    public AddPostWindow() {
        // Default constructor must be empty
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener when the activity is destroyed
        listener = null;
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

//    private void addToFeed(Post_Item newPost) {
//        // Directly call getAdapter() method from the listener
//            Adapter_Feed adapter = listener.getAdapter();
//            if (adapter != null) {
//                adapter.getPosts().add(0, newPost);
//                adapter.notifyDataSetChanged();
//            }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Get selected image URI
            selectedImageUri = data.getData();

            // Display the selected image in ImageView
            attachedImageView.setImageURI(selectedImageUri);
            attachedImageView.setVisibility(View.VISIBLE); // Make ImageView visible
        }
    }
}
