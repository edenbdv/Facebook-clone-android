package com.example.foobar;

import com.example.foobar.entities.User_Item;

public class UserManager {
    private static UserManager instance;
    private User_Item currentUser; // Currently logged-in user
    private User_Item ProfileUser; // User whose profile is being viewed

    private UserManager() {
        // Private constructor to prevent instantiation from outside
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public User_Item getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User_Item user) {
        currentUser = user;
    }

    public void setCurrentUsername(String username) {
        currentUser.setUsername(username);
    }

    public String getCurrentUsername() {
        return currentUser.getUsername();
    }

    public User_Item getProfileUser() {
        return ProfileUser;
    }

    public void setProfileUser(User_Item user) {
        ProfileUser = user;
    }

    public String getProfileUsername() {
        return ProfileUser.getUsername();
    }
}
