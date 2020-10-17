package com.moghtrb.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.moghtrb.R;
import com.moghtrb.adapters.FilterMultiCategoryAdapter;
import com.moghtrb.adapters.InfoAdapter;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.MultiCategoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MultipleCategoryService extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    MultiCategoryViewModel viewModel;
    FilterMultiCategoryAdapter adapter;
    @BindView(R.id.toolBarMulti)
    Toolbar toolBarMulti;
    @BindView(R.id.appBarMultiCat)
    AppBarLayout appBarMultiCat;
    @BindView(R.id.categoriesRecycler)
    RecyclerView categoriesRecycler;
    @BindView(R.id.MainMultiCategoryRecycler)
    RecyclerView mainRecycler;
    @BindView(R.id.swipeMulti)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.cordMulti)
    CoordinatorLayout cordMulti;
    private InfoAdapter infoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_category_service);
        ButterKnife.bind(this);
        toolBarMulti.setNavigationOnClickListener(view -> onBackPressed());
        viewModel = new ViewModelProvider(this).get(MultiCategoryViewModel.class);
        viewModel.setCtx(new MutableLiveData<>(this));
        setupRecyclerView();
        setUpMainRecycler();
        swipeRefreshLayout.setOnRefreshListener(this);
        viewModel.downloadInfo();
        onRefresh();
        viewModel.getListCategories().observe(this, list -> {
            adapter = new FilterMultiCategoryAdapter(this, list);
            categoriesRecycler.setAdapter(adapter);
            adapter.getPosition().observe(this, index -> {
                viewModel.setIndexCat(index);
                infoAdapter = null;
                viewModel.downloadInfo();
                onRefresh();


            });
        });
        viewModel.getListInfo().observe(this, list -> {
            infoAdapter = new InfoAdapter(this, list);
            mainRecycler.setAdapter(infoAdapter);

        });


    }

    private void setupRecyclerView() {
        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.addItemDecoration(new VerticalSpaceItemDecoration(2));
        categoriesRecycler.setItemAnimator(new DefaultItemAnimator());
        categoriesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // mapViewModel.setIsScrolling(true);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });


    }

    private void setUpMainRecycler() {
        mainRecycler.setHasFixedSize(true);
        mainRecycler.setLayoutManager(new LinearLayoutManager(this));
        mainRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        mainRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }

    private void showInternetStatus() {
        Snackbar snackbar = Snackbar
                .make(cordMulti, R.string.noInternet, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        snackbar.getView().setBackground(getResources().getDrawable(R.drawable.rounded_bottom));
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.blue));
        snackbar.setTextColor(getResources().getColor(R.color.white));
        snackbar.getView().setTextAlignment(Gravity.START);
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        if (isInternetAvailable()) {
            showInternetStatus();
            swipeRefreshLayout.setRefreshing(false);

        } else {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(() -> {
                if (infoAdapter != null) {
                    infoAdapter.notifyDataSetChanged();
                    if (infoAdapter.getItemCount() == 0)
                        Toast.makeText(this, R.string.noItem, Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }, 3000);
        }
    }
}