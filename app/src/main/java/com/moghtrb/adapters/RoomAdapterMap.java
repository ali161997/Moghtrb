package com.moghtrb.adapters;

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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moghtrb.R;
import com.moghtrb.models.Room;

import java.util.List;

public class RoomAdapterMap extends RecyclerView.Adapter<RoomAdapterMap.RoomViewHolder> {
    private static final String TAG = "RoomAdapterMap";
    private static RecyclerViewClickListener itemListener;
    final private Context ctx;
    final private List<Room> roomList;

    public RoomAdapterMap(Context ctx, List<Room> roomList, RecyclerViewClickListener itemListener) {
        this.ctx = ctx;
        this.roomList = roomList;
        RoomAdapterMap.itemListener = itemListener;

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
        View view = inflater.inflate(R.layout.item_recy_map, null);
        return new RoomViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder roomViewHolder, int i) {
        Room room = roomList.get(i);
        String s = Double.toString(room.getPrice());
        roomViewHolder.price.setText(s + "  LE/Month");
        if (ctx.getResources().getConfiguration().locale.getLanguage().equals("ar"))
            roomViewHolder.street.setText(room.getArAddress());
        else roomViewHolder.street.setText(room.getEnAddress());
        roomViewHolder.ratingBar.setRating(room.getRate());
        roomViewHolder.reviews.setText(room.getNum_reviews() + " Reviews");
        ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
        progressBarDrawable.setColor(R.color.quantum_googred);
        progressBarDrawable.setAlpha(1);
        progressBarDrawable.setBackgroundColor(R.color.gray);
        roomViewHolder.imageView.getHierarchy().setProgressBarImage(progressBarDrawable);
        roomViewHolder.imageView.setController(
                Fresco.newDraweeControllerBuilder()
                        .setTapToRetryEnabled(true)
                        .setUri(room.getUrlImage1())
                        .build());

        // room.geturlsImage()[0]
    }


    @Override
    public int getItemCount() {
        try {
            return roomList.size();
        } catch (Exception e) {
            Log.i(TAG, "getItemCount: 0");
            return 0;
        }


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
            imageView = itemView.findViewById(R.id.image_item_map);
            price = itemView.findViewById(R.id.tv_price_map);
            street = itemView.findViewById(R.id.tv_street_map);
            ratingBar = itemView.findViewById(R.id.rating_item_map);
            reviews = itemView.findViewById(R.id.review_item_map);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());

        }
    }


}
