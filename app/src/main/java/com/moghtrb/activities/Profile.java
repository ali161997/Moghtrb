package com.moghtrb.activities;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.moghtrb.R;
import com.moghtrb.viewmodels.ProfileViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Profile";
    @BindView(R.id.typename_profile)
    EditText typenameProfile;
    @BindView(R.id.tvUpdateProfile)
    MaterialTextView tvUpdateProfile;
    @BindView(R.id.linearUpdate)
    LinearLayout linearUpdate;
    @BindView(R.id.type_phone_profile)
    EditText typePhoneProfile;
    @BindView(R.id.college_Spinner_profile)
    Spinner collegeSpinnerProfile;
    @BindView(R.id.city_Spinner_profile)
    Spinner citySpinnerProfile;
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
    private HashMap<String, Object> updateUser;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        InitializeVariables();
        toolbarEdit.setNavigationOnClickListener(view -> onBackPressed());
        //  try {

        updateUser = new HashMap<>();
        setUpCitySpinner();
        SetupCollegeSpinner();


        EnableEditing(false);

        setUserData();
        //calender Constraints
        initPicker();
        setPrefixPhone();
//        } catch (Exception e) {
//            Log.i(TAG, "onCreate: " + e.getMessage());
//        }


    }

    private void setUpCitySpinner() {
        profileViewModel.getCities().observe(this, list -> {
            ArrayAdapter<String> adapterCities = new ArrayAdapter<>(this, R.layout.item_spinner, list);
            citySpinnerProfile.setAdapter(adapterCities);

        });
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

    private void setPrefixPhone() {
        typePhoneProfile.setText("+2");
        Selection.setSelection(typePhoneProfile.getText(), typePhoneProfile.getText().length());


        typePhoneProfile.addTextChangedListener(new TextWatcher() {

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
                    typePhoneProfile.setText("+2");
                    Selection.setSelection(typePhoneProfile.getText(), typePhoneProfile.getText().length());

                }

            }
        });
    }

    private void InitializeVariables() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.setContext(this);
    }

    private void setUserData() {
        profileViewModel.getUserData().observe(this, user -> {
            typenameProfile.setText(user.get("name").toString());
            if (user.containsKey("phone") && user.get("phone") != null)
                typePhoneProfile.setText(user.get("phone").toString());
            if (user.containsKey("collegeIndex")
                    && user.get("collegeIndex") != null
                    && collegeSpinnerProfile.getSelectedItem().toString() != null) {
                int index = Integer.parseInt(user.get("collegeIndex").toString());
                Log.i(TAG, "setUserData: collegeIndex:" + index);
                collegeSpinnerProfile.setSelection(index);

            }
            if (user.containsKey("cityIndex")
                    && user.get("cityIndex") != null
                    && citySpinnerProfile.getSelectedItem().toString() != null) {
                int index = Integer.parseInt(user.get("cityIndex").toString());
                Log.i(TAG, "setUserData: cityINdex:" + index);
                citySpinnerProfile.setSelection(index);

            }

            if (user.get("birthDate") != null && user.containsKey("birthDate"))
                btnBirthDate.setText(user.get("birthDate").toString());
            else btnBirthDate.setText(getResources().getString(R.string.birth_date));
            if (user.containsKey("gender") && user.get("gender") != null) {
                if (user.get("gender").equals("male"))
                    MaleBox.setChecked(true);
                else if (user.get("gender").equals("female"))
                    FemaleBox.setChecked(true);
            }
            if (validateForm()) {
                tvUpdateProfile.setVisibility(View.GONE);
            } else tvUpdateProfile.setVisibility(View.VISIBLE);

        });


    }

    private void SetupCollegeSpinner() {
        profileViewModel.getColleges().observe(this, list -> {
            ArrayAdapter<String> adapterColleges = new ArrayAdapter<>(this, R.layout.item_spinner, list);
            collegeSpinnerProfile.setAdapter(adapterColleges);


        });


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
                linearUpdate.setVisibility(View.GONE);
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
                linearUpdate.setVisibility(View.VISIBLE);
                btnEnableEditing.setVisibility(View.VISIBLE);
                tvUpdateProfile.setVisibility(View.GONE);
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

        updateUser.put("collegeIndex", collegeSpinnerProfile.getSelectedItemPosition());
        updateUser.put("cityIndex", citySpinnerProfile.getSelectedItemPosition());
        updateUser.put("birthDate", btnBirthDate.getText().toString());
        updateUser.put("phone", typePhoneProfile.getText().toString().trim());
        updateUser.put("email", profileViewModel.getUserData().getValue().get("email"));
        updateUser.put("name", typenameProfile.getText().toString().trim());
        if (MaleBox.isChecked()) updateUser.put("gender", "male");
        else updateUser.put("gender", "female");
        updateUser.put("photoUrl", profileViewModel.getUserData().getValue().get("photoUrl"));
    }

    private void EnableEditing(boolean enabled) {
        fieldPhoneProfile.setEnabled(enabled);
        fieldPhoneProfile.setFocusable(enabled);
        fieldNameProfile.setEnabled(enabled);
        fieldNameProfile.setFocusable(enabled);
        collegeSpinnerProfile.setEnabled(enabled);
        citySpinnerProfile.setEnabled(enabled);
        btnBirthDate.setEnabled(enabled);
        FemaleBox.setEnabled(enabled);
        MaleBox.setEnabled(enabled);
        updateProfile.setEnabled(enabled);
    }

}
