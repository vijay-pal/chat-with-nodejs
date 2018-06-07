package com.example.vijay.clientchat.presenter;

import com.example.vijay.clientchat.models.FriendRequest;
import com.example.vijay.clientchat.models.User;

/**
 * Created by vijay on 24/8/17.
 */

public interface UserPresenter {

    void register(User user, int requestCode);

    void sendFriendRequest(FriendRequest.Request request, int requestCode);

    void confirmFriendRequest(String currentUserId, FriendRequest.Confirmation confirmation, int requestCode);
}
