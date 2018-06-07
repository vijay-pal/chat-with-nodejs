package com.example.vijay.clientchat.presenter.impl;

import com.example.vijay.clientchat.listener.ResponseListener;
import com.example.vijay.clientchat.presenter.SearchPresenter;
import com.example.vijay.clientchat.services.ApiService;
import com.example.vijay.clientchat.services.Configs;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by vijay on 30/8/17.
 */

public class SearchPresenterImpl implements SearchPresenter {
    private ResponseListener mListener;
    private ApiService mApiService;

    public SearchPresenterImpl(ResponseListener mListener) {
        this.mListener = mListener;
        mApiService = new ApiService(Configs.CHAT_SERVER_URL);
    }

    @Override
    public void searchPeople(String userEmail, String query, int requestCode) {
        mApiService.getApi().searchPeople(query)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ObserverImpl<>(mListener, requestCode));
    }
}
