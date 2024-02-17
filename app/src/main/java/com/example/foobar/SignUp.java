package com.example.foobar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.IOException;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText displayNameEditText;
    private ImageView imageView;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            usernameEditText = findViewById(R.id.editTextUsername);
            passwordEditText = findViewById(R.id.editTextPassword);
            confirmPasswordEditText = findViewById(R.id.editTextPasswordV);
            displayNameEditText = findViewById(R.id.editTextDisplayName);

            imageView = findViewById(R.id.imageView);
            Button uploadButton = findViewById(R.id.btnUploadPhoto);
            Button submitButton  = findViewById(R.id.btnSubmit);


            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFileChooser();
                }
            });

            submitButton.setOnClickListener(v2 -> {
                // Validate input fields
                if (validateForm()) {
                    // If validation succeeds, proceed with form submission
                    saveUserDetails();
                    submitForm();
                }

            });

        }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
             imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setVisibility(View.VISIBLE); // Make the ImageView visible
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    // Method to validate the form input fields
    private boolean validateForm() {

        boolean isValid = true;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String displayName = displayNameEditText.getText().toString();

        // Retrieve all existing usernames from UsersData
        HashMap<String, HashMap<String, String>> allUsers = UsersData.getAllUsers();


        // Validate username
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            isValid = false;
        } else  if (allUsers.containsKey(username)) {
            usernameEditText.setError("Username already exists");
            isValid = false;
        } else if (!username.matches("^[a-zA-Z].*")) {
            usernameEditText.setError("Username must start with a letter");
            isValid = false;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            isValid = false;
        } else if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters long");
            isValid = false;
        } else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            passwordEditText.setError("Password must contain uppercase letters, lowercase letters, and digits");
            isValid = false;
        }


        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            isValid = false;
        }

        // Validate display name
        if (displayName.isEmpty()) {
            displayNameEditText.setError("Display Name is required");
            isValid = false;
        }
        return  isValid;
        }

    // Method to save user details to the UserData class
    private void saveUserDetails() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String displayName = displayNameEditText.getText().toString();

        // Get the profile picture URI from the selected image
        String profilePictureUri = null;
        if (imageUri != null) {
            profilePictureUri = imageUri.toString();
        }

        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put("password", password);
        userDetails.put("displayName", displayName);
        userDetails.put("profilePictureUri", profilePictureUri); // Add profile picture URI to userDetails
        UsersData.addUser(username, userDetails);
    }



    // Method to submit the form
    private void submitForm() {
        Intent i = new Intent(this, MainActivity2.class);
        // Add an extra with the message
        i.putExtra("signup_success_message", "Signup successful! You can now log in.");
        // Start the activity
        startActivity(i);

    }
    private void checkPictureUri(String username) {
        HashMap<String, String> userDetails = UsersData.getUserDetails(username);
        if (userDetails != null) {
            String profilePictureUri = userDetails.get("profilePictureUri");
            if (profilePictureUri != null) {
                // Profile picture URI exists
                System.out.println("Profile picture URI for user " + username + ": " + profilePictureUri);
            } else {
                // Profile picture URI does not exist
                System.out.println("No profile picture URI found for user " + username);
            }
        } else {
            // User not found
            System.out.println("User " + username + " not found");
        }
    }



}




