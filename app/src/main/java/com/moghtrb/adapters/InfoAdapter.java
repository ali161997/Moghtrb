package com.moghtrb.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.moghtrb.R;
import com.moghtrb.models.ServiceInfoModel;

import java.util.HashMap;
import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ItemsViewHolder> {
    private static final String TAG = "ProfileAdapter";
    final private Context ctx;
    final private List<ServiceInfoModel> list;
    private HashMap<String, HashMap<String, Object>> updates;


    public InfoAdapter(Context ctx, List<ServiceInfoModel> List) {
        this.ctx = ctx;
        this.list = List;
        updates = new HashMap<>();


    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsViewHolder ItemsViewHolder, int i) {
        ServiceInfoModel infoModel = list.get(i);

        // ItemsViewHolder.tv.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_inbox));
        ItemsViewHolder.tvLike.setText(Integer.toString(infoModel.getLikes()));
        ItemsViewHolder.tvDisLike.setText(Integer.toString(infoModel.getDisLikes()));
        ItemsViewHolder.tvName.setText(infoModel.getName());
        ItemsViewHolder.draweeView.setController(
                Fresco.newDraweeControllerBuilder().setTapToRetryEnabled(true)
                        .setOldController(ItemsViewHolder.draweeView.getController())
                        .setUri(infoModel.getInfoImage())
                        .build());


    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_rec_info, null, false);
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

    public HashMap<String, HashMap<String, Object>> getUpdateMap() {
        return updates;
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MaterialTextView tvName;
        MaterialTextView tvLike;
        MaterialTextView tvDisLike;
        MaterialButton likeBtn;
        MaterialButton disLikeBtn;
        MaterialButton locationBtn;
        MaterialButton dialBtn;
        SimpleDraweeView draweeView;


        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.infoName);
            tvDisLike = itemView.findViewById(R.id.disLikesCounterTv);
            tvLike = itemView.findViewById(R.id.likesCounterTv);
            likeBtn = itemView.findViewById(R.id.btnLike);
            disLikeBtn = itemView.findViewById(R.id.btnDisLike);
            locationBtn = itemView.findViewById(R.id.infoLocation);
            dialBtn = itemView.findViewById(R.id.btnDial);
            draweeView = itemView.findViewById(R.id.imageItemInfo);
            likeBtn.setOnClickListener(this);
            disLikeBtn.setOnClickListener(this);
            locationBtn.setOnClickListener(this);
            dialBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
            likeBtn.setOnTouchListener((v, event) -> {

                return false;
            });


        }

        private void updateDoc() {
            HashMap<String, Object> keys = new HashMap<>();
            keys.put("likes", list.get(getLayoutPosition()).getLikes());
            keys.put("disLikes", list.get(getLayoutPosition()).getDisLikes());
            updates.put(list.get(getLayoutPosition()).getDocID(), keys);
        }


        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: clicked" + this.getLayoutPosition());
            switch (v.getId()) {
                case R.id.btnLike:
                    if (likeBtn.isSelected()) {
                        likeBtn.setSelected(false);
                        list.get(getLayoutPosition()).setLikes(list.get(getLayoutPosition()).getLikes() - 1);
                    } else {
                        likeBtn.setSelected(true);
                        list.get(getLayoutPosition()).setLikes(list.get(getLayoutPosition()).getLikes() + 1);
                        if (disLikeBtn.isSelected()) {
                            disLikeBtn.setSelected(false);
                            list.get(getLayoutPosition()).setDisLikes(list.get(getLayoutPosition()).getDisLikes() - 1);
                        }

                    }
                    updateDoc();
                    notifyDataSetChanged();
                    break;
                case R.id.btnDisLike:
                    if (disLikeBtn.isSelected()) {
                        disLikeBtn.setSelected(false);
                        list.get(getLayoutPosition()).setDisLikes(list.get(getLayoutPosition()).getDisLikes() - 1);
                    } else {
                        disLikeBtn.setSelected(true);
                        list.get(getLayoutPosition()).setDisLikes(list.get(getLayoutPosition()).getDisLikes() + 1);
                        if (likeBtn.isSelected()) {
                            likeBtn.setSelected(false);
                            list.get(getLayoutPosition()).setLikes(list.get(getLayoutPosition()).getLikes() - 1);
                        }

                    }
                    updateDoc();
                    notifyDataSetChanged();

                    break;

                case R.id.infoLocation:
                    goToMapActivity();
                    break;
                case R.id.btnDial:
                    goToDialer();
                    break;

            }

        }

        private void goToDialer() {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + list.get(getLayoutPosition()).getPhone()));
            ctx.startActivity(intent);
        }

        private void goToMapActivity() {
            try {
                String ur = String.format("geo:%s,%s", list.get(getLayoutPosition()).getLocation().getLat(), list.get(getLayoutPosition()).getLocation().getLon());
                Uri gmmIntentUri = Uri.parse(ur);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(ctx.getPackageManager()) != null) {

                    ctx.startActivity(mapIntent);
                } else
                    Toast.makeText(ctx, "you should install google map app", Toast.LENGTH_LONG);
            } catch (Exception e) {
                Toast.makeText(ctx, "Error occurred", Toast.LENGTH_LONG);
            }
        }

    }

}