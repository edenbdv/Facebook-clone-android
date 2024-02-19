package com.example.foobar.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.CommentActivity;
import com.example.foobar.R;
import com.example.foobar.entities.Post_Item;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;


public class Adapter_Feed extends RecyclerView.Adapter<Adapter_Feed.MyViewHolder> {
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_time, tv_status;
        ImageView imgView_proPic, imgView_postPic;
        ImageButton btnLike;
        LinearLayout likeContainer;
        LinearLayout commentContainer;
        TextView tvLike;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgView_proPic = itemView.findViewById(R.id.proPic);
            imgView_postPic = itemView.findViewById(R.id.postPic);

            tv_name = itemView.findViewById(R.id.name);
            tv_time = itemView.findViewById(R.id.post_time);
            tv_status = itemView.findViewById(R.id.post_text);

            btnLike = itemView.findViewById(R.id.btnLike);
            likeContainer = itemView.findViewById(R.id.likeContainer);
            tvLike= itemView.findViewById(R.id.tvLike);

            commentContainer = itemView.findViewById(R.id.commentContainer);
        }
    }

    private final LayoutInflater mInflater;
    private final Context context; // Store the context
    ArrayList<Post_Item> PostList = new ArrayList<>();

    public Adapter_Feed(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.PostList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.post_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (PostList != null) {
            final Post_Item current = PostList.get(position);

            holder.tv_name.setText(current.getName());
            holder.tv_time.setText(current.getTime());
            holder.tv_status.setText(current.getText());
            //Glide.with(context).load(current.getPostpic()).into(holder.imgView_postPic);
            //holder.imgView_postPic.setImageResource(current.getPostpic());


            // Check if the post ID is between 1 and 10
            int postId = current.getId();
            if (postId >= 1 && postId <= 10) {
                // Convert profile picture path to Uri and load into ImageView from assets
                try {
                    InputStream profilePicInputStream = context.getAssets().open(current.getPropic().substring(1));
                    SVG profilePicSvg = SVG.getFromInputStream(profilePicInputStream);
                    holder.imgView_proPic.setImageDrawable(new PictureDrawable(profilePicSvg.renderToPicture()));
                } catch (IOException | SVGParseException e) {
                    e.printStackTrace();
                }

                // Convert post picture path to Uri and load into ImageView from assets
                try {
                    InputStream postPicInputStream = context.getAssets().open(current.getPostpic().substring(1));
                    SVG postPicSvg = SVG.getFromInputStream(postPicInputStream);
                    holder.imgView_postPic.setImageDrawable(new PictureDrawable(postPicSvg.renderToPicture()));
                } catch (IOException | SVGParseException e) {
                    e.printStackTrace();
                }
            } else {
                // Load profile picture from Uri for other post IDs
//                Uri profilePictureUri = Uri.parse(current.getPropic());
//                holder.imgView_proPic.setImageURI(profilePictureUri);

                // Inside onBindViewHolder method of Adapter_Feed class
                String profilePictureUri = current.getPropic();
                if (profilePictureUri != null) {
                    Uri uri = Uri.parse(profilePictureUri);
                    holder.imgView_proPic.setImageURI(uri);
                    holder.imgView_proPic.setVisibility(View.VISIBLE); // Make ImageView visible
                } else {
                    // Hide the ImageView if profilePictureUri is null
                    holder.imgView_proPic.setVisibility(View.GONE);
                }



                // Load post picture from Uri for other post IDs
                Uri postPictureUri = Uri.parse(current.getPostpic());
                holder.imgView_postPic.setImageURI(postPictureUri);
            }


            // Set initial like button color based on post's liked status
            int likeButtonColor = current.isLiked() ? R.color.colorPrimary : R.color.grey;
            holder.btnLike.setColorFilter(ContextCompat.getColor(context, likeButtonColor));
            holder.tvLike.setTextColor(ContextCompat.getColor(context, likeButtonColor)); // Update text color

            // Handle like button click
            holder.likeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle like status and update UI accordingly
                    boolean isLiked = !current.isLiked();
                    current.setLiked(isLiked);
                    // Update like button color
                    int colorResId = isLiked ? R.color.colorPrimary : R.color.grey;
                    holder.btnLike.setColorFilter(ContextCompat.getColor(context, colorResId));
                    holder.tvLike.setTextColor(ContextCompat.getColor(context, colorResId)); // Update text color
                }
            });


            // Set click listener for comment container
            holder.commentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start CommentActivity
                    Intent intent = new Intent(context, CommentActivity.class);
                    context.startActivity(intent);
                }
            });

        }
    }


    public void SetPosts(ArrayList<Post_Item> l) {
        this.PostList = l;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (PostList != null) {
            return PostList.size();
        } else return 0;
    }

    public ArrayList<Post_Item> getPosts() {
        return PostList;
    }

}


