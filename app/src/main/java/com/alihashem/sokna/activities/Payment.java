package com.alihashem.sokna.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import com.alihashem.sokna.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Payment extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PaymentActivity";
    @BindView(R.id.valueMonth)
    TextView valueMonth;
    @BindView(R.id.valuePreMonth)
    TextView valuePreMonth;
    @BindView(R.id.valueServices)
    TextView valueServices;
    @BindView(R.id.valueNet)
    TextView valueNet;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.vodafoneCheck)
    CheckedTextView vodafoneCheck;
    @BindView(R.id.etisalatCheck)
    CheckedTextView etisalatCheck;
    @BindView(R.id.orangeCheck)
    CheckedTextView orangeCheck;
    @BindView(R.id.weCheck)
    CheckedTextView weCheck;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.btnConfirm)
    MaterialButton btnConfirm;
    @BindView(R.id.typPhonePayment)
    EditText phoneEdit;
    @BindView(R.id.barPay)
    Toolbar barPay;
    MutableLiveData<HashMap<String, String>> phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        phoneNumbers = new MutableLiveData<>();
        setPrefixPhone();
        getNumbers();
        barPay.setNavigationOnClickListener(v ->
                onBackPressed()
        );

    }

    private void setPrefixPhone() {
        phoneEdit.setText("+2");
        Selection.setSelection(phoneEdit.getText(), phoneEdit.getText().length());


        phoneEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+2")) {
                    phoneEdit.setText("+2");
                    Selection.setSelection(phoneEdit.getText(), phoneEdit.getText().length());

                }

            }
        });
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 13;
        }
        return false;
    }

    @OnClick({R.id.vodafoneCheck, R.id.etisalatCheck, R.id.orangeCheck, R.id.weCheck, R.id.btnConfirm})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vodafoneCheck:
                etisalatCheck.setChecked(false);
                orangeCheck.setChecked(false);
                weCheck.setChecked(false);
                tvPhone.setText(phoneNumbers.getValue().get("vodafone"));
                break;
            case R.id.etisalatCheck:
                vodafoneCheck.setChecked(false);
                orangeCheck.setChecked(false);
                weCheck.setChecked(false);
                tvPhone.setText(phoneNumbers.getValue().get("etisalat"));
                break;
            case R.id.orangeCheck:
                vodafoneCheck.setChecked(false);
                etisalatCheck.setChecked(false);
                weCheck.setChecked(false);
                tvPhone.setText(phoneNumbers.getValue().get("orange"));

                break;
            case R.id.weCheck:
                vodafoneCheck.setChecked(false);
                etisalatCheck.setChecked(false);
                orangeCheck.setChecked(false);
                tvPhone.setText(phoneNumbers.getValue().get("we"));
                break;
            case R.id.btnConfirm:
                if (isValidMobile(phoneEdit.getText().toString())) {
                    Toast.makeText(this, "Ok", Toast.LENGTH_LONG).show();
                } else
                    phoneEdit.setError("not Valid");
                break;

        }
    }

    public MutableLiveData<HashMap<String, String>> getNumbers() {
        HashMap<String, String> map = new HashMap<>();
        DocumentReference docRef =
                FirebaseFirestore
                        .getInstance()
                        .collection("Payment")
                        .document("Cash");
        docRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                map.put("etisalat", document.get("etisalat").toString());
                                map.put("orange", document.get("orange").toString());
                                map.put("vodafone", document.get("vodafone").toString());
                                map.put("we", document.get("we").toString());
                                phoneNumbers.setValue(map);

                            } catch (Exception e) {
                                Log.i(TAG, "instance initializer: " + e.getMessage());
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                });
        return phoneNumbers;

    }
}
