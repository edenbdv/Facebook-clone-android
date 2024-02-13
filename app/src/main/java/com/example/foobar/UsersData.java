package com.example.foobar;
import java.util.HashMap;

public class UsersData {
    private static HashMap<String, HashMap<String, String>> users = new HashMap<>();

    public static HashMap<String, String> getUserDetails(String username) {
        return users.get(username);
    }

    public static void addUser(String username, HashMap<String, String> userDetails) {
        users.put(username, userDetails);
    }

    public static HashMap<String, HashMap<String, String>> getAllUsers() {
        return users;
    }
}
