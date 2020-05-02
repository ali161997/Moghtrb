package com.alihashem.moghtreb.activities;

import android.app.DatePickerDialog;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.models.User;
import com.alihashem.moghtreb.viewmodels.ProfileViewModel;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.typename_profile)
    EditText typenameProfile;
    @BindView(R.id.typemail_profile)
    EditText typemailProfile;
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
    ScrollView scrollViewEditing;
    @BindView(R.id.enableEditing)
    Button btnEnableEditing;
    @BindView(R.id.toolbarEdit)
    Toolbar toolbarEdit;
    private User updateUser;
    private ProfileViewModel profileViewModel;
    private DatePickerDialog.OnDateSetListener birthDate;
    private Calendar birthCalendar;
    private String dateText;
    private static final String TAG = "EditingProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        toolbarEdit.setNavigationOnClickListener(view ->
                onBackPressed());
        updateUser = new User();
        InitializeVariables();
        EnableEditing(false);
        SetupCollegeSpinner();
        setUserData();
        SetDateFromPicker();


    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == 13;
        }
        return false;
    }


    private void InitializeVariables() {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        //editingViewModel = ViewModelProviders.of(this).get(EditingViewModel.class);
        birthCalendar = Calendar.getInstance();

    }

    private void setUserData() {
        profileViewModel.getUserData().observe(this, user -> {
                    typemailProfile.setText(user.getEmail());
                    typePhoneProfile.setText(user.getPhone());
                    typenameProfile.setText(user.getName());
            try {
                collegeSpinnerProfile.setSelection(user.getCollege());
            } catch (Exception e) {
                Log.i(TAG, "setUserData: set College Spinner" + e.getMessage());
            }
            if (user.getBirthdate() != null)
                btnBirthDate.setText(user.getBirthdate());
            else
                btnBirthDate.setText(getResources().getString(R.string.birth_date));

                }
        );


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
                btnEnableEditing.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnBirthDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, birthDate, birthCalendar
                        .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                        birthCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
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
                Toast.makeText(this, "Update Completed", Toast.LENGTH_LONG).show();
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

        String email = typemailProfile.getText().toString().trim();
        String phone = typePhoneProfile.getText().toString();
        String name = typenameProfile.getText().toString().trim();
        String college = collegeSpinnerProfile.getSelectedItem().toString();
        String birthdate = btnBirthDate.getText().toString();
        if (TextUtils.isEmpty(email)) {
            typemailProfile.setError("required");
            valid = false;
        } else if (!isEmailValid(email)) {
            typemailProfile.setError("not valid format");
            valid = false;
        } else
            typemailProfile.setError(null);
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
        updateUser.setEmail(typemailProfile.getText().toString().trim());
        updateUser.setPhone(typePhoneProfile.getText().toString().trim());
        updateUser.setName(typenameProfile.getText().toString().trim());
        if (MaleBox.isChecked()) updateUser.setGender("male");
        else updateUser.setGender("female");
        updateUser.setPhotoUrl(profileViewModel.getUserData().getValue().getPhotoUrl());
    }


    private void SetDateFromPicker() {
        birthDate = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, monthOfYear);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateText = String.format("%s/%s/%s", year, monthOfYear, dayOfMonth);
            btnBirthDate.setText(dateText);
        };
    }

    private void EnableEditing(boolean enabled) {
        typemailProfile.setEnabled(enabled);
        typePhoneProfile.setEnabled(enabled);
        typenameProfile.setEnabled(enabled);
        collegeSpinnerProfile.setEnabled(enabled);
        btnBirthDate.setEnabled(enabled);
        FemaleBox.setEnabled(enabled);
        MaleBox.setEnabled(enabled);
        updateProfile.setEnabled(enabled);

    }

}
