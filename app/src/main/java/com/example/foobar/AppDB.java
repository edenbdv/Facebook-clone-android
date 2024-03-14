package com.example.foobar;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foobar.converters.DateConverter;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.FriendRequestDao;
import com.example.foobar.daos.FriendshipDao;
import com.example.foobar.daos.PostDao;
import com.example.foobar.daos.UserDao;
import com.example.foobar.entities.FriendRequest;
import com.example.foobar.entities.Friendship;
import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.User_Item;

@Database(entities = {User_Item.class, Friendship.class, FriendRequest.class, Post_Item.class}, version = 4, exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class AppDB extends RoomDatabase {
    private static volatile AppDB instance;

    public abstract UserDao userDao();

    public abstract FriendshipDao friendshipDao();

    public abstract FriendRequestDao friendRequestDao();

    public abstract PostDao postDao();

    public abstract FeedDao feedDao();

    public static AppDB getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "FooBar")
                            .fallbackToDestructiveMigration() // Add this line for destructive migration
                            .fallbackToDestructiveMigrationOnDowngrade() // Add this line to clear data if downgrading
                            .build();
                }
            }
        }
        return instance;
    }


}