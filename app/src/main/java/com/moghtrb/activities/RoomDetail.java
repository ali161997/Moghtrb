package com.moghtrb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.moghtrb.R;
import com.moghtrb.adapters.BookDetailAdapter;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.RoomDetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.alirezabdn.wp7progress.WP7ProgressBar;

public class RoomDetail extends AppCompatActivity implements View.OnClickListener,
        Spinner.OnItemSelectedListener {
    private final String TAG = "Booking_Activity";
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
    @BindView(R.id.btn_go_confirmation)
    MaterialButton btn_go_confirmation;
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
    @BindView(R.id.servicesText)
    TextView servicesText;
    @BindView(R.id.numPlaceAvailable)
    TextView placeAvailable;
    @BindView(R.id.horizontalView)
    LinearLayout horizontalScrollView;
    @BindView(R.id.toolbarBook)
    Toolbar toolbarBook;
    @BindView(R.id.BookLoadingProgress)
    WP7ProgressBar BookLoadingProgress;
    @BindView(R.id.mainViewBook)
    LinearLayout mainViewBook;
    @BindView(R.id.coordinatorBook)
    CoordinatorLayout coordinatorBook;
    @BindView(R.id.num_guests_count)
    MaterialTextView numGuestsTv;
    @BindView(R.id.if_conditioner)
    MaterialTextView isConditioner;
    @BindView(R.id.if_elevator)
    MaterialTextView isElevator;
    @BindView(R.id.hostComment)
    MaterialTextView hostComment;
    @BindView(R.id.book_horizontal_recycler)
    RecyclerView bookHorizontalRecycler;
    private RoomDetailViewModel booking_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        ButterKnife.bind(this);
        setupRecyclerView();
        booking_model = new ViewModelProvider(this).get(RoomDetailViewModel.class);
        booking_model.setRoomId(getIntent().getStringExtra("roomId"));
        booking_model.setDate(getIntent().getStringExtra("dates"));
        booking_model.setNumGuests(getIntent().getIntExtra("numGuests", 1));
        booking_model.setType(getIntent().getStringExtra("type"));
        booking_model.setNumDays(getIntent().getIntExtra("days", 0));
        roomOrderSpinnner.setOnItemSelectedListener(this);


    }

    private void setupRecyclerView() {
        bookHorizontalRecycler.setHasFixedSize(true);
        bookHorizontalRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bookHorizontalRecycler.addItemDecoration(new VerticalSpaceItemDecoration(2));
        bookHorizontalRecycler.setItemAnimator(new DefaultItemAnimator());
        bookHorizontalRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // mapViewModel.setIsScrolling(true);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        BookLoadingProgress.showProgressBar();
        BookLoadingProgress.setVisibility(View.VISIBLE);
        mainViewBook.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        super.onResume();

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
            //roomOrderSpinnner.setText(String.format("%s %s", roomDetail.getDepartOrder(), getResources().getString(R.string.th)));
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
            booking_model.getListImgs().observe(this, imageList ->
                    {
                        BookDetailAdapter imgAdapter = new BookDetailAdapter(this, imageList);
                        bookHorizontalRecycler.setAdapter(imgAdapter);


                    }
            );

        });
        booking_model.getRoom().observe(this, room -> {
            // image1Detail.setImageURI(room.getUrlImage1());
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

        });
        toolbarBook.setNavigationOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.btn_go_confirmation
            , R.id.increase, R.id.decrease})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_confirmation:
                Intent goConfirmation = new Intent(this, ConfirmRequest.class);
                goConfirmation.putExtra("roomId", booking_model.getRoomId().getValue());
                goConfirmation.putExtra("numGuests", booking_model.getNumGuests().getValue());
                goConfirmation.putExtra("type", booking_model.getType().getValue());
                goConfirmation.putExtra("roomOrder", booking_model.getSelectedFloor().getValue());
                try {
                    if (booking_model.getType().getValue().equalsIgnoreCase("Foreigner")) {
                        goConfirmation.putExtra("days", booking_model.getNumDays().getValue());
                        goConfirmation.putExtra("dates", booking_model.getDate().getValue());
                    } else if (booking_model.getType().getValue().equalsIgnoreCase("Student"))
                        goConfirmation.putExtra("dates", booking_model.getDate().getValue());


                } catch (Exception e) {
                    Log.i(TAG, "btn_go_confirm " + e.getMessage());
                }

                //send room id
                startActivity(goConfirmation);
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


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.roomOrderSpinner) {
            String s = String.format("%s %s", booking_model.getValuesFloors().getValue().get(position), getString(R.string.available));
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

}