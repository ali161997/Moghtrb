package com.alihashem.moghtreb.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.models.DatePickerCheckIn;
import com.alihashem.moghtreb.models.DatePickerCheckOut;
import com.alihashem.moghtreb.models.StudentTime;
import com.alihashem.moghtreb.viewmodels.BookViewModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

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
    @BindView(R.id.roomOrderValue)
    TextView roomOrderValue;
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
    @BindView(R.id.map_bottom_sheet)
    RelativeLayout mapBottomSheet;
    @BindView(R.id.mapSheet)
    MapView mapview;
    @BindView(R.id.BookLoadingProgress)
    WP7ProgressBar BookLoadingProgress;
    @BindView(R.id.pieProgress)
    WP10ProgressBar pieProgress;
    @BindView(R.id.mainViewBook)
    RelativeLayout mainViewBook;
    @BindView(R.id.anytime_bottom_sheet)
    RelativeLayout anytimeBottomSheet;
    @BindView(R.id.spinnerAnyTime)
    Spinner spinner_when;
    @BindView(R.id.selectall)
    CheckBox selectAllBox;
    @BindView(R.id.semester1)
    CheckBox semester1Box;
    @BindView(R.id.semester2)
    CheckBox semester2Box;
    @BindView(R.id.semester3)
    CheckBox semester3Box;
    @BindView(R.id.btn_save_anytime)
    Button BtnTimeSave;
    @BindView(R.id.btnCheckIn)
    Button checkIn;
    @BindView(R.id.btnCheckOut)
    Button checkOut;
    private BookViewModel booking_model;
    private BottomSheetBehavior bottomSheetMap;
    private BottomSheetBehavior whenSheet;

    private View studentView;
    private View foreignerView;
    private GoogleMap googleMap;

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
                animator.setStartDelay(1000);
                animator.setDuration(1000);
                animator.start();
            }
        }, 500);

    }

    @Override
    protected void onResume() {
        super.onResume();
        booking_model.getRoomDetail().observe(this, roomDetail -> {
            //
            BookLoadingProgress.hideProgressBar();
            BookLoadingProgress.setVisibility(View.GONE);
            mainViewBook.setVisibility(View.VISIBLE);
            placeAvailable.setText(String.format("%s %s", roomDetail.getBedsAvailable(), getResources().getString(R.string.placeAvaiable)));
            numRoomValue.setText(String.format("%s %s", roomDetail.getNumRoomsInDepart(), getResources().getString(R.string.room)));
            roomOrderValue.setText(String.format("%s %s", roomDetail.getDepartOrder(), getResources().getString(R.string.th)));
            if (roomDetail.isHaveBalcony())
                BalconyBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checked_black_24dp, 0, 0, 0);
            else
                BalconyBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_uncheck, 0, 0, 0);
            hostRateValue.setRating((float) roomDetail.getHostRate());
            numBathDepartValue.setText(String.format("%s %s", roomDetail.getNumBath(), getResources().getString(R.string.bath)));
            if (roomDetail.getServices().get("isInternet"))
                ifInternet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checked_black_24dp, 0);
            else
                ifInternet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uncheck, 0);
            if (roomDetail.getServices().get("isGas"))
                ifGas.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checked_black_24dp, 0);
            else
                ifGas.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uncheck, 0);
            if (roomDetail.getServices().get("isCleaning"))
                ifCleaning.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_checked_black_24dp, 0);
            else
                ifCleaning.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_uncheck, 0);

            image2Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(0));
            image3Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(1));
            image4Detail.setImageURI(booking_model.getRoomDetail().getValue().getUrlsImage().get(2));

        });
        booking_model.getRoom().observe(this, room -> {
            numBedRoom.setText(String.format("%s %s", booking_model.getRoom().getValue().getNumber_beds(), getResources().getString(R.string.bed)));
            image1Detail.setImageURI(room.getUrlImage1());
            String address = "";
            if (getResources().getConfiguration().locale.getLanguage().equals("ar"))
                address = room.getArAddress();
            else
                address = (room.getCity() + "/" + room.getRegion() + "/" + room.getStreet());


            bedTitle.setText(address);
            ratingItemBooking.setRating(room.getRate());
            reviewItem.setText(String.format("%s  %s", room.getNum_reviews(), getResources().getString(R.string.reviews)));
            priceBed.setText(String.format("%s %s", room.getPrice(), getResources().getString(R.string.LE)));
            numBedRoom.setText(String.format("%s %s", room.getNumber_beds(), getResources().getString(R.string.bed)));


        });
        toolbarBook.setNavigationOnClickListener(v -> onBackPressed());
        initMap();
        animateHorizontalView();

    }

    private void initMap() {
        new Handler().postDelayed(() -> {
            if (!isFinishing()) {
                if (mapview != null) {
                    mapview.getMapAsync(googleMap1 -> {
                        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));
                        googleMap = googleMap1;
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
                        CameraUpdateFactory.zoomTo(8);
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                    });
                    mapview.onCreate(null);
                    mapview.onResume();


                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetMap.getState() == BottomSheetBehavior.STATE_EXPANDED || whenSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetMap.setState(BottomSheetBehavior.STATE_COLLAPSED);
            whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.btn_request_booking, R.id.locationBtn, R.id.closeMap, R.id.btnCheckIn, R.id.btnCheckOut, R.id.CloseTimeSheet, R.id.btn_save_anytime})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CloseTimeSheet:
                whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_request_booking:
                if (booking_model.getDate().getValue() == null) {
                    Toast.makeText(this, "You need to select Dates", Toast.LENGTH_SHORT).show();
                    whenSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    pieProgress.setVisibility(View.VISIBLE);
                    pieProgress.showProgressBar();
                    booking_model.setType(spinner_when.getSelectedItem().toString());
                    booking_model.sendRequest(FirebaseAuth.getInstance().getUid()).observe(this, bool ->
                    {
                        pieProgress.hideProgressBar();
                        pieProgress.setVisibility(View.GONE);
                        if (bool) {
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

            case R.id.locationBtn:

                bottomSheetMap.setState(BottomSheetBehavior.STATE_EXPANDED);
                try {
                    add_Marker(new LatLng(booking_model.getRoom().getValue().getLatLng().getLat(), booking_model.getRoom().getValue().getLatLng().getLon()), "Location");
                } catch (Exception e) {
                    Log.i(TAG, "onClick: " + e.getMessage());
                }

                break;
            case R.id.closeMap:
                bottomSheetMap.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_save_anytime:
                if (spinner_when.getSelectedItemPosition() == 0)
                    if (studentTime.isSemester1() && studentTime.isSemester3() && !studentTime.isSemester3())
                        Toast.makeText(this, "you need to Check semester2", Toast.LENGTH_LONG).show();

                    else {
                        booking_model.setDate(studentTime.toString());
                        whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                else if (checkIn.getText().toString().equals("") || checkOut.getText().toString().equals(""))
                    Toast.makeText(this, "you need to Complete dates", Toast.LENGTH_LONG).show();
                else {
                    booking_model.setDate(checkIn.getText().toString() + "-" + checkOut.getText().toString());
                    whenSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.btnCheckIn:
                DialogFragment dialogin = new DatePickerCheckIn();
                dialogin.show(getSupportFragmentManager(), "in");
                break;
            case R.id.btnCheckOut:
                DialogFragment dialogout = new DatePickerCheckOut();
                dialogout.show(getSupportFragmentManager(), "out");
                break;


        }

    }

    private void setUpBottomSheet() {
        bottomSheetMap = BottomSheetBehavior.from(mapBottomSheet);
        whenSheet = BottomSheetBehavior.from(anytimeBottomSheet);
        bottomSheetMap.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetMap.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {


                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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

    private void add_Marker(LatLng latLng, String name) {
        googleMap.addMarker(new MarkerOptions().position((latLng)).title(name).snippet("i hope we succes"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

    @OnCheckedChanged({R.id.semester1, R.id.semester2, R.id.semester3, R.id.selectall})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.selectall) {
            semester1Box.setChecked(isChecked);
            semester2Box.setChecked(isChecked);
            semester3Box.setChecked(isChecked);
            studentTime.selectAll(isChecked);
        } else if (buttonView.getId() == R.id.semester1) {
            studentTime.setSemester1(true);
        } else if (buttonView.getId() == R.id.semester2) {
            studentTime.setSemester2(true);
        } else if (buttonView.getId() == R.id.semester3) {
            studentTime.setSemester3(true);
        }


    }
}