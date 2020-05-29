package com.alihashem.moghtrb.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alihashem.moghtrb.Interfaces.IOnBackPressed;
import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.activities.Bookings;
import com.alihashem.moghtrb.activities.Payment;
import com.alihashem.moghtrb.adapters.NotificationAdapter;
import com.alihashem.moghtrb.models.VerticalSpaceItemDecoration;
import com.alihashem.moghtrb.viewmodels.InboxViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Inbox extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        IOnBackPressed,
        NotificationAdapter.RecyclerViewClickListener {
    private static final String TAG = "inbox";
    @BindView(R.id.inboxRecycler)
    RecyclerView inboxRecycler;
    @BindView(R.id.inboxSwipe)
    SwipeRefreshLayout inboxSwipe;
    @BindView(R.id.toolbarInbox)
    Toolbar toolbarInbox;
    @BindView(R.id.appBarInbox)
    AppBarLayout appBarInbox;
    BottomNavigationView navigation;
    @BindView(R.id.emptyView)
    TextView emptyView;
    private boolean needReset = false;
    private CoordinatorLayout cordInbox;
    /*------------------*/
    private NotificationAdapter notificationAdapter;
    private InboxViewModel inboxViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        View layout = inflater.inflate(R.layout.fragment_inbox, null);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inboxViewModel.getNotyList().observe(getViewLifecycleOwner(), list ->
        {
            notificationAdapter = new NotificationAdapter(getContext(), list, this);
            inboxRecycler.setAdapter(notificationAdapter);
            inboxSwipe.setRefreshing(false);
            if (notificationAdapter.getItemCount() > 0)
                emptyView.setVisibility(View.GONE);
        });


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigation = getActivity().findViewById(R.id.navigation);
        cordInbox = getView().findViewById(R.id.cordInbox);
        navigation.removeBadge(R.id.inbox_id);
        inboxViewModel = new ViewModelProvider(getActivity()).get(InboxViewModel.class);
        inboxSwipe.setOnRefreshListener(this);
        inboxSwipe.setRefreshing(true);
        new Handler().postDelayed(() -> {

            inboxSwipe.setRefreshing(false);

        }, 4000);
        setUpNotificationRecycler();


    }

    private void setUpNotificationRecycler() {
        inboxRecycler.setHasFixedSize(true);
        inboxRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        inboxRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        inboxRecycler.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        if (inboxViewModel.getNotyList().getValue().get(position).getApprove().equals(0)) {

        } else if (inboxViewModel.getNotyList().getValue().get(position).getApprove().equals(1)) {
            Intent intent = new Intent(getActivity(), Payment.class);
            intent.putExtra("index", inboxViewModel.getNotyList().getValue().get(position).getRequestId());
            startActivity(intent);
        } else if (inboxViewModel.getNotyList().getValue().get(position).getApprove().equals(2)) {
            Intent intent = new Intent(getActivity(), Bookings.class);
            startActivity(intent);

        }
    }
    /*
    pop menu
     navigationBtn = getView().findViewById(R.id.notyBtn);
       case R.id.notyBtn:
                PopupMenu popup = new PopupMenu(getActivity(), navigationBtn);
                //Inflating the Popup using xml file
                popup.getMenu().add("title1");
                popup.getMenu().add("title2");
                popup.getMenu().add("title3");
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> {
                    Toast.makeText(
                            getActivity(),
                            "You Clicked : " + item.getTitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return true;
                });

                popup.show(); //showing popup menu
                break;
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


    @Override
    public boolean onBackPressed() {
        if (needReset) {
            //action not popBackStack
            Log.i(TAG, "onBackPressed: clicked need reset");
            return true;
        } else {
            Log.i(TAG, "onBackPressed: clicked not need reset");
            return false;
        }
    }

    @Override
    public void onRefresh() {
        if (isInternetAvailable()) {
            showInternetStatus();
            inboxSwipe.setRefreshing(false);

        } else {
            new Handler().postDelayed(() -> {
                inboxSwipe.setRefreshing(false);
                if (notificationAdapter != null)
                    notificationAdapter.notifyDataSetChanged();
                else {
                    emptyView.setVisibility(View.VISIBLE);
                }


            }, 3000);
        }


    }
    private void showInternetStatus() {
        Snackbar snackbar = Snackbar
                .make(cordInbox, R.string.noInternet, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(navigation);
        snackbar.getView().setBackground(getResources().getDrawable(R.drawable.rounded_bottom));
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.blue));
        snackbar.setTextColor(getResources().getColor(R.color.white));
        snackbar.getView().setTextAlignment(Gravity.START);
        snackbar.show();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }
}
