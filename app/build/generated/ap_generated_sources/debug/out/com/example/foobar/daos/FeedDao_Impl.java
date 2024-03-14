package com.example.foobar.daos;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
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
public final class FeedDao_Impl implements FeedDao {
  private final RoomDatabase __db;

  public FeedDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public List<Post_Item> getPostsFromFriends(final String username) {
    final String _sql = "SELECT * FROM Post_Item WHERE createdBy IN (SELECT CASE WHEN user1_username = ? THEN user2_username ELSE user1_username END FROM friendships WHERE user1_username = ? OR user2_username = ?) ORDER BY createdAt DESC LIMIT 20";
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

  @Override
  public List<Post_Item> getPostsFromNonFriends(final String username) {
    final String _sql = "SELECT * FROM Post_Item WHERE createdBy NOT IN (SELECT CASE WHEN user1_username = ? THEN user2_username ELSE user1_username END FROM friendships WHERE user1_username = ? OR user2_username = ?) AND createdBy != ? ORDER BY createdAt DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
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
    _argIndex = 4;
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
