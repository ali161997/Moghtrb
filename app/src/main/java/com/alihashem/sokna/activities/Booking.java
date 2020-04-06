package com.alihashem.sokna.activities;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.alihashem.sokna.R;
import com.alihashem.sokna.viewmodels.booking_view_model;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.MapsInitializer;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.alirezabdn.wp7progress.WP10ProgressBar;

public class Booking extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
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
    private booking_view_model booking_model;
    private BottomSheetBehavior bottomSheetMap;
    private GoogleMap googleMap;
    private WP10ProgressBar or;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mapview != null) {
            mapview.getMapAsync(this);
            mapview.onCreate(null);
            mapview.onResume();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);
        or = findViewById(R.id.pieProgress);


        bottomSheetMap = BottomSheetBehavior.from(mapBottomSheet);
        bottomSheetMap.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {


                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        toolbarBook.setNavigationOnClickListener(v -> onBackPressed());
        booking_model = ViewModelProviders.of(this).get(booking_view_model.class);
        booking_model.setRoomId(getIntent().getStringExtra("roomId"));
        booking_model.getRoomDetail().observe(this, roomDetail -> {
            //
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.i(TAG, token);
                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                });
        ObjectAnimator animator = ObjectAnimator.ofInt(horizontalScrollView, "scrollX", 400);
        animator.setStartDelay(1000);
        animator.setDuration(1000);
        animator.start();
        new Handler().postDelayed(() ->
        {

        }, 2000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));
        MapsInitializer.initialize(getApplicationContext());
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        CameraUpdateFactory.zoomTo(8);
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());


    }

    @OnClick({R.id.btn_request_booking, R.id.locationBtn, R.id.closeMapSheet})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeMapSheet:
                bottomSheetMap.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_request_booking:
                or.setVisibility(View.VISIBLE);
                or.showProgressBar();
                sendUpstream();
                new Handler().postDelayed(() ->
                {
                    or.hideProgressBar();
                    or.setVisibility(View.GONE);
                    btnRequestBooking.setIcon(getDrawable(R.drawable.ic_checked_black_24dp));
                    btnRequestBooking.setEnabled(false);
                    btnRequestBooking.setText(R.string.requestSubmitted);
                }, 5000);

                break;

            case R.id.locationBtn:
                bottomSheetMap.setState(BottomSheetBehavior.STATE_EXPANDED);
                try {
                    add_Marker(new LatLng(booking_model.getRoom().getValue().getLatLng().getLat(), booking_model.getRoom().getValue().getLatLng().getLon()), "Location");
                } catch (Exception e) {
                    Log.i(TAG, "onClick: " + e.getMessage());
                }

                break;

        }

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

    public void sendUpstream() {
        final String SENDER_ID = "272918520092";
        final String messageId = "message 1"; // Increment for each
        // [START fcm_send_upstream]
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        RemoteMessage m = new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
                .setMessageId(messageId)
                .addData("title", "Room Request")
                .addData("roomId", booking_model.getRoom().getValue().getRoomId())
                .addData("userId", FirebaseAuth.getInstance().getUid())
                .build();

        fm.send(m);
        Log.i(TAG, "sendUpstream: ");
        // [END fcm_send_upstream]
    }


}
