package com.example.foobar.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobar.R;

@Entity
public class Comment_Item {
    public String text;
    private boolean editMode;


    public Comment_Item(String text) {
        this.text = text;
        this.editMode = false;
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
