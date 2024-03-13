package com.example.foobar.databinding;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobar.R;
import com.example.foobar.entities.User_Item;

public abstract class Profile_Binding extends ViewDataBinding {
    @NonNull
    public final LinearLayout profileInfoContainer;

    @NonNull
    public final RecyclerView postList;

    @NonNull
    public final Button deleteProfileButton;

    @NonNull
    public final Button editProfileButton;

    @NonNull
    public final ImageView profilePicture;

    @NonNull
    public final TextView userName;

    @NonNull
    public final Button viewFriendsButton;

    @NonNull
    public final Button seeFriendRequestsButton;

    @Bindable
    protected User_Item mUserItem;

    protected Profile_Binding(Object _bindingComponent, View _root, int _localFieldCount,
                                     LinearLayout profileInfoContainer, RecyclerView postList, Button deleteProfileButton,
                                     Button editProfileButton, ImageView profilePicture, TextView userName,
                                     Button viewFriendsButton, Button seeFriendRequestsButton) {
        super(_bindingComponent, _root, _localFieldCount);
        this.profileInfoContainer = profileInfoContainer;
        this.postList = postList;
        this.deleteProfileButton = deleteProfileButton;
        this.editProfileButton = editProfileButton;
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.viewFriendsButton = viewFriendsButton;
        this.seeFriendRequestsButton = seeFriendRequestsButton;
    }

    public abstract void setUserItem(@Nullable User_Item userItem);

    @Nullable
    public User_Item getUserItem() {
        return mUserItem;
    }

    @NonNull
    public static Profile_Binding bind(View view, DataBindingComponent component) {
        return (Profile_Binding)bind(component, view, R.layout.profile_activity);
    }

    @NonNull
    public static Profile_Binding bind(@NonNull View view) {
        return bind(view, DataBindingUtil.getDefaultComponent());
    }


}
