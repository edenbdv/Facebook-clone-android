package com.example.foobar.adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.R;
import com.example.foobar.entities.Comment_Item;
import android.util.Log;


import java.util.List;

public class Adapter_Comment extends RecyclerView.Adapter<Adapter_Comment.CommentViewHolder> {

    private List<Comment_Item> comments;
    private Context context;


    public Adapter_Comment(List<Comment_Item> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        Comment_Item comment = comments.get(position);
        holder.textViewComment.setText(comment.getText());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete comment action
                deleteCommentById(comment.getCommentId());
            }
        });

        // Set click listeners for edit and delete icons
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit comment action
                editComment(holder.getAdapterPosition()); // Use getAdapterPosition() to get the current position
            }
        });
    }

    private void deleteCommentById(int commentId) {
        for (int i = 0; i < comments.size(); i++) {
            Comment_Item comment = comments.get(i);
            if (comment.getCommentId()==(commentId)) {
                comments.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, comments.size() - i);
                return; // Exit the loop after deleting the comment
            }
        }
    }



    private void editComment(int position) {
        Comment_Item comment = comments.get(position);
        String originalCommentText = comment.getText();

        // Create an EditText to get the new comment text
        final EditText input = new EditText(context);
        input.setText(originalCommentText); // Set the original comment text as a placeholder

        // Set cursor position to the end of the text
        input.setSelection(originalCommentText.length());

        // Create an AlertDialog with the EditText
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Comment");
        builder.setView(input);

        // Set up the buttons for positive (Save) and negative (Cancel) actions
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editedCommentText = input.getText().toString();
                // Update the comment text
                comment.setText(editedCommentText);
                // Notify adapter that the item has changed at the specified position
                notifyItemChanged(position);
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



    @Override
    public int getItemCount() {
        return comments.size();
    }



    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewComment;
        ImageView imgEdit;
        ImageView imgDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }

        public void bind(Comment_Item comment) {
            textViewComment.setText(comment.getText());
        }
    }
}
