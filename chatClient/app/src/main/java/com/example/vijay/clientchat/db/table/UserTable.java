package com.example.vijay.clientchat.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vijay.clientchat.db.Query;
import com.example.vijay.clientchat.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VIJAY PAL on 30/8/17.
 */

public class UserTable {
  private SQLiteDatabase db;

  public UserTable(SQLiteDatabase db) {
    this.db = db;
  }

  public void insertUser(User user) {
    ContentValues row = new ContentValues();
    row.put(Query.COLUMN__ID, user.getId());
    row.put(Query.COLUMN_LOGIN_ID, user.getLoginId());
    row.put(Query.COLUMN_DISPLAY_NAME, user.getDisplayName());
    row.put(Query.COLUMN_GIVEN_NAME, user.getGivenName());
    row.put(Query.COLUMN_FAMILY_NAME, user.getFamilyName());
    row.put(Query.COLUMN_EMAIL, user.getEmail());
    row.put(Query.COLUMN_PHOTO_URL, user.getPhotoUrl());
    row.put(Query.COLUMN_LOGIN_TOKEN, user.getToken());
    row.put(Query.COLUMN_SERVER_AUTH_CODE, user.getServerAuthCode());
    row.put(Query.COLUMN_MOBILE, user.getId());
    row.put(Query.COLUMN_LOGIN_VIA, user.getLoginVia());
    row.put(Query.COLUMN_CREATED_AT, user.getCreatedAt());
    row.put(Query.COLUMN_UPDATE_AT, user.getUpdateAt());

    db.insertWithOnConflict(Query.TABLE_USER, null, row, SQLiteDatabase.CONFLICT_REPLACE);

    if (user.getFriends() != null) {
      for (User.Friend friend : user.getFriends()) {
        insertFriend(friend, user.getId());
      }
    }
  }

  public void insertFriend(User.Friend friend, String friendId) {
    ContentValues row = new ContentValues();
    row.put(Query.COLUMN__ID, friend.getId());
    row.put(Query.COLUMN_DISPLAY_NAME, friend.getDisplayName());
    row.put(Query.COLUMN_EMAIL, friend.getEmail());
    row.put(Query.COLUMN_STATUS, friend.getStatus());
    row.put(Query.COLUMN_FRIEND_ID, friendId);
    db.insertWithOnConflict(Query.TABLE_FRIEND, null, row, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public User getUserById(String userId) {
    User user = null;
    Cursor cursor = db.rawQuery(Query.SELECT_USER_BY_USER_ID, new String[]{userId});
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        user = new User();
        user.setId(cursor.getString(cursor.getColumnIndex(Query.COLUMN__ID)));
        user.setLoginId(cursor.getString(cursor.getColumnIndex(Query.COLUMN_LOGIN_ID)));
        user.setDisplayName(cursor.getString(cursor.getColumnIndex(Query.COLUMN_DISPLAY_NAME)));
        user.setGivenName(cursor.getString(cursor.getColumnIndex(Query.COLUMN_GIVEN_NAME)));
        user.setFamilyName(cursor.getString(cursor.getColumnIndex(Query.COLUMN_FAMILY_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(Query.COLUMN_EMAIL)));
        user.setPhotoUrl(cursor.getString(cursor.getColumnIndex(Query.COLUMN_PHOTO_URL)));
        user.setToken(cursor.getString(cursor.getColumnIndex(Query.COLUMN_LOGIN_TOKEN)));
        user.setServerAuthCode(cursor.getString(cursor.getColumnIndex(Query.COLUMN_SERVER_AUTH_CODE)));
        user.setLoginVia(cursor.getString(cursor.getColumnIndex(Query.COLUMN_LOGIN_VIA)));
        user.setCreatedAt(cursor.getLong(cursor.getColumnIndex(Query.COLUMN_CREATED_AT)));
        user.setUpdateAt(cursor.getLong(cursor.getColumnIndex(Query.COLUMN_UPDATE_AT)));
      }
      if (!cursor.isClosed()) {
        cursor.close();
      }
    }
    return user;
  }

  public List<User.Friend> getFriendsByUserId(String userId, int status) {
    List<User.Friend> friends = new ArrayList<>();
    Cursor cursor = db.rawQuery(Query.SELECT_FRIENDS_BY_USER_ID_AND_STATUS, new String[]{userId, String.valueOf(status)});
    if (cursor != null) {
      if (cursor.getCount() > 0) {
        User.Friend friend;
        while (cursor.moveToNext()) {
          friend = new User.Friend();
          friend.setId(cursor.getString(cursor.getColumnIndex(Query.COLUMN__ID)));
          friend.setDisplayName(cursor.getString(cursor.getColumnIndex(Query.COLUMN_DISPLAY_NAME)));
          friend.setEmail(cursor.getString(cursor.getColumnIndex(Query.COLUMN_EMAIL)));
          friend.setStatus(cursor.getInt(cursor.getColumnIndex(Query.COLUMN_STATUS)));

          friends.add(friend);
        }
      }

      if (!cursor.isClosed()) {
        cursor.close();
      }
    }
    return friends;
  }
}
