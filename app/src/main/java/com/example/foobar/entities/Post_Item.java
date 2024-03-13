package com.example.foobar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Post_Item {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String _id;  // id from the mongoDB
    private String text;
    private String picture;
    private String createdBy;
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    private Date createdAt;
    private boolean liked;

    public Post_Item(String text, String picture, String createdBy, boolean liked) {
        this.text = text;
        this.picture = picture;
        this.createdBy = createdBy;
        this.createdAt = new Date();
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getPicture() {
        return picture;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String get_id() {
        return _id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Post_Item{" +
                "id=" + id + '\'' +
                ", _id" + _id + '\'' +
                ", text='" + text + '\'' +
                ", picture='" + picture + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
