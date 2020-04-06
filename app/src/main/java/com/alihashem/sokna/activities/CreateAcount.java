package com.alihashem.sokna.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.alihashem.sokna.R;
import com.alihashem.sokna.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAcount extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @BindView(R.id.toolbarCreate)
    androidx.appcompat.widget.Toolbar toolbarCreate;
    private List<String> group;
    private Button create_account;
    private String TAG = "create_acount";
    private EditText name_signup;
    private EditText email_signup;
    private EditText phone_signup;
    private EditText password_signup;
    private Spinner spinner_college_signup;
    private Button datePicker_birth_signup;
    private ProgressBar prgbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private user newUser = null;
    private RadioGroup getGender;
    private DatePickerDialog.OnDateSetListener dateBirth;
    private Calendar birthCalendar;
    private MutableLiveData<List<String>> colleges;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacount);
        ButterKnife.bind(this);
        group = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        colleges = new MutableLiveData<>();
        getFaculties();
        name_signup = findViewById(R.id.typename_signup);
        email_signup = findViewById(R.id.typemail_signup);
        phone_signup = findViewById(R.id.type_phone_signup);
        password_signup = findViewById(R.id.type_password_signup);
        create_account = findViewById(R.id.createAccount_signup);
        getGender = findViewById(R.id.radio_gender_signup);
        prgbar = findViewById(R.id.progressBar_signup);
        prgbar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
        spinner_college_signup = findViewById(R.id.college_Spinner_signup);
        datePicker_birth_signup = findViewById(R.id.date_picker_signup);

        toolbarCreate.setNavigationOnClickListener(view ->
                onBackPressed());
        mAuth = FirebaseAuth.getInstance();
        newUser = new user();
        getGender.setOnCheckedChangeListener(this);
        create_account.setOnClickListener(this);
        datePicker_birth_signup.setOnClickListener(this);
        birthCalendar = Calendar.getInstance();
        SetupCollegeSpinner();
        setPrefixPhone();

        getDate();
    }


    private void SetupCollegeSpinner() {
        colleges.observe(this, list -> {
            ArrayAdapter<String> adapterColleges = new ArrayAdapter<>(this, R.layout.spinner_where_item, list);
            spinner_college_signup.setAdapter(adapterColleges);
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            Toast.makeText(CreateAcount.this, "in validating", Toast.LENGTH_SHORT).show();
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
                                .addOnSuccessListener(documentReference ->
                                        Toast.makeText(CreateAcount.this, "success added", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(CreateAcount.this, "fail added", Toast.LENGTH_SHORT).show());
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CreateAcount.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
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

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private boolean validateForm() {
        boolean valid = true;
        String email = email_signup.getText().toString().trim();
        String phone = phone_signup.getText().toString();
        String name = name_signup.getText().toString().trim();
        try {
            String college = spinner_college_signup.getSelectedItem().toString();
        } catch (Exception e) {
            Log.i(TAG, "validateForm: " + e.getCause());
            name = "Any";
        }


        String birthdate = datePicker_birth_signup.getText().toString();
        String password = password_signup.getText().toString();

        if (TextUtils.isEmpty(email)) {
            email_signup.setError("required");
            valid = false;
        } else if (!isEmailValid(email)) {
            email_signup.setError("not valid format");
            valid = false;
        } else
            email_signup.setError(null);
        if (TextUtils.isEmpty(phone)) {
            phone_signup.setError("Required");
            valid = false;
        } else if (!isValidMobile(phone)) {
            phone_signup.setError("not valid format");
            valid = false;
        } else
            phone_signup.setError(null);

        if (TextUtils.isEmpty(name)) {
            name_signup.setError("Required");
            valid = false;
        }
        if (TextUtils.isEmpty(birthdate)) {
            datePicker_birth_signup.setError("Required");
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            password_signup.setError("Required");
            valid = false;
        } else if (password.length() < 8) {
            password_signup.setError("should more than 8 character");
            valid = false;
        } else if (!isValidPassword(password)) {
            password_signup.setError("at least 1 alphabet,1 Number and 1 Special Character,");
            valid = false;
        } else
            password_signup.setError(null);

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), Phone.class);
            intent.putExtra("phone", phone_signup.getText().toString());
            startActivity(intent);
            finish();


        } else {
            Toast.makeText(CreateAcount.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
        newUser.setCollege(spinner_college_signup.getSelectedItemPosition());
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
            String birh = String.format("%s/%s/%s", year, monthOfYear + 1, dayOfMonth);
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
            case R.id.date_picker_signup:
                DatePickerDialog datePickerDialogbirth = new DatePickerDialog(this, dateBirth, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogbirth.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialogbirth.show();
                break;

        }
    }

    private void getFaculties() {
        String docName;
        if (getResources().getConfiguration().locale.getLanguage().equals("ar"))
            docName = "ar-Faculties";
        else
            docName = "en-Faclties";
        Log.i(TAG, "setFaculties: " + docName);


        db.collection("Faculties").document(docName)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    group = (List<String>) document.get("faculties");
                    Log.d(TAG, "succes document");

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
            colleges.setValue(group);
        });


    }
}

