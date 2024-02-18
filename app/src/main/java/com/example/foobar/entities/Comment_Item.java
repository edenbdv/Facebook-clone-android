package com.example.foobar.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment_Item {
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey
    private int commentId;

    private String postId;
    private String text;
    private boolean editMode;

    public Comment_Item(int commentId, String postId, String text) {
        this.commentId = commentId;
        this.postId = postId;
        this.text = text;
        this.editMode = false;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}