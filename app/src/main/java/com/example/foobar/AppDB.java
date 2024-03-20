package com.example.foobar;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

@Database(entities = {User_Item.class, Friendship.class, FriendRequest.class, Post_Item.class}, version = 7, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDB extends RoomDatabase {
    private static volatile AppDB instance;

    public abstract UserDao userDao();

    public abstract FriendshipDao friendshipDao();

    public abstract FriendRequestDao friendRequestDao();

    public abstract PostDao postDao();

    public abstract FeedDao feedDao();


    public static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Define how to migrate from version 5 to version 6 here
            // This could involve renaming tables, adding columns, etc.
        }
    };

    public static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Define how to migrate from version 6 to version 7 here
            // This could involve renaming tables, adding columns, etc.
        }
    };

    public static AppDB getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "FooBar")
                            .addMigrations(MIGRATION_5_6, MIGRATION_6_7) // Add migration for version 6 to 7
                            .build();
                }
            }
        }
        return instance;
    }
}
