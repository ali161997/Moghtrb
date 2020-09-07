package com.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moghtrb.models.RoomDetailModel;
import com.moghtrb.models.paymentsModel;

import java.util.HashMap;
import java.util.Map;

public class ConfirmRequestViewModel extends ViewModel {
    private static final String TAG = "ConfirmRequestViewModel";
    MutableLiveData<String> roomId;
    private MutableLiveData<String> Date;
    private MutableLiveData<String> type;
    private MutableLiveData<Boolean> completed;
    private MutableLiveData<Integer> numGuests;
    private FirebaseFirestore db;
    private MutableLiveData<String> selectedFloor;
    private MutableLiveData<RoomDetailModel> roomDetail;
    private MutableLiveData<paymentsModel> payments;
    private MutableLiveData<Integer> numDays;
    private MutableLiveData<Boolean> codeVerified;
    private MutableLiveData<Double> valueCode;
    private MutableLiveData<Double> commission;
    private MutableLiveData<Double> min;

    public ConfirmRequestViewModel() {
        db = FirebaseFirestore.getInstance();
        payments = new MutableLiveData<>(new paymentsModel());
        roomId = new MutableLiveData<>();
        Date = new MutableLiveData<>();
        codeVerified = new MutableLiveData<>();
        selectedFloor = new MutableLiveData<>();
        type = new MutableLiveData<>();
        numDays = new MutableLiveData<>();
        completed = new MutableLiveData<>();
        valueCode = new MutableLiveData<>();
        numGuests = new MutableLiveData<>(1);
        commission = new MutableLiveData<>();
        min = new MutableLiveData<>();


    }

    public MutableLiveData<Double> getMin() {
        return min;
    }

    public void setMin(MutableLiveData<Double> min) {
        this.min = min;
    }

    public MutableLiveData<Double> getCommission() {
        return commission;
    }

    public void setCommission(MutableLiveData<Double> commission) {
        this.commission = commission;
    }

    public MutableLiveData<Double> getValueCode() {
        return valueCode;
    }

    public void setValueCode(MutableLiveData<Double> valueCode) {
        this.valueCode = valueCode;
    }

    public MutableLiveData<Boolean> getCodeVerified() {
        return codeVerified;
    }

    public void setCodeVerified(MutableLiveData<Boolean> codeVerified) {
        this.codeVerified = codeVerified;
    }

    public void verifyCode(String code) {
        db.collection("Codes").document("promoCodes")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot d = task.getResult();
                if (d.exists()) {
                    if (d.contains(code)) {
                        codeVerified.setValue(true);
                        valueCode.setValue(d.getDouble(code));
                        Log.i(TAG, "verifyCode: " + d.getDouble(code));
                    }
                } else {
                    codeVerified.setValue(false);
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public MutableLiveData<Integer> getNumDays() {
        return numDays;
    }

    public void setNumDays(MutableLiveData<Integer> numDays) {
        this.numDays = numDays;
    }

    public MutableLiveData<paymentsModel> getPayments() {
        return payments;
    }

    public void setPayments(MutableLiveData<paymentsModel> payments) {
        this.payments = payments;
    }

    public MutableLiveData<String> getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId.setValue(roomId);
        getRoomFromDatabase();
    }

    public MutableLiveData<String> getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date.setValue(date);
    }

    public MutableLiveData<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    public MutableLiveData<Boolean> getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed.setValue(completed);
    }

    public MutableLiveData<Integer> getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(Integer numGuests) {
        this.numGuests.setValue(numGuests);
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public MutableLiveData<String> getSelectedFloor() {
        return selectedFloor;
    }

    public void setSelectedFloor(String selectedFloor) {
        this.selectedFloor.setValue(selectedFloor);
    }

    public MutableLiveData<RoomDetailModel> getRoomDetail() {
        return roomDetail;
    }

    public void setRoomDetail(MutableLiveData<RoomDetailModel> roomDetail) {
        this.roomDetail = roomDetail;
    }

    public MutableLiveData<Boolean> sendRequest(String userId) {
        double total = numGuests.getValue() * (payments.getValue().getAdvance() + payments.getValue().getMonthPrice() + payments.getValue().getServices());
        Map<String, Object> request = new HashMap<>();
        Timestamp timestamp = Timestamp.now();
        request.put("roomId", roomId.getValue());
        request.put("userId", userId);
        request.put("timeRequest", timestamp);
        request.put("type", type.getValue());
        request.put("numGuests", numGuests.getValue());
        request.put("dates", Date.getValue());
        request.put("roomOrder", selectedFloor.getValue());
        request.put("total", total);
        request.put("commission", commission.getValue());
        request.put("min", min.getValue());
        request.put("month", payments.getValue().getMonthPrice());
        request.put("advance", payments.getValue().getAdvance());
        request.put("services", payments.getValue().getServices());


        db.collection("Requests").document()
                .set(request)
                .addOnSuccessListener(aVoid -> {
                    completed.setValue(true);
                })
                .addOnFailureListener(e -> completed.setValue(false));

        return completed;
    }

    private void getRoomFromDatabase() {
        DocumentReference docRef = db.collection("Rooms").document(roomId.getValue());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot d = task.getResult();
                Log.i(TAG, "getRoomFromDatabase: " + d.getData());
                if (d.exists()) {
                    paymentsModel model = new paymentsModel();
                    model.setMonthPrice(d.getDouble("price"));
                    model.setServices(d.getDouble("services"));
                    model.setAdvance(d.getDouble("advance"));
                    model.setDayCost(d.getDouble("dayCost"));
                    payments.setValue(model);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

}
