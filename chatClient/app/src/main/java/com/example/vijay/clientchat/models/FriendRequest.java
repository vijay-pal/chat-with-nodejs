package com.example.vijay.clientchat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vijay on 31/8/17.
 */

public class FriendRequest {

    public static class Request {
        @SerializedName("_id")
        @Expose
        private String id;

        @SerializedName("sender_email")
        @Expose
        private String sender;

        @SerializedName("sender_name")
        @Expose
        private String senderName;

        public Request() {
            super();
        }

        public Request(String id, String sender, String senderName) {
            this.id = id;
            this.sender = sender;
            this.senderName = senderName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }
    }

    public static class Confirmation {
        @SerializedName("_id")
        @Expose
        private String id;

        @SerializedName("requester_email")
        @Expose
        private String requesterEmail;

        @SerializedName("sender_email")
        @Expose
        private String senderEmail;

        @SerializedName("sender_name")
        @Expose
        private String senderName;

        @SerializedName("confirm")
        @Expose
        private String confirm;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRequesterEmail() {
            return requesterEmail;
        }

        public void setRequesterEmail(String requesterEmail) {
            this.requesterEmail = requesterEmail;
        }

        public String getSenderEmail() {
            return senderEmail;
        }

        public void setSenderEmail(String senderEmail) {
            this.senderEmail = senderEmail;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getConfirm() {
            return confirm;
        }

        public void setConfirm(String confirm) {
            this.confirm = confirm;
        }
    }

    public static class Response extends ResponseStatus {

    }
}
