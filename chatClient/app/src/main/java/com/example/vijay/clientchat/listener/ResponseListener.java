package com.example.vijay.clientchat.listener;

/**
 * Created by vijay on 24/8/17.
 */

public interface ResponseListener {

    void onProgress();

    void onError(Throwable t);

    void onResult(Object result, int requestCode);

    void onComplete();
}
