package com.example.foobar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;

public class FeedActivityTry extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_try);

        String username = getIntent().getStringExtra("username");

        // Retrieve stored user details from UsersData
        HashMap<String, String> storedUserData = UsersData.getUserDetails(username);


        // Example: Retrieve other user details
        if (storedUserData != null) {
            String password = storedUserData.get("password");
            String displayName = storedUserData.get("displayName");
            String profilePictureUri = storedUserData.get("profilePictureUri");

            // Display user details in TextViews
            // Display the username
            TextView usernameTextView = findViewById(R.id.usernameTextView);
            usernameTextView.setText("Username: " + username);

            TextView displayNameTextView = findViewById(R.id.displayNameTextView);
            displayNameTextView.setText("Display Name: " + displayName);

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);


//            if (profilePictureUri != null) {
//                ImageView imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture);
//                imageViewProfilePicture.setImageURI(Uri.parse(profilePictureUri));
//                imageViewProfilePicture.setVisibility(View.VISIBLE); // Make the ImageView visible
//
//            }
        }
    }

}