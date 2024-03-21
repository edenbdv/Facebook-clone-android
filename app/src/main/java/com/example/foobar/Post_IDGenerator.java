package com.example.foobar;

public class Post_IDGenerator {
    private static int nextId = 1;

    public static int getNextId() {
        return nextId++;
    }
}
