package com.example.vijay.clientchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.db.DatabaseHelper;
import com.example.vijay.clientchat.db.table.UserTable;
import com.example.vijay.clientchat.models.RegistrationResponse;
import com.example.vijay.clientchat.models.User;
import com.example.vijay.clientchat.presenter.UserPresenter;
import com.example.vijay.clientchat.presenter.impl.UserPresenterImpl;
import com.example.vijay.clientchat.utils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
  View.OnClickListener {

  private static final String TAG = "SignInActivity";
  private static final int RC_SIGN_IN = 9001;

  private GoogleApiClient mGoogleApiClient;

  private UserPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Views

    // Button listeners
    findViewById(R.id.sign_in_button).setOnClickListener(this);

    mPresenter = new UserPresenterImpl(this);

    // [START configure_signin]
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestEmail()
      .build();
    // [END configure_signin]

    // [START build_client]
    // Build a GoogleApiClient with access to the Google Sign-In API and the
    // options specified by gso.
    mGoogleApiClient = new GoogleApiClient.Builder(this)
      .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
      .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
      .build();
    // [END build_client]

    // [START customize_button]
    // Set the dimensions of the sign-in button.
    SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
    signInButton.setSize(SignInButton.SIZE_STANDARD);
    // [END customize_button]
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  // [START onActivityResult]
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }
  // [END onActivityResult]

  // [START handleSignInResult]
  private void handleSignInResult(GoogleSignInResult result) {
    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    if (result.isSuccess()) {
      // Signed in successfully, show authenticated UI.
      GoogleSignInAccount acct = result.getSignInAccount();

      User user = new User();
      user.setLoginId(acct.getId());
      user.setDisplayName(acct.getDisplayName());
      user.setEmail(acct.getEmail());
      user.setFamilyName(acct.getFamilyName());
      user.setGivenName(acct.getGivenName());
      user.setPhotoUrl(acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : null);
      user.setToken(acct.getIdToken());
      user.setServerAuthCode(acct.getServerAuthCode());
      user.setLoginVia("g+");

      mPresenter.register(user, 0);

    }
  }
  // [END handleSignInResult]

  // [START signIn]
  private void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }
  // [END signIn]

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  @Override
  protected void onStop() {
    super.onStop();
    onComplete();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.sign_in_button:
        signIn();
        break;
    }
  }

  @Override
  public void onResult(Object result, int requestCode) {
    if (result != null && result instanceof RegistrationResponse) {
      RegistrationResponse response = (RegistrationResponse) result;
      Toast.makeText(this, response.getMessage(), Toast.LENGTH_LONG).show();
      if (response.getStatus()) {
        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        UserTable table = new UserTable(helper.open(DatabaseHelper.WRITE_MODE));
        table.insertUser(response.getUser());
        Intent intent = new Intent(this, PeopleListActivity.class);
        intent.putExtra(Constants.KEY_USER_ID, response.getUser().getId());
        intent.putExtra(Constants.KEY_USER_EMAIL, response.getUser().getEmail());
        intent.putExtra(Constants.KEY_USER_NAME, response.getUser().getDisplayName());
        startActivity(intent);
        finish();
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.i("DATA", "onDestroy");
  }
}