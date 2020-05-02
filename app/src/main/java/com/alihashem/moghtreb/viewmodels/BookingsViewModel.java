package com.alihashem.moghtreb.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.moghtreb.models.BookingsModel;

import java.util.ArrayList;
import java.util.List;

public class BookingsViewModel extends ViewModel {
    MutableLiveData<List<BookingsModel>> listBookings;


    public BookingsViewModel() {
        listBookings = new MutableLiveData<>();
    }

    public MutableLiveData<List<BookingsModel>> getListBookings() {
        getBookings();
        return listBookings;
    }

    public void setListBookings(List<BookingsModel> listBookings) {
        this.listBookings.setValue(listBookings);
    }

    private void getBookings() {
        List<BookingsModel> list = new ArrayList<>();
        list.add(new BookingsModel("room1", "bvcksbdjvvbfs-bvskjbfjfdk", 300.0));
        list.add(new BookingsModel("room1", "bvcksbdjvvbfs-bvskjbfjfdk", 500.0));
        list.add(new BookingsModel("room2", "bvcksbdjvvbfs-bvskjbfjfdk", 600.0));
        listBookings.setValue(list);
    }
}
