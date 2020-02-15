package com.example.sokna.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sokna.R;
import com.example.sokna.models.user;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class createacount extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    boolean verified = false;
    private Button create_account;
    private String TAG = "create_acount";
    private EditText name_signup;
    private EditText email_signup;
    private EditText phone_signup;
    private EditText password_signup;
    private Spinner spinner_college_signup;
    private Button datePicker_birth_signup;
    private TextView signup_tv;
    private ProgressBar prgbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private user newUser = null;
    private RadioGroup getGender;
    private ImageView back;
    private DatePickerDialog.OnDateSetListener dateBirth;
    private Calendar birthCalendar;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacount);

        name_signup = findViewById(R.id.typename_signup);
        email_signup = findViewById(R.id.typemail_signup);
        phone_signup = findViewById(R.id.type_phone_signup);
        password_signup = findViewById(R.id.type_password_signup);
        create_account = findViewById(R.id.createAccount_signup);
        signup_tv = findViewById(R.id.signup_tv);
        getGender = findViewById(R.id.radio_gender_signup);
        prgbar = findViewById(R.id.progressBar_signup);
        spinner_college_signup = findViewById(R.id.college_Spinner_signup);
        datePicker_birth_signup = findViewById(R.id.date_picker_signup);
        back = findViewById(R.id.arrowback_signup);
        mAuth = FirebaseAuth.getInstance();
        newUser = new user();
        db = FirebaseFirestore.getInstance();
        String s = mAuth.getUid();
        getGender.setOnCheckedChangeListener(this);
        back.setOnClickListener(this);
        create_account.setOnClickListener(this);
        datePicker_birth_signup.setOnClickListener(this);
        birthCalendar = Calendar.getInstance();
        SetupCollegeSpinner();

        getDate();
    }

    private void SetupCollegeSpinner() {
        ArrayAdapter<String> adapterColleges = new ArrayAdapter<>(this, R.layout.spinner_where_item, getColleges());
        spinner_college_signup.setAdapter(adapterColleges);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Toast.makeText(createacount.this, "in validating", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        getDataForm();
                        db.collection("users").document(user.getUid()).set(newUser)
                                .addOnSuccessListener(documentReference -> Toast.makeText(createacount.this, "success added", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(createacount.this, "fail added", Toast.LENGTH_SHORT).show());
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(createacount.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = email_signup.getText().toString().trim();
        String phone = phone_signup.getText().toString();
        String name = name_signup.getText().toString().trim();
        String college = spinner_college_signup.getSelectedItem().toString();
        String birthdate = datePicker_birth_signup.getText().toString();
        String password = password_signup.getText().toString();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email))
                email_signup.setError("Required.");
            if (TextUtils.isEmpty(phone) || !verifying())
                phone_signup.setError("Required");
            if (TextUtils.isEmpty(name))
                name_signup.setError("Required");
            if (TextUtils.isEmpty(birthdate))
                datePicker_birth_signup.setError("Required");
            if (TextUtils.isEmpty(password))
                password_signup.setError("Required");
            valid = false;
        } else {
            email_signup.setError(null);
            phone_signup.setError(null);
            name_signup.setError(null);
            datePicker_birth_signup.setError(null);
            password_signup.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            signup_tv.setText(getResources().getString(R.string.signup));
            Intent intent = new Intent(getApplicationContext(), home_explore.class);
            startActivity(intent);
            finish();


        } else {


        }
    }

    private void showProgressDialog() {
        prgbar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressDialog() {
        prgbar.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private boolean verifying() {
        String phoneNumber = phone_signup.toString();
        String smsCode = "123456";


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber,
                60L,
                TimeUnit.SECONDS,
                this, /* activity */
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        // Instant verification is applied and a credential is directly returned.
                        // ...
                        Log.i(TAG, "Phone completed");
                        verified = true;
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.i(TAG, "Phone Incompleted");
                        verified = false;


                    }

                    // ...
                });
        return verified;
    }


    private void getDataForm() {
        newUser.setCollege(spinner_college_signup.getSelectedItem().toString().trim());
        newUser.setBirthdate(datePicker_birth_signup.getText().toString());
        newUser.setEmail(email_signup.getText().toString().trim());
        newUser.setPhone(phone_signup.getText().toString().trim());
        newUser.setName(name_signup.getText().toString().trim());
    }

    private void getDate() {
        dateBirth = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, monthOfYear);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String birh = String.format("%s/%s/%s", year, monthOfYear, dayOfMonth);
            datePicker_birth_signup.setText(birh);
        };
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.Female_box) {
            newUser.setGender("Female");

        } else if (checkedId == R.id.Male_box)
            newUser.setGender("Male");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount_signup:
                createAccount(email_signup.getText().toString(), password_signup.getText().toString());

                break;
            case R.id.arrowback_signup:
                onBackPressed();
                break;
            case R.id.date_picker_signup:
                DatePickerDialog datePickerDialogbirth = new DatePickerDialog(this, dateBirth, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogbirth.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialogbirth.show();
                break;

        }
    }

    private List<String> getColleges() {

        List<String> college = new ArrayList<>();
        college.add("Faculty");
        college.add("Computers and Information");
        college.add("Agriculture");
        college.add("Law");
        college.add("Sciences");
        college.add("Commerce");


        return college;
    }
}

