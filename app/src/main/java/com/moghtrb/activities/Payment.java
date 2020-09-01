package com.moghtrb.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.material.textview.MaterialTextView;
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
    @BindView(R.id.valueMonth)
    TextView valueMonth; // سعر الشهر
    @BindView(R.id.valuePreMonth)
    TextView valuePreMonth; //مقدما
    @BindView(R.id.valueServices)
    TextView valueServices;
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
    @BindView(R.id.valueCommission)
    TextView valueCommission;
    @BindView(R.id.costMonthTv)
    TextView tvAmount;
    @BindView(R.id.valueMin)
    TextView valueMin;
    @BindView(R.id.appBarLog)
    AppBarLayout appBarPayment;
    @BindView(R.id.mobilesRadios)
    RadioGroup radioGroupMobiles;
    @BindView(R.id.linearDetail)
    LinearLayout linearDetail;
    @BindView(R.id.tvDetails)
    MaterialTextView tvDetails;
    private MutableLiveData<HashMap<String, String>> phoneNumbers;
    private MutableLiveData<String> indexNotification;
    private MutableLiveData<HashMap<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        data = new MutableLiveData<>();
        indexNotification = new MutableLiveData<>();
        radioGroupMobiles.setOnCheckedChangeListener(this);
        indexNotification.setValue(getIntent().getStringExtra("index"));
        Log.i(TAG, "onCreate: noty id " + getIntent().getStringExtra("index"));
        phoneNumbers = new MutableLiveData<>();

        data.observe(this, data -> {
            if (data.get("type").equalsIgnoreCase("student")) {
                valueMonth.setText(data.get("month"));
                tvAmount.setText(getString(R.string.cost_per_month));
                valuePreMonth.setText(data.get("advance"));
                valueServices.setText(data.get("services"));

            } else {
                valueMonth.setText(data.get("dayCost"));
                tvAmount.setText(getString(R.string.cost_per_day));
            }

            valueNet.setText(data.get("total"));
            valueCommission.setText(data.get("commission"));
            valueMin.setText(data.get("min"));

        });
        setPrefixPhone();
        getNumbers();
        barPay.setNavigationOnClickListener(v ->
                onBackPressed()
        );
        indexNotification.observe(this, this::getDetails);

    }

    private void getDetails(String detailID) {
        HashMap<String, String> map = new HashMap<>();
        DocumentReference docRef =
                FirebaseFirestore
                        .getInstance()
                        .collection("Accepted-Requests")
                        .document(detailID);
        docRef
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                Log.i(TAG, "getDetails: " + document.getData());
                                map.put("type", document.get("type").toString());
                                map.put("dayCost", document.get("dayCost").toString());
                                map.put("month", document.get("month").toString());
                                map.put("advance", document.get("advance").toString());
                                map.put("total", document.get("total").toString());
                                map.put("commission", document.get("commission").toString());
                                map.put("min", document.get("min").toString());
                                if (document.get("services").toString().equals(null))
                                    map.put("services", "0");
                                else map.put("services", document.get("services").toString());
                                data.setValue(map);
                                Log.i(TAG, "getDetails: " + document.getData());
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

    @OnClick({R.id.tvPhone, R.id.btnConfirm, R.id.tvDetails})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                if (isValidMobile(phoneEdit.getText().toString().trim()) && radioGroupMobiles.getCheckedRadioButtonId() != -1) {
                    uploadConfirmation();
                } else
                    phoneEdit.setError("not Valid");
                break;
            case R.id.tvPhone:
                if (tvPhone.getText().toString().equals(""))
                    Toast.makeText(this, "Check first", Toast.LENGTH_SHORT).show();
                else copyPhoneToClip();
                break;
            case R.id.tvDetails:
                if (linearDetail.getVisibility() == View.VISIBLE)
                    extend(false);
                else extend(true);
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

    private void extend(boolean extended) {
        if (extended) {
            tvDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
            linearDetail.setVisibility(View.VISIBLE);
        } else {
            linearDetail.setVisibility(View.GONE);
            tvDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
        }
    }
}
