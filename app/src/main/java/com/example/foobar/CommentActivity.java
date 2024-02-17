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
import com.example.foobar.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerViewComments;
    private ArrayList<Comment_Item> commentList;
    private Adapter_Comment commentAdapter;
    private String postId;

    private static Map<String, List<Comment_Item>> commentCache = new HashMap<>(); // In-memory cache for comments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_popup);

        postId = getIntent().getStringExtra("postId");

        recyclerViewComments = findViewById(R.id.recyclerViewComments);

        // Check if comments for the post are already in the cache
        if (commentCache.containsKey(postId)) {
            commentList = new ArrayList<>(commentCache.get(postId));
        } else {
            commentList = new ArrayList<>();
        }


        commentAdapter = new Adapter_Comment(commentList,this);
        recyclerViewComments.setAdapter(commentAdapter);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

        // Add comment button click listener
        Button btnAddComment = findViewById(R.id.btnAddComment);
        EditText editTextComment = findViewById(R.id.editTextComment);
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newCommentText = editTextComment.getText().toString().trim();
                if (!newCommentText.isEmpty()) {
                    Comment_Item newComment = new Comment_Item(postId, newCommentText);
                    commentList.add(newComment);
                    // Update the cache
                    commentCache.put(postId, new ArrayList<>(commentList));
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

