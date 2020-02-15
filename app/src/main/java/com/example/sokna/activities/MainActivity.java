package com.example.sokna.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static int splash_time = 2000;
    ImageView splashLogo;
    Animation alpha;
    private FirebaseAuth mAuth;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (pref.contains("lang")) {
            if (!pref.getString("lang", null).equals(null))
                setDefaultLanguage(pref.getString("lang", null));
        }


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

    private void setDefaultLanguage(String Code) {
        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(Code)); // API 17+ only.
        res.updateConfiguration(conf, dm);

    }


}
