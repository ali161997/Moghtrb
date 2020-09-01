package com.moghtrb.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.moghtrb.R;
import com.moghtrb.models.HostID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IdentifyHost extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "IdentifyHost";
    @BindView(R.id.toolbarIdentify)
    Toolbar toolbarIdentify;
    boolean confirmed = false;
    ProgressBar prog;
    private TextView resultText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_identifyhost);
        ButterKnife.bind(this);
        findViewById(R.id.startRead).setOnClickListener(this);
        toolbarIdentify.setNavigationOnClickListener(view -> onBackPressed());
        resultText = findViewById(R.id.ifHostIdentified);
        prog = findViewById(R.id.identifyProgress);
        prog.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRead:
                new IntentIntegrator(this).setCaptureActivity(Capture.class).initiateScan();
                break;


        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void showProgressDialog() {
        prog.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressDialog() {
        prog.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                resultText.setText(String.format("%s", "Can not Identify Host ! "));


            } else {
                showProgressDialog();
                if (checkForHostId(result.getContents())) {
                    resultText.setText(String.format("%s", " Host Identified  ✌"));
                    new Handler().postDelayed(() -> {
                        hideProgressDialog();
                        Intent homeintent = new Intent(IdentifyHost.this, Home.class);
                        startActivity(homeintent);
                        HostID.getInstance().setHostId(result.getContents());
                        Log.i(TAG, "onActivityResult: " + result.getContents());

                        finish();

                    }, 2000);
                } else {
                    resultText.setText(String.format("%s", "Can not Identify Host ! "));
                    Toast.makeText(IdentifyHost.this, "Please Try Again", Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean checkForHostId(String id) {

        db.collection("Hosts")
                .document(id)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                confirmed = document.exists();
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
                confirmed = false;
            }
        });
        return confirmed;
    }

}
