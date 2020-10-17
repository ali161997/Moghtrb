package com.moghtrb.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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
    private MutableLiveData<Double> commission;
    private MutableLiveData<Double> min;
    private MutableLiveData<Double> total;
    private MutableLiveData<Boolean> profileCompleted;

    public ConfirmRequestViewModel() {
        db = FirebaseFirestore.getInstance();
        payments = new MutableLiveData<>(new paymentsModel());
        roomId = new MutableLiveData<>();
        Date = new MutableLiveData<>();
        selectedFloor = new MutableLiveData<>();
        type = new MutableLiveData<>();
        numDays = new MutableLiveData<>();
        completed = new MutableLiveData<>();
        numGuests = new MutableLiveData<>(1);
        commission = new MutableLiveData<>();
        min = new MutableLiveData<>();
        total = new MutableLiveData<>();
        profileCompleted = new MutableLiveData<>();

    }

    public MutableLiveData<Boolean> getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(Boolean profileCompleted) {
        this.profileCompleted.setValue(profileCompleted);
    }

    public void isProfileCompleted() {

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid());
        docRef.get()
                .addOnSuccessListener(doc -> {

                    profileCompleted.setValue(doc.contains("completed") && doc.getBoolean("completed"));

                }).addOnFailureListener(e -> {
            Log.i("Profile View Model", "Error");
        });

    }

    public MutableLiveData<Double> getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total.setValue(total);
    }

    public MutableLiveData<Double> getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min.setValue(min);
    }

    public MutableLiveData<Double> getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission.setValue(commission);
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
        Map<String, Object> request = new HashMap<>();
        Timestamp timestamp = Timestamp.now();
        request.put("roomId", roomId.getValue());
        request.put("userId", userId);
        request.put("timeRequest", timestamp);
        request.put("type", type.getValue());
        request.put("numGuests", numGuests.getValue());
        if (type.getValue().equalsIgnoreCase("foreigner"))
            request.put("days", numDays.getValue());

        String[] arr = Date.getValue().split("-");
        request.put("from", arr[0]);
        request.put("to", arr[1]);
        request.put("roomOrder", selectedFloor.getValue());
        request.put("total", total.getValue());
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
