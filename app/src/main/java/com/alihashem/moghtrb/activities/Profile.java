package com.alihashem.moghtrb.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.models.User;
import com.alihashem.moghtrb.viewmodels.ProfileViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Profile";
    @BindView(R.id.typename_profile)
    EditText typenameProfile;
    @BindView(R.id.type_phone_profile)
    EditText typePhoneProfile;
    @BindView(R.id.college_Spinner_profile)
    Spinner collegeSpinnerProfile;
    @BindView(R.id.Male_box)
    AppCompatRadioButton MaleBox;
    @BindView(R.id.Female_box)
    AppCompatRadioButton FemaleBox;
    @BindView(R.id.radio_gender_profile)
    RadioGroup radioGenderProfile;
    @BindView(R.id.update_profile)
    Button updateProfile;
    @BindView(R.id.progressBar_profile)
    ProgressBar progressBarProfile;
    @BindView(R.id.btnBirthDate)
    Button btnBirthDate;
    @BindView(R.id.scrollEditingProfile)
    NestedScrollView scrollViewEditing;
    @BindView(R.id.enableEditing)
    Button btnEnableEditing;
    @BindView(R.id.toolbarEdit)
    Toolbar toolbarEdit;
    @BindView(R.id.appBarEdit)
    AppBarLayout appBarEdit;
    @BindView(R.id.fieldNameProfile)
    TextInputLayout fieldNameProfile;
    @BindView(R.id.fieldPhoneProfile)
    TextInputLayout fieldPhoneProfile;
    private MaterialDatePicker materialDatePicker;
    private User updateUser;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        toolbarEdit.setNavigationOnClickListener(view -> onBackPressed());
        try {
            updateUser = new User();
            InitializeVariables();
            EnableEditing(false);
            SetupCollegeSpinner();
            setUserData();
            //calender Constraints
            initPicker();
        } catch (Exception e) {
            Log.i(TAG, "onCreate: " + e.getMessage());
        }


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

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 13;
        }
        return false;
    }


    private void InitializeVariables() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

    }

    private void setUserData() {
        profileViewModel.getUserData().observe(this, user -> {
            try {
                typePhoneProfile.setText(user.getPhone());
                typenameProfile.setText(user.getName());
                collegeSpinnerProfile.setSelection(user.getCollege());
                if (user.getBirthdate() != null) btnBirthDate.setText(user.getBirthdate());
                else btnBirthDate.setText(getResources().getString(R.string.birth_date));
                if (user.getGender() != null) {
                    if (user.getGender().equals("male"))
                        MaleBox.setChecked(true);
                    else if (user.getGender().equals("female"))
                        FemaleBox.setChecked(true);
                }
            } catch (Exception e) {
                Log.i(TAG, "setUserData: " + e.getMessage());
            }


        });


    }

    private void SetupCollegeSpinner() {

        ArrayAdapter<String> adapterColleges = new ArrayAdapter<>(this, R.layout.item_spinner, profileViewModel.getColleges().getValue());
        collegeSpinnerProfile.setAdapter(adapterColleges);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.update_profile, R.id.enableEditing, R.id.btnBirthDate})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile:
                if (validateForm()) {
                    UpdateProfile();
                } else {
                    Toast.makeText(this, "You Need to Complete Form", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.enableEditing:
                EnableEditing(true);
                btnEnableEditing.setVisibility(View.GONE);
                break;
            case R.id.btnBirthDate:
                materialDatePicker.show(getSupportFragmentManager(), TAG);

                break;


        }
    }

    private void UpdateProfile() {
        showProgressDialog();
        getDataForm();
        profileViewModel.setUserData(updateUser);
        profileViewModel.getUpdateCompleted().observe(this, boo -> {
            if (boo) {
                Toast.makeText(this, "Update Completed", Toast.LENGTH_LONG).show();
                hideProgressDialog();
                EnableEditing(false);
                btnEnableEditing.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "error occurred", Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });

    }

    private void showProgressDialog() {
        progressBarProfile.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressDialog() {
        progressBarProfile.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private boolean validateForm() {
        boolean valid = true;

        String phone = typePhoneProfile.getText().toString();
        String name = typenameProfile.getText().toString().trim();
        String college = collegeSpinnerProfile.getSelectedItem().toString();
        String birthdate = btnBirthDate.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            typePhoneProfile.setError("Required");
            valid = false;
        } else if (!isValidMobile(phone)) {
            typePhoneProfile.setError("not valid format");
            valid = false;
        } else
            typePhoneProfile.setError(null);

        if (TextUtils.isEmpty(name)) {
            typenameProfile.setError("Required");
            valid = false;
        }
        if (TextUtils.isEmpty(birthdate)) {
            btnBirthDate.setError("Required");
            valid = false;
        }


        return valid;
    }

    private void getDataForm() {
        updateUser.setCollege(collegeSpinnerProfile.getSelectedItemPosition());
        updateUser.setBirthdate(btnBirthDate.getText().toString());
        updateUser.setPhone(typePhoneProfile.getText().toString().trim());
        updateUser.setName(typenameProfile.getText().toString().trim());
        if (MaleBox.isChecked()) updateUser.setGender("male");
        else updateUser.setGender("female");
        updateUser.setPhotoUrl(profileViewModel.getUserData().getValue().getPhotoUrl());
    }

    private void EnableEditing(boolean enabled) {
        fieldPhoneProfile.setEnabled(enabled);
        fieldPhoneProfile.setFocusable(enabled);
        fieldNameProfile.setEnabled(enabled);
        fieldNameProfile.setFocusable(enabled);
        collegeSpinnerProfile.setEnabled(enabled);
        btnBirthDate.setEnabled(enabled);
        FemaleBox.setEnabled(enabled);
        MaleBox.setEnabled(enabled);
        updateProfile.setEnabled(enabled);
    }

}
