package com.alihashem.moghtreb.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.activities.Bookings;
import com.alihashem.moghtreb.activities.FeedBack;
import com.alihashem.moghtreb.activities.IdentifyHost;
import com.alihashem.moghtreb.activities.Profile;
import com.alihashem.moghtreb.activities.ReferHost;
import com.alihashem.moghtreb.activities.Settings;
import com.alihashem.moghtreb.activities.signing;
import com.alihashem.moghtreb.adapters.ProfileAdapter;
import com.alihashem.moghtreb.viewmodels.MenuViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;

public class Menu extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {
    private final int PICK_IMAGE_REQUEST = 71;
    private ListView listView;
    private SimpleDraweeView userImageView;
    private String TAG = "Menu_activity";
    private FirebaseAuth mAuth;
    private Button edit_profile;
    private TextView tvUserName;
    private MenuViewModel menuViewModel;
    private ProfileAdapter customAdapter;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeVariables();
        listView.setOnItemClickListener(this);
        edit_profile.setOnClickListener(this);
        userImageView.setOnClickListener(this);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getUserData();
    }

    private void InitializeVariables() {
        listView = getView().findViewById(R.id.list_view_profile);
        mAuth = FirebaseAuth.getInstance();
        edit_profile = getView().findViewById(R.id.edit_profile);
        userImageView = getView().findViewById(R.id.image_profile);
        tvUserName = getView().findViewById(R.id.user_name);
        menuViewModel = new ViewModelProvider(getActivity()).get(MenuViewModel.class);
        customAdapter = new ProfileAdapter(getActivity(), R.layout.item_listview, new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.list_choices))));
        listView.setAdapter(customAdapter);
        tvUserName.setText(menuViewModel.getUsername().getValue());
        userImageView.setImageURI(menuViewModel.getUserImage().getValue());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    private void signOut() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Log out")
                .setMessage("Do you really want to Log Out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                    Intent intent = new Intent(getActivity(), signing.class);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseAuth.getInstance().getUid())
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    Log.i(TAG, "onComplete: UnSubscribed failed");
                                } else Log.i(TAG, "onComplete: UnSubscribed completed");

                            });
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(intent);
                    getActivity().finish();


                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
                break;
            case R.id.image_profile:
                chooseImage();

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("users_prof_pics/" + FirebaseAuth.getInstance().getUid());
            StorageTask<UploadTask.TaskSnapshot> uploaded = ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss();
                        menuViewModel.setImageReference(ref.toString());

                        setUserImageView(ref.toString());


                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, ref.getDownloadUrl().toString());

                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        // Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    });
        }
    }

    private void getUserData() {
        menuViewModel.getUsername().observe(getViewLifecycleOwner(), string ->

                tvUserName.setText(string)

        );
        menuViewModel.getUserImage().observe(getViewLifecycleOwner(), this::setUserImageView);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent = new Intent(getActivity(), Bookings.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), Settings.class);
                startActivity(intent2);
                break;

            case 2:
                Intent intent3 = new Intent(getActivity(), ReferHost.class);
                startActivity(intent3);
                break;
            case 3:
                Intent intentBar = new Intent(getActivity(), IdentifyHost.class);
                startActivity(intentBar);
                break;
            case 4:
                //---help
                break;
            case 5:
                Intent intentFeed = new Intent(getActivity(), FeedBack.class);
                startActivity(intentFeed);
                break;
            case 6:
                signOut();
                break;

        }


    }

    private void setUserImageView(String url) {
        if (TextUtils.isEmpty(url)) {
        } else {
            if (url.contains("https:")) {

                userImageView.setController(
                        Fresco.newDraweeControllerBuilder()
                                .setTapToRetryEnabled(true)
                                .setUri(url)
                                .build());

            } else {

                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {

                    userImageView.setController(
                            Fresco.newDraweeControllerBuilder()
                                    .setTapToRetryEnabled(true)
                                    .setUri(uri)
                                    .build());
                });
            }
        }

    }
}