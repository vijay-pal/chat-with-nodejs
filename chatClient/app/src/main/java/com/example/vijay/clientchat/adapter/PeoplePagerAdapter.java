package com.example.vijay.clientchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vijay.clientchat.fragments.FriendsFragment;
import com.example.vijay.clientchat.utils.Constants;

/**
 * Created by vijay on 6/6/18.
 */

public class PeoplePagerAdapter extends FragmentPagerAdapter {

  private String mCurrentUserId;
  private String mCurrentUserName;
  private String mCurrentUserEmail;

  public PeoplePagerAdapter(FragmentManager fm, String userId, String userEmail, String userName) {
    super(fm);
    this.mCurrentUserId = userId;
    this.mCurrentUserName = userName;
    this.mCurrentUserEmail = userEmail;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return FriendsFragment.newInstance(mCurrentUserId, mCurrentUserEmail, mCurrentUserName,
          Constants.FRIEND_STATUS_CONFIRMED);
      case 1:
        return FriendsFragment.newInstance(mCurrentUserId, mCurrentUserEmail, mCurrentUserName,
          Constants.FRIEND_STATUS_PENDING);
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "Friends";
      case 1:
        return "Pending Request";
      default:
        return super.getPageTitle(position);
    }

  }
}
