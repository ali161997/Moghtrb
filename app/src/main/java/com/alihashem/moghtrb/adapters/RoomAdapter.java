package com.alihashem.moghtrb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.models.Room;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private static final String TAG = "RoomAdapter";
    private static RecyclerViewClickListener itemListener;
    final private Context ctx;
    final private List<Room> roomList;

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
        View view = inflater.inflate(R.layout.item_recy_explore, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RoomViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final RoomViewHolder roomViewHolder, int i) {
        Room room = roomList.get(i);
        roomViewHolder.container.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_explore));

        String s = Double.toString(room.getPrice());
        roomViewHolder.price.setText(String.format("%s %s", s, ctx.getResources().getString(R.string.LEperMonth)));
        if (ctx.getResources().getConfiguration().locale.getLanguage().equals("ar")) {
            roomViewHolder.street.setText(room.getArAddress());
            if (room.getGender().toLowerCase().equals("male"))
                roomViewHolder.genderTv.setText("أولاد");
            else if (room.getGender().toLowerCase().equals("female"))
                roomViewHolder.genderTv.setText("بنات");
        } else {
            roomViewHolder.street.setText(room.getEnAddress());
            roomViewHolder.genderTv.setText(room.getGender());
        }

        roomViewHolder.ratingBar.setRating(room.getRate());
        roomViewHolder.reviews.setText(String.format("%s %s", room.getNum_reviews(), ctx.getResources().getString(R.string.reviews)));
        if (room.getbedsAvailable() < 0)
            roomViewHolder.numBedInRoom.setText(String.format("%s %s", 0, ctx.getResources().getString(R.string.bedInRoom)));
        else
            roomViewHolder.numBedInRoom.setText(String.format("%s %s", room.getbedsAvailable(), ctx.getResources().getString(R.string.bedInRoom)));

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
    }


    @Override
    public int getItemCount() {
        try {
            return roomList.size();
        } catch (Exception e) {
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
        TextView numBedInRoom;
        TextView genderTv;
        MaterialCardView container;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            numBedInRoom = itemView.findViewById(R.id.numBedInRoom);
            imageView = itemView.findViewById(R.id.image_item);
            price = itemView.findViewById(R.id.tv_price);
            street = itemView.findViewById(R.id.tv_street);
            ratingBar = itemView.findViewById(R.id.rating_item);
            reviews = itemView.findViewById(R.id.review_item);
            genderTv = itemView.findViewById(R.id.genderTV);
            container = itemView.findViewById(R.id.containerExplore);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());


        }


    }


}
