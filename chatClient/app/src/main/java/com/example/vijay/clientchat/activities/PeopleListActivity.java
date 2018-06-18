package com.example.vijay.clientchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.adapter.PeoplePagerAdapter;
import com.example.vijay.clientchat.fragments.FriendsFragment;
import com.example.vijay.clientchat.models.FriendRequest;
import com.example.vijay.clientchat.models.SearchResponse;
import com.example.vijay.clientchat.presenter.SearchPresenter;
import com.example.vijay.clientchat.presenter.UserPresenter;
import com.example.vijay.clientchat.presenter.impl.SearchPresenterImpl;
import com.example.vijay.clientchat.presenter.impl.UserPresenterImpl;
import com.example.vijay.clientchat.utils.Constants;
import com.example.vijay.clientchat.widget.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 30/8/17.
 */

public class PeopleListActivity extends BaseActivity implements MaterialSearchView.OnQueryTextListener,
  MaterialSearchView.SearchViewListener, AdapterView.OnItemClickListener, FriendsFragment.FragmentEventListener {

  private List<SearchResponse.People> suggestionPeople = new ArrayList<>();

  private PeoplePagerAdapter mAdapter;
  private SearchPresenter mSearchPresenter;
  private UserPresenter mUserPresenter;

  private String mCurrentUserId;
  private String mCurrentUserName;
  private String mCurrentUserEmail;

  private MaterialSearchView mSearchView;

  private Toolbar mToolbar;
  private boolean isSearching;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_member_list);

    Intent intent = getIntent();
    if (intent != null) {
      mCurrentUserId = intent.getStringExtra(Constants.KEY_USER_ID);
      mCurrentUserEmail = intent.getStringExtra(Constants.KEY_USER_EMAIL);
      mCurrentUserName = intent.getStringExtra(Constants.KEY_USER_NAME);
    }

    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    super.setUpToolBar(mToolbar);

    mSearchView = (MaterialSearchView) findViewById(R.id.search_view);

    mSearchView.setOnQueryTextListener(this);
    mSearchView.setOnSearchViewListener(this);
    mSearchView.setVoiceSearch(true);
    mSearchView.setCursorDrawable(R.drawable.shape_cursor_adapter);
    mSearchView.setOnItemClickListener(this);

    mSearchPresenter = new SearchPresenterImpl(this);
    mUserPresenter = new UserPresenterImpl(this);
    mAdapter = new PeoplePagerAdapter(getSupportFragmentManager(), mCurrentUserId, mCurrentUserEmail, mCurrentUserName);

    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
    tabLayout.setupWithViewPager(viewPager);
    viewPager.setAdapter(mAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_search, menu);

    MenuItem item = menu.findItem(R.id.action_search);
    mSearchView.setMenuItem(item);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public void onBackPressed() {
    if (mSearchView.isSearchOpen()) {
      mSearchView.closeSearch();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void onResult(Object result, int requestCode) {
    switch (requestCode) {
      case Constants.REQUEST_CODE_1: {
        isSearching = false;
        if (result != null && result instanceof SearchResponse) {
          SearchResponse response = (SearchResponse) result;
          if (response.getStatus()) {
            suggestionPeople.clear();
            suggestionPeople.addAll(response.getPeoples());
            mSearchView.setSuggestions(suggestionPeople);
          }
        }
      }
      break;
      case Constants.REQUEST_CODE_2:
      case Constants.REQUEST_CODE_3: {
        if (result != null && result instanceof FriendRequest.Response) {
          FriendRequest.Response response = (FriendRequest.Response) result;
          if (response.isStatus()) {
            mAdapter.notifyDataSetChanged();
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }
      break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    if (suggestionPeople.size() > position) {
      SearchResponse.People people = suggestionPeople.get(position);
      FriendRequest.Request request = new FriendRequest.Request(people.getId(), mCurrentUserEmail, mCurrentUserName);
      mUserPresenter.sendFriendRequest(request, Constants.REQUEST_CODE_2);
    }
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    searchQuery(query);
    return true;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    searchQuery(newText);
    return true;
  }

  private void searchQuery(String query) {
    Log.i("DATA", "query::" + query);
    if (!isSearching) {
      mSearchPresenter.searchPeople(mCurrentUserEmail, query, Constants.REQUEST_CODE_1);
    }
  }

  @Override
  public void onSearchViewShown() {

  }

  @Override
  public void onSearchViewClosed() {

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      if (matches != null && matches.size() > 0) {
        String searchWrd = matches.get(0);
        if (!TextUtils.isEmpty(searchWrd)) {
          mSearchView.setQuery(searchWrd, false);
        }
      }

      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void confirmFriendRequest(FriendRequest.Confirmation confirmation) {
    mUserPresenter.confirmFriendRequest(mCurrentUserId, confirmation, Constants.REQUEST_CODE_3);
  }
}
