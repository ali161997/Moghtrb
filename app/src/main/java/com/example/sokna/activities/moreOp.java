package com.example.sokna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;

public class moreOp extends AppCompatActivity {
    Button create_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_op);
        create_account = findViewById(R.id.createAccount);
        create_account.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), createacount.class);
            startActivity(intent);

        });
    }
}
