package com.moghtrb.fragments;


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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moghtrb.R;
import com.moghtrb.activities.Profile;
import com.moghtrb.adapters.MoreAdapter;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.MoreViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class More extends Fragment implements View.OnClickListener {
    private final int PICK_IMAGE_REQUEST = 71;
    private RecyclerView recyclerView;
    private SimpleDraweeView userImageView;
    private static final String TAG = "More";
    private Button edit_profile;
    private TextView tvUserName;
    private MoreViewModel menuViewModel;
    private StorageReference storageReference;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        return inflater.inflate(R.layout.fragment_more, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeVariables();
        edit_profile.setOnClickListener(this);
        userImageView.setOnClickListener(this);
        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        getUserData();
    }

    private void InitializeVariables() {
        recyclerView = getView().findViewById(R.id.recyclerProfile);
        edit_profile = getView().findViewById(R.id.edit_profile);
        userImageView = getView().findViewById(R.id.image_profile);
        tvUserName = getView().findViewById(R.id.user_name);
        menuViewModel = new ViewModelProvider(getActivity()).get(MoreViewModel.class);
        MoreAdapter itemArrayAdapter = new MoreAdapter(getActivity(), new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.list_choices))));
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
                edit_profile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
        Log.i(TAG, "onActivityResult: first");
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                uploadImage();
            } catch (Exception e) {
                Log.i(TAG, "onActivityResult: " + e.getMessage());
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

    public String addExt(String strs) {
        String ext = "_500x500";
        String sub = "?";
        strs = strs.replace(sub, (ext + sub));
        return strs;
    }

    private void uploadImage() {
        Log.i(TAG, "uploadImage: ");

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("users_prof_pics/" + FirebaseAuth.getInstance().getUid());
            ref.putFile(filePath)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                progressDialog.dismiss();
                                String re = addExt(uri.toString());
                                menuViewModel.setImageReference(re);
                                setUserImageView(re);
                                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

                            });
                        }
                    })
                    .addOnSuccessListener(taskSnapshot -> {


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
        menuViewModel.getUsername().observe(getViewLifecycleOwner(), string -> {

                    String[] separated = string.split(" ");
                    tvUserName.setText(separated[0].trim());
                }

        );
        menuViewModel.getUserImage().observe(getViewLifecycleOwner(), this::setUserImageView);
        menuViewModel.getProfileCompleted().observe(getViewLifecycleOwner(), bool -> {
            Log.i(TAG, "getUserData: completed" + bool);
            if (bool)
                edit_profile.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                edit_profile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.maps_sv_error_icon, 0);
        });

    }

    private void setUserImageView(String url) {
        Log.i(TAG, "setUserImageView: " + url);
        if (url != null && !url.equals("")) {
            userImageView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setUri(url)
                            .build());
        }
    }
}