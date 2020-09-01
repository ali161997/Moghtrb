package com.moghtrb.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.moghtrb.R;
import com.moghtrb.models.Room;
import com.moghtrb.models.ShimmerProgress;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private static final String TAG = "RoomAdapter";
    private static RecyclerViewClickListener itemListener;
    final private Context ctx;
    final private List<Room> roomList;
    DisplayMetrics displayMetrics;
    int width;
    int height;
    private Boolean isStudent;
    private List<ShimmerProgress> progresses;

    public RoomAdapter(Context ctx, List<Room> roomList, RecyclerViewClickListener itemListener, Boolean isStudent) {
        this.ctx = ctx;
        this.roomList = roomList;
        progresses = new ArrayList<>(roomList.size());
        RoomAdapter.itemListener = itemListener;
        this.isStudent = isStudent;
        displayMetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


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
        if (roomList.size() - 1 == i) {
            roomViewHolder.container.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_last_item_explore));
        }
        roomViewHolder.container.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_explore));
        if (isStudent) {
            String s = Double.toString(room.getPrice());
            roomViewHolder.price.setText(String.format("%s %s", s, ctx.getResources().getString(R.string.LEperMonth)));
        } else {
            String s = Double.toString(room.getDayCost());
            roomViewHolder.price.setText(String.format("%s %s", s, ctx.getResources().getString(R.string.LEPerDay)));
        }

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
        Uri imageUri = Uri.parse(room.getUrlImage1());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(width, (int) dpToPixel(300)))
                .build();

        roomViewHolder.imageView.setController(
                Fresco.newDraweeControllerBuilder().setTapToRetryEnabled(true)
                        .setOldController(roomViewHolder.imageView.getController())
                        .setImageRequest(request)
                        .build());

        ShimmerProgress progress = new ShimmerProgress();
        progresses.add(progress);
        try {
            roomViewHolder.imageView.getHierarchy().setProgressBarImage(progresses.get(i));
        } catch (Exception e) {
            Log.i(TAG, "onBindViewHolder: " + e.getMessage());
        }

        progresses.get(i).getFinished().observe((LifecycleOwner) roomViewHolder.imageView.getContext(), finished ->
        {

            if (finished) {
                roomViewHolder.shimmerFrameLayout.hideShimmer();
                roomViewHolder.shimmerFrameLayout.stopShimmer();
            }
        });


    }

    private float dpToPixel(float dp) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
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
        TextView genderTv;
        MaterialCardView container;
        ShimmerFrameLayout shimmerFrameLayout;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
            price = itemView.findViewById(R.id.tv_price);
            street = itemView.findViewById(R.id.tv_street);
            ratingBar = itemView.findViewById(R.id.rating_item);
            reviews = itemView.findViewById(R.id.review_item);
            genderTv = itemView.findViewById(R.id.genderTV);
            container = itemView.findViewById(R.id.containerExplore);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerImage);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());


        }


    }


}
