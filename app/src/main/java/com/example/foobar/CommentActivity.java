package com.example.foobar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.adapters.Adapter_Comment;
import com.example.foobar.entities.Comment_Item;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewComments;
    private ArrayList<Comment_Item> commentList;
    private Adapter_Comment commentAdapter; // Replace YourCommentAdapter with the name of your adapter class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_popup);

        // Initialize RecyclerView for comments
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        commentList = new ArrayList<>();

        // Populate commentList with your comments
        commentList.add(new Comment_Item("First comment"));
        commentList.add(new Comment_Item("Second comment"));
        // Add more comments as needed

        // Initialize and set up the adapter
        commentAdapter = new Adapter_Comment(commentList); // Replace YourCommentAdapter with the name of your adapter class
        recyclerViewComments.setAdapter(commentAdapter);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        // Add comment button click listener
        Button btnAddComment = findViewById(R.id.btnAddComment);
        EditText editTextComment = findViewById(R.id.editTextComment);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText
                String newCommentText = editTextComment.getText().toString().trim();
                if (!newCommentText.isEmpty()) {
                    // Create a new comment with the user's input text
                    Comment_Item newComment = new Comment_Item(newCommentText);
                    // Add the new comment to the list
                    commentList.add(newComment);
                    // Notify the adapter that the data set has changed
                    commentAdapter.notifyDataSetChanged();
                    // Clear the EditText after adding the comment
                    editTextComment.setText("");
                } else {
                    // Display a message indicating that the comment cannot be empty
                    Toast.makeText(CommentActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

