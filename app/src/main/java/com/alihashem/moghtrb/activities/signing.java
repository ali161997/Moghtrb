package com.alihashem.moghtrb.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class signing extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SigningActivity";
    private static final int RC_SIGN_IN = 9001;
    //FaceBook
    private LoginButton loginButtonfacebook;
    private AuthCredential credential;
    //FireBase;
    private User newUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar prgbar;
    //Google
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    // [START declare_auth]
    private CallbackManager mCallbackManager;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);
        findViewById(R.id.createAccount).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.signInBtnGoogle).setOnClickListener(this);
        prgbar = findViewById(R.id.progressBar);
        prgbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //For faceBook
        FaceBookVariables();
        //for google
        GoogleVariables();

    }

    private void GoogleVariables() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void FaceBookVariables() {
        mCallbackManager = CallbackManager.Factory.create();
        loginButtonfacebook = findViewById(R.id.buttonFacebookLogin);
        loginButtonfacebook.setReadPermissions("email", "public_profile");
        loginButtonfacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]

            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                fireBaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if (isNewUser) {
                            newUser = new User();
                            newUser.setName(task.getResult().getUser().getDisplayName());
                            newUser.setEmail(task.getResult().getUser().getEmail());
                            newUser.setPhone(task.getResult().getUser().getPhoneNumber());
                            newUser.setPhotoUrl(task.getResult().getUser().getPhotoUrl().toString());
                            db.collection("users").document(user.getUid()).set(newUser)
                                    .addOnSuccessListener(documentReference ->
                                            Snackbar.make(findViewById(R.id.relativeSigning), "Success.", Snackbar.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Snackbar.make(findViewById(R.id.relativeSigning), "Success.", Snackbar.LENGTH_SHORT).show());
                        }
                        updateUI(user);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.relativeSigning),
                                "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Error occurred" + task.getException().getMessage());
                        Snackbar.make(findViewById(R.id.relativeSigning),
                                "Error occurred", Snackbar.LENGTH_LONG).show();
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if (isNewUser) {
                            newUser = new User();
                            newUser.setName(task.getResult().getUser().getDisplayName());
                            newUser.setEmail(task.getResult().getUser().getEmail());
                            newUser.setPhone(task.getResult().getUser().getPhoneNumber());
                            newUser.setPhotoUrl(task.getResult().getUser().getPhotoUrl().toString());
                            db.collection("users").document(user.getUid()).set(newUser)
                                    .addOnSuccessListener(documentReference -> Toast.makeText(signing.this, "success added", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(signing.this, "fail added", Toast.LENGTH_SHORT).show());
                        }

                        updateUI(user);

                        Log.i(TAG, "handleFacebookAccessToken: " + task.getResult().getUser().getDisplayName());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(signing.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
    }


    private void showProgressDialog() {

        prgbar.setVisibility(View.VISIBLE);
    }

    private void hideProgressDialog() {

        prgbar.setVisibility(View.INVISIBLE);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(signing.this, Home.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "error signing", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount:
                Intent intent = new Intent(this, CreateAccount.class);
                startActivity(intent);
                break;
            case R.id.login:
                Intent intent1 = new Intent(this, Login.class);
                startActivity(intent1);
                break;
            case R.id.signInBtnGoogle:
                if (InternetNotAvailable())
                    Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
                else
                    signIn();


                break;
        }

    }

    private boolean InternetNotAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }
}
