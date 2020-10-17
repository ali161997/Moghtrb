package com.moghtrb.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moghtrb.R;
import com.moghtrb.models.ServicesModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ServicesViewModel extends ViewModel {
    MutableLiveData<List<ServicesModel>> list;
    MutableLiveData<Context> ctx;

    // TODO: Implement the ViewModel
    public ServicesViewModel() {
        list = new MutableLiveData<>(new ArrayList<>());
        ctx = new MutableLiveData<>();

    }

    public void setCtx(Context ctx) {
        this.ctx.setValue(ctx);
    }

    public MutableLiveData<List<ServicesModel>> getList() {
        Log.i(TAG, "getList: true");
        return list;
    }

    public void setList(MutableLiveData<List<ServicesModel>> list) {
        this.list = list;
    }

    public void prepareList() {
        list = new MutableLiveData<>(new ArrayList<>());
        ServicesModel homeMade = new ServicesModel(ctx.getValue().getResources().getString(R.string.homeMade), R.drawable.ic_cooking);
        ServicesModel laundry = new ServicesModel(ctx.getValue().getResources().getString(R.string.laundry), R.drawable.ic_electrical_appliance);
        ServicesModel cleaners = new ServicesModel(ctx.getValue().getResources().getString(R.string.cleaners), R.drawable.ic_cleaners);
        ServicesModel restaurants = new ServicesModel(ctx.getValue().getResources().getString(R.string.restaurants), R.drawable.ic_baseline_fastfood_24);
        ServicesModel stades = new ServicesModel(ctx.getValue().getResources().getString(R.string.football), R.drawable.ic_football_field);
        ServicesModel taxi = new ServicesModel(ctx.getValue().getResources().getString(R.string.taxi), R.drawable.ic_baseline_local_taxi_24);
        ServicesModel coffee = new ServicesModel(ctx.getValue().getResources().getString(R.string.coffee), R.drawable.ic_coffee_breaks);
        ServicesModel workSpace = new ServicesModel(ctx.getValue().getResources().getString(R.string.workSpace), R.drawable.ic_work);
        ServicesModel gym = new ServicesModel(ctx.getValue().getResources().getString(R.string.gym), R.drawable.ic_gym);

        list.getValue().add(homeMade);
        list.getValue().add(workSpace);
        list.getValue().add(restaurants);
        list.getValue().add(stades);
        list.getValue().add(gym);
        list.getValue().add(laundry);
        list.getValue().add(cleaners);
        list.getValue().add(taxi);
        list.getValue().add(coffee);

    }
}