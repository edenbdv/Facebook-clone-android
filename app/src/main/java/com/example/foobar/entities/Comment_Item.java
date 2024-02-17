package com.example.foobar.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobar.R;

@Entity
public class Comment_Item {
    private String postId;

    public String text;
    private boolean editMode;


    public Comment_Item( String postId, String text) {
        this.postId = postId;
        this.text = text;
        this.editMode = false;
    }
    public String getPostId() {
        return postId;
    }

    public String getText() {
        return text;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
