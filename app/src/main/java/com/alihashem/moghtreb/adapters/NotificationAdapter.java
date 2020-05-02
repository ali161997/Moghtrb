package com.alihashem.moghtreb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.models.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private static final String TAG = "NotificationAdapter";
    private static RecyclerViewClickListener itemListener;
    final private Context ctx;
    final private List<Notification> notyList;

    public NotificationAdapter(Context ctx, List<Notification> notyList, RecyclerViewClickListener itemListener) {
        this.ctx = ctx;
        this.notyList = notyList;
        NotificationAdapter.itemListener = itemListener;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_notification, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder notyViewHolder, int i) {
        Notification noty = notyList.get(i);
        if (ctx.getResources().getConfiguration().locale.getLanguage().equals("ar")) {
            notyViewHolder.title.setText(noty.getArtitle());
            notyViewHolder.body.setText(noty.getArbody());
        } else {
            notyViewHolder.title.setText(noty.getEntitle());
            notyViewHolder.body.setText(noty.getEnbody());
        }
        Date date = noty.getTime().toDate();
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        notyViewHolder.time.setText(sfd.format(date));
    }


    @Override
    public int getItemCount() {
        try {
            return notyList.size();
        } catch (Exception e) {
            return 0;

        }

    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView body;
        TextView time;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notyTitle);
            body = itemView.findViewById(R.id.notyBody);
            time = itemView.findViewById(R.id.notyTime);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());


        }


    }


}
