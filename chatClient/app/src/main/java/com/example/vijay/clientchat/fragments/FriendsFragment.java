package com.example.vijay.clientchat.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.RecyclerItemClickListener;
import com.example.vijay.clientchat.activities.ChatActivity;
import com.example.vijay.clientchat.adapter.MemberAdapter;
import com.example.vijay.clientchat.db.DatabaseHelper;
import com.example.vijay.clientchat.db.table.UserTable;
import com.example.vijay.clientchat.models.FriendRequest;
import com.example.vijay.clientchat.models.User;
import com.example.vijay.clientchat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 6/6/18.
 */

public class FriendsFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

  private List<User.Friend> friends = new ArrayList<>();
  private RecyclerView.Adapter mAdapter = new MemberAdapter(friends);
  private Context mContext;
  private String mCurrentUserId;
  private String mCurrentUserName;
  private String mCurrentUserEmail;
  private int mFriendStatus;
  private FragmentEventListener mListener;

  public static Fragment newInstance(String userId, String userEmail, String userName, int status) {
    Fragment fragment = new FriendsFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Constants.KEY_USER_ID, userId);
    bundle.putString(Constants.KEY_USER_EMAIL, userEmail);
    bundle.putString(Constants.KEY_USER_NAME, userName);
    bundle.putInt(Constants.KEY_STATUS, status);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    Bundle bundle = getArguments();
    if (bundle != null) {
      mCurrentUserId = bundle.getString(Constants.KEY_USER_ID);
      mCurrentUserEmail = bundle.getString(Constants.KEY_USER_EMAIL);
      mCurrentUserName = bundle.getString(Constants.KEY_USER_NAME);
      mFriendStatus = bundle.getInt(Constants.KEY_STATUS);
    }
    if (context instanceof FragmentEventListener) {
      mListener = (FragmentEventListener) context;
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_friends, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setAdapter(mAdapter);
    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, this));
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    UserTable table = new UserTable(DatabaseHelper.getInstance(mContext).open(DatabaseHelper.READ_MODE));
    List<User.Friend> friends = table.getFriendsByUserId(mCurrentUserId, mFriendStatus);
    if (friends != null) {
      this.friends.addAll(friends);
      mAdapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onItemClick(View view, int position) {
    if (position < friends.size()) {
      User.Friend friend = friends.get(position);
      if (friend.getStatus() == Constants.FRIEND_STATUS_PENDING) {
        FriendRequest.Confirmation confirmation = new FriendRequest.Confirmation();
        confirmation.setId(friend.getId());
        confirmation.setRequesterEmail(friend.getEmail());
        confirmation.setSenderEmail(mCurrentUserEmail);
        confirmation.setSenderName(mCurrentUserName);
        confirmation.setConfirm("yes");
        if (mListener != null) {
          mListener.confirmFriendRequest(confirmation);
        }
        return;
      }
      Intent intent = new Intent(mContext, ChatActivity.class);
      intent.putExtra("sender", mCurrentUserEmail);
      intent.putExtra("sender_id", mCurrentUserId);
      intent.putExtra("receiver", friend.getEmail());
      startActivity(intent);
    }
  }

  public interface FragmentEventListener {
    void confirmFriendRequest(FriendRequest.Confirmation confirmation);
  }
}
