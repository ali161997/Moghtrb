package com.moghtrb.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moghtrb.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    @BindView(R.id.toolbarLog)
    Toolbar toolbarLog;
    private Button go;
    private ProgressBar prgbar;
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private TextView forgetTv;
    private boolean mailFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        toolbarLog.setNavigationOnClickListener(view -> onBackPressed());
        forgetTv = findViewById(R.id.forget);
        go = findViewById(R.id.btn_go);
        prgbar = findViewById(R.id.progressBar);
        prgbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.typeMailLogin);
        password = findViewById(R.id.type_password_login);
        go.setOnClickListener(this);
        forgetTv.setOnClickListener(this);


    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        mailFound = !isNewUser;

                    });
        }
        return matcher.matches() && mailFound;

    }

    private boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
                        Toast.makeText(Login.this, R.string.authSucess, Toast.LENGTH_SHORT).show();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(this, R.string.errorLOgin, Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
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


    private void hideProgressDialog() {
        prgbar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private boolean validateForm() {
        boolean valid;

        String str_email = email.getText().toString();
        String str_password = password.getText().toString();
        if (TextUtils.isEmpty(str_password) || !isValidPassword(str_password)) {
            password.setError("wrong password");
            valid = false;

        } else {
            password.setError(null);
            valid = true;
        }
        if (TextUtils.isEmpty(str_email) || !isEmailValid(str_email)) {
            email.setError("wrong mail");
            valid = false;
        } else {
            email.setError(null);
            valid = true;
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            finish();

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
                    if (InternetNotAvailable())
                        Toast.makeText(this, R.string.noInternet, Toast.LENGTH_LONG).show();
                    else
                        signIn(email.getText().toString().trim(), password.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.forget:
                Intent intent = new Intent(Login.this, Reset.class);
                startActivity(intent);
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
