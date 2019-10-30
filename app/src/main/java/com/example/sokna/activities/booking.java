package com.example.sokna.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private booking_view_model booking_model;
    private final String TAG = "Booking Activity";
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


    }

    private void setDataView() {
        try {


            image1Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(0));
            image2Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(1));
            image3Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(2));
            image4Detail.setImageURI(booking_model.getRoom().getValue().geturlsImage().get(3));
            bedTitle.setText(booking_model.getRoom().getValue().getStreet());
            ratingItemBooking.setRating(booking_model.getRoom().getValue().getRate());
            reviewItem.setText(booking_model.getRoom().getValue().getNum_reviews());
            priceBed.setText(String.valueOf(booking_model.getRoom().getValue().getPrice()));
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
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
                break;

        }

    }
}
