package com.moghtrb.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.moghtrb.R;
import com.moghtrb.activities.RoomDetail;
import com.moghtrb.models.BookingsModel;

import java.util.List;


public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsViewHolder> {
    private static final String TAG = "RoomAdapter";
    final private Context ctx;
    final private List<BookingsModel> bookingsList;


    public BookingsAdapter(Context ctx, List<BookingsModel> bookingsList) {
        this.ctx = ctx;
        this.bookingsList = bookingsList;


    }

    @Override
    public void onBindViewHolder(@NonNull BookingsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final BookingsViewHolder bookingsViewHolder, int i) {
        BookingsModel bookingsModel = bookingsList.get(i);
        bookingsViewHolder.container.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_inbox));
        bookingsViewHolder.cashAmount.setText(String.format("%s %s", bookingsModel.getCashPayed(), ctx.getResources().getString(R.string.LE)));
        bookingsViewHolder.numGuests.setText(String.format("%s %s", bookingsModel.getNumGuests(), ctx.getResources().getString(R.string.guestsCount)));
        bookingsViewHolder.from.setText(String.format("%s", bookingsModel.getFrom()));
        bookingsViewHolder.to.setText(String.format("%s", bookingsModel.getTo()));
        bookingsViewHolder.totalAmount.setText(String.format("%s %s", bookingsModel.getTotal(), ctx.getResources().getString(R.string.LE)));
        bookingsViewHolder.hostPhone.setText(String.format("%s", bookingsModel.getHostPhone()));


    }

    @NonNull
    @Override
    public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_recy_bookings, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new BookingsViewHolder(view);


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        try {
            return bookingsList.size();
        } catch (Exception e) {
            return 0;

        }

    }


    public class BookingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView from;
        TextView to;
        MaterialButton goToRoom;
        TextView cashAmount;
        TextView totalAmount;
        Button expandBtn;
        TextView city;
        TextView region;
        TextView numGuests;
        LinearLayout linearGuests;
        LinearLayout linearTotal;
        LinearLayout linearDates;
        LinearLayout payedOnline;
        Button goLocation;
        Button btnCopy;
        TextView hostPhone;
        MaterialCardView container;


        BookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            hostPhone = itemView.findViewById(R.id.hostPhone);
            from = itemView.findViewById(R.id.fromDate);
            to = itemView.findViewById(R.id.toDate);
            goToRoom = itemView.findViewById(R.id.goToRoom);
            cashAmount = itemView.findViewById(R.id.amountPayedValue);
            totalAmount = itemView.findViewById(R.id.totalPayedValue);
            numGuests = itemView.findViewById(R.id.guests_numValue);
            linearGuests = itemView.findViewById(R.id.linearGuests);
            linearTotal = itemView.findViewById(R.id.linearTotal);
            expandBtn = itemView.findViewById(R.id.expand);
            payedOnline = itemView.findViewById(R.id.linearPayedOnline);
            linearDates = itemView.findViewById(R.id.linearDates);
            goLocation = itemView.findViewById(R.id.goToLocationBtn);
            btnCopy = itemView.findViewById(R.id.btnCopy);
            container = itemView.findViewById(R.id.containerBookings);
            btnCopy.setOnClickListener(this);
            goLocation.setOnClickListener(this);
            goToRoom.setOnClickListener(this);
            expandBtn.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.goToRoom) {
                Intent intent = new Intent(ctx, RoomDetail.class);
                intent.putExtra("roomId", bookingsList.get(getLayoutPosition()).getRoomId());
                ctx.startActivity(intent);
            } else if (v.getId() == R.id.expand) {
                float deg = expandBtn.getRotation() + 180F;
                expandBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
                if (linearTotal.getVisibility() == View.VISIBLE)
                    setExpanded(false);
                else {
                    setExpanded(true);
                }


            } else if (v.getId() == R.id.btnCopy) {
                copyPhoneToClip();

            } else if (v.getId() == R.id.goToLocationBtn) {
                goToMapActivity();
            }
        }

        private void setExpanded(boolean expanded) {
            if (expanded) {
                linearTotal.setVisibility(View.VISIBLE);
                linearDates.setVisibility(View.VISIBLE);
                payedOnline.setVisibility(View.VISIBLE);
                linearGuests.setVisibility(View.VISIBLE);
            } else {
                linearTotal.setVisibility(View.GONE);
                linearGuests.setVisibility(View.GONE);
                linearDates.setVisibility(View.GONE);
                payedOnline.setVisibility(View.GONE);
            }


        }

        private void copyPhoneToClip() {
            ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("host phone", hostPhone.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ctx, "copied", Toast.LENGTH_SHORT).show();
        }

        private void goToMapActivity() {
            try {
                String ur = String.format("geo:%s,%s", bookingsList.get(getLayoutPosition()).getLatLng().getLat(), bookingsList.get(getLayoutPosition()).getLatLng().getLon());
                Uri gmmIntentUri = Uri.parse(ur);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {

                    ctx.startActivity(mapIntent);
                } else Toast.makeText(ctx, "you should install google map app", Toast.LENGTH_LONG);
            } catch (Exception e) {
                Toast.makeText(ctx, "Error occurred", Toast.LENGTH_LONG);
            }


        }
    }
}