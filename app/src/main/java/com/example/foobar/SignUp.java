package com.example.foobar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.example.foobar.entities.User_Item;
import com.example.foobar.repositories.UsersRepository;
import com.example.foobar.viewModels.UserViewModel;

public class SignUp extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText displayNameEditText;
    private ImageView imageView;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.initRepo(this, "", "");

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
        // Create an intent to capture image from camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create an intent to pick image from gallery
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Create chooser to display camera and gallery options
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });

        // Start the activity with the chooser intent
        startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                // User selected image from gallery
                imageUri = data.getData();
            } else if (data.getExtras() != null && data.getExtras().get("data") != null) {
                // User captured image from camera
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                // Resize the captured image to a larger size
                Bitmap resizedBitmap = resizeBitmap(bitmap, 1200, 880);

                // Load the resized image into the ImageView
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(resizedBitmap);

                // Save the resized image to a file and obtain a URI for future reference
                Uri imageFileUri = saveImageToFile(resizedBitmap);
                imageUri = imageFileUri; // Assign the image URI for future reference if needed

                return; // Exit the method
            }
        }

        // Load the selected or captured image into the ImageView
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to resize the bitmap to a desired width and height
    private Bitmap resizeBitmap(Bitmap bitmap, int desiredWidth, int desiredHeight) {
        return Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true);
    }
    // Method to validate the form input fields
    private boolean validateForm() {

        boolean isValid = true;
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String displayName = displayNameEditText.getText().toString();

        // Validate username
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            isValid = false;
//        } else  if (allUsers.containsKey(username)) {
//            usernameEditText.setError("Username already exists");
//            isValid = false;
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

        // Validate profile picture
        if (imageUri == null) {
            Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
            isValid =false;
        }

        return  isValid;
    }

    // Method to save user details to the UserData class
    private void saveUserDetails() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String displayName = displayNameEditText.getText().toString();
        // Get the profile picture URI from the selected image
        //String profilePictureUri = null;
        String profilePictureUri = "";
        if (imageUri != null) {
            profilePictureUri = imageUri.toString();
        }
        User_Item newUser = new User_Item(username, password, displayName, profilePictureUri);
        userViewModel.createUser(newUser);
    }



    // Method to submit the form
    private void submitForm() {
        Intent i = new Intent(this, MainActivity.class);
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

    private Uri saveImageToFile(Bitmap bitmap) {
        // Save the bitmap to a file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(imageFile);
}

}