package com.moghtrb.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moghtrb.Interfaces.IOnBackPressed;
import com.moghtrb.R;
import com.moghtrb.activities.RoomDetail;
import com.moghtrb.adapters.RoomAdapterMap;
import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.VerticalSpaceItemDecoration;
import com.moghtrb.viewmodels.MapViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Map extends Fragment implements
        View.OnClickListener, IOnBackPressed,
        RoomAdapterMap.RecyclerViewClickListener {
    @BindView(R.id.map)
    MapView mapview;
    @BindView(R.id.get_location)
    FloatingActionButton getLocation;
    @BindView(R.id.map_recycler)
    RecyclerView recyclerMap;
    @BindView(R.id.progress_loadplaces)
    ProgressBar progressLoadPlaces;
    @BindView(R.id.list_places_bottom_sheet)
    RelativeLayout listPlacesBottomSheet;
    @BindView(R.id.sideProgressBar)
    ProgressBar sideProgressBar;
    boolean needReset;
    Geocoder geocoder;
    @BindView(R.id.tryAgainBtnMap)
    MaterialButton tryAgainBtnMap;
    @BindView(R.id.locationTextMap)
    TextView locationTextMap;
    LocationManager manager;
    private GoogleMap googleMap;
    private RoomAdapterMap roomAdapter;
    private String TAG = "map_get_user_location";
    private BottomSheetBehavior bottomSheetPlaces;
    private LatLngBounds latLngBounds;
    private LatLng assiut_latlng;
    private AutocompleteSupportFragment autocompleteFragment;
    private BottomNavigationView bottom_navigation;
    private MapViewModel mapViewModel;
    private CoordinatorLayout bottomCord;
    private FusedLocationProviderClient fusedLocationClient;
    private int lastIndex = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        assiut_latlng = new LatLng(27.180134, 31.189283);
        latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));
        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, layout);


        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapViewModel.getSheetState().observe(getViewLifecycleOwner(), num ->
        {
            bottomSheetPlaces.setState(num);
            if (num != 4) {
                getData();
            }
        });
        mapViewModel.getListRooms().observe(getViewLifecycleOwner(), roomList ->
                {
                    Log.i(TAG, "onActivityCreated: room Observed");
                    if (roomAdapter == null) {
                        roomAdapter = new RoomAdapterMap(getContext(), roomList, this);
                        recyclerMap.setAdapter(roomAdapter);
                        Log.i(TAG, "onActivityCreated: room adapter==null");
                    } else if (lastIndex == 0) {
                        roomAdapter.notifyDataSetChanged();
                        Log.i(TAG, "onActivityCreated: lastiNdeex==0");
                    } else {
                        Log.i(TAG, "onActivityCreated: else");
                        roomAdapter.notifyItemRangeChanged(lastIndex, mapViewModel.getListRooms().getValue().size() - lastIndex);
                    }
                }
        );


    }

    @Override
    public void onResume() {
        super.onResume();
        setupMapSheet();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        progressLoadPlaces.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
        SetupAutoComplete();
        bottomSheetPlaces.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetPlaces.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottom_navigation.clearAnimation();
                    getLocation.clearAnimation();
                    bottomCord.setVisibility(View.VISIBLE);

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    needReset = true;
                    startAnimationBottomNavigation();
                    startAnimationFab();

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        initMap();

    }

    private void initVariables() {
        mapViewModel = new ViewModelProvider(getActivity()).get(MapViewModel.class);
        mapViewModel.setContext(getActivity());
        bottom_navigation = getActivity().findViewById(R.id.navigation);
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_map);
        bottomCord = getActivity().findViewById(R.id.cord_home);
        bottomSheetPlaces = BottomSheetBehavior.from(listPlacesBottomSheet);
    }

    private void initMap() {
        new Handler().postDelayed(() -> {
            if (isAdded()) {
                if (mapview != null) {
                    mapview.getMapAsync(googleMap1 -> {
                        googleMap = googleMap1;
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
                        CameraUpdateFactory.zoomTo(8);
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                        addMarkerCities(assiut_latlng, getString(R.string.moghtrbExist));
                    });
                    mapview.onCreate(null);
                    mapview.onResume();


                }
            }
        }, 1000);
    }

    private void showProgress() {
        progressLoadPlaces.setVisibility(View.VISIBLE);

    }

    private void hideProgress() {
        progressLoadPlaces.setVisibility(View.INVISIBLE);
    }

    private void startAnimationBottomNavigation() {
        int height = bottom_navigation.getHeight();
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, height);
        animation.setFillAfter(true);
        animation.setDuration(200);
        bottom_navigation.startAnimation(animation);
        bottomCord.setVisibility(View.INVISIBLE);

    }

    private void startAnimationFab() {
        int height = listPlacesBottomSheet.getHeight();
        TranslateAnimation animationFab = new TranslateAnimation(0, 0, 0, height - 20);
        animationFab.setFillAfter(true);
        animationFab.setDuration(200);
        getLocation.startAnimation(animationFab);
    }


    private void addMarkerRooms(LatLng latLng, String name) {
        if (latLng != null && googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position((latLng)).title(name).snippet("get best rooms"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void addMarkerCities(LatLng latLng, String name) {
        try {
            googleMap.addMarker(new MarkerOptions().position((latLng)).title(name).snippet(getString(R.string.app_name)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)      // Sets the center of the map to Mountain View
                    .zoom(7)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            Log.i(TAG, "addMarkerCities: " + e.getMessage());
        }

    }

    //from here
    private void setupMapSheet() {
        recyclerMap.setHasFixedSize(true);
        recyclerMap.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerMap.addItemDecoration(new VerticalSpaceItemDecoration(2));
        recyclerMap.setItemAnimator(new DefaultItemAnimator());
        recyclerMap.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mapViewModel.setIsScrolling(true);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                if (mapViewModel.getIsScrolling().getValue() && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !mapViewModel.getIsLastItemReached().getValue()) {
                    Log.i(TAG, "onScrolled: get lasted");
                    sideProgressBar.setVisibility(View.VISIBLE);
                    mapViewModel.setIsScrolling(false);
                    lastIndex = mapViewModel.getListRooms().getValue().size() - 1;
                    mapViewModel.downloadNextRooms();
                    new Handler().postDelayed(() -> {
                        sideProgressBar.setVisibility(View.GONE);
                        roomAdapter.notifyItemRangeChanged(lastIndex, mapViewModel.getListRooms().getValue().size() - lastIndex);

                    }, 3000);

                }
            }

        });


    }


    @Override
    public void recyclerViewListClicked(View v, int position) {

        Intent intent = new Intent(getActivity(), RoomDetail.class);
        intent.putExtra("roomId", mapViewModel.getListRooms().getValue().get(position).getRoomId());
        intent.putExtra("type", "foreigner");
        intent.putExtra("numGuests", 1);
        startActivity(intent);
    }
    //to here

    private void SetupAutoComplete() {
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                googleMap.clear();
                mapViewModel.getPlaceSelectedName().setValue(place.getName());
                addMarkerRooms(place.getLatLng(), place.getName());
                mapViewModel.setPlaceSelectedlatlng(new MyLatLong(place.getLatLng().latitude, place.getLatLng().longitude));

                mapViewModel.downloadRooms();
                getData();
                bottomSheetPlaces.setState(BottomSheetBehavior.STATE_EXPANDED);


            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(latLngBounds));
        autocompleteFragment.setCountry("EG");
    }

    @OnClick({R.id.get_location, R.id.btn_close_list_places, R.id.tryAgainBtnMap})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_location:
                checkPermission();
                mapViewModel.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
                getLocation();
                break;
            case R.id.tryAgainBtnMap:
                tryAgainBtnMap.setVisibility(View.INVISIBLE);
                mapViewModel.downloadRooms();
                getData();
                break;
            case R.id.btn_close_list_places:
                mapViewModel.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                tryAgainBtnMap.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS disabled,enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //nearby 500 meter write   //pagination
    private void getData() {
        showProgress();
        roomAdapter = null;
        lastIndex = 0;
        tryAgainBtnMap.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(() -> {
            if (roomAdapter != null) {
                roomAdapter.notifyDataSetChanged();
                if (roomAdapter.getItemCount() == 0)
                    tryAgainBtnMap.setVisibility(View.VISIBLE);
            }
            hideProgress();
        }, 5000);

    }

    @Override
    public boolean onBackPressed() {
        if (needReset) {
            //action not popBackStack
            mapViewModel.setSheetState(4);
            needReset = false;
            return true;
        } else return false;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                getLocation();
                break;

        }
    }

    private void getLocation() {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                mapViewModel.setPlaceSelectedlatlng(new MyLatLong(location.getLatitude(), location.getLongitude()));
                                locationTextMap.setText(String.format("%s :%s", getResources().getString(R.string.location), addresses.get(0).getAddressLine(0)));
                                addMarkerRooms(new LatLng(location.getLatitude(), location.getLongitude()), addresses.get(0).getAddressLine(0));
                                mapViewModel.setPlaceSelectedlatlng(new MyLatLong(location.getLatitude(), location.getLongitude()));
                                mapViewModel.downloadRooms();
                                Toast.makeText(getActivity(), "" + location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                locationTextMap.setText(getResources().getString(R.string.cannotFind));
                                e.printStackTrace();
                            }

                        } else {
                            Log.i(TAG, "onClick: " + "can not get location");
                        }

                    }).addOnFailureListener(getActivity(), e -> {
                Log.i(TAG, "getLocation: " + e.getCause());
                Log.i(TAG, "getLocation: " + e.getMessage());

            });
    }

}