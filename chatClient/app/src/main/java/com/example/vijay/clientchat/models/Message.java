package com.example.vijay.clientchat.models;

/**
 * Created by vijay on 31/8/17.
 */

public class Message {
  public final static int TYPE_LOG = 1;
  public final static int TYPE_ACTION = 2;
  public final static int TYPE_MESSAGE_USER = 3;
  public final static int TYPE_MESSAGE_FRIENED = 4;
  public final static int TYPE_IMAGE_MESSAGE_USER = 5;
  public final static int TYPE_IMAGE_MESSAGE_FRIEND = 6;

  private int viewType;
  private String id;
  private String message;
  private String sender;
  private String receiver;
  private String type;
  private String url;
  private long createdAt;
  private long receivedAt;
  private String status;

  private Message(int viewType, String id, String message, String sender, String receiver, String type, String url, long createdAt, long receivedAt, String status) {
    this.viewType = viewType;
    this.id = id;
    this.message = message;
    this.sender = sender;
    this.receiver = receiver;
    this.type = type;
    this.url = url;
    this.createdAt = createdAt;
    this.receivedAt = receivedAt;
    this.status = status;
  }

  public int getViewType() {
    return viewType;
  }

  public String getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public long getReceivedAt() {
    return receivedAt;
  }

  public String getStatus() {
    return status;
  }

  public static class Builder {
    private int viewType;

    private String id;
    private String message;
    private String sender;
    private String receiver;
    private String type;
    private String url;
    private long createdAt;
    private long receivedAt;
    private String status;

    public Builder(int viewType) {
      this.viewType = viewType;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder sender(String sender) {
      this.sender = sender;
      return this;
    }

    public Builder receiver(String receiver) {
      this.receiver = receiver;
      return this;
    }

    public Builder type(String type) {
      this.type = type;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder createdAt(long createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder receivedAt(long receivedAt) {
      this.receivedAt = receivedAt;
      return this;
    }

    public Builder status(String status) {
      this.sender = status;
      return this;
    }

    public Message build() {
      return new Message(viewType, id, message, sender, receiver, type, url, createdAt, receivedAt, status);
    }
  }
}
