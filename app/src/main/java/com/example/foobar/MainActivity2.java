package com.example.foobar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, FeedActivity.class);
        //i.putExtra("username", username); // Pass the username to FeedActivity
        startActivity(i);
    }
}
//
//        usernameEditText = findViewById(R.id.editTextText);
//        passwordEditText = findViewById(R.id.editTextTextPassword);
//
//
//        // Check if there's a signup success message passed from the SignUp activity
//        Bundle extras = getIntent().getExtras();
//        if (extras != null && extras.containsKey("signup_success_message")) {
//            String message = extras.getString("signup_success_message");
//            // Display the message (you can customize how you want to display it)
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        }
//
//
//        Button btnLogin = findViewById(R.id.btnLogin);
//        btnLogin.setOnClickListener(v -> {
//            boolean isValid = validateForm();
//            if(isValid) {
//                String username = usernameEditText.getText().toString();
//
//                Intent i = new Intent(this, FeedActivity.class);
//                i.putExtra("username", username); // Pass the username to FeedActivity
//                startActivity(i);
//            }
//
//        });
//
//
//
//        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
//        btnCreateAccount.setOnClickListener(v -> {
//            Intent i = new Intent(this, FeedActivity.class);
//            startActivity(i);
//        });
//
//    }
//
//    private boolean validateForm() {
//
//        boolean isValid = true;
//        String username = usernameEditText.getText().toString();
//        String password = passwordEditText.getText().toString();
//
//        // Retrieve all existing usernames from UsersData
//        HashMap<String, HashMap<String, String>> allUsers = UsersData.getAllUsers();
//
//
//        // Validate username
//        if (username.isEmpty()) {
//            usernameEditText.setError("Username is required");
//            isValid = false;
//        } else if ((allUsers.isEmpty()) || (!allUsers.containsKey(username))) {
//            usernameEditText.setError("Invalid username");
//            isValid = false;
//        } else {   // Validate password
//            // Retrieve stored user details from UsersData
//            HashMap<String, String> storedUserData = UsersData.getUserDetails(username);
//            String storedPassword = storedUserData.get("password");
//            if (password.isEmpty()) {
//                passwordEditText.setError("Password is required");
//                isValid = false;
//            } else if (!password.equals(storedPassword)) {
//                passwordEditText.setError("Invalid password");
//                isValid = false;
//            }
//        }
//        return isValid;
//    }
//}
//
