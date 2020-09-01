package com.moghtrb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moghtrb.R;
import com.moghtrb.adapters.ServicesAdapter;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.ServicesViewModel;

public class Services extends Fragment {

    private static final String TAG = "Services";
    private ServicesViewModel mViewModel;
    private ServicesAdapter servicesAdapter;
    private RecyclerView recyclerView;

    public static Services newInstance() {
        return new Services();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.servicesRecycler);
        setUpRecycler();
        mViewModel = new ViewModelProvider(getActivity()).get(ServicesViewModel.class);
        mViewModel.getList().observe(getViewLifecycleOwner(), list -> {
            servicesAdapter = new ServicesAdapter(getActivity(), list);
            recyclerView.setAdapter(servicesAdapter);

        });
        // TODO: Use the ViewModel

    }

    private void setUpRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}