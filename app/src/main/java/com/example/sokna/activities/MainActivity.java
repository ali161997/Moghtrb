package com.example.sokna.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static int splash_time = 2000;
    ImageView splashLogo;
    Animation alpha;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashLogo = findViewById(R.id.logoSplash);
        alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        splashLogo.setAnimation(alpha);


        new Handler().postDelayed(() -> {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(MainActivity.this, home_explore.class);
                startActivity(intent);

            } else {
                Intent homeintent = new Intent(MainActivity.this, signing.class);
                startActivity(homeintent);
            }

            finish();

        }, splash_time);
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
