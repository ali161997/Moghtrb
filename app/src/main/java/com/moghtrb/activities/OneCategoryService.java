package com.moghtrb.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.moghtrb.R;
import com.moghtrb.adapters.InfoAdapter;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.InfoViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneCategoryService extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "OneCategoryService";
    @BindView(R.id.toolBarTitleInfo)
    TextView toolBarTitleInfo;
    @BindView(R.id.toolbarInfo)
    Toolbar toolbarInfo;
    @BindView(R.id.appBarInfo)
    AppBarLayout appBarInfo;
    @BindView(R.id.InfoRecycler)
    RecyclerView InfoRecycler;
    @BindView(R.id.swipeInfo)
    SwipeRefreshLayout swipeInfo;
    @BindView(R.id.cordInfo)
    CoordinatorLayout cordInfo;
    private InfoViewModel mViewModel;
    private InfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_category_service);
        ButterKnife.bind(this);
        mViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        mViewModel.setCtx(this);
        int index = getIntent().getIntExtra("index", 0);
        mViewModel.setTypeData(index);
        Log.i(TAG, "onCreate: " + index);
        mViewModel.getTypeData().observe(this, mt -> {
            toolBarTitleInfo.setText(mViewModel.getListServices().get(mt));
        });
        toolbarInfo.setNavigationOnClickListener(view -> onBackPressed());
        setUpRecycler();
        swipeInfo.setOnRefreshListener(this);
        onRefresh();
        mViewModel.setCtx(this);
        mViewModel.getListInfo().observe(this, list -> {
            infoAdapter = new InfoAdapter(this, list);
            InfoRecycler.setAdapter(infoAdapter);

        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (infoAdapter != null) {
            mViewModel.UpdateValues(infoAdapter.getUpdateMap());
        }
    }

    private void setUpRecycler() {
        InfoRecycler.setHasFixedSize(true);
        InfoRecycler.setLayoutManager(new LinearLayoutManager(this));
        InfoRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        InfoRecycler.setItemAnimator(new DefaultItemAnimator());
    }

    private boolean internetNotAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {
        swipeInfo.setRefreshing(true);
        if (internetNotAvailable()) {
            showInternetStatus();
            swipeInfo.setRefreshing(false);

        } else {
            new Handler().postDelayed(() ->
            {
                if (infoAdapter != null) {
                    infoAdapter.notifyDataSetChanged();
                }
                swipeInfo.setRefreshing(false);
            }, 3000);
        }

    }

    private void showInternetStatus() {
        Snackbar snackbar = Snackbar
                .make(cordInfo, R.string.noInternet, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        snackbar.getView().setBackground(getResources().getDrawable(R.drawable.rounded_bottom));
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.blue));
        snackbar.setTextColor(getResources().getColor(R.color.white));
        snackbar.getView().setTextAlignment(Gravity.START);
        snackbar.show();
    }
}