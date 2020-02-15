package com.example.sokna.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private Button go;
    private RelativeLayout relativeLayout;
    private ProgressBar prgbar;
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        relativeLayout = findViewById(R.id.relative_log);
        go = findViewById(R.id.btn_go);
        prgbar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.typeMailLogin);
        password = findViewById(R.id.type_password_login);
        back = findViewById(R.id.arrowback_login);
        back.setOnClickListener(this);
        go.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Error Login ,Try Again", Toast.LENGTH_LONG).show();
                    }
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
        // [END sign_in_with_email]
    }

    private void showProgressDialog() {
        prgbar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void setDefaultLanguage(String Code) {

        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(Code)); // API 17+ only.
        res.updateConfiguration(conf, dm);

    }

    private void hideProgressDialog() {
        prgbar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private boolean validateForm() {
        boolean valid = true;

        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
            if (TextUtils.isEmpty(str_email)) {
                email.setError("Required");
                valid = false;
            } else if (TextUtils.isEmpty(str_password)) {
                password.setError("Required");
                valid = false;
            }
        } else {
            password.setError(null);
            email.setError(null);
            valid = true;
        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(login.this, home_explore.class);
            startActivity(intent);
            finish();

        } else {


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                try {
                    signIn(email.getText().toString(), password.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.arrowback_login:
                onBackPressed();
                break;
        }
    }
}
