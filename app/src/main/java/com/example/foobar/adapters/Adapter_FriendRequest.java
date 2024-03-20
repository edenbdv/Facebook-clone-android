package com.example.foobar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foobar.R;
import java.util.List;

public class Adapter_FriendRequest extends RecyclerView.Adapter<Adapter_FriendRequest.FriendRequestViewHolder> {

    // Define interface for button click listener
    public interface OnButtonClickListener {
        void onAcceptButtonClick(String senderUsername);
        void onCancelButtonClick(String senderUsername);
    }

    private List<String> friendRequests;
    private OnButtonClickListener listener;

    public Adapter_FriendRequest(List<String> friendRequests, OnButtonClickListener listener) {
        this.friendRequests = friendRequests;
        this.listener = listener;

    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        String username = friendRequests.get(position);
        holder.usernameTextView.setText(username);
        // Set onClickListener for accept button
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onAcceptButtonClick(username);
                }
            }
        });
        // Set onClickListener for cancel button
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancelButtonClick(username);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button acceptButton;
        Button cancelButton;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            acceptButton = itemView.findViewById(R.id.accept_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }
}
