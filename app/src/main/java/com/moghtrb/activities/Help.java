package com.moghtrb.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.moghtrb.R;

public class Help extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private static final String TAG = "HELP";
    SimpleDraweeView logo;
    AppBarLayout appBarHelp;
    Toolbar toolbarHelp;
    MaterialTextView phone1;
    MaterialTextView phone2;
    MaterialTextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        logo = findViewById(R.id.logoHelp);
        appBarHelp = findViewById(R.id.helpAppBar);
        appBarHelp.addOnOffsetChangedListener(this);
        toolbarHelp = findViewById(R.id.toolBarHelp);
        phone1 = findViewById(R.id.tvPhone1);
        phone1.setOnClickListener(this);
        phone2 = findViewById(R.id.tvPhone2);
        phone2.setOnClickListener(this);
        mail = findViewById(R.id.mailTV);
        mail.setOnClickListener(this);


        toolbarHelp.setNavigationOnClickListener(v -> onBackPressed());

    }

    private float dpToPixel(float dp) {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(appBarLayout.getTotalScrollRange()) == -1)//if expanded
        {
        } else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) { //if collapsed
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPhone1:
                copyToClipBoard(phone1.getText());
                break;
            case R.id.tvPhone2:
                copyToClipBoard(phone2.getText());
                break;
            case R.id.mailTV:
                startComposer();
                break;
        }
    }

    private void startComposer() {
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
        intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent.setData(Uri.parse("mailto:info@moghtrb.com")); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private void copyToClipBoard(CharSequence phone) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("host phone", phone);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to Clip Board", Toast.LENGTH_SHORT).show();
    }
}