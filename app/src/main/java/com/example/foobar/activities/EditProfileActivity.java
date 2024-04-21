package com.example.foobar.activities;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foobar.ImageLoader;
import com.example.foobar.R;
import com.example.foobar.entities.User_Item;
import com.example.foobar.viewModels.UserViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditProfileActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 2;
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView displayNameTextView;
    private TextView passwordTextView;

    private ImageView ProfilePicView;
    private UserViewModel userViewModel;

    private ImageLoader imageLoader;

    private String profile_username;
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        profile_username = getIntent().getStringExtra("profile_username");
        String current_username = getIntent().getStringExtra("current_username");

        imageLoader = new ImageLoader();


        // Initialize TextViews
        displayNameTextView = findViewById(R.id.textViewCurrentDisplay);
        passwordTextView = findViewById(R.id.textViewCurrentPassword);



        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.initRepo(this, profile_username,current_username ,"");

        // handle profile picture
        userViewModel.getUserLiveData();
        ProfilePicView = findViewById(R.id.currentProfilePic);
        loadDetails();


        ImageView editDisplayNameIcon = findViewById(R.id.imgEditDisplay);
        ImageView editPasswordIcon = findViewById(R.id.imgEditPassword);
        ImageView editProfilePicIcon = findViewById(R.id.imgEditProfilePic);


        // Set click listeners on edit icons
        editDisplayNameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a window to allow editing the display name
                openEditDisplayNameWindow();
            }
        });

        editPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a window to allow editing the password
                openEditPasswordWindow();
            }
        });

        editProfilePicIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a window to allow editing the profile picture
                openUploadProfilePicDialog();
            }
        });



    }


    public void  loadDetails() {

        userViewModel.getUserLiveData().observe(this, new Observer<User_Item>() {
            @Override
            public void onChanged(User_Item userItem) {

                // Update UI with user data
                if (userItem != null) {


                    // Set the text of TextViews with user details
                    displayNameTextView.setText( userItem.getDisplayName());
                    passwordTextView.setText(userItem.getPassword());

                    String profilePicture = userItem.getProfilePic();
                    Log.d("edit", "pic: " + profilePicture);

                    Bitmap profileBitmap = null;

                    profileBitmap = imageLoader.decodeBase64ToBitmap(profilePicture);
                    if (profileBitmap != null) {
                        ProfilePicView.setImageBitmap(profileBitmap);

                    } else {
                        // Load profile picture from URI using the image loader
                        imageLoader.loadImageFromUri(profilePicture, ProfilePicView);
                    }
                }
            }
        });

    }

    public void openEditDisplayNameWindow() {
        // Create an EditText to get the new comment text
        final EditText input = new EditText(this);

        String oldDisplayName = displayNameTextView.getText().toString();


        // Set any attributes you want for the EditText
        input.setText(oldDisplayName);

        // Create an AlertDialog with the EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit  display name");
        builder.setView(input);

        // Set up the buttons for positive (Save) and negative (Cancel) actions
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedDisplayText = input.getText().toString();

                // Send request to server to update the display name
                userViewModel.updateUser(profile_username,"displayName",editedDisplayText);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void   openEditPasswordWindow() {
        // Create an EditText to get the new comment text
        final EditText input = new EditText(this);
        String oldPassword = passwordTextView.getText().toString();


        // Set any attributes you want for the EditText
        input.setText(oldPassword);

        // Create an AlertDialog with the EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit password");
        builder.setView(input);

        // Set up the buttons for positive (Save) and negative (Cancel) actions
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedPassword = input.getText().toString();

                // Send request to server to update the display name
                userViewModel.updateUser(profile_username,"password",editedPassword);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void openUploadProfilePicDialog() {
        // Create a dialog with options to select from gallery or take a photo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Profile Picture");

        // Add options to select from gallery or take a photo
        String[] options = {"Select from Gallery", "Take a Photo"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Option to select from gallery
                        openGallery();
                        break;
                    case 1:
                        // Option to take a photo
                        openCamera();
                        break;
                }
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // User selected image from gallery
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ProfilePicView.setImageBitmap(bitmap);

                // Update user profile picture URI in the ViewModel
                userViewModel.updateUser(profile_username, "profilePic", imageUri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
            // User captured image from camera
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            // Handle the captured image bitmap as needed
            // For example, display it in an ImageView
            ProfilePicView.setImageBitmap(bitmap);

            // Convert the bitmap to a URI and update user profile picture URI in the ViewModel
            Uri imageFileUri = saveImageToFile(bitmap);
            userViewModel.updateUser(profile_username, "profilePic", imageFileUri.toString());
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



