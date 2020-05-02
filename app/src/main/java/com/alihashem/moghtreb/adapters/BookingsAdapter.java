package com.alihashem.moghtreb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.models.BookingsModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;


public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsViewHolder> {
    private static final String TAG = "RoomAdapter";
    private static BtnGoClicked itemListener;
    final private Context ctx;
    final private List<BookingsModel> bookingsList;


    public BookingsAdapter(Context ctx, List<BookingsModel> bookingsList, BtnGoClicked itemListener) {
        this.ctx = ctx;
        this.bookingsList = bookingsList;
        BookingsAdapter.itemListener = itemListener;

    }

    @Override
    public void onBindViewHolder(@NonNull BookingsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final BookingsViewHolder bookingsViewHolder, int i) {
        BookingsModel bookingsModel = bookingsList.get(i);
        bookingsViewHolder.cashAmount.setText(bookingsModel.getCashPayed().toString());
        String[] dates = bookingsModel.getDates().split("-");
        bookingsViewHolder.from.setText(dates[0]);
        bookingsViewHolder.to.setText(dates[1]);
        bookingsViewHolder.goToRoom.setOnClickListener(v -> {

            itemListener.BtnGoOnClick(bookingsViewHolder.goToRoom, i);
        });


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

    public interface BtnGoClicked {

        void BtnGoOnClick(View v, int position);

    }

    public class BookingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView from;
        TextView to;
        MaterialButton goToRoom;
        TextView cashAmount;

        public BookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.fromDate);
            to = itemView.findViewById(R.id.toDate);
            goToRoom = itemView.findViewById(R.id.goToRoom);
            cashAmount = itemView.findViewById(R.id.amountPayed);
            goToRoom.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            itemListener.BtnGoOnClick(v, this.getLayoutPosition());
        }


    }


}
