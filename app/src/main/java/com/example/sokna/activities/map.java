package com.example.sokna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sokna.Interfaces.IOnBackPressed;
import com.example.sokna.R;
import com.example.sokna.adapters.RoomAdapterMap;
import com.example.sokna.models.VerticalSpaceItemDecoration;
import com.example.sokna.viewmodels.MapViewModel;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;


public class map extends Fragment implements OnMapReadyCallback, View.OnClickListener, IOnBackPressed, RoomAdapterMap.RecyclerViewClickListener {
    @BindView(R.id.map)
    MapView mapview;
    @BindView(R.id.get_location)
    FloatingActionButton getLocation;
    @BindView(R.id.btn_close_list_places)
    Button btnCloseListPlaces;
    @BindView(R.id.map_recycler)
    RecyclerView recyclerMap;
    @BindView(R.id.progress_loadplaces)
    ProgressBar progressLoadPlaces;
    @BindView(R.id.list_places_bottom_sheet)
    CardView listPlacesBottomSheet;
    private GoogleMap googleMap;
    private RoomAdapterMap roomAdapter;



    private String TAG = "map get user location";
    private BottomSheetBehavior bottomSheet_list_places;
    private LatLngBounds latLngBounds;
    private LatLng assiut_latlng;
    private AutocompleteSupportFragment autocompleteFragment;
    private BottomNavigationView bottom_navigation;

    private MapViewModel mapViewModel;
    private CoordinatorLayout bottomCord;
    boolean needReset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assiut_latlng = new LatLng(27.180134, 31.189283);
        latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        if (container == null) {

            return null;
        }
        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, layout);


        return layout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitializeVariables();
        mapViewModel.getSheetState().observe(this, integer -> bottomSheet_list_places.setState(mapViewModel.getSheetState().getValue()));
        SetupAutoComplete();
        bottomSheet_list_places.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_list_places.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mapViewModel.setSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomCord.setVisibility(View.VISIBLE);
                    bottom_navigation.clearAnimation();
                    getLocation.clearAnimation();
                    needReset = false;

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mapViewModel.setSheetState(BottomSheetBehavior.STATE_EXPANDED);
                    show_progress();
                    setup_bottom_sheet();
                    start_anim_bottom_navigation();
                    start_anim_Fab();
                    bottomCord.setVisibility(View.INVISIBLE);
                    needReset = true;
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
        setup_bottom_sheet();
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
        animation.setDuration(1000);
        bottom_navigation.startAnimation(animation);

    }

    private void start_anim_Fab() {
        int height = listPlacesBottomSheet.getHeight() - 80;
        TranslateAnimation animationFab = new TranslateAnimation(0, 0, 0, -height);
        animationFab.setFillAfter(true);
        animationFab.setDuration(1000);
        getLocation.startAnimation(animationFab);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        CameraUpdateFactory.zoomTo(8);
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
//from here

    private void setup_bottom_sheet() {
        recyclerMap.setHasFixedSize(true);
        recyclerMap.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        roomAdapter = new RoomAdapterMap(getContext(), mapViewModel.getListRooms().getValue(), this);
        mapViewModel.getListRooms().observe(this, roomList ->
        {
            roomAdapter.notifyDataSetChanged();
            if (roomList.isEmpty()) {
                show_progress();
            } else hide_progress();
        });
        recyclerMap.addItemDecoration(new VerticalSpaceItemDecoration(2));
        recyclerMap.setAdapter(roomAdapter);
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

        Intent intent = new Intent(getActivity(), booking.class);
        intent.putExtra("room_selected", mapViewModel.getListRooms().getValue().get(position));
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
                mapViewModel.getPlaceSelectedlatlng().setValue(place.getLatLng());
                add_Marker(place.getLatLng(), place.getName());
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

    private void InitializeVariables() {
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.setContext(getApplicationContext());
        Places.initialize(getApplicationContext(), getString(R.string.api_key));



        bottom_navigation = getActivity().findViewById(R.id.navigation);
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_map);
        bottomCord = getActivity().findViewById(R.id.cord_home);
        bottomSheet_list_places = BottomSheetBehavior.from(listPlacesBottomSheet);
    }


    @OnClick({R.id.get_location, R.id.btn_close_list_places})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_location:
                bottomSheet_list_places.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btn_close_list_places:
                bottomSheet_list_places.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (needReset) {
            //action not popBackStack
            mapViewModel.setSheetState(4);
            return true;
        } else return false;
    }
}
