package com.example.vijay.clientchat.presenter.impl;

import com.example.vijay.clientchat.listener.ResponseListener;
import com.example.vijay.clientchat.models.FriendRequest;
import com.example.vijay.clientchat.models.User;
import com.example.vijay.clientchat.presenter.UserPresenter;
import com.example.vijay.clientchat.services.ApiService;
import com.example.vijay.clientchat.services.Configs;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vijay on 24/8/17.
 */

public class UserPresenterImpl implements UserPresenter {

    private ResponseListener mListener;
    private ApiService mApiService;

    public UserPresenterImpl(ResponseListener mListener) {
        this.mListener = mListener;
        mApiService = new ApiService(Configs.CHAT_SERVER_URL);
    }

    @Override
    public void register(User user, int requestCode) {
        if (mListener != null) mListener.onProgress();

        mApiService.getApi().register(user)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ObserverImpl<>(mListener, requestCode));
    }

    @Override
    public void sendFriendRequest(FriendRequest.Request request, int requestCode) {
        if (mListener != null) mListener.onProgress();

        mApiService.getApi().sendFriendRequest(request)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ObserverImpl<>(mListener, requestCode));
    }

    @Override
    public void confirmFriendRequest(String currentUserId, FriendRequest.Confirmation confirmation, int requestCode) {
        if (mListener != null) mListener.onProgress();

        mApiService.getApi().confirmFriendRequest(currentUserId, confirmation)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ObserverImpl<>(mListener, requestCode));
    }
}
