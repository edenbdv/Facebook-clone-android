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

import com.example.foobar.adapters.Adapter_Feed;
import com.example.foobar.entities.Post_Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddPostWindow extends AppCompatActivity {

    public interface PostIdUpdater {
        void updateNextPostId();
    }

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText postContentEditText;
    private Button submitButton;
    private ImageView attachedImageView;
    private ImageButton attachImageButton;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_popup);

        int nextPostId = getIntent().getIntExtra("nextPostId", 0);
        final FeedActivity feedActivity = (FeedActivity) getIntent().getSerializableExtra("FeedActivityReference");


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
                String profilePictureUri = getIntent().getStringExtra("profilePictureUri");

                // Check if the user has provided both content and an image
                if (postContent.isEmpty() || selectedImageUri == null) {
                    // Show an error message to the user
                    Toast.makeText(AddPostWindow.this, "Please enter post content and attach an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new Post_Item object with the post content and any other necessary data
                Post_Item newPost = new Post_Item(nextPostId, 0, 0, profilePictureUri,
                        selectedImageUri.toString(), username, currentTime, postContent, false);

                // Add the new post to the feed
                addToFeed(newPost);

                // Increment the next post ID for the next post
                feedActivity.updateNextPostId();

                // Finish the activity
                finish();
            }
        });
    }


    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
    private void addToFeed(Post_Item newPost) {
        // Get the reference to the feed adapter from the FeedActivity
        FeedActivity feedActivity = (FeedActivity) getParent();
        Adapter_Feed adapter = feedActivity.getAdapter();

        // Add the new post to the adapter's data list
        adapter.getPosts().add(0, newPost); // Add at the beginning of the list

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }

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
