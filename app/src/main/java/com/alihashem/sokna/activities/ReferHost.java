package com.alihashem.sokna.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alihashem.sokna.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferHost extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbarRefer)
    Toolbar toolbarRefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_host);
        ButterKnife.bind(this);
        toolbarRefer.setNavigationOnClickListener(view -> onBackPressed());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
