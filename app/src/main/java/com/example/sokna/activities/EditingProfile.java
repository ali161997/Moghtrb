package com.example.sokna.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.lifecycle.ViewModelProviders;

import com.example.sokna.R;
import com.example.sokna.models.user;
import com.example.sokna.viewmodels.Editing_view_model;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditingProfile extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.Profile_tv)
    TextView ProfileTv;
    @BindView(R.id.arrowBack_profile)
    Button arrowBackProfile;
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
    private user updateUser;
    private Editing_view_model editingViewModel;
    private DatePickerDialog.OnDateSetListener birthDate;
    private Calendar birthCalendar;
    private String dateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_profile);
        ButterKnife.bind(this);
        updateUser = new user();

        InitializeVariables();
        EnableEditing(false);
        SetupCollegeSpinner();
        setUserData();
        SetDateFromPicker();


    }

    private void InitializeVariables() {
        editingViewModel = ViewModelProviders.of(this).get(Editing_view_model.class);
        birthCalendar = Calendar.getInstance();

    }

    private void setUserData() {
        editingViewModel.getUserData().observe(this, user -> {
                    typemailProfile.setText(user.getEmail());
                    typePhoneProfile.setText(user.getPhone());
                    typenameProfile.setText(user.getName());
            collegeSpinnerProfile.setSelection(editingViewModel.getColleges().getValue().indexOf(user.getCollege()));
                    btnBirthDate.setText(user.getBirthdate());
                }
        );


    }

    private void SetupCollegeSpinner() {
        ArrayAdapter<String> adapterColleges = new ArrayAdapter<>(this, R.layout.spinner_where_item, editingViewModel.getColleges().getValue());
        collegeSpinnerProfile.setAdapter(adapterColleges);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.update_profile, R.id.arrowBack_profile, R.id.enableEditing, R.id.btnBirthDate})
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
            case R.id.arrowBack_profile:
                onBackPressed();
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
        editingViewModel.setUserData(updateUser);
        editingViewModel.getUpdateCompleted().observe(this, boo -> {
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

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) || TextUtils.isEmpty(birthdate)) {
            if (TextUtils.isEmpty(email))
                typemailProfile.setError("Required.");
            if (TextUtils.isEmpty(phone))
                typePhoneProfile.setError("Required");
            if (TextUtils.isEmpty(name))
                typenameProfile.setError("Required");
            if (TextUtils.isEmpty(birthdate))
                btnBirthDate.setError("required");

            valid = false;
        } else {
            typemailProfile.setError(null);
            typenameProfile.setError(null);
            typePhoneProfile.setError(null);
            btnBirthDate.setError(null);
        }

        return valid;
    }

    private void getDataForm() {


        updateUser.setCollege(collegeSpinnerProfile.getSelectedItem().toString().trim());
        updateUser.setBirthdate(btnBirthDate.getText().toString());
        updateUser.setEmail(typemailProfile.getText().toString().trim());
        updateUser.setPhone(typePhoneProfile.getText().toString().trim());
        updateUser.setName(typenameProfile.getText().toString().trim());
        if (MaleBox.isChecked()) updateUser.setGender("Male");
        else updateUser.setGender("Female");
        updateUser.setPhotoUrl(editingViewModel.getUserData().getValue().getPhotoUrl());
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
