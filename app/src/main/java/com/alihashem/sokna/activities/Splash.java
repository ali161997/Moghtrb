package com.alihashem.sokna.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alihashem.sokna.R;
import com.alihashem.sokna.Repository.explore_Repository;
import com.alihashem.sokna.models.MyContextWrapper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Splash extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static int splash_time = 2000;
    ImageView splashLogo;
    Animation alpha;
    private SharedPreferences pref;


    private FirebaseAuth mAuth;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);


        splashLogo = findViewById(R.id.logoSplash);
        alpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        splashLogo.setAnimation(alpha);
        new Handler().postDelayed(() -> {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(Splash.this, Home.class);
                startActivity(intent);
                Log.d(TAG, "onCreate: saccessful sign");

            } else {
                Intent homeintent = new Intent(Splash.this, signing.class);
                Log.d(TAG, "onCreate: failed go sign");
                startActivity(homeintent);
            }

            finish();

        }, splash_time);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences pref = newBase.getSharedPreferences("MyPref", 0);
        Locale newLocale;
        if (pref.contains("lang")) {

            newLocale = new Locale(pref.getString("lang", null));
        } else
            newLocale = new Locale(newBase.getResources().getConfiguration().locale.getLanguage());

        // .. create or get your new Locale object here.
        explore_Repository.getInstance().setLang(newLocale.getLanguage());
        Context context = MyContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);
    }


}