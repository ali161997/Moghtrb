package com.alihashem.sokna.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alihashem.sokna.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Reset extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ResetActivity";

    @BindView(R.id.typeMailReset)
    TextInputEditText typeMailReset;
    @BindView(R.id.toolbarReset)
    Toolbar toolbarReset;

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        ButterKnife.bind(this);
        toolbarReset.setNavigationOnClickListener(view -> onBackPressed());

    }

    public void sendPasswordReset(String emailAddress) {
        // [START send_password_reset]
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        typeMailReset.setFocusable(false);
                        Snackbar.make(findViewById(android.R.id.content), "Successfully Sent follow Instructions",
                                Snackbar.LENGTH_LONG).show();
                    }
                });
        // [END send_password_reset]
    }

    @OnClick({R.id.btnSend})
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend) {
            if (isEmailValid(typeMailReset.getText().toString())) {
                sendPasswordReset(typeMailReset.getText().toString());
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Error Mail Format",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
