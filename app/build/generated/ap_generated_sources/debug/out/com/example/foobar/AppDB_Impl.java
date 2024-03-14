package com.example.foobar;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.foobar.daos.FeedDao;
import com.example.foobar.daos.FeedDao_Impl;
import com.example.foobar.daos.FriendRequestDao;
import com.example.foobar.daos.FriendRequestDao_Impl;
import com.example.foobar.daos.FriendshipDao;
import com.example.foobar.daos.FriendshipDao_Impl;
import com.example.foobar.daos.PostDao;
import com.example.foobar.daos.PostDao_Impl;
import com.example.foobar.daos.UserDao;
import com.example.foobar.daos.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDB_Impl extends AppDB {
  private volatile UserDao _userDao;

  private volatile FriendshipDao _friendshipDao;

  private volatile FriendRequestDao _friendRequestDao;

  private volatile PostDao _postDao;

  private volatile FeedDao _feedDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `User_Item` (`username` TEXT NOT NULL, `password` TEXT, `displayName` TEXT, `profilePic` TEXT, PRIMARY KEY(`username`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `friendships` (`user1_username` TEXT NOT NULL, `user2_username` TEXT NOT NULL, PRIMARY KEY(`user1_username`, `user2_username`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_friendships_user1_username_user2_username` ON `friendships` (`user1_username`, `user2_username`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `friend_requests` (`senderUsername` TEXT NOT NULL, `receiverUsername` TEXT NOT NULL, PRIMARY KEY(`senderUsername`, `receiverUsername`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_friend_requests_senderUsername_receiverUsername` ON `friend_requests` (`senderUsername`, `receiverUsername`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Post_Item` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `_id` TEXT, `text` TEXT, `picture` TEXT, `createdBy` TEXT, `createdAt` INTEGER DEFAULT CURRENT_TIMESTAMP, `liked` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '44f5e62f63c7c8569518d35a94758f77')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `User_Item`");
        db.execSQL("DROP TABLE IF EXISTS `friendships`");
        db.execSQL("DROP TABLE IF EXISTS `friend_requests`");
        db.execSQL("DROP TABLE IF EXISTS `Post_Item`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUserItem = new HashMap<String, TableInfo.Column>(4);
        _columnsUserItem.put("username", new TableInfo.Column("username", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserItem.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserItem.put("displayName", new TableInfo.Column("displayName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserItem.put("profilePic", new TableInfo.Column("profilePic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserItem = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserItem = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserItem = new TableInfo("User_Item", _columnsUserItem, _foreignKeysUserItem, _indicesUserItem);
        final TableInfo _existingUserItem = TableInfo.read(db, "User_Item");
        if (!_infoUserItem.equals(_existingUserItem)) {
          return new RoomOpenHelper.ValidationResult(false, "User_Item(com.example.foobar.entities.User_Item).\n"
                  + " Expected:\n" + _infoUserItem + "\n"
                  + " Found:\n" + _existingUserItem);
        }
        final HashMap<String, TableInfo.Column> _columnsFriendships = new HashMap<String, TableInfo.Column>(2);
        _columnsFriendships.put("user1_username", new TableInfo.Column("user1_username", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFriendships.put("user2_username", new TableInfo.Column("user2_username", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFriendships = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFriendships = new HashSet<TableInfo.Index>(1);
        _indicesFriendships.add(new TableInfo.Index("index_friendships_user1_username_user2_username", true, Arrays.asList("user1_username", "user2_username"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoFriendships = new TableInfo("friendships", _columnsFriendships, _foreignKeysFriendships, _indicesFriendships);
        final TableInfo _existingFriendships = TableInfo.read(db, "friendships");
        if (!_infoFriendships.equals(_existingFriendships)) {
          return new RoomOpenHelper.ValidationResult(false, "friendships(com.example.foobar.entities.Friendship).\n"
                  + " Expected:\n" + _infoFriendships + "\n"
                  + " Found:\n" + _existingFriendships);
        }
        final HashMap<String, TableInfo.Column> _columnsFriendRequests = new HashMap<String, TableInfo.Column>(2);
        _columnsFriendRequests.put("senderUsername", new TableInfo.Column("senderUsername", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFriendRequests.put("receiverUsername", new TableInfo.Column("receiverUsername", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFriendRequests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFriendRequests = new HashSet<TableInfo.Index>(1);
        _indicesFriendRequests.add(new TableInfo.Index("index_friend_requests_senderUsername_receiverUsername", true, Arrays.asList("senderUsername", "receiverUsername"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoFriendRequests = new TableInfo("friend_requests", _columnsFriendRequests, _foreignKeysFriendRequests, _indicesFriendRequests);
        final TableInfo _existingFriendRequests = TableInfo.read(db, "friend_requests");
        if (!_infoFriendRequests.equals(_existingFriendRequests)) {
          return new RoomOpenHelper.ValidationResult(false, "friend_requests(com.example.foobar.entities.FriendRequest).\n"
                  + " Expected:\n" + _infoFriendRequests + "\n"
                  + " Found:\n" + _existingFriendRequests);
        }
        final HashMap<String, TableInfo.Column> _columnsPostItem = new HashMap<String, TableInfo.Column>(7);
        _columnsPostItem.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPostItem.put("_id", new TableInfo.Column("_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPostItem.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPostItem.put("picture", new TableInfo.Column("picture", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPostItem.put("createdBy", new TableInfo.Column("createdBy", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPostItem.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", false, 0, "CURRENT_TIMESTAMP", TableInfo.CREATED_FROM_ENTITY));
        _columnsPostItem.put("liked", new TableInfo.Column("liked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPostItem = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPostItem = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPostItem = new TableInfo("Post_Item", _columnsPostItem, _foreignKeysPostItem, _indicesPostItem);
        final TableInfo _existingPostItem = TableInfo.read(db, "Post_Item");
        if (!_infoPostItem.equals(_existingPostItem)) {
          return new RoomOpenHelper.ValidationResult(false, "Post_Item(com.example.foobar.entities.Post_Item).\n"
                  + " Expected:\n" + _infoPostItem + "\n"
                  + " Found:\n" + _existingPostItem);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "44f5e62f63c7c8569518d35a94758f77", "77967e1cbd99e256521125abede7ff81");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "User_Item","friendships","friend_requests","Post_Item");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `User_Item`");
      _db.execSQL("DELETE FROM `friendships`");
      _db.execSQL("DELETE FROM `friend_requests`");
      _db.execSQL("DELETE FROM `Post_Item`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FriendshipDao.class, FriendshipDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FriendRequestDao.class, FriendRequestDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PostDao.class, PostDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FeedDao.class, FeedDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public FriendshipDao friendshipDao() {
    if (_friendshipDao != null) {
      return _friendshipDao;
    } else {
      synchronized(this) {
        if(_friendshipDao == null) {
          _friendshipDao = new FriendshipDao_Impl(this);
        }
        return _friendshipDao;
      }
    }
  }

  @Override
  public FriendRequestDao friendRequestDao() {
    if (_friendRequestDao != null) {
      return _friendRequestDao;
    } else {
      synchronized(this) {
        if(_friendRequestDao == null) {
          _friendRequestDao = new FriendRequestDao_Impl(this);
        }
        return _friendRequestDao;
      }
    }
  }

  @Override
  public PostDao postDao() {
    if (_postDao != null) {
      return _postDao;
    } else {
      synchronized(this) {
        if(_postDao == null) {
          _postDao = new PostDao_Impl(this);
        }
        return _postDao;
      }
    }
  }

  @Override
  public FeedDao feedDao() {
    if (_feedDao != null) {
      return _feedDao;
    } else {
      synchronized(this) {
        if(_feedDao == null) {
          _feedDao = new FeedDao_Impl(this);
        }
        return _feedDao;
      }
    }
  }
}
