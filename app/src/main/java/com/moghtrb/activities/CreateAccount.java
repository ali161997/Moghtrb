package com.moghtrb.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.R;
import com.moghtrb.repositories.explore_Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @BindView(R.id.toolbarCreate)
    androidx.appcompat.widget.Toolbar toolbarCreate;
    @BindView(R.id.cardVerifySheet)
    MaterialCardView verifyBottomSheet;
    private MaterialDatePicker materialDatePicker;
    private Button btnCreatAccount;
    private String TAG = "create_acount";
    private EditText editNameSignUp;
    private EditText editMailSignUp;
    private EditText phone_signup;
    private EditText password_signup;
    private Spinner spinner_college_signup;
    private Button btnBirthDate;
    private ProgressBar prgbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private HashMap<String, Object> newUser = null;
    private RadioGroup getGender;
    private BottomSheetBehavior bottomSheetVerify;
    private ProgressBar progressBarSheetVerify;
    private MutableLiveData<Boolean> isEmailExist = new MutableLiveData<>();
    private Spinner selectCitySpinner;
    //--------------------------------------------
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private MutableLiveData<Boolean> phoneVerified = new MutableLiveData<>();
    private explore_Repository repository;


    private void initListeners() {
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
        btnCreatAccount.setOnClickListener(this);
        btnBirthDate.setOnClickListener(this);
        getGender.setOnCheckedChangeListener(this);
    }

    private void initPicker() {
        long toDay = MaterialDatePicker.todayInUtcMilliseconds();
        CalendarConstraints.Builder constraintBulder = new CalendarConstraints.Builder();
        constraintBulder.setEnd(toDay);
        constraintBulder.setStart(getMilli());

        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        datePickerBuilder.setSelection(toDay);
        datePickerBuilder.setCalendarConstraints(constraintBulder.build());
        datePickerBuilder.setTitleText("Select Birth Date");
        materialDatePicker = datePickerBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            btnBirthDate.setText(materialDatePicker.getHeaderText());

        });
    }

    private long getMilli() {
        String myDate = "1970/01/01 00:00:00";
        long millis = 0;
        if (Build.VERSION.SDK_INT <= 26) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(myDate);
                millis = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            LocalDateTime localDateTime = LocalDateTime.parse(myDate, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
            millis = localDateTime.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();
        }
        return millis;


    }

    private void initFindViews() {
        repository = explore_Repository.getInstance();
        progressBarSheetVerify = findViewById(R.id.progressSheetVerify);
        editNameSignUp = findViewById(R.id.typename_signup);
        editMailSignUp = findViewById(R.id.typemail_signup);
        phone_signup = findViewById(R.id.type_phone_signup);
        password_signup = findViewById(R.id.type_password_signup);
        btnCreatAccount = findViewById(R.id.createAccount_signup);
        getGender = findViewById(R.id.radio_gender_signup);
        prgbar = findViewById(R.id.progressBar_signup);
        mVerificationField = findViewById(R.id.fieldVerificationCode);
        mStartButton = findViewById(R.id.buttonStartVerification);
        mVerifyButton = findViewById(R.id.buttonVerifyPhone);
        mResendButton = findViewById(R.id.buttonResend);
        spinner_college_signup = findViewById(R.id.college_Spinner_signup);
        btnBirthDate = findViewById(R.id.date_picker_signup);
        mPhoneNumberField = findViewById(R.id.fieldPhoneNumber);
        selectCitySpinner = findViewById(R.id.city_Spinner_signup);

        //completed

    }

    private void setUpVerifySheet() {
        bottomSheetVerify = BottomSheetBehavior.from(verifyBottomSheet);
        bottomSheetVerify.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetVerify.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {


                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        newUser = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        initFindViews();
        getFaculties();
        getCities();
        initListeners();
        setPrefixPhone();
        setUpVerifySheet();
        prgbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
        toolbarCreate.setNavigationOnClickListener(view -> onBackPressed());
        initPicker();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                phoneVerified.setValue(true);
                progressBarSheetVerify.setVisibility(View.GONE);


                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                // [END_EXCLUDE]
                Log.i(TAG, "onVerificationCompleted: instant verification");

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                Log.i(TAG, "onVerificationCompleted: sTATE_VERIFY_SUCCESS");
                // [END_EXCLUDE]
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                phoneVerified.setValue(false);
                progressBarSheetVerify.setVisibility(View.GONE);
                // [START_EXCLUDE silent]
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), R.string.tooManyRequests,
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                Log.i(TAG, "onVerificationFailed: STATE_VERIFY_FAILED");
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_LONG).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                Log.i(TAG, "onCodeSent: STATE_CODE_SENT");
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks
        );        // OnVerificationStateChangedCallbacks
        progressBarSheetVerify.setVisibility(View.VISIBLE);
        // [END start_phone_auth]

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        phoneVerified.setValue(true);
                        // [END_EXCLUDE]
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            // [START_EXCLUDE silent]
                            mVerificationField.setError("Invalid code.");
                            phoneVerified.setValue(false);
                            // [END_EXCLUDE]
                        }
                        // [START_EXCLUDE silent]
                        // Update UI
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetVerify.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetVerify.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Toast.makeText(CreateAccount.this, "in validating", Toast.LENGTH_SHORT).show();
            return;
        }
        // start here
        isEmailExist.observe(this, b -> {
            if (b) {
                editMailSignUp.setError(getString(R.string.mailExistance));
            } else {
                mPhoneNumberField.setText(phone_signup.getText().toString());
                bottomSheetVerify.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        phoneVerified.observe(this, verified ->
        {
            bottomSheetVerify.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (verified) {
                Snackbar.make(findViewById(android.R.id.content), "Verification Succeeded",
                        Snackbar.LENGTH_SHORT).show();
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
                                        .addOnSuccessListener(documentReference ->
                                                Toast.makeText(CreateAccount.this, R.string.status_verification_succeeded, Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(CreateAccount.this, "failure", Toast.LENGTH_SHORT).show());
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        });
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Verification Failed",
                        Snackbar.LENGTH_SHORT).show();
            }


        });

        // [END create_user_with_email]
    }

    private void setPrefixPhone() {
        phone_signup.setText("+2");
        Selection.setSelection(phone_signup.getText(), phone_signup.getText().length());


        phone_signup.addTextChangedListener(new TextWatcher() {

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
                    phone_signup.setText("+2");
                    Selection.setSelection(phone_signup.getText(), phone_signup.getText().length());

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

    private void isEmailExist(String email) {
        if (email != null && !email.equals(""))
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                            if (isNewUser) {
                                isEmailExist.setValue(false);
                            } else {
                                Toast.makeText(this, R.string.mailExistance, Toast.LENGTH_SHORT).show();
                                isEmailExist.setValue(true);
                            }
                        }
                    });

    }

    private boolean isEmailValidFormat(String email) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#_;$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private boolean validateForm() {
        AtomicBoolean valid = new AtomicBoolean(true);
        String email = editMailSignUp.getText().toString().trim();
        String phone = phone_signup.getText().toString();
        String name = editNameSignUp.getText().toString().trim();
        try {
            String college = spinner_college_signup.getSelectedItem().toString();
        } catch (Exception e) {
            Log.i(TAG, "validateForm: " + e.getCause());
            name = "Any";
        }


        String birthdate = btnBirthDate.getText().toString();
        String password = password_signup.getText().toString();
        isEmailExist(email);

        if (TextUtils.isEmpty(phone)) {
            phone_signup.setError(getString(R.string.required));
            valid.set(false);
        } else if (!isValidMobile(phone)) {
            phone_signup.setError(getString(R.string.noValidFormat));
            valid.set(false);
        } else
            phone_signup.setError(null);

        if (TextUtils.isEmpty(name)) {
            editNameSignUp.setError(getString(R.string.required));
            valid.set(false);
        }
        if (TextUtils.isEmpty(birthdate)) {
            btnBirthDate.setError(getString(R.string.required));
            valid.set(false);
        }
        if (TextUtils.isEmpty(password)) {
            password_signup.setError(getString(R.string.required));
            valid.set(false);
        } else if (password.length() < 8) {
            password_signup.setError(getString(R.string.more8Character));
            valid.set(false);
        } else if (!isValidPassword(password)) {
            password_signup.setError(getString(R.string.validFormat));
            valid.set(false);
        } else
            password_signup.setError(null);
        if (TextUtils.isEmpty(email)) {
            editMailSignUp.setError(getString(R.string.required));
            valid.set(false);
        } else if (!isEmailValidFormat(email)) {
            editMailSignUp.setError(getString(R.string.noValidFormat));
            valid.set(false);
        } else editMailSignUp.setError(null);

        return valid.get();
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            // on completed
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();


        } else {
            Toast.makeText(CreateAccount.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

    private void getDataForm() {
        newUser.put("completed", true);
        newUser.put("cityIndex", selectCitySpinner.getSelectedItemPosition());
        newUser.put("collegeIndex", spinner_college_signup.getSelectedItemPosition());
        newUser.put("birthDate", btnBirthDate.getText().toString());
        newUser.put("email", editMailSignUp.getText().toString().trim());
        newUser.put("phone", phone_signup.getText().toString().trim());
        newUser.put("name", editNameSignUp.getText().toString().trim());
        newUser.put("photoUrl", "");

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.Female_box) {
            newUser.put("gender", "female");

        } else if (checkedId == R.id.Male_box)
            newUser.put("gender", "male");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount_signup:
                if (InternetNotAvailable())
                    Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
                else
                    createAccount(editMailSignUp.getText().toString(), password_signup.getText().toString());

                break;
            case R.id.date_picker_signup:
                materialDatePicker.show(getSupportFragmentManager(), TAG);

                break;
            case R.id.buttonStartVerification:
                if (!validatePhoneNumber()) {
                    return;
                }

                startPhoneNumberVerification(mPhoneNumberField.getText().toString().trim());
                break;
            case R.id.buttonVerifyPhone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.buttonResend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;

        }
    }

    private void getFaculties() {
        repository.getAllCities().observe(this, list -> {
            ArrayAdapter<String> adapterCities = new ArrayAdapter<>(this, R.layout.item_spinner, list);
            selectCitySpinner.setAdapter(adapterCities);
        });

    }

    private void getCities() {
        repository.getFaculties().observe(this, list -> {
            ArrayAdapter<String> adapterColleges = new ArrayAdapter<>(this, R.layout.item_spinner, list);
            spinner_college_signup.setAdapter(adapterColleges);
        });

    }

    private boolean InternetNotAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }
}

