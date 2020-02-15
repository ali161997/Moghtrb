package com.example.sokna.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

public class IdentifyHost extends AppCompatActivity implements View.OnClickListener {
    Button btnExplore;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_host);
        findViewById(R.id.startRead).setOnClickListener(this);
        btnExplore = findViewById(R.id.exploreRooms);
        btnExplore.setOnClickListener(this);
        resultText = findViewById(R.id.ifHostIdentified);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRead:
                new IntentIntegrator(this).setCaptureActivity(CaptureActivity.class).initiateScan();
                break;
            case R.id.exploreRooms:

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                btnExplore.setEnabled(false);
                resultText.setText(String.format("%s", "Can not Identify Host ! "));

            } else {
                btnExplore.setEnabled(true);
                resultText.setText(String.format("%s", " Host Identified  ✌"));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setDefaultLanguage(String Code) {

        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(Code)); // API 17+ only.
        res.updateConfiguration(conf, dm);

    }
}
