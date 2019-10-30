package com.example.sokna.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sokna.R;
import com.example.sokna.adapters.ProfileAdapter;
import com.example.sokna.viewmodels.ProfileViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;

public class profile extends Fragment implements View.OnClickListener, ListView.OnItemClickListener {
    ListView listView;
    private SimpleDraweeView userImageView;
    private String TAG = "profile_activity";
    private FirebaseAuth mAuth;
    private TextView edit_profile;
    private FirebaseFirestore db;
    private TextView tvUserName;
    private ProfileViewModel profileViewModel;
    private ProfileAdapter customAdapter;
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        return inflater.inflate(R.layout.fragment_profile, null);
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
        db = FirebaseFirestore.getInstance();
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        customAdapter = new ProfileAdapter(getActivity(), R.layout.listview_profile_item, profileViewModel.getItems().getValue());
        listView.setAdapter(customAdapter);
        tvUserName.setText(profileViewModel.getUsername().getValue());
        userImageView.setImageURI(profileViewModel.getUserImage().getValue());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    private void signOut() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Log out")
                .setMessage("Do you really want to Log Out?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(getActivity(), signing.class);
                    startActivity(intent);


                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                Intent intent = new Intent(getActivity(), EditingProfile.class);
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
                        profileViewModel.setImageReference(ref.toString());

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getUserData() {
        profileViewModel.getUsername().observe(this, string ->

                tvUserName.setText(string)

        );
        profileViewModel.getUserImage().observe(this, this::setUserImageView);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent intent1 = new Intent(getActivity(), ListUserPlaces.class);
                startActivity(intent1);


                break;
            case 1:
                Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(getActivity(), refer_host.class);
                startActivity(intent3);
                break;
            case 3:
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                signOut();
                break;

        }


    }

    private void setUserImageView(String url) {
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