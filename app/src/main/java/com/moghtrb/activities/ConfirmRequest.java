package com.moghtrb.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.moghtrb.R;
import com.moghtrb.models.StudentTime;
import com.moghtrb.viewmodels.ConfirmRequestViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ir.alirezabdn.wp7progress.WP10ProgressBar;

public class ConfirmRequest extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener
        , CheckBox.OnCheckedChangeListener {

    private static final String TAG = "ConfirmRequest";
    private static StudentTime studentTime = new StudentTime();
    @BindView(R.id.toolBarConfirm)
    Toolbar toolbarConfirm;
    @BindView(R.id.appBarConfirm)
    AppBarLayout appBarConfirm;
    @BindView(R.id.btnSetDate)
    MaterialButton btnSetDate;
    @BindView(R.id.tvDetails)
    MaterialTextView tvDetails;
    @BindView(R.id.costMonthTv)
    TextView costMonthTv;
    @BindView(R.id.valueMonth)
    TextView valueMonth;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    @BindView(R.id.costAdvanceTv)
    TextView costAdvanceTv;
    @BindView(R.id.valuePreMonth)
    TextView valuePreMonth;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.servicesTv)
    TextView servicesTv;
    @BindView(R.id.valueServices)
    TextView valueServices;
    @BindView(R.id.linear3)
    LinearLayout linear3;
    @BindView(R.id.commissionTv)
    TextView commissionTv;
    @BindView(R.id.valueCommission)
    TextView valueCommission;
    @BindView(R.id.linear9)
    LinearLayout linear9;
    @BindView(R.id.linearDetail)
    LinearLayout linearDetail;
    @BindView(R.id.netTv)
    TextView netTv;
    @BindView(R.id.valueNet)
    TextView valueNet;
    @BindView(R.id.linear5)
    LinearLayout linear5;
    @BindView(R.id.MinTv)
    TextView MinTv;
    @BindView(R.id.valueMin)
    TextView valueMin;
    @BindView(R.id.btnConfirm)
    MaterialButton btnConfirm;
    @BindView(R.id.typPromoCode)
    TextInputEditText typPromoCode;
    @BindView(R.id.fieldPromoCode)
    TextInputLayout fieldPromoCode;
    @BindView(R.id.CloseTimeSheet)
    MaterialButton CloseTimeSheet;
    @BindView(R.id.btn_save_anytime)
    MaterialButton btnSaveAnytime;
    @BindView(R.id.linearTime)
    LinearLayout linearTime;
    @BindView(R.id.spinnerAnyTime)
    Spinner spinner_when;
    @BindView(R.id.cardSpin)
    MaterialCardView cardSpin;
    @BindView(R.id.selectDates)
    MaterialButton selectDates;
    @BindView(R.id.checkInValue)
    MaterialTextView checkInValue;
    @BindView(R.id.checkOutValue)
    MaterialTextView checkOutValue;
    @BindView(R.id.semester1)
    MaterialRadioButton semester1;
    @BindView(R.id.semester2)
    MaterialRadioButton semester2;
    @BindView(R.id.semester3)
    MaterialRadioButton semester3;
    @BindView(R.id.semester12)
    MaterialRadioButton semester12;
    @BindView(R.id.radioTime)
    RadioGroup radioTimeSheet;
    @BindView(R.id.anytime_bottom_sheet)
    RelativeLayout anytimeBottomSheet;
    @BindView(R.id.coordinator_confirm)
    CoordinatorLayout coordinatorConfirm;
    @BindView(R.id.pieProgress)
    WP10ProgressBar pieProgress;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    MaterialDatePicker.Builder datePickerBuilder;
    Calendar myCalender;
    private BottomSheetBehavior whenSheet;
    private ConfirmRequestViewModel viewModel;
    private View studentView;
    private View foreignerView;
    private boolean extend = false;
    private double commission;
    private double min;

    @Override
    public void onBackPressed() {

        if (whenSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_request);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(ConfirmRequestViewModel.class);
        viewModel.setRoomId(getIntent().getStringExtra("roomId"));
        viewModel.setDate(getIntent().getStringExtra("dates"));
        viewModel.setNumGuests(getIntent().getIntExtra("numGuests", 1));
        viewModel.setType(getIntent().getStringExtra("type"));
        viewModel.setSelectedFloor(getIntent().getStringExtra("roomOrder"));
        if (getIntent().getStringExtra("type").equalsIgnoreCase("Foreigner"))
            viewModel.setNumDays(new MutableLiveData<>(getIntent().getIntExtra("days", 1)));

        studentView = findViewById(R.id.student_view);
        foreignerView = findViewById(R.id.foreigner_view);
        spinner_when.setOnItemSelectedListener(this);
        initPicker();
        setUpBottomSheet();
        toolbarConfirm.setNavigationOnClickListener(v -> onBackPressed());
        viewModel.getPayments().observe(this, model -> {
            if (viewModel.getType().getValue().equalsIgnoreCase("foreigner")) {
                valueMonth.setText(Double.toString(model.getDayCost()));
                costMonthTv.setText(getString(R.string.cost_per_day));
                valueNet.setText(Double.toString(model.getDayCost() * viewModel.getNumDays().getValue() * viewModel.getNumGuests().getValue()));
                valueMin.setText(Double.toString((model.getDayCost() * viewModel.getNumDays().getValue() * viewModel.getNumGuests().getValue()) / 2.0));
                valueCommission.setText(Double.toString(model.getCommissionDays() * viewModel.getNumGuests().getValue()));


            } else {
                valueMonth.setText(Double.toString(model.getMonthPrice()));
                valuePreMonth.setText(Double.toString(model.getAdvance()));
                valueServices.setText(Double.toString(model.getServices()));
                double total = viewModel.getNumGuests().getValue() * (model.getMonthPrice() + model.getAdvance() + model.getServices());
                valueNet.setText(Double.toString(total));
                commission = model.getCommission() * viewModel.getNumGuests().getValue();
                valueCommission.setText(Double.toString(commission));
                viewModel.setCommission(new MutableLiveData<>(commission));
                min = (total + commission) / 2.0;
                valueMin.setText(Double.toString(min));
                viewModel.setMin(new MutableLiveData<>(min));
            }


        });


    }

    private void dateConstraints() {
        if (spinner_when.getSelectedItemPosition() == 0) {
            switch (radioTimeSheet.getCheckedRadioButtonId()) {
                case R.id.semester1:
                    studentTime.setSemester1(true);
                    studentTime.setSemester2(false);
                    studentTime.setSemester3(false);
                    studentTime.setSemester12(false);
                    break;
                case R.id.semester2:
                    studentTime.setSemester1(false);
                    studentTime.setSemester2(true);
                    studentTime.setSemester3(false);
                    studentTime.setSemester12(false);
                    break;
                case R.id.semester3:
                    studentTime.setSemester3(true);
                    studentTime.setSemester1(false);
                    studentTime.setSemester2(false);
                    studentTime.setSemester12(false);
                    break;
                case R.id.semester12:
                    studentTime.setSemester12(true);
                    studentTime.setSemester1(false);
                    studentTime.setSemester2(false);
                    studentTime.setSemester3(false);
            }
            if (studentTime.toString().equals(""))
                Toast.makeText(this, "you need to check dates", Toast.LENGTH_LONG).show();
            else {
                whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModel.setDate(studentTime.toString());
            }
        } else {
            if (checkInValue.getText().toString().equals("") || checkOutValue.getText().toString().equals(""))
                Toast.makeText(this, "you need to Complete dates", Toast.LENGTH_LONG).show();
            else {
                viewModel.setDate(checkInValue.getText().toString() + "-" + checkOutValue.getText().toString());
                whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

        }
    }

    private void setUpBottomSheet() {
        whenSheet = BottomSheetBehavior.from(anytimeBottomSheet);
        whenSheet.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    whenSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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

    private void showMessage() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Notice))
                .setMessage(getString(R.string.Wait_our_confirmation))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                }).show();
    }

    @OnClick({R.id.btnVerify, R.id.btnConfirm, R.id.btnSetDate, R.id.CloseTimeSheet, R.id.btn_save_anytime, R.id.selectDates, R.id.tvDetails})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVerify:
                if (!typPromoCode.getText().toString().equals("")) {
                    try {
                        viewModel.verifyCode(typPromoCode.getText().toString());
                        viewModel.getCodeVerified().observe(this, d -> {
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
                        viewModel.getValueCode().observe(this, num -> {
                                    double c = commission - num;
                                    valueCommission.setText(Double.toString(c));
                                    valueMin.setText(Double.toString(min - num));
                                    viewModel.setCommission(new MutableLiveData<>(commission));
                                    viewModel.setMin(new MutableLiveData<>(min - num));

                                }
                        );
                    } catch (Exception e) {
                        Log.i(TAG, "onClick: " + e.getMessage());
                    }
                }
                break;
            case R.id.tvDetails:
                extend(!extend);
                extend = !extend;

                break;
            case R.id.CloseTimeSheet:
                whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btnConfirm:
                if (viewModel.getDate().getValue() == null) {
                    Snackbar mySnackbar = Snackbar.make(coordinatorConfirm,
                            getResources().getString(R.string.checkDates), Snackbar.LENGTH_SHORT);
                    mySnackbar.show();

                } else {
                    pieProgress.setVisibility(View.VISIBLE);
                    pieProgress.showProgressBar();
                    if (spinner_when.getSelectedItemPosition() == 0)
                        viewModel.setType("student");
                    else viewModel.setType("foreigner");
                    Toast.makeText(this, "complted request", Toast.LENGTH_SHORT).show();
                    viewModel.sendRequest(FirebaseAuth.getInstance().getUid()).observe(this, bool ->
                    {
                        pieProgress.hideProgressBar();
                        pieProgress.setVisibility(View.GONE);
                        if (bool) {
                            showMessage();
                            btnConfirm.setIcon(getDrawable(R.drawable.ic_checked_black_24dp));
                            btnConfirm.setEnabled(false);
                            btnConfirm.setText(R.string.requestSubmitted);

                        } else {
                            btnConfirm.setIcon(null);
                            btnConfirm.setEnabled(true);
                            btnConfirm.setText("Resend");
                        }

                    });
                }
                break;
            case R.id.btnSetDate:
                whenSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btn_save_anytime:
                dateConstraints();
                break;
            case R.id.selectDates:
                materialDatePicker.show(getSupportFragmentManager(), TAG);
                break;
        }
    }

    private void initPicker() {
        myCalender = Calendar.getInstance(TimeZone.getDefault());
        myCalender.clear();
        long toDay = MaterialDatePicker.todayInUtcMilliseconds();
        myCalender.setTimeInMillis(toDay);
        myCalender.set(Calendar.MONTH, Calendar.JANUARY);
        long january = myCalender.getTimeInMillis();
        myCalender.set(Calendar.MONTH, Calendar.DECEMBER);
        long december = myCalender.getTimeInMillis();
        CalendarConstraints.Builder constraintBulder = new CalendarConstraints.Builder();
        constraintBulder.setStart(january);
        constraintBulder.setEnd(december);
        constraintBulder.setValidator(DateValidatorPointForward.now());


//-------
        datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        datePickerBuilder.setSelection(new Pair<>(toDay, toDay));
        datePickerBuilder.setCalendarConstraints(constraintBulder.build());
        datePickerBuilder.setTitleText("select Dates");
        materialDatePicker = datePickerBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            checkInValue.setText(getMilli(materialDatePicker.getSelection().first));
            checkOutValue.setText(getMilli(materialDatePicker.getSelection().second));

        });

    }

    private String getMilli(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(date));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerAnyTime) {
            if (position == 0) {
                foreignerView.setVisibility(View.INVISIBLE);
                studentView.setVisibility(View.VISIBLE);
            } else {
                foreignerView.setVisibility(View.VISIBLE);
                studentView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnCheckedChanged({R.id.semester1, R.id.semester2, R.id.semester3})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.semester1) {
            studentTime.setSemester1(true);
        } else if (buttonView.getId() == R.id.semester2) {
            studentTime.setSemester2(true);
        } else if (buttonView.getId() == R.id.semester3) {
            studentTime.setSemester3(true);
        }
    }
}