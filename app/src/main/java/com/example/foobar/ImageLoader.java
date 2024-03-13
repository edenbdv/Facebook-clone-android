package com.example.foobar;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.foobar.entities.Post_Item;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {
    public void loadImageFromAssets(Context context, String path, ImageView imageView) {
        try {
            InputStream inputStream = context.getAssets().open(path.substring(1));
            SVG svg = SVG.getFromInputStream(inputStream);
            imageView.setImageDrawable(new PictureDrawable(svg.renderToPicture()));
        } catch (IOException | SVGParseException e) {
            e.printStackTrace();
        }
    }

    public void loadImageFromUri(String uriString, ImageView imageView) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            imageView.setImageURI(uri);
            imageView.setVisibility(View.VISIBLE); // Make ImageView visible
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public void loadImagesBasedOnPostId(Context context, Post_Item post, ImageView proPicImageView, ImageView postPicImageView) {
        int postId = post.getId();
        if (postId >= 1 && postId <= 10) {
            // Convert profile picture path to Uri and load into ImageView from assets
            loadImageFromAssets(context, post.getPicture(), proPicImageView);
            loadImageFromAssets(context, post.getPicture(), postPicImageView);
        } else {
            // Load profile picture and post picture from URIs
            loadImageFromUri(post.getPicture(), proPicImageView);
            loadImageFromUri(post.getPicture(), postPicImageView);
        }
    }
}
