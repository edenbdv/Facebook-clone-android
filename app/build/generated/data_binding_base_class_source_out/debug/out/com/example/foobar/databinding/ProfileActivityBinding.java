// Generated by view binder compiler. Do not edit!
package com.example.foobar.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.foobar.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ProfileActivityBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button deleteProfileButton;

  @NonNull
  public final Button editProfileButton;

  @NonNull
  public final RecyclerView postList;

  @NonNull
  public final LinearLayout profileInfoContainer;

  @NonNull
  public final ImageView profilePicture;

  @NonNull
  public final Button seeFriendRequestsButton;

  @NonNull
  public final TextView userName;

  @NonNull
  public final Button viewFriendsButton;

  private ProfileActivityBinding(@NonNull RelativeLayout rootView,
      @NonNull Button deleteProfileButton, @NonNull Button editProfileButton,
      @NonNull RecyclerView postList, @NonNull LinearLayout profileInfoContainer,
      @NonNull ImageView profilePicture, @NonNull Button seeFriendRequestsButton,
      @NonNull TextView userName, @NonNull Button viewFriendsButton) {
    this.rootView = rootView;
    this.deleteProfileButton = deleteProfileButton;
    this.editProfileButton = editProfileButton;
    this.postList = postList;
    this.profileInfoContainer = profileInfoContainer;
    this.profilePicture = profilePicture;
    this.seeFriendRequestsButton = seeFriendRequestsButton;
    this.userName = userName;
    this.viewFriendsButton = viewFriendsButton;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ProfileActivityBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ProfileActivityBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.profile_activity, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ProfileActivityBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.delete_profile_button;
      Button deleteProfileButton = ViewBindings.findChildViewById(rootView, id);
      if (deleteProfileButton == null) {
        break missingId;
      }

      id = R.id.edit_profile_button;
      Button editProfileButton = ViewBindings.findChildViewById(rootView, id);
      if (editProfileButton == null) {
        break missingId;
      }

      id = R.id.post_list;
      RecyclerView postList = ViewBindings.findChildViewById(rootView, id);
      if (postList == null) {
        break missingId;
      }

      id = R.id.profile_info_container;
      LinearLayout profileInfoContainer = ViewBindings.findChildViewById(rootView, id);
      if (profileInfoContainer == null) {
        break missingId;
      }

      id = R.id.profile_picture;
      ImageView profilePicture = ViewBindings.findChildViewById(rootView, id);
      if (profilePicture == null) {
        break missingId;
      }

      id = R.id.see_friend_requests_button;
      Button seeFriendRequestsButton = ViewBindings.findChildViewById(rootView, id);
      if (seeFriendRequestsButton == null) {
        break missingId;
      }

      id = R.id.user_name;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      id = R.id.view_friends_button;
      Button viewFriendsButton = ViewBindings.findChildViewById(rootView, id);
      if (viewFriendsButton == null) {
        break missingId;
      }

      return new ProfileActivityBinding((RelativeLayout) rootView, deleteProfileButton,
          editProfileButton, postList, profileInfoContainer, profilePicture,
          seeFriendRequestsButton, userName, viewFriendsButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
