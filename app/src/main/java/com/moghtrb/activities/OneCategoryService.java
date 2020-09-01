package com.moghtrb.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.moghtrb.R;
import com.moghtrb.adapters.InfoAdapter;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.InfoViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneCategoryService extends AppCompatActivity {


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
    private InfoViewModel mViewModel;
    private InfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_category_service);
        ButterKnife.bind(this);
        toolbarInfo.setNavigationOnClickListener(view -> onBackPressed());
        setUpRecycler();
        mViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        mViewModel.getListInfo().observe(this, list -> {
            infoAdapter = new InfoAdapter(this, list);
            InfoRecycler.setAdapter(infoAdapter);

        });


    }

    private void setUpRecycler() {
        InfoRecycler.setHasFixedSize(true);
        InfoRecycler.setLayoutManager(new LinearLayoutManager(this));
        InfoRecycler.addItemDecoration(new VerticalSpaceItemDecoration(10));
        InfoRecycler.setItemAnimator(new DefaultItemAnimator());
    }

}