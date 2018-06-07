package com.example.vijay.clientchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.models.User;

import java.util.List;

/**
 * Created by VIJAY PAL on 25/8/17.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
  private List<User.Friend> mFriends;

  public MemberAdapter(List<User.Friend> mFriends) {
    this.mFriends = mFriends;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MemberAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    User.Friend friend = mFriends.get(position);
    holder.mUsernameView.setText(friend.getDisplayName());
    holder.mUserEmailView.setText(friend.getEmail());
  }

  @Override
  public int getItemCount() {
    return mFriends.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView mUsernameView;
    private final TextView mUserEmailView;

    ViewHolder(View itemView) {
      super(itemView);
      mUsernameView = itemView.findViewById(R.id.username);
      mUserEmailView = itemView.findViewById(R.id.user_email);
    }
  }
}
