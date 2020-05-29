package com.alihashem.moghtrb.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.adapters.BookingsAdapter;
import com.alihashem.moghtrb.models.VerticalSpaceItemDecoration;
import com.alihashem.moghtrb.viewmodels.BookingsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class
Bookings extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.bookingsRecycler)
    RecyclerView bookingsRecycler;
    @BindView(R.id.swipeBookings)
    SwipeRefreshLayout swipeBookings;
    @BindView(R.id.toolbarBookings)
    Toolbar toolbarBookings;
    private BookingsAdapter bookingsAdapter;
    private BookingsViewModel bookingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        ButterKnife.bind(this);
        swipeBookings.setOnRefreshListener(this);
        bookingsViewModel = new ViewModelProvider(this).get(BookingsViewModel.class);
        bookingsViewModel.getListBookings().observe(this, list -> {
            bookingsAdapter = new BookingsAdapter(this, list);
            bookingsRecycler.setAdapter(bookingsAdapter);

        });
        bookingsRecycler.setHasFixedSize(true);
        bookingsRecycler.setLayoutManager(new LinearLayoutManager(this));
        bookingsRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        bookingsRecycler.setItemAnimator(new DefaultItemAnimator());
        onRefresh();
        toolbarBookings.setNavigationOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            swipeBookings.setRefreshing(false);
            if (bookingsAdapter != null)
                bookingsAdapter.notifyDataSetChanged();


        }, 4000);

    }
}
