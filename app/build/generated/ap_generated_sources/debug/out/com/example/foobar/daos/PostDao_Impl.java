package com.example.foobar.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.foobar.converters.DateConverter;
import com.example.foobar.entities.Post_Item;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class PostDao_Impl implements PostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Post_Item> __insertionAdapterOfPost_Item;

  private final EntityInsertionAdapter<Post_Item> __insertionAdapterOfPost_Item_1;

  private final SharedSQLiteStatement __preparedStmtOfUpdateText;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePicture;

  private final SharedSQLiteStatement __preparedStmtOfUpdateUsername;

  private final SharedSQLiteStatement __preparedStmtOfDeletePost;

  private final SharedSQLiteStatement __preparedStmtOfDeletePostByUser;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public PostDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPost_Item = new EntityInsertionAdapter<Post_Item>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Post_Item` (`id`,`_id`,`text`,`picture`,`createdBy`,`createdAt`,`liked`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post_Item entity) {
        statement.bindLong(1, entity.getId());
        if (entity.get_id() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.get_id());
        }
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        if (entity.getPicture() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPicture());
        }
        if (entity.getCreatedBy() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCreatedBy());
        }
        final Long _tmp = DateConverter.toTimestamp(entity.getCreatedAt());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        final int _tmp_1 = entity.isLiked() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
      }
    };
    this.__insertionAdapterOfPost_Item_1 = new EntityInsertionAdapter<Post_Item>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `Post_Item` (`id`,`_id`,`text`,`picture`,`createdBy`,`createdAt`,`liked`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post_Item entity) {
        statement.bindLong(1, entity.getId());
        if (entity.get_id() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.get_id());
        }
        if (entity.getText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getText());
        }
        if (entity.getPicture() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPicture());
        }
        if (entity.getCreatedBy() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCreatedBy());
        }
        final Long _tmp = DateConverter.toTimestamp(entity.getCreatedAt());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        final int _tmp_1 = entity.isLiked() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
      }
    };
    this.__preparedStmtOfUpdateText = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Post_Item SET text = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePicture = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Post_Item SET picture = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateUsername = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE Post_Item SET createdBy = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePost = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Post_Item WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePostByUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Post_Item WHERE createdBy = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM Post_Item";
        return _query;
      }
    };
  }

  @Override
  public void createPost(final Post_Item post) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPost_Item.insert(post);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertList(final List<Post_Item> postItems) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPost_Item_1.insert(postItems);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final Post_Item postItem) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPost_Item_1.insert(postItem);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateText(final int id, final String newText) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateText.acquire();
    int _argIndex = 1;
    if (newText == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, newText);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateText.release(_stmt);
    }
  }

  @Override
  public void updatePicture(final int id, final String newPicture) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePicture.acquire();
    int _argIndex = 1;
    if (newPicture == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, newPicture);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdatePicture.release(_stmt);
    }
  }

  @Override
  public void updateUsername(final int id, final String newUsername) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateUsername.acquire();
    int _argIndex = 1;
    if (newUsername == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, newUsername);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateUsername.release(_stmt);
    }
  }

  @Override
  public void deletePost(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePost.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletePost.release(_stmt);
    }
  }

  @Override
  public void deletePostByUser(final String userName) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePostByUser.acquire();
    int _argIndex = 1;
    if (userName == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userName);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeletePostByUser.release(_stmt);
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
  public List<Post_Item> getUserPosts(final String username) {
    final String _sql = "SELECT * FROM Post_Item WHERE createdBy = ? ORDER BY createdAt DESC";
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
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfId_1 = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfPicture = CursorUtil.getColumnIndexOrThrow(_cursor, "picture");
      final int _cursorIndexOfCreatedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "createdBy");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
      final List<Post_Item> _result = new ArrayList<Post_Item>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Post_Item _item;
        final String _tmpText;
        if (_cursor.isNull(_cursorIndexOfText)) {
          _tmpText = null;
        } else {
          _tmpText = _cursor.getString(_cursorIndexOfText);
        }
        final String _tmpPicture;
        if (_cursor.isNull(_cursorIndexOfPicture)) {
          _tmpPicture = null;
        } else {
          _tmpPicture = _cursor.getString(_cursorIndexOfPicture);
        }
        final String _tmpCreatedBy;
        if (_cursor.isNull(_cursorIndexOfCreatedBy)) {
          _tmpCreatedBy = null;
        } else {
          _tmpCreatedBy = _cursor.getString(_cursorIndexOfCreatedBy);
        }
        final boolean _tmpLiked;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfLiked);
        _tmpLiked = _tmp != 0;
        _item = new Post_Item(_tmpText,_tmpPicture,_tmpCreatedBy,_tmpLiked);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmp_id;
        if (_cursor.isNull(_cursorIndexOfId_1)) {
          _tmp_id = null;
        } else {
          _tmp_id = _cursor.getString(_cursorIndexOfId_1);
        }
        _item.set_id(_tmp_id);
        final Date _tmpCreatedAt;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfCreatedAt);
        }
        _tmpCreatedAt = DateConverter.toDate(_tmp_1);
        _item.setCreatedAt(_tmpCreatedAt);
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
