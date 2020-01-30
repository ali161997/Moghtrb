package com.example.sokna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.example.sokna.models.user;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class signing extends AppCompatActivity implements View.OnClickListener {
    private Button create_account;
    private Button login;
    private LoginButton loginButtonfacebook;
    private TextView more_options;
    private AuthCredential credential;
    private user newUser;
    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar prgbar;

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
        mAuth = FirebaseAuth.getInstance();
        create_account = findViewById(R.id.createAccount);
        login = findViewById(R.id.login);
        more_options = findViewById(R.id.more_options);
        prgbar = findViewById(R.id.progressBar_facebook);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        loginButtonfacebook = findViewById(R.id.buttonFacebookLogin);
        create_account.setOnClickListener(this);
        login.setOnClickListener(this);
        more_options.setOnClickListener(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

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
                            newUser = new user();
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
                        updateUI(user);
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
            Intent intent = new Intent(signing.this, home_explore.class);
            startActivity(intent);
            finish();

        } else {
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount:
                Intent intent = new Intent(getApplicationContext(), createacount.class);
                startActivity(intent);
                break;
            case R.id.login:
                Intent intent1 = new Intent(getApplicationContext(), login.class);
                startActivity(intent1);
                break;
            case R.id.more_options:
                Intent intent2 = new Intent(getApplicationContext(), moreOp.class);
                startActivity(intent2);
                break;
        }

    }
}
