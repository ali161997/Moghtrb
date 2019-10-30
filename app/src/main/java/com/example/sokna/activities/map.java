package com.example.sokna.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sokna.R;
import com.example.sokna.adapters.ListTextAdapter;
import com.example.sokna.viewmodels.MapViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.MapsInitializer;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.facebook.FacebookSdk.getApplicationContext;


public class map extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    @BindView(R.id.map)
    MapView mapview;
    @BindView(R.id.get_location)
    FloatingActionButton getLocation;
    @BindView(R.id.btn_close_list_places)
    Button btnCloseListPlaces;
    @BindView(R.id.btn_again_list_places)
    Button btnAgainListPlaces;
    @BindView(R.id.list_view_places)
    ListView listViewPlaces;
    @BindView(R.id.progress_loadplaces)
    ProgressBar progressLoadplaces;
    @BindView(R.id.list_places_bottom_sheet)
    CardView listPlacesBottomSheet;
    private GoogleMap googleMap;


    private String TAG = "map get user location";
    private List<Place.Field> placeFields;
    private PlacesClient placesClient;
    private ArrayList<String> listplacesNames = new ArrayList<>();
    private ArrayList<LatLng> listplacesLatlng = new ArrayList<>();
    private FindCurrentPlaceRequest request;
    private BottomSheetBehavior bottomSheet_list_places;
    private ListTextAdapter customAdapter;
    private LatLngBounds latLngBounds;
    private LatLng assiut_latlng;
    private AutocompleteSupportFragment autocompleteFragment;
    private BottomNavigationView bottom_navigation;
    private TranslateAnimation animation;
    private TranslateAnimation animation_fab;
    private CardView cardView_bottom;
    private MapViewModel mapViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assiut_latlng = new LatLng(27.180134, 31.189283);
        latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        if (container == null) {

            return null;
        }
        View layout = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, layout);
        btnAgainListPlaces.setBackgroundResource(R.drawable.ic_refresh_black_24dp);
        btnCloseListPlaces.setBackgroundResource(R.drawable.ic_close);


        return layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeVariables();
        get_my_location();
        mapViewModel.getListPlacesNames().setValue(listplacesNames);
        mapViewModel.getListPlaceslatlng().setValue(listplacesLatlng);
        // bottomSheet_list_places.setState(mapViewModel.getSheetState().getValue());


        SetupAutoComplete();


        bottomSheet_list_places.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_list_places.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottom_navigation.clearAnimation();
                        cardView_bottom.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        if (mapview != null) {
            mapview.getMapAsync(this);
            mapview.onCreate(null);
            mapview.onResume();

        }
    }

    private void show_progress() {
        progressLoadplaces.setVisibility(View.VISIBLE);

    }

    private void hide_progress() {
        progressLoadplaces.setVisibility(View.INVISIBLE);

    }

    private void start_anim_bottom_navigation() {
        int height = bottom_navigation.getHeight();
        animation = new TranslateAnimation(0, 0, 0, height);


        animation.setFillAfter(true);

        animation.setDuration(1800);
        bottom_navigation.startAnimation(animation);
        cardView_bottom.setVisibility(View.INVISIBLE);
    }

    private void start_anim_Fab() {
        int height = listPlacesBottomSheet.getHeight() - 80;
        animation_fab = new TranslateAnimation(0, 0, 0, -height);
        animation_fab.setFillAfter(true);
        animation_fab.setDuration(1000);
        getLocation.startAnimation(animation_fab);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());


    }

    private void add_Marker(LatLng latLng, String name) {
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

    private void get_my_location() {

        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        listplacesNames.add(placeLikelihood.getPlace().getName());
                        listplacesLatlng.add(placeLikelihood.getPlace().getLatLng());
                        Log.e(TAG, "Place found: " + placeLikelihood.getPlace().getName());

                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            Log.i(TAG, "get_my_location: access location denied");
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            // getLocationPermission();
        }

    }

    private void setup_listplaces_bottom_sheet() {

        if (mapViewModel.getListPlacesNames().getValue().isEmpty()
        ) {

            get_my_location();

        } else {
            hide_progress();
        }
        mapViewModel.getListPlacesNames().observe(this, strings -> {
            customAdapter = new ListTextAdapter(getActivity(), R.layout.listview_places_item, strings);
        });

        listViewPlaces.setAdapter(customAdapter);
        listViewPlaces.setOnItemClickListener((parent, view1, position, id) -> {
            mapViewModel.getPlaceSelectedName().setValue(mapViewModel.getListPlacesNames().getValue().get(position));
            mapViewModel.getPlaceSelectedlatlng().setValue(mapViewModel.getListPlaceslatlng().getValue().get(position));
            bottomSheet_list_places.setState(BottomSheetBehavior.STATE_COLLAPSED);
            add_Marker(mapViewModel.getPlaceSelectedlatlng().getValue(), mapViewModel.getPlaceSelectedName().getValue());
            cardView_bottom.setVisibility(View.VISIBLE);
            bottom_navigation.clearAnimation();
            getLocation.clearAnimation();

        });


    }

    private void SetupAutoComplete() {
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mapViewModel.getPlaceSelectedName().setValue(place.getName());
                mapViewModel.getPlaceSelectedlatlng().setValue(place.getLatLng());
                add_Marker(place.getLatLng(), place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(latLngBounds));
        autocompleteFragment.setCountry("EG");
    }

    private void InitializeVariables() {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        bottom_navigation = getActivity().findViewById(R.id.navigation);
        cardView_bottom = getActivity().findViewById(R.id.card_bottom);
        autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_map);

        // Initialize the SDK
        placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        request = FindCurrentPlaceRequest.newInstance(placeFields);
        placesClient = Places.createClient(getActivity());
        Places.initialize(getActivity(), getString(R.string.api_key));
        bottomSheet_list_places = BottomSheetBehavior.from(listPlacesBottomSheet);
    }


    @OnClick({R.id.get_location, R.id.btn_close_list_places, R.id.btn_again_list_places})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_location:
                mapViewModel.getSheetState().setValue(BottomSheetBehavior.STATE_EXPANDED);
                show_progress();
                setup_listplaces_bottom_sheet();
                start_anim_bottom_navigation();
                start_anim_Fab();
                bottomSheet_list_places.setState(BottomSheetBehavior.STATE_EXPANDED);
                cardView_bottom.setVisibility(View.INVISIBLE);

                break;
            case R.id.btn_close_list_places:
                bottomSheet_list_places.setState(BottomSheetBehavior.STATE_COLLAPSED);
                mapViewModel.getSheetState().setValue(BottomSheetBehavior.STATE_COLLAPSED);
                cardView_bottom.setVisibility(View.VISIBLE);
                bottom_navigation.clearAnimation();
                getLocation.clearAnimation();

                break;
            case R.id.btn_again_list_places:
                get_my_location();
                setup_listplaces_bottom_sheet();
                mapViewModel.getListPlacesNames().setValue(listplacesNames);
                mapViewModel.getListPlaceslatlng().setValue(listplacesLatlng);
                break;
        }
    }
}
