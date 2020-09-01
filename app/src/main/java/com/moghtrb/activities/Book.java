package com.moghtrb.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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
import androidx.lifecycle.ViewModelProvider;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.moghtrb.R;
import com.moghtrb.models.ShimmerProgress;
import com.moghtrb.models.StudentTime;
import com.moghtrb.viewmodels.BookViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ir.alirezabdn.wp7progress.WP10ProgressBar;
import ir.alirezabdn.wp7progress.WP7ProgressBar;

public class Book extends AppCompatActivity implements View.OnClickListener,
        Spinner.OnItemSelectedListener
        , CheckBox.OnCheckedChangeListener {
    private static StudentTime studentTime = new StudentTime();
    private final String TAG = "Booking_Activity";
    @BindView(R.id.image1_detail)
    SimpleDraweeView image1Detail;
    @BindView(R.id.image2_detail)
    SimpleDraweeView image2Detail;
    @BindView(R.id.image5_detail)
    SimpleDraweeView image5Detail;
    @BindView(R.id.image6_detail)
    SimpleDraweeView image6Detail;
    @BindView(R.id.linearRequest)
    LinearLayout linearLayoutRequest;
    @BindView(R.id.image3_detail)
    SimpleDraweeView image3Detail;
    @BindView(R.id.image4_detail)
    SimpleDraweeView image4Detail;
    @BindView(R.id.bed_title)
    TextView bedTitle;
    @BindView(R.id.rating_item_booking)
    RatingBar ratingItemBooking;
    @BindView(R.id.review_item_booking)
    TextView reviewItem;
    @BindView(R.id.ratingview_booking)
    LinearLayout ratingviewBooking;
    @BindView(R.id.line_booking)
    View lineBooking;
    @BindView(R.id.price_bed)
    TextView priceBed;
    @BindView(R.id.if_internet)
    MaterialTextView ifInternet;
    @BindView(R.id.if_gas)
    MaterialTextView ifGas;
    @BindView(R.id.if_cleaning)
    MaterialTextView ifCleaning;
    @BindView(R.id.btn_request_booking)
    MaterialButton btnRequestBooking;
    @BindView(R.id.numBedText)
    TextView numBedText;
    @BindView(R.id.numBedRoom)
    TextView numBedRoom;
    @BindView(R.id.numRoomText)
    TextView numRoomText;
    @BindView(R.id.numRoomValue)
    TextView numRoomValue;
    @BindView(R.id.roomOrderText)
    TextView roomOrderText;
    @BindView(R.id.roomOrderSpinner)
    Spinner roomOrderSpinnner;
    @BindView(R.id.haveBalconyText)
    TextView haveBalconyText;
    @BindView(R.id.BalconyBox)
    MaterialTextView BalconyBox;
    @BindView(R.id.numBathDepartText)
    TextView numBathDepartText;
    @BindView(R.id.numBathDepartValue)
    TextView numBathDepartValue;
    @BindView(R.id.hostRateText)
    TextView hostRateText;
    @BindView(R.id.hostRateValue)
    RatingBar hostRateValue;
    @BindView(R.id.locationText)
    TextView locationText;
    @BindView(R.id.locationBtn)
    Button locationIcon;
    @BindView(R.id.servicesText)
    TextView servicesText;
    @BindView(R.id.numPlaceAvailable)
    TextView placeAvailable;
    @BindView(R.id.horizontalView)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.toolbarBook)
    Toolbar toolbarBook;
    @BindView(R.id.BookLoadingProgress)
    WP7ProgressBar BookLoadingProgress;
    @BindView(R.id.pieProgress)
    WP10ProgressBar pieProgress;
    @BindView(R.id.mainViewBook)
    LinearLayout mainViewBook;
    @BindView(R.id.anytime_bottom_sheet)
    RelativeLayout anytimeBottomSheet;
    @BindView(R.id.spinnerAnyTime)
    Spinner spinner_when;
    @BindView(R.id.semester1)
    MaterialRadioButton semester1Box;
    @BindView(R.id.semester2)
    MaterialRadioButton semester2Box;
    @BindView(R.id.semester3)
    MaterialRadioButton semester3Box;
    @BindView(R.id.semester12)
    MaterialRadioButton semester12Box;
    @BindView(R.id.btn_save_anytime)
    Button BtnTimeSave;
    @BindView(R.id.radioTime)
    RadioGroup radioTimeSheet;
    @BindView(R.id.checkInValue)
    TextView checkIn;
    @BindView(R.id.checkOutValue)
    TextView checkOut;
    @BindView(R.id.coordinatorBook)
    CoordinatorLayout coordinatorBook;
    @BindView(R.id.shimmerImage1)
    ShimmerFrameLayout shimmerImage1;
    @BindView(R.id.shimmerImage2)
    ShimmerFrameLayout shimmerImage2;
    @BindView(R.id.shimmerImage3)
    ShimmerFrameLayout shimmerImage3;
    @BindView(R.id.shimmerImage4)
    ShimmerFrameLayout shimmerImage4;
    @BindView(R.id.shimmerImage5)
    ShimmerFrameLayout shimmerImage5;
    @BindView(R.id.shimmerImage6)
    ShimmerFrameLayout shimmerImage6;
    @BindView(R.id.num_guests_count)
    MaterialTextView numGuestsTv;
    @BindView(R.id.if_conditioner)
    MaterialTextView isConditioner;
    @BindView(R.id.if_elevator)
    MaterialTextView isElevator;
    @BindView(R.id.hostComment)
    MaterialTextView hostComment;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    MaterialDatePicker.Builder datePickerBuilder;
    Calendar myCalender;
    private String[] pickerVals;
    private BookViewModel booking_model;
    private BottomSheetBehavior whenSheet;
    private View studentView;
    private View foreignerView;
    private ShimmerProgress shimmerProgress1;
    private ShimmerProgress shimmerProgress2;
    private ShimmerProgress shimmerProgress3;
    private ShimmerProgress shimmerProgress4;
    private ShimmerProgress shimmerProgress5;
    private ShimmerProgress shimmerProgress6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
        booking_model = new ViewModelProvider(this).get(BookViewModel.class);
        booking_model.setRoomId(getIntent().getStringExtra("roomId"));
        booking_model.setDate(getIntent().getStringExtra("dates"));
        booking_model.setNumGuests(getIntent().getIntExtra("numGuests", 1));
        booking_model.setType(getIntent().getStringExtra("type"));
        studentView = findViewById(R.id.student_view);
        foreignerView = findViewById(R.id.foreigner_view);
        roomOrderSpinnner.setOnItemSelectedListener(this);
        spinner_when.setOnItemSelectedListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        BookLoadingProgress.showProgressBar();
        BookLoadingProgress.setVisibility(View.VISIBLE);
        mainViewBook.setVisibility(View.GONE);
        setUpBottomSheet();


    }

    private void animateHorizontalView() {
        new Handler().postDelayed(() ->
        {
            if (!isFinishing()) {
                ObjectAnimator animator = ObjectAnimator.ofInt(horizontalScrollView, "scrollX", 400);
                animator.setStartDelay(300);
                animator.setDuration(1000);
                animator.start();
            }
        }, 300);

    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerProgress1 = new ShimmerProgress();
        shimmerProgress2 = new ShimmerProgress();
        shimmerProgress3 = new ShimmerProgress();
        shimmerProgress4 = new ShimmerProgress();
        shimmerProgress5 = new ShimmerProgress();
        shimmerProgress6 = new ShimmerProgress();


        image1Detail.getHierarchy().setProgressBarImage(shimmerProgress1);
        image2Detail.getHierarchy().setProgressBarImage(shimmerProgress2);
        image3Detail.getHierarchy().setProgressBarImage(shimmerProgress3);
        image4Detail.getHierarchy().setProgressBarImage(shimmerProgress4);
        image5Detail.getHierarchy().setProgressBarImage(shimmerProgress5);
        image6Detail.getHierarchy().setProgressBarImage(shimmerProgress6);
        shimmerProgress1.getFinished().observe(this, finished -> {
            if (finished) {
                shimmerImage1.stopShimmer();
                shimmerImage1.hideShimmer();
            }
        });
        shimmerProgress2.getFinished().observe(this, finished -> {
            if (finished) {
                shimmerImage2.stopShimmer();
                shimmerImage2.hideShimmer();
            }
        });
        shimmerProgress3.getFinished().observe(this, finished -> {
            if (finished) {
                shimmerImage3.stopShimmer();
                shimmerImage3.hideShimmer();
            }
        });
        shimmerProgress4.getFinished().observe(this, finished -> {
            if (finished) {
                shimmerImage4.stopShimmer();
                shimmerImage4.hideShimmer();
            }
        });
        shimmerProgress5.getFinished().observe(this, finished -> {
            if (finished) {
                shimmerImage5.stopShimmer();
                shimmerImage5.hideShimmer();
            }
        });
        shimmerProgress6.getFinished().observe(this, finished -> {
            if (finished) {
                shimmerImage6.stopShimmer();
                shimmerImage6.hideShimmer();
            }
        });

        booking_model.getNumGuests().observe(this, num -> {
            numGuestsTv.setText(num + "");
        });
        booking_model.getKeysFloors().observe(this, list -> {
            ArrayAdapter<String> adapterFloors = new ArrayAdapter<>(this, R.layout.item_spinner_book, list);
            adapterFloors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roomOrderSpinnner.setAdapter(adapterFloors);
        });
        booking_model.getRoomDetail().observe(this, roomDetail -> {
            //
            BookLoadingProgress.hideProgressBar();
            BookLoadingProgress.setVisibility(View.GONE);
            mainViewBook.setVisibility(View.VISIBLE);
            numRoomValue.setText(String.format("%s %s", roomDetail.getNumRoomsInDepart(), getResources().getString(R.string.room)));
            // roomOrderSpinnner.setText(String.format("%s %s", roomDetail.getDepartOrder(), getResources().getString(R.string.th)));
            if (roomDetail.isHaveBalcony())
                BalconyBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked_black_24dp, 0, 0, 0);
            else
                BalconyBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_uncheck, 0, 0, 0);
            hostRateValue.setRating((float) roomDetail.getHostRate());
            numBathDepartValue.setText(String.format("%s %s", roomDetail.getNumBath(), getResources().getString(R.string.bath)));
            if (roomDetail.getServices().get("isInternet"))
                ifInternet.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_checked_black_24dp);
            else
                ifInternet.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_uncheck);
            if (roomDetail.getServices().get("isGas"))
                ifGas.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_checked_black_24dp);
            else
                ifGas.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_uncheck);
            if (roomDetail.getServices().get("isCleaning"))
                ifCleaning.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_checked_black_24dp);
            else
                ifCleaning.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_uncheck);
            if (roomDetail.getServices().get("isConditioning"))
                isConditioner.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_checked_black_24dp);
            else
                isConditioner.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_uncheck);
            if (roomDetail.getServices().get("isElevator"))
                isElevator.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_checked_black_24dp);
            else
                isElevator.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_uncheck);
            hostComment.setText(roomDetail.getHostComment());
            try {
                image2Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(0));
                image3Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(1));
                image4Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(2));
                image5Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(3));
                image6Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(4));


            } catch (Exception e) {
                Log.i(TAG, "onResume: " + e.getMessage());
            }
        });
        booking_model.getRoom().observe(this, room -> {
            image1Detail.setImageURI(room.getUrlImage1());
            String address = "";
            if (getResources().getConfiguration().locale.getLanguage().equals("ar"))
                address = room.getArAddress();
            else
                address = room.getEnAddress();
            bedTitle.setText(address);
            ratingItemBooking.setRating(room.getRate());
            reviewItem.setText(String.format("%s  %s", room.getNum_reviews(), getResources().getString(R.string.reviews)));
            priceBed.setText(String.format("%s %s", room.getPrice(), getResources().getString(R.string.LE)));
            numBedRoom.setText(String.format("%s %s", room.getTotalBeds(), getResources().getString(R.string.bed)));
            if (room.getTotalBeds() <= 0)
                linearLayoutRequest.setVisibility(View.GONE);


        });
        toolbarBook.setNavigationOnClickListener(v -> onBackPressed());
        animateHorizontalView();
        initPicker();

    }

    @Override
    public void onBackPressed() {

        if (whenSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.btn_request_booking, R.id.locationBtn
            , R.id.increase, R.id.decrease, R.id.closeMap, R.id.selectDates, R.id.CloseTimeSheet, R.id.btn_save_anytime})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CloseTimeSheet:
                whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_request_booking:

                if (booking_model.getDate().getValue() == null) {
                    Snackbar mySnackbar = Snackbar.make(coordinatorBook,
                            getResources().getString(R.string.checkDates), Snackbar.LENGTH_SHORT);
                    mySnackbar.setAction(R.string.set, new CheckDateListener());
                    mySnackbar.show();

                } else {
                    pieProgress.setVisibility(View.VISIBLE);
                    pieProgress.showProgressBar();
                    if (spinner_when.getSelectedItemPosition() == 0)
                        booking_model.setType("student");
                    else booking_model.setType("foreigner");
                    booking_model.sendRequest(FirebaseAuth.getInstance().getUid()).observe(this, bool ->
                    {
                        pieProgress.hideProgressBar();
                        pieProgress.setVisibility(View.GONE);
                        if (bool) {
                            showMessage();
                            btnRequestBooking.setIcon(getDrawable(R.drawable.ic_checked_black_24dp));
                            btnRequestBooking.setEnabled(false);
                            btnRequestBooking.setText(R.string.requestSubmitted);

                        } else {
                            btnRequestBooking.setIcon(null);
                            btnRequestBooking.setEnabled(true);
                            btnRequestBooking.setText("Resend");
                        }

                    });
                }
                break;
            case R.id.increase:
                Integer num = booking_model.getNumGuests().getValue();
                if (num < booking_model.getTotalPlacesAvailable().getValue())
                    booking_model.setNumGuests(++num);
                break;
            case R.id.decrease:
                int number = booking_model.getNumGuests().getValue();
                if (number > 1)
                    booking_model.setNumGuests(--number);
                break;

            case R.id.locationBtn:

                try {
                    goToMapActivity();
                    Log.i(TAG, "onClick: " + booking_model.getRoom().getValue().getLatLng().getLat() + "/" + booking_model.getRoom().getValue().getLatLng().getLon());
                } catch (Exception e) {
                    Log.i(TAG, "onClick: " + e.getMessage());
                }

                break;
            case R.id.btn_save_anytime:
                dateConstraints();
                break;
            case R.id.selectDates:
                materialDatePicker.show(getSupportFragmentManager(), TAG);
                break;


        }

    }

    private void showMessage() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.Notice))
                .setMessage(getString(R.string.timeMessageBook))
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                }).show();
    }

    private void goToMapActivity() {
        try {
            String ur = String.format("geo:%s,%s", booking_model.getRoom().getValue().getLatLng().getLat(), booking_model.getRoom().getValue().getLatLng().getLon());
            Uri gmmIntentUri = Uri.parse(ur);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {

                startActivity(mapIntent);
            } else Toast.makeText(this, "you should install google map app", Toast.LENGTH_LONG);
        } catch (Exception e) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_LONG);
        }
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
                booking_model.setDate(studentTime.toString());
            }
        } else {
            if (checkIn.getText().toString().equals("") || checkOut.getText().toString().equals(""))
                Toast.makeText(this, "you need to Complete dates", Toast.LENGTH_LONG).show();
            else {
                booking_model.setDate(checkIn.getText().toString() + "-" + checkOut.getText().toString());
                whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

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
            checkIn.setText(getMilli(materialDatePicker.getSelection().first));
            checkOut.setText(getMilli(materialDatePicker.getSelection().second));

        });

    }

    private String getMilli(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(date));
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
        } else if (parent.getId() == R.id.roomOrderSpinner) {
            String s = String.format("%s %s", booking_model.getValuesFloors().getValue().get(position), getString(R.string.Available));
            placeAvailable.setText(s);
            booking_model.setSelectedFloor(roomOrderSpinnner.getSelectedItem().toString());
            long x = ((Number) booking_model.getValuesFloors().getValue().get(position)).longValue();

            booking_model.setTotalPlacesAvailable((int) x);
            booking_model.setNumGuests(1);
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

    private class CheckDateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            whenSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}