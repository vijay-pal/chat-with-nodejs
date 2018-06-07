package com.example.vijay.clientchat;

import android.app.Application;

import com.example.vijay.clientchat.services.Configs;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by vijay on 24/8/17.
 */

public class CApplication extends Application {

  private Socket mSocket;

  {
    try {
      mSocket = IO.socket(Configs.CHAT_SERVER_URL);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public Socket getSocket() {
    return mSocket;
  }
}
