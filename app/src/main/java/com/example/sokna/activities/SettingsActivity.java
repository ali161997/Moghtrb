package com.example.sokna.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Button back;
    private RadioGroup radioGroupSelectLanguage;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroupSelectLanguage = findViewById(R.id.radioSelectLanguage);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if (pref.contains("lang")) {
            if (!pref.getString("lang", null).equals(null)) {
                if (pref.getString("lang", null).equals("ar"))
                    radioGroupSelectLanguage.check(R.id.arabicButton);
                else
                    radioGroupSelectLanguage.check(R.id.englishButton);

            } else
                radioGroupSelectLanguage.check(R.id.englishButton);
        }
        back = findViewById(R.id.backSitting);
        radioGroupSelectLanguage.setOnCheckedChangeListener(this);
        back.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backSitting) {
            onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.radioSelectLanguage) {
            if (checkedId == R.id.englishButton) {
                setDefaultLanguage("en");
                editor.putString("lang", "en");
            } else if (checkedId == R.id.arabicButton) {
                setDefaultLanguage("ar");
                editor.putString("lang", "ar");
            }
        }
        editor.commit();
        Toast.makeText(this, "App Will Restart In One Second ", Toast.LENGTH_LONG).show();
        Intent nextIntent = new Intent(this, MainActivity.class);
        new Handler().postDelayed(() ->
        {
            ProcessPhoenix.triggerRebirth(this, nextIntent);
        }, 1000);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setDefaultLanguage(String Code) {
        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(Code)); // API 17+ only.
        res.updateConfiguration(conf, dm);

    }
}
