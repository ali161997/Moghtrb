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
        prepareList();
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

    private void prepareList() {
        ServicesModel model1 = new ServicesModel("HomeMade Food", R.drawable.ic_cooking);
        ServicesModel model2 = new ServicesModel("Laundry", R.drawable.ic_electrical_appliance);
        ServicesModel model3 = new ServicesModel("Cleaners", R.drawable.ic_cleaners);
        ServicesModel model4 = new ServicesModel("Restaurants", R.drawable.ic_baseline_fastfood_24);
        ServicesModel model5 = new ServicesModel("Coffee shop", R.drawable.ic_coffee_breaks);
        ServicesModel model6 = new ServicesModel("Football Stade", R.drawable.ic_football_field);
        ServicesModel model7 = new ServicesModel("Taxi Drivers", R.drawable.ic_baseline_local_taxi_24);


        list.getValue().add(model1);
        list.getValue().add(model2);
        list.getValue().add(model3);
        list.getValue().add(model4);
        list.getValue().add(model5);
        list.getValue().add(model6);
        list.getValue().add(model7);


        Log.i(TAG, "prepareList: " + R.drawable.ic_cooking);
    }
}