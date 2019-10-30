package com.example.sokna.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.sokna.R;
import com.example.sokna.models.user;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditingProfile extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.Profile_tv)
    TextView ProfileTv;
    @BindView(R.id.arrowBack_profile)
    ImageView arrowBackProfile;
    @BindView(R.id.typename_profile)
    EditText typenameProfile;
    @BindView(R.id.typemail_profile)
    EditText typemailProfile;
    @BindView(R.id.type_phone_profile)
    EditText typePhoneProfile;
    @BindView(R.id.type_password_profile)
    EditText typePasswordProfile;
    @BindView(R.id.college_Spinner_profile)
    Spinner collegeSpinnerProfile;
    @BindView(R.id.Male_box)
    AppCompatRadioButton MaleBox;
    @BindView(R.id.Female_box)
    AppCompatRadioButton FemaleBox;
    @BindView(R.id.radio_gender_profile)
    RadioGroup radioGenderProfile;
    @BindView(R.id.birh_tv_profile)
    TextView birhTvProfile;
    @BindView(R.id.date_picker_profile)
    DatePicker datePickerProfile;
    @BindView(R.id.update_profile)
    Button updateProfile;
    @BindView(R.id.progressBar_profile)
    ProgressBar progressBarProfile;
    private user updateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_profile);
        ButterKnife.bind(this);
        updateUser = new user();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.update_profile, R.id.arrowBack_profile})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile:
                break;
            case R.id.arrowBack_profile:
                onBackPressed();
                break;

        }
    }

    private void UpdateProfile() {

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
        String birthdate = getDate();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)) {
            if (TextUtils.isEmpty(email))
                typemailProfile.setError("Required.");
            if (TextUtils.isEmpty(phone))
                typePhoneProfile.setError("Required");
            if (TextUtils.isEmpty(name))
                typenameProfile.setError("Required");

            valid = false;
        } else {
            typemailProfile.setError(null);
            typenameProfile.setError(null);
            typePhoneProfile.setError(null);
        }

        String password = typePasswordProfile.getText().toString();
        if (TextUtils.isEmpty(password)) {
            typePasswordProfile.setError("Required.");
            valid = false;
        } else {
            typePasswordProfile.setError(null);
        }

        return valid;
    }

    private void getDataForm() {


        updateUser.setCollege(collegeSpinnerProfile.getSelectedItem().toString().trim());
        updateUser.setBirthdate(getDate().trim());
        updateUser.setEmail(typemailProfile.getText().toString().trim());
        updateUser.setPhone(typePhoneProfile.getText().toString().trim());
        updateUser.setName(typenameProfile.getText().toString().trim());
    }

    private String getDate() {
        return datePickerProfile.getYear() + "/"
                + datePickerProfile.getMonth() + "/" + datePickerProfile.getDayOfMonth();
    }

}
