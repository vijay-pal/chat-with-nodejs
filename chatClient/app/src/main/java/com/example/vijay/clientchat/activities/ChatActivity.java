package com.example.vijay.clientchat.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vijay.clientchat.CApplication;
import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.adapter.MessageAdapter;
import com.example.vijay.clientchat.db.DatabaseHelper;
import com.example.vijay.clientchat.db.table.UserTable;
import com.example.vijay.clientchat.models.Conversation;
import com.example.vijay.clientchat.models.Message;
import com.example.vijay.clientchat.models.User;
import com.example.vijay.clientchat.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by VIJAY-PAL on 24/8/17.
 */

public class ChatActivity extends BaseActivity implements TextWatcher, TextView.OnEditorActionListener,
  View.OnClickListener {

  private final static String TAG = "ChatActivity";
  private static final int TYPING_TIMER_LENGTH = 600;

  private List<Message> mMessages = new ArrayList<>();
  private Handler mTypingHandler = new Handler();
  private Gson gson = new GsonBuilder().create();
  private RecyclerView mMessagesView;
  private EditText mInputMessageView;

  private RecyclerView.Adapter mAdapter;
  private Socket mSocket;
  private String mSender;
  private String mSenderId;
  private String mReceiver;

  private boolean mTyping = false;
  private Boolean isConnected = false;

  private ActionBar mActionBar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    setUpToolBar((Toolbar) findViewById(R.id.toolbar));
    mActionBar = getSupportActionBar();

    mInputMessageView = (EditText) findViewById(R.id.edit_message);

    mMessagesView = (RecyclerView) findViewById(R.id.recycler_view);
    mMessagesView.setLayoutManager(new LinearLayoutManager(this));

    mAdapter = new MessageAdapter(mMessages);
    mMessagesView.setAdapter(mAdapter);

    mInputMessageView.addTextChangedListener(this);
    mInputMessageView.setOnEditorActionListener(this);

    ImageButton btnSend = (ImageButton) findViewById(R.id.btn_send);
    btnSend.setOnClickListener(this);
    ImageButton btnAttachment = (ImageButton) findViewById(R.id.btn_attachment);
    btnAttachment.setOnClickListener(this);

    initChat();
    createSocketConnection();
  }

  private void initChat() {
    Intent intent = getIntent();
    if (intent != null) {
      mSender = "2";//intent.getStringExtra("sender");
      mSenderId = "2";//intent.getStringExtra("sender_id");
      mReceiver = "1";//intent.getStringExtra("receiver");
//      addLog(getResources().getString(R.string.message_welcome));
//      addParticipantsLog(1);
      if (mActionBar != null) {
        mActionBar.setTitle(mReceiver);
      }
    }
  }

  private void createSocketConnection() {
    CApplication app = (CApplication) getApplication();
    mSocket = app.getSocket();
    mSocket.on(Socket.EVENT_CONNECT, onConnect);
    mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
    mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
    mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    mSocket.on("new_message", onNewMessage);
    mSocket.on("image_message", onNewImageMessage);
    mSocket.on("user joined", onUserJoined);
    mSocket.on("user left", onUserLeft);
    mSocket.on("typing", onTyping);
    mSocket.on("stop_typing", onStopTyping);
    mSocket.connect();
  }

  private void disconnectSocketConnection() {
    mSocket.disconnect();
    mSocket.off(Socket.EVENT_CONNECT, onConnect);
    mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
    mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
    mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    mSocket.off("new_message", onNewMessage);
    mSocket.off("image_message", onNewImageMessage);
    mSocket.off("user joined", onUserJoined);
    mSocket.off("user left", onUserLeft);
    mSocket.off("typing", onTyping);
    mSocket.off("stop_typing", onStopTyping);
  }

  private void addMessage(Message message) {
    mMessages.add(message);
    mAdapter.notifyItemInserted(mMessages.size() - 1);
    scrollToBottom();
  }

  private void addTyping(String username) {
    mMessages.add(new Message.Builder(Message.TYPE_ACTION)
      .sender(username).build());
    mAdapter.notifyItemInserted(mMessages.size() - 1);
    scrollToBottom();
  }

  private void removeTyping(String username) {
    for (int i = mMessages.size() - 1; i >= 0; i--) {
      Message message = mMessages.get(i);
      if (message.getViewType() == Message.TYPE_ACTION && message.getSender().equals(username)) {
        mMessages.remove(i);
        mAdapter.notifyItemRemoved(i);
      }
    }
  }

  private void attemptSend() {
    if (null == mSender) return;
    if (!mSocket.connected()) return;

    mTyping = false;

    String strMsg = mInputMessageView.getText().toString().trim();
    if (TextUtils.isEmpty(strMsg)) {
      mInputMessageView.requestFocus();
      return;
    }
    mInputMessageView.setText("");
    Message message = new Message.Builder(Message.TYPE_MESSAGE_USER)
      .message(strMsg).sender(mSender)
      .receiver(mReceiver)
      .build();
    addMessage(message);

    // perform the sending message attempt.
    Conversation conversation = new Conversation();
    conversation.setSender(mSender);
    conversation.setReceiver(mReceiver);
    conversation.setMessage(message.getMessage());
    mSocket.emit("one_to_one", gson.toJson(conversation, Conversation.class));
  }

  private void addLog(String message) {
    mMessages.add(new Message.Builder(Message.TYPE_LOG)
      .message(message).build());
    mAdapter.notifyItemInserted(mMessages.size() - 1);
    scrollToBottom();
  }

  private void addParticipantsLog(int numUsers) {
    addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
  }

  private void scrollToBottom() {
    mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
  }

  @Override
  public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
    if (id == R.id.send || id == EditorInfo.IME_NULL) {
      attemptSend();
      return true;
    }
    return false;
  }

  @Override
  public void onResult(Object result, int requestCode) {

  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

  }

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    if (null == mSender) return;
    if (!mSocket.connected()) return;

    if (!mTyping) {
      mTyping = true;
      mSocket.emit("typing", mReceiver);
    }

    mTypingHandler.removeCallbacks(onTypingTimeout);
    mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
  }

  @Override
  public void afterTextChanged(Editable editable) {

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    disconnectSocketConnection();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_send:
        attemptSend();
        break;
      case R.id.btn_attachment:
        pickImage();
        break;
    }
  }

  public void pickImage() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, 1);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
      if (data == null) {
        //Display an error
        return;
      }
      try {
        Uri uri = data.getData();
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream != null) {
          DisplayMetrics metrics = new DisplayMetrics();
          getWindowManager().getDefaultDisplay().getMetrics(metrics);
          Bitmap bitmap = Utils.scaleDownBitmap(BitmapFactory.decodeStream(inputStream), metrics.widthPixels * 7 / 8);
          String encodedImage = Utils.encodeImage(bitmap);
          Conversation object = new Conversation();
          object.setReceiver(mReceiver);
          object.setSender(mSender);
          object.setMessage(encodedImage);
          object.setType(Utils.getFileMineType(this, uri));
          mSocket.emit("image-message", gson.toJson(object, Conversation.class));

          Message message = new Message.Builder(Message.TYPE_IMAGE_MESSAGE_USER)
            .sender(mSender)
            .receiver(mReceiver)
            .message(encodedImage).build();
          addMessage(message);
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
    }
  }

  private Emitter.Listener onConnect = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
      Log.i(TAG, "onConnect::" + mSender);
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (!isConnected) {
            if (null != mSender)
              mSocket.emit("connect_user", mSender);
            isConnected = true;
          }
        }
      });
    }
  };

  private Emitter.Listener onDisconnect = new Emitter.Listener() {
    @Override
    public void call(Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          isConnected = false;
          Log.i(TAG, "onDisconnect::" + mSender);
        }
      });
    }
  };

  private Emitter.Listener onConnectError = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Log.i(TAG, "onConnectError::" + args);
        }
      });
    }
  };

  private Emitter.Listener onNewMessage = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Conversation conversation = gson.fromJson(args[0].toString(), Conversation.class);
          removeTyping(conversation.getSender());
          Message message = new Message.Builder(Message.TYPE_MESSAGE_FRIENED)
            .message(conversation.getMessage())
            .sender(conversation.getSender()).build();
          addMessage(message);
        }
      });
    }
  };

  private Emitter.Listener onNewImageMessage = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Conversation conversation = gson.fromJson(args[0].toString(), Conversation.class);
          removeTyping(conversation.getSender());
          Message message = new Message.Builder(Message.TYPE_IMAGE_MESSAGE_FRIEND)
            .message(conversation.getMessage())
            .sender(conversation.getSender()).build();
          addMessage(message);
        }
      });
    }
  };

  private Emitter.Listener onUserJoined = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          JSONObject data = (JSONObject) args[0];
          String username;
          int numUsers;
          try {
            username = data.getString("username");
            numUsers = data.getInt("numUsers");
          } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return;
          }

          addLog(getResources().getString(R.string.message_user_joined, username));
          addParticipantsLog(numUsers);
        }
      });
    }
  };

  private Emitter.Listener onUserLeft = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          JSONObject data = (JSONObject) args[0];
          String username;
          int numUsers;
          try {
            username = data.getString("username");
            numUsers = data.getInt("numUsers");
          } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return;
          }

          addLog(getResources().getString(R.string.message_user_left, username));
          addParticipantsLog(numUsers);
          removeTyping(username);
        }
      });
    }
  };

  private Emitter.Listener onTyping = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (mActionBar != null) {
            mActionBar.setSubtitle(getString(R.string.action_typing));
          }
        }
      });
    }
  };

  private Emitter.Listener onStopTyping = new Emitter.Listener() {
    @Override
    public void call(final Object... args) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (mActionBar != null) {
            mActionBar.setSubtitle("");
          }
        }
      });
    }
  };

  private Runnable onTypingTimeout = new Runnable() {
    @Override
    public void run() {
      if (!mTyping) return;
      mTyping = false;
      mSocket.emit("stop_typing", mReceiver);
    }
  };
}
