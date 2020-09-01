package com.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.moghtrb.models.BookingsModel;
import com.moghtrb.models.MyLatLong;

import java.util.ArrayList;
import java.util.List;

public class BookingsViewModel extends ViewModel {
    private static final String TAG = "BookingsViewModel";
    private MutableLiveData<List<BookingsModel>> listBookings;
    private List<BookingsModel> localListBookings = new ArrayList<>();


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
        listBookings.setValue(null);
        localListBookings.clear();
        listBookings.setValue(localListBookings);
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("Bookings").
                orderBy("time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
//                        List<DocumentSnapshot> listDocs = queryDocumentSnapshots.getDocuments();
//                        for (DocumentSnapshot d : listDocs) {
//
//
//                        }
//                        listBookings.setValue(localListBookings);
//
//                        Log.i(TAG, "execute: get Bookings");
                    }

                }).addOnFailureListener(e -> {
            Log.i(TAG, "get from bookings" + e);
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot d : task.getResult()) {
                    BookingsModel booking = new BookingsModel();
                    booking.setCashPayed(d.get("cash").toString());
                    booking.setTotal(d.get("total").toString());
                    booking.setFrom(d.get("from").toString());
                    booking.setTo(d.get("to").toString());
                    booking.setRoomId(d.get("roomId").toString());
                    booking.setNumGuests(d.get("numGuests").toString());
                    booking.setHostPhone(d.get("hostPhone").toString());
                    double lat = d.getDouble("lat");
                    double lon = d.getDouble("long");
                    booking.setLatLng(new MyLatLong(lat, lon));
                    listBookings.getValue().add(booking);
                }
            }

        });
    }
}
