package com.example.foobar.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.foobar.entities.Friendship;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FriendshipDao_Impl implements FriendshipDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Friendship> __insertionAdapterOfFriendship;

  private final EntityDeletionOrUpdateAdapter<Friendship> __deletionAdapterOfFriendship;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public FriendshipDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFriendship = new EntityInsertionAdapter<Friendship>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `friendships` (`user1_username`,`user2_username`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Friendship entity) {
        if (entity.getUser1_username() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUser1_username());
        }
        if (entity.getUser2_username() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUser2_username());
        }
      }
    };
    this.__deletionAdapterOfFriendship = new EntityDeletionOrUpdateAdapter<Friendship>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `friendships` WHERE `user1_username` = ? AND `user2_username` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Friendship entity) {
        if (entity.getUser1_username() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUser1_username());
        }
        if (entity.getUser2_username() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUser2_username());
        }
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM friendships";
        return _query;
      }
    };
  }

  @Override
  public void insertList(final List<Friendship> friendships) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFriendship.insert(friendships);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final Friendship friendship) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFriendship.insert(friendship);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Friendship friendship) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFriendship.handle(friendship);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clear() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfClear.release(_stmt);
    }
  }

  @Override
  public List<String> getUserFriends(final String username) {
    final String _sql = "SELECT CASE WHEN user1_username = ? THEN user2_username ELSE user1_username END AS friendUsername FROM friendships WHERE user1_username = ? OR user2_username = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    _argIndex = 2;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    _argIndex = 3;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final String _item;
        final String _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(0);
        }
        _item = _tmp;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
