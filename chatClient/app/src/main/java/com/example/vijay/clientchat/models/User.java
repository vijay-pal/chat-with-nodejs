package com.example.vijay.clientchat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vijay on 28/8/17.
 */

public class User {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("login_id")
    @Expose
    private String loginId = "";

    @SerializedName("display_name")
    @Expose
    private String displayName = "";

    @SerializedName("family_name")
    @Expose
    private String familyName = "";

    @SerializedName("email")
    @Expose
    private String email = "";

    @SerializedName("given_name")
    @Expose
    private String givenName = "";

    @SerializedName("photo_url")
    @Expose
    private String photoUrl = "";

    @SerializedName("login_token")
    @Expose
    private String token = "";

    @SerializedName("server_auth_code")
    @Expose
    private String serverAuthCode = "";

    @SerializedName("login_via")
    @Expose
    private String loginVia = "";

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("update_at")
    @Expose
    private long updateAt;

    @SerializedName("friends")
    @Expose
    private List<Friend> friends;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerAuthCode() {
        return serverAuthCode;
    }

    public void setServerAuthCode(String serverAuthCode) {
        this.serverAuthCode = serverAuthCode;
    }

    public String getLoginVia() {
        return loginVia;
    }

    public void setLoginVia(String loginVia) {
        this.loginVia = loginVia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public static class Friend {
        @SerializedName("_id")
        @Expose
        private String id = "";

        @SerializedName("display_name")
        @Expose
        private String displayName = "";

        @SerializedName("email")
        @Expose
        private String email = "";

        @SerializedName("status")
        @Expose
        private int status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
