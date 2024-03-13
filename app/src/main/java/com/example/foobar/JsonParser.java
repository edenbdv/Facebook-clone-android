package com.example.foobar;

import android.content.Context;

import com.example.foobar.entities.Post_Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static List<Post_Item> parseJsonData(Context context) {
        List<Post_Item> posts = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("Posts.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonData = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < Math.min(jsonArray.length(), 10); i++) {
                JSONObject jsonPost = jsonArray.getJSONObject(i);
                int id = jsonPost.getInt("id");
                String text = jsonPost.getString("text");
                String picture = jsonPost.getString("picture");
                String authorP = jsonPost.getString("authorP");
                String authorN = jsonPost.getString("authorN");
                String date = jsonPost.getString("date");
                Post_Item post = new Post_Item(text, picture, authorN, false);

                // Parse other fields like author, image URLs, likes, comments, etc.
                posts.add(post);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }
}

