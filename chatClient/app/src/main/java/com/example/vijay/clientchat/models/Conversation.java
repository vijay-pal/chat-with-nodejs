package com.example.vijay.clientchat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 25/8/17.
 */


public class Conversation {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message = "";

    @SerializedName("sender")
    @Expose
    private String sender = "";

    @SerializedName("receiver")
    @Expose
    private String receiver = "";

    @SerializedName("type")
    @Expose
    private String type = "";

    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("received_at")
    @Expose
    private long receivedAt;

    @SerializedName("status")
    @Expose
    private String status = "";

    public Conversation() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(long receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

