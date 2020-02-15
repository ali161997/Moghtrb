package com.example.sokna.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class veryfying extends AppCompatActivity implements View.OnClickListener {

    String phone;
    TextView phone_tv;
    EditText get_code_et;
    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    @BindView(R.id.line)
    View line;
    @BindView(R.id.txt_ver)
    TextView txtVer;
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.entering)
    EditText entering;
    @BindView(R.id.startVerifyPhone)
    Button startVerifyPhone;
    @BindView(R.id.buttonVerifyPhone)
    Button buttonVerifyPhone;
    @BindView(R.id.buttonResend)
    Button buttonResend;
    String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veryfying);
        ButterKnife.bind(this);
        x = getIntent().getStringExtra("phone");
        phone_tv = findViewById(R.id.phone_number);
        get_code_et = findViewById(R.id.entering);
        phoneNumber.setText(x);


    }

    @OnClick({R.id.startVerifyPhone, R.id.buttonVerifyPhone, R.id.buttonResend})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startVerifyPhone:
                break;
            case R.id.buttonVerifyPhone:
                break;
            case R.id.buttonResend:
                break;
        }
    }


    // [END sign_in_with_phone]

}
