package com.alihashem.moghtreb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.adapters.BookingsAdapter;
import com.alihashem.moghtreb.models.VerticalSpaceItemDecoration;
import com.alihashem.moghtreb.viewmodels.BookingsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Bookings extends AppCompatActivity implements BookingsAdapter.BtnGoClicked, SwipeRefreshLayout.OnRefreshListener {
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
        bookingsRecycler.setHasFixedSize(true);
        bookingsRecycler.setLayoutManager(new LinearLayoutManager(this));
        bookingsRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        bookingsRecycler.setItemAnimator(new DefaultItemAnimator());
        bookingsViewModel = new ViewModelProvider(this).get(BookingsViewModel.class);
        bookingsViewModel.getListBookings().observe(this, list -> {
            bookingsAdapter = new BookingsAdapter(this, list, this);
            bookingsRecycler.setAdapter(bookingsAdapter);
        });
        toolbarBookings.setNavigationOnClickListener(v -> onBackPressed());

    }

    @Override
    public void BtnGoOnClick(View v, int position) {
        Intent intent = new Intent(this, Book.class);
        intent.putExtra("roomId", bookingsViewModel.getListBookings().getValue().get(position).getRoomId());
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            swipeBookings.setRefreshing(false);
            bookingsAdapter.notifyDataSetChanged();
        }, 3000);

    }
}
