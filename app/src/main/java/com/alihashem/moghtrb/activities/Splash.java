package com.alihashem.moghtrb.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.models.MyContextWrapper;
import com.alihashem.moghtrb.repositories.explore_Repository;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.libraries.maps.MapsInitializer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Splash extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private static int splashTime = 2000;
    ImageView splashLogo;
    Animation alpha;

    private FirebaseAuth mAuth;


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
            alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
            splashLogo.setAnimation(alpha);
        }

    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(Splash.this, Home.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, findViewById(R.id.logoSplash), "logo_transition");
                startActivity(intent);
                Log.d(TAG, "onCreate: successful sign");


            } else {
                Intent homeintent = new Intent(Splash.this, signing.class);
                Log.d(TAG, "onCreate: failed go sign");
                startActivity(homeintent);
            }

            finish();

        }, splashTime);

        if (isFinishing()) {
            MapsInitializer.initialize(getApplicationContext());

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(getApplicationContext());
        splashLogo = findViewById(R.id.logoSplash);
// set an enter transition
        getWindow().setEnterTransition(new Explode());
// set an exit transition
        getWindow().setExitTransition(new Explode());


    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences pref = newBase.getSharedPreferences("MyPref", 0);
        Locale newLocale = new Locale(newBase.getResources().getConfiguration().locale.getLanguage());
        if (pref.contains("lang"))
            newLocale = new Locale(pref.getString("lang", null));
        // .. create or get your new Locale object here.

        explore_Repository.getInstance().setLang(newLocale.getLanguage());
        Context context = MyContextWrapper.wrap(newBase, newLocale);
        super.attachBaseContext(context);


    }
}