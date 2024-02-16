package com.example.foobar;

import android.os.Bundle;

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
    }
}

