package com.moghtrb.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.textview.MaterialTextView;
import com.moghtrb.R;
import com.moghtrb.activities.MultipleCategoryService;
import com.moghtrb.activities.OneCategoryService;
import com.moghtrb.models.ServicesModel;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ItemsViewHolder> {
    private static final String TAG = "ServicesAdapter";
    final private Context ctx;
    final private List<ServicesModel> list;


    public ServicesAdapter(Context ctx, List<ServicesModel> List) {
        this.ctx = ctx;
        this.list = List;


    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsViewHolder ItemsViewHolder, int i) {
        ServicesModel model = list.get(i);
        ItemsViewHolder.tv.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_inbox));
        ItemsViewHolder.imageView.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_explore));
        ItemsViewHolder.tv.setText(model.getServiceName());
        ItemsViewHolder.imageView.setBackground(ctx.getResources().getDrawable(model.getServiceIconId()));

    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_rec_services, null, false);
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
        MaterialTextView tv;
        SimpleDraweeView imageView;

        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.serviceName);
            imageView = itemView.findViewById(R.id.imageViewServices);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: clicked" + this.getLayoutPosition());

            if (getLayoutPosition() == 2) {
                Intent intent = new Intent(ctx, MultipleCategoryService.class);
                ctx.startActivity(intent);
            } else {
                Intent intent = new Intent(ctx, OneCategoryService.class);
                intent.putExtra("index", this.getLayoutPosition());
                ctx.startActivity(intent);
            }

        }


    }
}