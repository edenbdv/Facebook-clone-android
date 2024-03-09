package com.example.foobar.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobar.R;

import java.util.Date;

@Entity
public class Post_Item {
// i added the primary key , but without auto-generate
    @PrimaryKey
    private int id;
    private String text;
    private String postpic;
    private String name;

    private Date createdAt;
    private int comments;
    private String propic;
    //private String name;

    private String time;
    private int likes;
    private boolean liked;

    public Post_Item(int id, int likes ,int comments, String propic, String postpic, String name, String time, String text, boolean liked) {
        this.id = id;
        this.text = text;
        this.postpic = postpic;
        this.name = name;

        this.likes = likes;
        this.comments = comments;
        this.propic = propic;
        this.time = time;
        this.liked = liked;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getPostpic() {
        return postpic;
    }

    public void setPostpic(String postpic) {
        this.postpic = postpic;
    }

    public String getName() {
        return name;
    }

    public void setName(String author_name) {
        this.name = author_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}



