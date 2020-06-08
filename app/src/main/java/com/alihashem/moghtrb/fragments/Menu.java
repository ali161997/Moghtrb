package com.alihashem.moghtrb.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.activities.Profile;
import com.alihashem.moghtrb.adapters.MenuAdapter;
import com.alihashem.moghtrb.models.VerticalSpaceItemDecoration;
import com.alihashem.moghtrb.viewmodels.MenuViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class Menu extends Fragment implements View.OnClickListener {
    private final int PICK_IMAGE_REQUEST = 71;
    RecyclerView recyclerView;
    private SimpleDraweeView userImageView;
    private String TAG = "Menu_activity";
    private Button edit_profile;
    private TextView tvUserName;
    private MenuViewModel menuViewModel;
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
        edit_profile.setOnClickListener(this);
        userImageView.setOnClickListener(this);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getUserData();
        getUserData();
    }

    private void InitializeVariables() {
        recyclerView = getView().findViewById(R.id.recyclerProfile);
        edit_profile = getView().findViewById(R.id.edit_profile);
        userImageView = getView().findViewById(R.id.image_profile);
        tvUserName = getView().findViewById(R.id.user_name);
        menuViewModel = new ViewModelProvider(getActivity()).get(MenuViewModel.class);
        MenuAdapter itemArrayAdapter = new MenuAdapter(getActivity(), new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.list_choices))));
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);
        tvUserName.setText(menuViewModel.getUsername().getValue());
        userImageView.setImageURI(menuViewModel.getUserImage().getValue());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            progressDialog.dismiss();
                            menuViewModel.setImageReference(uri.toString());
                            setUserImageView(uri.toString());
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

                        });


                    }).addOnCompleteListener(task -> {
            })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setUserImageView(String url) {
        Log.i(TAG, "setUserImageView: " + url);
        if (url != null) {
            userImageView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setTapToRetryEnabled(true)
                            .setUri(url)
                            .build());
        }
    }
}