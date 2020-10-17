package com.moghtrb.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.R;

import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Payment extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "PaymentActivity";
    @BindView(R.id.valueNet)
    TextView valueNet;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.vodafoneCheck)
    RadioButton vodafoneCheck;
    @BindView(R.id.etisalatCheck)
    RadioButton etisalatCheck;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.btnConfirm)
    MaterialButton btnConfirm;
    @BindView(R.id.typPhonePayment)
    EditText phoneEdit;
    @BindView(R.id.barPay)
    Toolbar barPay;
    @BindView(R.id.valueMin)
    TextView valueMin;
    @BindView(R.id.commissionTv)
    TextView commissionTv;
    @BindView(R.id.valueCommission)
    TextView valueCommission;
    @BindView(R.id.typPromoCode)
    TextInputEditText typPromoCode;
    @BindView(R.id.fieldPromoCode)
    TextInputLayout fieldPromoCode;
    @BindView(R.id.appBarLog)
    AppBarLayout appBarPayment;
    @BindView(R.id.mobilesRadios)
    RadioGroup radioGroupMobiles;
    HashMap<String, Object> deta;
    private MutableLiveData<HashMap<String, String>> phoneNumbers;
    private MutableLiveData<String> indexNotification;
    private MutableLiveData<HashMap<String, Object>> data;
    private MutableLiveData<Boolean> codeVerified;
    private MutableLiveData<Double> valueCode;
    private MutableLiveData<Double> commission;
    private MutableLiveData<Double> min;
    private MutableLiveData<Double> total;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        data = new MutableLiveData<>();
        indexNotification = new MutableLiveData<>();
        valueCode = new MutableLiveData<>();
        codeVerified = new MutableLiveData<>();
        commission = new MutableLiveData<>();
        min = new MutableLiveData<>();
        total = new MutableLiveData<>();
        radioGroupMobiles.setOnCheckedChangeListener(this);
        indexNotification.setValue(getIntent().getStringExtra("index"));
        Log.i(TAG, "onCreate: noty id " + getIntent().getStringExtra("index"));
        phoneNumbers = new MutableLiveData<>();

        data.observe(this, data -> {
            commission.setValue((Double) data.get("commission"));
            min.setValue((Double) data.get("min"));
            total.setValue((Double) data.get("total"));


        });
        setPrefixPhone();
        getNumbers();
        barPay.setNavigationOnClickListener(v ->
                onBackPressed()
        );
        indexNotification.observe(this, this::getDetails);
        commission.observe(this, t -> {
            valueCommission.setText(Double.toString(t));
        });
        min.observe(this, y -> {
            valueMin.setText(Double.toString(y));
        });
        total.observe(this, total -> {
            valueNet.setText(Double.toString(total));
        });

    }

    public void verifyCode(String code) {
        FirebaseFirestore.getInstance().collection("Codes").document("promoCodes")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot d = task.getResult();
                if (d.exists()) {
                    if (d.contains(code)) {
                        deta = (HashMap<String, Object>) d.get(code);
                        if (((Long) deta.get("num")).doubleValue() < ((Long) deta.get("total")).doubleValue()) {
                            codeVerified.setValue(true);
                            this.code = code;
                            valueCode.setValue(((Long) deta.get("value")).doubleValue());
                        } else codeVerified.setValue(false);
                    }
                } else {
                    codeVerified.setValue(false);
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    private void getDetails(String detailID) {
        HashMap<String, Object> map = new HashMap<>();
        DocumentReference docRef =
                FirebaseFirestore
                        .getInstance()
                        .collection("Accepted-Requests")
                        .document(detailID);
        Log.i(TAG, "getDetails: " + detailID);
        docRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                Log.i(TAG, "getDetails: " + document.getData());
                                map.put("type", document.getString("type"));
                                map.put("month", document.getDouble("month"));
                                map.put("numGuests", document.getDouble("numGuests"));
                                map.put("advance", document.getDouble("advance"));
                                map.put("total", document.getDouble("total"));
                                map.put("commission", document.getDouble("commission"));
                                map.put("min", document.getDouble("min"));
                                if (document.get("services").toString().equals(null))
                                    map.put("services", 0.0);
                                else map.put("services", document.getDouble("services"));
                                data.setValue(map);
                                Log.i(TAG, "getDetails: " + document.getData());
                                map.put("dayCost", document.getDouble("dayCost"));
                            } catch (Exception e) {
                                Log.i(TAG, "getDetails: " + e.getMessage());
                            }
                        } else {
                            Toast.makeText(this, "no data Existed", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                });

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

    @OnClick({R.id.tvPhone, R.id.btnConfirm, R.id.btnVerify})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVerify:
                if (!typPromoCode.getText().toString().equals("")) {
                    try {
                        verifyCode(typPromoCode.getText().toString());
                        codeVerified.observe(this, d -> {
                            if (d) {
                                Toast t = Toast.makeText(this, R.string.successfulCoupon, Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                t.show();
                                valueCommission.setTextSize(40);
                                valueCommission.setTextColor(R.color.green);
                            } else {
                                Toast t = Toast.makeText(this, R.string.noCoupon, Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                t.show();
                            }

                        });
                        valueCode.observe(this, num -> {
                                    double c =
                                            (commission.getValue() - num * (double) data.getValue().get("numGuests"));
                                    double rem = commission.getValue() - c;
                                    commission.setValue(c);
                                    min.setValue(min.getValue() - rem);
                                    total.setValue(total.getValue() - rem);

                                }
                        );
                    } catch (Exception e) {
                        Log.i(TAG, "onClick: " + e.getMessage());
                    }
                }
                break;
            case R.id.btnConfirm:
                try {
                    if (isValidMobile(phoneEdit.getText().toString().trim()) && radioGroupMobiles.getCheckedRadioButtonId() != -1) {
                        uploadConfirmation();
                        UpdateValues();
                    } else
                        phoneEdit.setError("not Valid");
                } catch (Exception e) {
                    Log.i(TAG, "onClick: " + e.getMessage());
                }
                break;
            case R.id.tvPhone:
                if (tvPhone.getText().toString().equals(""))
                    Toast.makeText(this, "Check first", Toast.LENGTH_SHORT).show();
                else copyPhoneToClip();
                break;

        }
    }

    private void copyPhoneToClip() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("host phone", tvPhone.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied to Clip Board", Toast.LENGTH_SHORT).show();
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
                                map.put("vodafone", document.get("vodafone").toString());
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

    private void uploadConfirmation() {
        HashMap<String, Object> Conf = new HashMap<>();
        Timestamp timestamp = Timestamp.now();
        Conf.put("time", timestamp);
        Conf.put("phone", phoneEdit.getText().toString());
        Conf.put("coupon", code);
        Conf.put("min", min.getValue());
        Conf.put("total", total.getValue());
        Log.i(TAG, "uploadConfirmation: " + Conf);
        FirebaseFirestore.getInstance().collection("Wait-Confirmation")
                .document(indexNotification.getValue())
                .set(Conf)
                .addOnSuccessListener(aVoid -> {
                    showMessage();
                    Toast.makeText(this, "Confirmation Completed", Toast.LENGTH_LONG).show();
                    btnConfirm.setEnabled(false);
                    appBarPayment.setExpanded(true);

                })
                .addOnFailureListener(e -> Toast.makeText(this, "Confirmation Error", Toast.LENGTH_LONG).show());

    }

    public void UpdateValues() {
        if (codeVerified.getValue()) {
            deta.put("num", ((Long) deta.get("num")).doubleValue() + 1);
            FirebaseFirestore.getInstance().collection("Codes")
                    .document("promoCodes")
                    .update(code, deta).addOnCompleteListener(task -> {
                Log.i(TAG, "UpdateValues: completed");
            }).addOnFailureListener(e -> {
                Log.i(TAG, "UpdateValues: failure" + e.getMessage());
            });
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.mobilesRadios) {
            switch (group.getCheckedRadioButtonId()) {
                case R.id.vodafoneCheck:
                    tvPhone.setText(phoneNumbers.getValue().get("vodafone"));
                    tvPhone.setTextColor(getResources().getColor(R.color.quantum_googred));
                    break;
                case R.id.etisalatCheck:
                    tvPhone.setText(phoneNumbers.getValue().get("etisalat"));
                    tvPhone.setTextColor(getResources().getColor(R.color.green));
                    break;
            }
        }
    }

    private void showMessage() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Notice))
                .setMessage(getString(R.string.timeMessagePayment))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                }).show();
    }
}
