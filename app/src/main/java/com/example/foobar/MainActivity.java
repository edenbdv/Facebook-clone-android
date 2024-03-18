package com.example.foobar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foobar.viewModels.UserViewModel;
import com.example.foobar.webApi.UserAPI;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private UserViewModel userViewModel;
    //private UsersRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UserViewModel and UserRepository in your activity
        //userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextTextPassword);

        // Check if there's a signup success message passed from the SignUp activity
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("signup_success_message")) {
            String message = extras.getString("signup_success_message");
            // Display the message (you can customize how you want to display it)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            userViewModel.initRepo(this, username, password);

            // Perform user validation asynchronously
            new Thread(() -> {
                int result = userViewModel.validateUser(username, password);
                runOnUiThread(() -> {
                    if (result > 0) {
                        // Valid user credentials, proceed with login
                        // Call the web API to create token
                        userViewModel.getTokenLiveData().observe(this, token -> {
                            if (!token.isEmpty()) {
                                // Token received, navigate to FeedActivity
                                Log.d("Token", token);
                                saveCurrentUserAndToken(username, token);
                                navigateToFeedActivity(username);
                            }
                        });
                    } else {
                        // Invalid user credentials, show error message
                        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();

        });

        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(v -> {
            Intent i = new Intent(this, SignUp.class);
            startActivity(i);
        });
    }


    private void saveCurrentUserAndToken(String username, String token) {
        // Save the current user's username and token in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        Log.d("Saved username: ", username);
        editor.putString("token", token);
        Log.d("Saved token: ", token);
        editor.apply();
    }

    private void navigateToFeedActivity(String username) {
        // Start the FeedActivity and pass the username as an extra
        Intent intent = new Intent(MainActivity.this, FeedActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
