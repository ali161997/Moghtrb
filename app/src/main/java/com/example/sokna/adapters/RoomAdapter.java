package com.example.sokna.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sokna.R;
import com.example.sokna.models.Room;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private static RecyclerViewClickListener itemListener;


    final private Context ctx;
    final private List<Room> roomList;
    private static final String TAG = "RoomAdapter";

    public RoomAdapter(Context ctx, List<Room> roomList, RecyclerViewClickListener itemListener) {
        this.ctx = ctx;
        this.roomList = roomList;
        RoomAdapter.itemListener = itemListener;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_recy_explore, null);
        return new RoomViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder roomViewHolder, int i) {
        Room room = roomList.get(i);
        String s = Double.toString(room.getPrice());
        roomViewHolder.price.setText(String.format("%s  LE/Month", s));
        roomViewHolder.street.setText(room.getStreet());
        roomViewHolder.ratingBar.setRating(room.getRate());
        roomViewHolder.reviews.setText(String.format("%s Reviews", room.getNum_reviews()));
        ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        progressBarDrawable.setColor(R.color.quantum_googred);
        progressBarDrawable.setAlpha(1);
        progressBarDrawable.setBackgroundColor(R.color.gray);
        roomViewHolder.imageView.getHierarchy().setProgressBarImage(progressBarDrawable);
        //roomViewHolder.progressBar.setVisibility(View.VISIBLE);
        roomViewHolder.imageView.setController(
                Fresco.newDraweeControllerBuilder()
                        .setTapToRetryEnabled(true)
                        .setUri(room.geturlsImage().get(0))
                        .build());

        // room.geturlsImage()[0]
    }


    @Override
    public int getItemCount() {

        try {
            return roomList.size();
        } catch (Exception e) {
            Log.i(TAG, "getItemCount: is null");
        }
        return 0;

    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDraweeView imageView;
        TextView price;
        TextView street;
        RatingBar ratingBar;
        TextView reviews;
        ProgressBar progressBar;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
            price = itemView.findViewById(R.id.tv_price);
            street = itemView.findViewById(R.id.tv_street);
            ratingBar = itemView.findViewById(R.id.rating_item);
            reviews = itemView.findViewById(R.id.review_item);
            progressBar = itemView.findViewById(R.id.progress_image_rec);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());

        }
    }


}
