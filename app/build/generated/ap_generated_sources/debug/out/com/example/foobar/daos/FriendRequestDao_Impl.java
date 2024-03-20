package com.example.foobar.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.foobar.entities.FriendRequest;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FriendRequestDao_Impl implements FriendRequestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FriendRequest> __insertionAdapterOfFriendRequest;

  private final EntityInsertionAdapter<FriendRequest> __insertionAdapterOfFriendRequest_1;

  private final EntityDeletionOrUpdateAdapter<FriendRequest> __deletionAdapterOfFriendRequest;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public FriendRequestDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFriendRequest = new EntityInsertionAdapter<FriendRequest>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `friend_requests` (`senderUsername`,`receiverUsername`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FriendRequest entity) {
        if (entity.getSenderUsername() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSenderUsername());
        }
        if (entity.getReceiverUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getReceiverUsername());
        }
      }
    };
    this.__insertionAdapterOfFriendRequest_1 = new EntityInsertionAdapter<FriendRequest>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `friend_requests` (`senderUsername`,`receiverUsername`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FriendRequest entity) {
        if (entity.getSenderUsername() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSenderUsername());
        }
        if (entity.getReceiverUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getReceiverUsername());
        }
      }
    };
    this.__deletionAdapterOfFriendRequest = new EntityDeletionOrUpdateAdapter<FriendRequest>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `friend_requests` WHERE `senderUsername` = ? AND `receiverUsername` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FriendRequest entity) {
        if (entity.getSenderUsername() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getSenderUsername());
        }
        if (entity.getReceiverUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getReceiverUsername());
        }
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM friend_requests";
        return _query;
      }
    };
  }

  @Override
  public void addFriendReq(final FriendRequest friendRequest) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFriendRequest.insert(friendRequest);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertList(final List<FriendRequest> friendRequests) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFriendRequest_1.insert(friendRequests);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final FriendRequest friendRequest) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFriendRequest_1.insert(friendRequest);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFriendReq(final FriendRequest friendRequest) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFriendRequest.handle(friendRequest);
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
  public List<String> getFriendRequests(final String username) {
    final String _sql = "SELECT senderUsername FROM friend_requests WHERE receiverUsername = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
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
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public FriendRequest getFriendRequest(final String sender, final String receiver) {
    final String _sql = "SELECT * FROM friend_requests WHERE senderUsername = ? AND receiverUsername = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (sender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sender);
    }
    _argIndex = 2;
    if (receiver == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, receiver);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSenderUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "senderUsername");
      final int _cursorIndexOfReceiverUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "receiverUsername");
      final FriendRequest _result;
      if (_cursor.moveToFirst()) {
        final String _tmpSenderUsername;
        if (_cursor.isNull(_cursorIndexOfSenderUsername)) {
          _tmpSenderUsername = null;
        } else {
          _tmpSenderUsername = _cursor.getString(_cursorIndexOfSenderUsername);
        }
        final String _tmpReceiverUsername;
        if (_cursor.isNull(_cursorIndexOfReceiverUsername)) {
          _tmpReceiverUsername = null;
        } else {
          _tmpReceiverUsername = _cursor.getString(_cursorIndexOfReceiverUsername);
        }
        _result = new FriendRequest(_tmpSenderUsername,_tmpReceiverUsername);
      } else {
        _result = null;
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
