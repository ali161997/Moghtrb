package com.alihashem.sokna.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;

import com.alihashem.sokna.R;

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
            if (checkedId == R.id.englishButton)
                editor.putString("lang", "en").commit();
            else
                editor.putString("lang", "ar").commit();

            Toast.makeText(this, "you need to Restart App to Apply Changes", Toast.LENGTH_LONG).show();
//            Intent nextIntent = new Intent(this, MainActivity.class);
//            new Handler().postDelayed(() ->
//            {
//                ProcessPhoenix.triggerRebirth(this, nextIntent);
//            }, 2000);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
