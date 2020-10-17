package com.moghtrb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.moghtrb.R;

import java.util.List;

public class FilterMultiCategoryAdapter extends RecyclerView.Adapter<FilterMultiCategoryAdapter.ItemsViewHolder> {
    private static final String TAG = "ProfileAdapter";
    final private Context ctx;
    final private List<String> list;
    MaterialTextView tvLast;
    private MutableLiveData<Integer> position;

    public FilterMultiCategoryAdapter(Context ctx, List<String> List) {
        this.ctx = ctx;
        this.list = List;
        position = new MutableLiveData<>();


    }

    public MutableLiveData<Integer> getPosition() {
        return position;
    }

    public void setPosition(MutableLiveData<Integer> position) {
        this.position = position;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsViewHolder ItemsViewHolder, int i) {
        String category = list.get(i);

        // ItemsViewHolder.tv.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_inbox));
        ItemsViewHolder.tvItem.setText(category);


    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_rec_category, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
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
        MaterialTextView tvItem;
        int last = -1;


        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.itemCategory);
            itemView.setOnClickListener(this);
            tvItem.setOnClickListener(this);
            tvItem.setOnTouchListener((v, event) -> {

                return false;
            });


        }


        @Override
        public void onClick(View v) {
            if (tvLast != null)
                tvLast.setSelected(false);
            tvLast = tvItem;
            position.setValue(getLayoutPosition());
            tvItem.setSelected(true);


        }

    }

}

