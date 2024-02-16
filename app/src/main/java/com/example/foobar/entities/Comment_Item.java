package com.example.foobar.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobar.R;

@Entity


public class Comment_Item {
    public String text;

    public Comment_Item(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
