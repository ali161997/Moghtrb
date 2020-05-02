package com.alihashem.moghtreb.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alihashem.moghtreb.Interfaces.IOnBackPressed;
import com.alihashem.moghtreb.R;
import com.alihashem.moghtreb.activities.Book;
import com.alihashem.moghtreb.adapters.RoomAdapterMap;
import com.alihashem.moghtreb.models.MyLatLong;
import com.alihashem.moghtreb.models.VerticalSpaceItemDecoration;
import com.alihashem.moghtreb.viewmodels.MapViewModel;
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
    CardView listPlacesBottomSheet;
    boolean needReset;
    Geocoder geocoder;
    @BindView(R.id.tryAgainBtnMap)
    MaterialButton tryAgainBtnMap;
    @BindView(R.id.locationTextMap)
    TextView locationTextMap;
    private GoogleMap googleMap;
    private RoomAdapterMap roomAdapter;
    private String TAG = "map_get_user_location";
    private BottomSheetBehavior bottomSheet_list_places;
    private LatLngBounds latLngBounds;
    private LatLng assiut_latlng;
    private AutocompleteSupportFragment autocompleteFragment;
    private BottomNavigationView bottom_navigation;
    private MapViewModel mapViewModel;
    private CoordinatorLayout bottomCord;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        assiut_latlng = new LatLng(27.180134, 31.189283);
        latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));


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
            bottomSheet_list_places.setState(num);
            if (num != 4) {
                getData();
            }
        });
        mapViewModel.getListRooms().observe(getViewLifecycleOwner(), roomList ->
        {
            roomAdapter = new RoomAdapterMap(getContext(), roomList, this);
            recyclerMap.setAdapter(roomAdapter);
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            try {
                addMarkerCities(assiut_latlng, "service exists in assiut");
            } catch (Exception e) {
                Log.i(TAG, "onResume: addMarker Cities" + e.getMessage());
            }


        }, 4000);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariables();
        progressLoadPlaces.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.MULTIPLY);
        SetupAutoComplete();
        if (isAdded())
            bottomSheet_list_places.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        bottomSheet_list_places.setState(BottomSheetBehavior.STATE_EXPANDED);

                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottom_navigation.clearAnimation();
                        getLocation.clearAnimation();
                        bottomCord.setVisibility(View.VISIBLE);

                    } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        needReset = true;
                        start_anim_bottom_navigation();
                        start_anim_Fab();

                    }

                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });
        initMap();

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
                    });
                    mapview.onCreate(null);
                    mapview.onResume();


                }
            }
        }, 500);
    }

    private void show_progress() {
        progressLoadPlaces.setVisibility(View.VISIBLE);

    }

    private void hide_progress() {
        progressLoadPlaces.setVisibility(View.INVISIBLE);

    }

    private void start_anim_bottom_navigation() {
        int height = bottom_navigation.getHeight();
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, height);
        animation.setFillAfter(true);
        animation.setDuration(200);
        bottom_navigation.startAnimation(animation);
        bottomCord.setVisibility(View.INVISIBLE);

    }

    private void start_anim_Fab() {
        int height = listPlacesBottomSheet.getHeight();
        TranslateAnimation animationFab = new TranslateAnimation(0, 0, 0, height - 20);
        animationFab.setFillAfter(true);
        animationFab.setDuration(500);
        getLocation.startAnimation(animationFab);
    }


    private void addMarkerRooms(LatLng latLng, String name) {
        googleMap.addMarker(new MarkerOptions().position((latLng)).title(name).snippet("i hope we succes"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addMarkerCities(LatLng latLng, String name) {

        googleMap.addMarker(new MarkerOptions().position((latLng)).title(name).snippet("Sokna Hosting"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(7)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });


    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        Intent intent = new Intent(getActivity(), Book.class);
        intent.putExtra("roomId", mapViewModel.getListRooms().getValue().get(position).getRoomId());
        intent.putExtra("type", "foreigner");
        try {
            intent.putExtra("dates", "");
        } catch (Exception e) {
            Log.i(TAG, "recyclerViewListClicked: " + e.getMessage());
        }

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
                mapViewModel.getPlaceSelectedName().setValue(place.getName());
                addMarkerRooms(place.getLatLng(), place.getName());
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

    private void initVariables() {
        mapViewModel = new ViewModelProvider(getActivity()).get(MapViewModel.class);
        mapViewModel.setContext(getActivity());
        bottom_navigation = getActivity().findViewById(R.id.navigation);
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_map);
        bottomCord = getActivity().findViewById(R.id.cord_home);
        bottomSheet_list_places = BottomSheetBehavior.from(listPlacesBottomSheet);
    }

    @OnClick({R.id.get_location, R.id.btn_close_list_places, R.id.tryAgainBtnMap})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_location:
                mapViewModel.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
                tryAgainBtnMap.setVisibility(View.INVISIBLE);
                break;
            case R.id.tryAgainBtnMap:
                tryAgainBtnMap.setVisibility(View.INVISIBLE);
                getData();
                break;
            case R.id.btn_close_list_places:
                mapViewModel.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                tryAgainBtnMap.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void getData() {
        show_progress();
        setupMapSheet();
        getLocation();
        new Handler().postDelayed(() -> {
            roomAdapter.notifyDataSetChanged();
            if (roomAdapter.getItemCount() == 0) {
                hide_progress();
                tryAgainBtnMap.setVisibility(View.VISIBLE);
            } else {
                hide_progress();
                tryAgainBtnMap.setVisibility(View.INVISIBLE);
            }
        }, 5000);
    }

    @Override
    public boolean onBackPressed() {
        if (needReset) {
            //action not popBackStack
            mapViewModel.setSheetState(4);
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

    private void getLocation() {
        checkPermission();
        setupMapSheet();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            mapViewModel.setPlaceSelectedlatlng(new MyLatLong(location.getLatitude(), location.getLongitude()));
                            locationTextMap.setText(String.format("%s :%s", getResources().getString(R.string.location), addresses.get(0).getAddressLine(0)));
                            addMarkerRooms(new LatLng(location.getLatitude(), location.getLongitude()), addresses.get(0).getAddressLine(0));
                            Toast.makeText(getActivity(), "" + location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            locationTextMap.setText(getResources().getString(R.string.cannotFind));
                            e.printStackTrace();
                        }

                    } else {
                        Log.i(TAG, "onClick: " + "can not get location");
                        //show snackbar
                    }

                }).addOnFailureListener(getActivity(), e -> {
            Log.i(TAG, "getLocation: " + e.getCause());
            Log.i(TAG, "getLocation: " + e.getMessage());

        });
    }

}