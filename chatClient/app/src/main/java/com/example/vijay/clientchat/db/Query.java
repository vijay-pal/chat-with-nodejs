package com.example.vijay.clientchat.db;

/**
 * Created by vijay on 30/8/17.
 */

public interface Query {
  String TABLE_CONVERSATION = "conversation";
  String TABLE_USER = "user";
  String TABLE_FRIEND = "friend";

  String COLUMN_LOGIN_ID = "login_id";
  String COLUMN_DISPLAY_NAME = "display_name";
  String COLUMN_GIVEN_NAME = "given_name";
  String COLUMN_FAMILY_NAME = "family_name";
  String COLUMN_EMAIL = "email";
  String COLUMN_PHOTO_URL = "photo_url";
  String COLUMN_LOGIN_TOKEN = "login_token";
  String COLUMN_SERVER_AUTH_CODE = "server_auth_code";
  String COLUMN_MOBILE = "mobile";
  String COLUMN_LOGIN_VIA = "login_via";
  String COLUMN_FRIEND_ID = "friend_id";
  String COLUMN_UPDATE_AT = "update_at";

  String COLUMN__ID = "_id";
  String COLUMN_MESSAGE = "message";
  String COLUMN_TYPE = "type";
  String COLUMN_URL = "url";
  String COLUMN_SENDER = "sender";
  String COLUMN_RECEIVER = "receiver";
  String COLUMN_CREATED_AT = "created_at";
  String COLUMN_RECEIVED_AT = "received_at";
  String COLUMN_STATUS = "status";

  String COLUMN_ = "";

  String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" + COLUMN__ID + " TEXT PRIMARY KEY  NOT NULL , " + COLUMN_LOGIN_ID + " TEXT, " +
    COLUMN_DISPLAY_NAME + " TEXT, " + COLUMN_GIVEN_NAME + " TEXT, " + COLUMN_FAMILY_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT UNIQUE , " +
    COLUMN_PHOTO_URL + " TEXT, " + COLUMN_LOGIN_TOKEN + " TEXT, " + COLUMN_SERVER_AUTH_CODE + " TEXT, " + COLUMN_MOBILE + " NUMERIC, " +
    COLUMN_LOGIN_VIA + " TEXT, " + COLUMN_CREATED_AT + " NUMERIC, " + COLUMN_UPDATE_AT + " NUMERIC)";

  String CREATE_TABLE_FRIEND = "CREATE TABLE " + TABLE_FRIEND + " (" + COLUMN__ID + " TEXT PRIMARY KEY  NOT NULL , " +
    COLUMN_DISPLAY_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_FRIEND_ID + " TEXT, " + COLUMN_STATUS + " TEXT)";

  String CREATE_TABLE_CONVERSATION = "CREATE TABLE " + TABLE_CONVERSATION + " (" + COLUMN__ID + " TEXT PRIMARY KEY  NOT NULL , " +
    COLUMN_MESSAGE + " TEXT, " + COLUMN_TYPE + " TEXT, " + COLUMN_URL + " TEXT, " + COLUMN_SENDER + " TEXT, " +
    COLUMN_RECEIVER + " TEXT, " + COLUMN_CREATED_AT + " NUMERIC, " + COLUMN_RECEIVED_AT + " NUMERIC, " + COLUMN_STATUS + " TEXT)";

  String SELECT_USER_BY_USER_ID = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN__ID + "=?";

  String SELECT_FRIENDS_BY_USER_ID_AND_STATUS = "SELECT * FROM " + TABLE_FRIEND + " WHERE " + COLUMN_FRIEND_ID + "=? AND " + COLUMN_STATUS + "=?";
}
