package com.moghtrb.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.moghtrb.R;
import com.moghtrb.models.ShimmerProgress;

import java.util.ArrayList;
import java.util.List;

public class BookDetailAdapter extends RecyclerView.Adapter<BookDetailAdapter.ItemsViewHolder> {
    private static final String TAG = "BookAdapter";
    final private Context ctx;
    final private List<String> list;
    DisplayMetrics displayMetrics;
    int width;
    int height;
    private List<ShimmerProgress> progresses;


    public BookDetailAdapter(Context ctx, List<String> List) {
        this.ctx = ctx;
        this.list = List;
        progresses = new ArrayList<>(list.size());
        displayMetrics = new DisplayMetrics();
        ((Activity) ctx).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    private float dpToPixel(float dp) {
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsViewHolder ItemsViewHolder, int i) {
        String str = list.get(i);
        Uri imageUri = Uri.parse(str);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(displayMetrics.widthPixels, (int) dpToPixel(300)))
                .build();

        ItemsViewHolder.img.setController(
                Fresco.newDraweeControllerBuilder().setTapToRetryEnabled(true)
                        .setOldController(ItemsViewHolder.img.getController())
                        .setImageRequest(request)
                        .build());
        ShimmerProgress progress = new ShimmerProgress();
        progresses.add(progress);
        try {
            ItemsViewHolder.img.getHierarchy().setProgressBarImage(progresses.get(i));
            progresses.get(i).getFinished().observe((LifecycleOwner) ItemsViewHolder.img.getContext(), finished ->
            {

                if (finished) {
                    ItemsViewHolder.shimmerFrameLayout.hideShimmer();
                    ItemsViewHolder.shimmerFrameLayout.stopShimmer();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "onBindViewHolder: " + e.getMessage());
        }

    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_recy_book, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ItemsViewHolder(view);


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        try {
            return list.size();
        } catch (Exception e) {
            return 0;

        }

    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SimpleDraweeView img;
        ShimmerFrameLayout shimmerFrameLayout;

        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageBook);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerImgBook);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: clicked" + this.getLayoutPosition());
            switch (this.getLayoutPosition()) {

            }

        }


    }
}