package com.alihashem.moghtrb.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;

import com.alihashem.moghtrb.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @BindView(R.id.toolbarSetting)
    Toolbar toolbarSetting;
    private RadioGroup radioGroupSelectLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbarSetting.setNavigationOnClickListener(view -> onBackPressed());
        AppCompatRadioButton enButton = findViewById(R.id.englishButton);
        radioGroupSelectLanguage = findViewById(R.id.radioSelectLanguage);
        AppCompatRadioButton arButton = findViewById(R.id.arabicButton);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        if (getResources().getConfiguration().locale.getLanguage().equals("ar"))
            arButton.setChecked(true);
        else
            enButton.setChecked(true);
        editor.putString("lang", getResources().getConfiguration().locale.getLanguage()).commit();
        radioGroupSelectLanguage.setOnCheckedChangeListener(this);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.radioSelectLanguage) {
            if (checkedId == R.id.englishButton) {
                editor.clear().commit();
            } else
                editor.putString("lang", "ar").commit();
            Toast.makeText(this, " Restart App to Apply Changes", Toast.LENGTH_LONG).show();
            Restart();

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void Restart() {
        new AlertDialog.Builder(this)
                .setTitle("Restart")
                .setMessage("Restart App to Apply Changes?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
//                    if (ProcessPhoenix.isPhoenixProcess(this)) {
//                        return;
//                    }
//                    Intent nextIntent = new Intent(this, Splash.class);
//                    ProcessPhoenix.triggerRebirth(this, nextIntent);
                    Intent mStartActivity = new Intent(this, Splash.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, 0, mPendingIntent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }).setNegativeButton(android.R.string.no, null).show();


    }


}
