package com.alihashem.sokna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alihashem.sokna.Interfaces.IOnBackPressed;
import com.alihashem.sokna.R;
import com.alihashem.sokna.adapters.NotificationAdapter;
import com.alihashem.sokna.models.VerticalSpaceItemDecoration;
import com.alihashem.sokna.services.MessagingService;
import com.alihashem.sokna.viewmodels.InboxViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Inbox extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, IOnBackPressed, NotificationAdapter.RecyclerViewClickListener {
    private static final String TAG = "inbox";
    @BindView(R.id.inboxRecycler)
    RecyclerView inboxRecycler;
    @BindView(R.id.inboxSwipe)
    SwipeRefreshLayout inboxSwipe;
    MaterialButton btnExploreInbox;
    @BindView(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @BindView(R.id.toolbarInbox)
    Toolbar toolbarInbox;
    @BindView(R.id.appBarInbox)
    AppBarLayout appBarInbox;
    BottomNavigationView navigation;
    private boolean needReset = false;
    private MessagingService myMessagingService;
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
            notificationAdapter.notifyDataSetChanged();
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigation = getActivity().findViewById(R.id.navigation);
        navigation.removeBadge(R.id.inbox_id);
        inboxViewModel = ViewModelProviders.of(this).get(InboxViewModel.class);
        inboxSwipe.setOnRefreshListener(this);
        setUpNotificationRecycler();


    }

    private void setUpNotificationRecycler() {
        inboxRecycler.setHasFixedSize(true);
        inboxRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationAdapter = new NotificationAdapter(getContext(), inboxViewModel.getNotyList().getValue(), this);
        inboxRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        inboxRecycler.setAdapter(notificationAdapter);
        inboxRecycler.setItemAnimator(new DefaultItemAnimator());
        if (notificationAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            inboxSwipe.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            inboxSwipe.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        Intent intent = new Intent(getActivity(), Payment.class);
        startActivity(intent);
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
        new Handler().postDelayed(() -> {
            notificationAdapter.notifyDataSetChanged();
            inboxSwipe.setRefreshing(false);

        }, 3000);

    }
}
