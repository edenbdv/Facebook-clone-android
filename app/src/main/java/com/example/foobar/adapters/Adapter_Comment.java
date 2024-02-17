package com.example.foobar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.R;
import com.example.foobar.entities.Comment_Item;

import java.util.List;

public class Adapter_Comment extends RecyclerView.Adapter<Adapter_Comment.CommentViewHolder> {

    private List<Comment_Item> comments;

    public Adapter_Comment(List<Comment_Item> comments) {
        this.comments = comments;
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
        // Set text for the comment
        holder.textViewComment.setText(comment.getText());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete comment action
                deleteComment(position);
            }
        });

        // Set click listeners for edit and delete icons
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit comment action
                editComment(position);
            }
        });
    }

    private void deleteComment(int position) {
        // Remove the comment from the list
        comments.remove(position);
        // Notify adapter that an item has been removed at the specified position
        notifyItemRemoved(position);
    }


    private void editComment(int position) {
        // Update the comment text in the list
        Comment_Item comment = comments.get(position);

        // Show the EditText with the original comment text
        comment.setEditMode(true);

        // Notify adapter that the item has changed at the specified position
        notifyItemChanged(position);
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
