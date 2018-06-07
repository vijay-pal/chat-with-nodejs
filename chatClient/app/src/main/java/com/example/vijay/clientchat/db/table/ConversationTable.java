package com.example.vijay.clientchat.db.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.vijay.clientchat.db.Query;
import com.example.vijay.clientchat.models.Conversation;

/**
 * Created by vijay on 30/8/17.
 */

public class ConversationTable {
    private SQLiteDatabase db;

    public ConversationTable(SQLiteDatabase db) {
        this.db = db;
    }

    public void insertConversation(Conversation conversation) {
        ContentValues row = new ContentValues();
        row.put(Query.COLUMN__ID, conversation.getId());
        row.put(Query.COLUMN_MESSAGE, conversation.getMessage());
        row.put(Query.COLUMN_SENDER, conversation.getSender());
        row.put(Query.COLUMN_RECEIVER, conversation.getReceiver());
        row.put(Query.COLUMN_TYPE, conversation.getType());
        row.put(Query.COLUMN_URL, conversation.getUrl());
        row.put(Query.COLUMN_STATUS, conversation.getStatus());
        row.put(Query.COLUMN_CREATED_AT, conversation.getCreatedAt());
        row.put(Query.COLUMN_RECEIVED_AT, conversation.getReceivedAt());

        db.insertWithOnConflict(Query.TABLE_USER, null, row, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
