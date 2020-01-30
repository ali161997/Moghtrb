package com.example.sokna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import com.example.sokna.R;
import com.example.sokna.models.Room;
import com.example.sokna.viewmodels.booking_view_model;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class booking extends AppCompatActivity implements View.OnClickListener {

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
    CheckBox ifInternet;
    @BindView(R.id.if_gas)
    CheckBox ifGas;
    @BindView(R.id.if_cleaning)
    CheckBox ifCleaning;
    @BindView(R.id.scroll_booking)
    ScrollView scrollBooking;
    @BindView(R.id.back_booking)
    Button backBooking;
    @BindView(R.id.btn_request_booking)
    Button btnRequestBooking;
    @BindView(R.id.request_booking)
    CardView requestBooking;
    @BindView(R.id.bedPriceText)
    TextView bedPriceText;
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
    CheckBox BalconyBox;
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
    @BindView(R.id.locationIcon)
    ImageView locationIcon;
    @BindView(R.id.servicesText)
    TextView servicesText;
    private booking_view_model booking_model;
    private final String TAG = "Booking_Activity";
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(this);
        backBooking.setBackgroundResource(R.drawable.arrow_back);
        booking_model = ViewModelProviders.of(this).get(booking_view_model.class);
        room = getIntent().getParcelableExtra("room_selected");
        booking_model.set_room(room);
        setDataView();
        booking_model.getRoomDtail().observe(this, roomDetail -> {
            numBedRoom.setText(String.format("%s %s", roomDetail.getNumBedsInRoom(), getResources().getString(R.string.bed)));
            numRoomValue.setText(String.format("%s %s", roomDetail.getNumRoomsInDepart(), getResources().getString(R.string.room)));
            roomOrderValue.setText(String.format("%s %s", roomDetail.getDepartOrder(), getResources().getString(R.string.th)));
            BalconyBox.setChecked(roomDetail.isHaveBalacon());
            hostRateValue.setRating((float) roomDetail.getHostRate());
            numBathDepartValue.setText(String.format("%s %s", roomDetail.getNumBath(), getResources().getString(R.string.bath)));
            ifInternet.setChecked(roomDetail.isInternetAvailable());
            ifGas.setChecked(roomDetail.isNaturalGasAvailable());
            ifCleaning.setChecked(roomDetail.isPeriodCleaning());

        });


    }

    private void setDataView() {
        try {


            image1Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(0));
            image2Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(1));
            image3Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(2));
            image4Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(3));
            bedTitle.setText(booking_model.getRoom().getValue().getStreet());
            ratingItemBooking.setRating(booking_model.getRoom().getValue().getRate());
            reviewItem.setText(String.format("%s  %s", booking_model.getRoom().getValue().getNum_reviews(), getResources().getString(R.string.reviews)));
            priceBed.setText(String.format("%s %s", booking_model.getRoom().getValue().getPrice(), getResources().getString(R.string.LE)));
            numBedRoom.setText(String.format("%s %s", booking_model.getRoomDtail().getValue().getNumBedsInRoom(), getResources().getString(R.string.bed)));
            numRoomValue.setText(String.format("%s %s", booking_model.getRoomDtail().getValue().getNumRoomsInDepart(), getResources().getString(R.string.room)));
            roomOrderValue.setText(String.format("%s %s", booking_model.getRoomDtail().getValue().getDepartOrder(), getResources().getString(R.string.th)));
            BalconyBox.setChecked(booking_model.getRoomDtail().getValue().isHaveBalacon());
            hostRateValue.setRating((float) booking_model.getRoomDtail().getValue().getHostRate());
            numBathDepartValue.setText(String.format("%s %s", booking_model.getRoomDtail().getValue().getNumBath(), getResources().getString(R.string.bath)));
            ifInternet.setChecked(booking_model.getRoomDtail().getValue().isInternetAvailable());
            ifGas.setChecked(booking_model.getRoomDtail().getValue().isNaturalGasAvailable());
            ifCleaning.setChecked(booking_model.getRoomDtail().getValue().isPeriodCleaning());


        } catch (Exception e) {
            Log.i(TAG, "we are here" + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.back_booking, R.id.btn_request_booking})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_booking:
                onBackPressed();
                break;
            case R.id.btn_request_booking:
                Intent paymentclass = new Intent(this, PaymentActivity.class);
                startActivity(paymentclass);
                break;

        }

    }
}
