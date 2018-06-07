package com.example.vijay.clientchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.models.Message;
import com.example.vijay.clientchat.utils.Utils;

import java.util.List;

/**
 * Created by vijay on 25/8/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<Message> mMessages;

  public MessageAdapter(List<Message> messages) {
    mMessages = messages;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case Message.TYPE_MESSAGE_USER:
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_message_item, parent, false));
      case Message.TYPE_MESSAGE_FRIENED:
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_message_item, parent, false));
      case Message.TYPE_IMAGE_MESSAGE_USER:
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_image_message_item, parent, false));
      case Message.TYPE_IMAGE_MESSAGE_FRIEND:
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_image_message_item, parent, false));
      case Message.TYPE_LOG:
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_log, parent, false));
    }
    return null;

  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case Message.TYPE_MESSAGE_USER:
      case Message.TYPE_MESSAGE_FRIENED:
      case Message.TYPE_LOG:
        bindTextMessage((ViewHolder) holder, position);
        break;
      case Message.TYPE_IMAGE_MESSAGE_USER:
      case Message.TYPE_IMAGE_MESSAGE_FRIEND:
        bindImageMessage((ImageViewHolder) holder, position);
        break;
    }
  }

  private void bindTextMessage(ViewHolder holder, int position) {
    Message message = mMessages.get(position);
    holder.mMessageView.setText(message.getMessage());
  }

  private void bindImageMessage(ImageViewHolder holder, int position) {
    Message message = mMessages.get(position);
    if (message.getMessage() != null)
      holder.mImageMessage.setImageBitmap(Utils.decodeBase64(message.getMessage()));
  }

  @Override
  public int getItemCount() {
    return mMessages.size();
  }

  @Override
  public int getItemViewType(int position) {
    return mMessages.get(position).getViewType();
  }


  class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView mMessageView;

    ViewHolder(View itemView) {
      super(itemView);
      mMessageView = itemView.findViewById(R.id.message);
    }
  }

  static class ImageViewHolder extends RecyclerView.ViewHolder {
    private final ImageView mImageMessage;

    ImageViewHolder(View itemView) {
      super(itemView);
      mImageMessage = itemView.findViewById(R.id.image);
    }
  }
}
