package com.example.vijay.clientchat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vijay on 30/8/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int WRITE_MODE = 1;
    public static final int READ_MODE = 0;
    private final static String DB_NAME = "chat.db";
    private final static int DB_VERSION = 1;
    private static DatabaseHelper helper;

    public static DatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        }
        return helper;
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Query.CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(Query.CREATE_TABLE_FRIEND);
        sqLiteDatabase.execSQL(Query.CREATE_TABLE_CONVERSATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public SQLiteDatabase open(int mode) {
        if (mode == WRITE_MODE) {
            return getWritableDatabase();
        }
        return getReadableDatabase();
    }
}
