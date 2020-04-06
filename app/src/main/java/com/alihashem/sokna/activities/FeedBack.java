package com.alihashem.sokna.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alihashem.sokna.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBack extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.typeFeedBack)
    TextInputEditText typeFeedBack;
    @BindView(R.id.submitTextBtn)
    MaterialButton submitButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @BindView(R.id.toolbarFeed)
    Toolbar toolbarFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        submitButton.setOnClickListener(this);
        toolbarFeed.setNavigationOnClickListener(view -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitTextBtn:
                String text = typeFeedBack.getText().toString();
                if (!TextUtils.isEmpty(text))
                    uploadFeedBack();
                else
                    Toast.makeText(getApplicationContext(), "Please Enter FeedBack", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void uploadFeedBack() {
        HashMap<String, String> feedBack = new HashMap<>();
        feedBack.put("feedBack", typeFeedBack.getText().toString());
        db.collection("FeedBacks").document(FirebaseAuth.getInstance().getUid())
                .set(feedBack)
                .addOnSuccessListener(aVoid ->
                {
                    typeFeedBack.setText("");
                    Toast.makeText(getApplicationContext(), "Thank you For FeedBack", Toast.LENGTH_SHORT).show();
                })

                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "please Try again", Toast.LENGTH_SHORT).show());

    }
}
