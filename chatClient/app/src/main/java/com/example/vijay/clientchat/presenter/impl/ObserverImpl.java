package com.example.vijay.clientchat.presenter.impl;

import com.example.vijay.clientchat.listener.ResponseListener;

import rx.Observer;

/**
 * Created by vijay on 24/8/17.
 */

public class ObserverImpl<T> implements Observer<T> {
    private ResponseListener mListener;
    private int requestCode;

    public ObserverImpl(ResponseListener mListener, int requestCode) {
        this.mListener = mListener;
        this.requestCode = requestCode;
    }

    @Override
    public void onCompleted() {
        if (mListener != null) {
            mListener.onComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mListener != null) {
            e.printStackTrace();
            mListener.onError(e);
        }
    }

    @Override
    public void onNext(T t) {
        if (mListener != null) {
            mListener.onResult(t, requestCode);
        }
    }
}
