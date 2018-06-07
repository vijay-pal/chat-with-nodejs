package com.example.vijay.clientchat.activities;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.vijay.clientchat.listener.ResponseListener;

/**
 * Created by vijay on 24/8/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements ResponseListener {

  private ProgressDialog mProgressDialog;

  protected void showSnakeBar(View parent, CharSequence message) {
    if (parent == null) {
      parent = findViewById(android.R.id.content);
    }
    Snackbar snackbar = Snackbar
      .make(parent, message, Snackbar.LENGTH_LONG);
    snackbar.show();
  }

  protected void setUpToolBar(Toolbar toolBar) {
    setSupportActionBar(toolBar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onProgress() {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(this);
      mProgressDialog.setMessage("Loading...");
      mProgressDialog.setIndeterminate(true);
    }

    mProgressDialog.show();
  }

  @Override
  public void onError(Throwable t) {
    if (t != null) {
      showSnakeBar(null, t.getMessage());
    }
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
  }

  @Override
  public void onComplete() {
    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }
  }
}
