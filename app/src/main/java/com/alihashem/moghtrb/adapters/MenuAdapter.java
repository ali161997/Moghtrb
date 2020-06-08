package com.alihashem.moghtrb.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.activities.Bookings;
import com.alihashem.moghtrb.activities.FeedBack;
import com.alihashem.moghtrb.activities.IdentifyHost;
import com.alihashem.moghtrb.activities.ReferHost;
import com.alihashem.moghtrb.activities.Settings;
import com.alihashem.moghtrb.activities.signing;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ItemsViewHolder> {
    final private Context ctx;
    final private List<String> list;
    private static final String TAG = "ProfileAdapter";


    public MenuAdapter(Context ctx, List<String> List) {
        this.ctx = ctx;
        this.list = List;


    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsViewHolder ItemsViewHolder, int i) {
        String str = list.get(i);
        ItemsViewHolder.tv.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.anim_item_inbox));
        ItemsViewHolder.tv.setText(str);

    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.item_listview, null, false);
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
        TextView tv;

        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.recyclerItemProfile);
            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: clicked" + this.getLayoutPosition());
            switch (this.getLayoutPosition()) {
                case 0:
                    Intent intent = new Intent(ctx, Bookings.class);
                    ctx.startActivity(intent);
                    break;
                case 1:
                    Intent intent2 = new Intent(ctx, Settings.class);
                    ctx.startActivity(intent2);
                    break;

                case 2:
                    Intent intent3 = new Intent(ctx, ReferHost.class);
                    ctx.startActivity(intent3);
                    break;
                case 3:
                    Intent intentBar = new Intent(ctx, IdentifyHost.class);
                    ctx.startActivity(intentBar);
                    break;
                case 4:
                    //---help
                    break;
                case 5:
                    Intent intentFeed = new Intent(ctx, FeedBack.class);
                    ctx.startActivity(intentFeed);
                    break;
                case 6:
                    signOut();
                    break;

            }

        }

        private void signOut() {
            new AlertDialog.Builder(ctx)
                    .setTitle("Log out")
                    .setMessage("Do you really want to Log Out?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                        Intent intent = new Intent(ctx.getApplicationContext(), signing.class);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseAuth.getInstance().getUid())
                                .addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Log.i(TAG, "onComplete: UnSubscribed failed");
                                    } else Log.i(TAG, "onComplete: UnSubscribed completed");

                                });
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        ctx.startActivity(intent);
                        ((Activity) ctx).finish();


                    })
                    .setNegativeButton(android.R.string.no, null).show();

        }


    }
}