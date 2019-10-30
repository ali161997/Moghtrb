package com.example.sokna.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sokna.R;
import com.example.sokna.Repository.Uploading_explore;
import com.example.sokna.adapters.RoomAdapter;
import com.example.sokna.models.Date_require;
import com.example.sokna.models.Filter;
import com.example.sokna.models.Place_require;
import com.example.sokna.models.Search;
import com.example.sokna.models.VerticalSpaceItemDecoration;
import com.example.sokna.viewmodels.view_model_explore;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.snackbar.Snackbar;
import com.yahoo.mobile.client.android.util.RangeSeekBar;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindArray;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.facebook.AccessTokenManager.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;


public class explore extends Fragment implements RoomAdapter.RecyclerViewClickListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        RangeSeekBar.OnRangeSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener,
        CheckBox.OnCheckedChangeListener,
        Spinner.OnItemSelectedListener {
    @BindView(R.id.CloseFilterSheet)
    Button CloseFilterSheet;
    @BindView(R.id.CloseGuestsSheet)
    Button CloseGuestsSheet;
    @BindView(R.id.CloseTimeSheet)
    Button CloseTimeSheet;
    @BindView(R.id.CloseWhereSheet)
    Button CloseWhereSheet;
    @BindDrawable(R.drawable.ic_close)
    Drawable close;
    @BindView(R.id.Where_btn)
    Button WhereBtn;
    @BindView(R.id.Guests_btn)
    Button GuestsBtn;
    @BindView(R.id.Time_btn)
    Button TimeBtn;
    @BindView(R.id.Filter_btn)
    Button FilterBtn;
    @BindView(R.id.filter_bottom_sheet)
    RelativeLayout filterBottomSheet;
    @BindView(R.id.num_guests_bottom_sheet)
    RelativeLayout numGuestsBottomSheet;
    @BindView(R.id.anytime_bottom_sheet)
    RelativeLayout anytimeBottomSheet;
    @BindView(R.id.anyWhere_bottom_sheet)
    RelativeLayout anyWhereBottomSheet;
    @BindView(R.id.decrease)
    Button DecreaseGuests;
    @BindView(R.id.increase)
    Button IncreaseGuests;
    @BindView(R.id.explore_recycler)
    RecyclerView ExploreRecyclerHome;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout SwipeRefreshHome;
    @BindView(R.id.app_bar)
    AppBarLayout AppBar;
    @BindView(R.id.btn_save_filter)
    Button BtnFilteSave;
    @BindView(R.id.btn_save_num_guests)
    Button BtnGuestsSave;
    @BindView(R.id.selectall)
    CheckBox selectallBox;
    @BindView(R.id.semester1)
    CheckBox semester1Box;
    @BindView(R.id.semester2)
    CheckBox semester2Box;
    @BindView(R.id.semester3)
    CheckBox semester3Box;
    @BindView(R.id.checkboxes_view)
    LinearLayout checkboxesView;
    @BindView(R.id.btn_save_anytime)
    Button BtnTimeSave;
    @BindView(R.id.checkin_picker)
    DatePicker checkinPicker;
    @BindView(R.id.checkout_picker)
    DatePicker checkoutPicker;
    @BindView(R.id.sheet_spin_faculty)
    Spinner SpinnerFaculty;
    @BindView(R.id.spinner)
    Spinner spinner_when;
    @BindArray(R.array.faculties)
    String[] faculties;
    @BindView(R.id.start_price)
    TextView StartRangePrice;
    @BindView(R.id.end_price)
    TextView EndRangePrice;
    @BindView(R.id.toolbar)
    Toolbar ToolBarHome;
    @BindView(R.id.btn_search)
    ImageButton SearchBtn;
    @BindView(R.id.card_search)
    CardView SearchCard;
    @BindView(R.id.smoking)
    CheckBox smoking;
    @BindView(R.id.num_guests_count)
    TextView GuestsCount;
    @BindView(R.id.range_seek_sheet)
    RangeSeekBar rangeSeekSheet;
    @BindView(R.id.cordy)
    CoordinatorLayout cordinator;
    @BindView(R.id.radio_sheet_bottom)
    RadioGroup RadioSFilterheet;
    //-------------------
    private BottomNavigationView bottom_navigation;
    private CoordinatorLayout homeCord;
    private CardView cardView_bottom;
    //------------------------
    private BottomSheetBehavior bottomSheet_filter;
    private BottomSheetBehavior bottomSheet_anyWhere;
    private BottomSheetBehavior bottomSheet_anytime;
    private BottomSheetBehavior bottomSheet_num_guests;
    //where Bottom Sheet Fields
    private AutocompleteSupportFragment autocompleteFragment;
    private LatLngBounds latLngBounds;
    //Animations-------------------------
    private TranslateAnimation animation;
    private TranslateAnimation toolbar_anim;
    //When Bottom Sheet Fields
    private View student_view;
    private View foreigner_view;
    //Recycler View Home Fields
    private RoomAdapter roomAdapter;
    private static final int VERTICAL_ITEM_SPACE = 10;
    //View Models
    private view_model_explore viewmodel_explore;

    //App Bar Search
    private static boolean expanded = false;

    //search fields
    private static Place_require place_require;
    private static int num_guests;
    private static Date_require date_require;
    private static Filter filter_object;
    private boolean shared;
    private String roomate_fac_name;
    private static MutableLiveData<Search> mut_search;
    private static Search search;
    private String time_requred = "";
    private boolean issmoking;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        View layout = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, layout);
        SetDrawablesToButtons();
        return layout;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        latLngBounds = new LatLngBounds(new LatLng(24.09082, 25.51965), new LatLng(31.5084, 34.89005));
        mut_search = new MutableLiveData<>();
        search = new Search();
        place_require = new Place_require();
        date_require = new Date_require();
        search.setFilter(filter_object);
        search.setPlace(place_require);
        search.setNum_guests(num_guests);
        search.setDate_require(date_require);
        viewmodel_explore = ViewModelProviders.of(this).get(view_model_explore.class);


    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializeVariables();
        HomeRecycler();
        set_behaviors_sheets();
        SheetsCallbacks();
        SaveStatesBottomSheets();
        StopDragAppSearchBar();
        SetupAutoCompleteFragment();
        SwipeRefreshHome.setOnRefreshListener(this);
        RadioSFilterheet.setOnCheckedChangeListener(this);
        rangeSeekSheet.setOnRangeSeekBarChangeListener(this);
        rangeSeekSheet.setRangeValues(viewmodel_explore.getMinMaxSeek().getValue().get(0),
                viewmodel_explore.getMinMaxSeek().getValue().get(1));
        StartRangePrice.setText(String.valueOf(viewmodel_explore.getMinMaxSeek().getValue().get(0)));
        EndRangePrice.setText(String.valueOf(viewmodel_explore.getMinMaxSeek().getValue().get(1)));

        if (viewmodel_explore.getmakeRefesh().equals(null)) {
            SwipeRefreshHome.setRefreshing(true);
            new Handler().postDelayed(() -> SwipeRefreshHome.setRefreshing(false), 6000);
            roomAdapter.notifyDataSetChanged();
            viewmodel_explore.setMakeRefresh(false);

        }
        if (!isInternetAvailable()) {
            showInternetStatus();

        }
        Uploading_explore uploading_explore = new Uploading_explore();
        uploading_explore.settoUpload();


    }


    private void SaveStatesBottomSheets() {
        viewmodel_explore.getState_when().observe(this, integer -> {

            bottomSheet_anytime.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }

        });
        viewmodel_explore.getState_where().observe(this, integer -> {

            bottomSheet_anyWhere.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }
        });
        viewmodel_explore.getState_num_guests().observe(this, integer -> {

            bottomSheet_num_guests.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }

        });
        viewmodel_explore.getState_filter().observe(this, integer -> {

            bottomSheet_filter.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }
        });
        viewmodel_explore.getState_search().observe(this, aBoolean -> {
            if (aBoolean) {
                AppBar.setExpanded(aBoolean);
                SearchBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down);
                SearchCard.setCardBackgroundColor(getResources().getColor(R.color.dark_blue));
                SearchCard.setCardElevation(0);

            }

        });


    }

    private static Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Intent intent = new Intent(getActivity(), booking.class);
        intent.putExtra("room_selected", viewmodel_explore.getrooms().getValue().get(position));
        startActivity(intent);
    }

    private void InitializeVariables() {
        bottom_navigation = getActivity().findViewById(R.id.navigation);
        homeCord = getView().findViewById(R.id.cordy);
        cardView_bottom = getActivity().findViewById(R.id.card_bottom);
        student_view = getView().findViewById(R.id.student_view);
        foreigner_view = getView().findViewById(R.id.foreigner_view);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_anywhere);
        AppBar.setLiftOnScroll(false);
    }

    private void set_behaviors_sheets() {
        bottomSheet_filter = BottomSheetBehavior.from(filterBottomSheet);
        bottomSheet_anytime = BottomSheetBehavior.from(anytimeBottomSheet);
        bottomSheet_num_guests = BottomSheetBehavior.from(numGuestsBottomSheet);
        bottomSheet_anyWhere = BottomSheetBehavior.from(anyWhereBottomSheet);

    }

    private void HomeRecycler() {
        ExploreRecyclerHome.setHasFixedSize(true);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ExploreRecyclerHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            ExploreRecyclerHome.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        }

        roomAdapter = new RoomAdapter(getContext(), viewmodel_explore.getrooms().getValue(), this);

        viewmodel_explore.getrooms().observe(this, roomList -> {
            roomAdapter.notifyDataSetChanged();

        });

        ExploreRecyclerHome.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        ExploreRecyclerHome.setAdapter(roomAdapter);
        ExploreRecyclerHome.setItemAnimator(new DefaultItemAnimator());
        ExploreRecyclerHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void StartAnimBottomNavigation() {
        int height = bottom_navigation.getHeight();
        animation = new TranslateAnimation(0, 0, 0, height);
        animation.setFillAfter(true);

        animation.setDuration(1000);
        bottom_navigation.startAnimation(animation);
        cardView_bottom.setVisibility(View.INVISIBLE);
    }

    private void StartAnimTopBar() {
        toolbar_anim = new TranslateAnimation(0, 0, 0, -ToolBarHome.getHeight());
        toolbar_anim.setFillAfter(true);
        toolbar_anim.setDuration(1000);
        AppBar.startAnimation(toolbar_anim);
    }

    private void EndAnimTopBar() {
        toolbar_anim = new TranslateAnimation(0, 0, 0, 0);
        toolbar_anim.setFillAfter(true);
        toolbar_anim.setDuration(1000);
        AppBar.startAnimation(toolbar_anim);
    }

    private void EndAnimBottomNavigation() {
        animation = new TranslateAnimation(0, 0, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        bottom_navigation.startAnimation(animation);
        cardView_bottom.setVisibility(View.VISIBLE);
    }

    private void StopDragAppSearchBar() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) AppBar.getLayoutParams();
        params.setBehavior(new AppBarLayout.Behavior());
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }


        });

    }

    private void SheetsCallbacks() {
        bottomSheet_filter.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_filter.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    EndAnimBottomNavigation();
                    EndAnimTopBar();
                    cardView_bottom.setVisibility(View.VISIBLE);

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        bottomSheet_anytime.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_anytime.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    EndAnimBottomNavigation();
                    EndAnimTopBar();
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        bottomSheet_num_guests.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    EndAnimBottomNavigation();
                    EndAnimTopBar();
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        bottomSheet_anyWhere.setBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    EndAnimBottomNavigation();
                    EndAnimTopBar();

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void SetupAutoCompleteFragment() {
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                search.getPlace().setLatitude(place.getLatLng().latitude);
                search.getPlace().setLongitude(place.getLatLng().longitude);
                search.getPlace().setPlace_requireName(place.getName());
                WhereBtn.setText(place.getName());
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_COLLAPSED);

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

    @Override
    public void onRefresh() {
        if (!isInternetAvailable()) {
            showInternetStatus();
            SwipeRefreshHome.setRefreshing(false);

        } else {
            roomAdapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> SwipeRefreshHome.setRefreshing(false), 2000);
        }


    }

    private void showInternetStatus() {
        Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.cord_home),
                "No Internet Connection..!", Snackbar.LENGTH_LONG);
        snack.getView().setBackgroundColor(getResources().getColor(R.color.blue));
        snack.getView().setFitsSystemWindows(true);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snack.getView().getLayoutParams();
        params.setMargins(1, bottom_navigation.getHeight(), 1, 2);
        snack.getView().setLayoutParams(params);
        snack.getView().setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);


        snack.show();
    }

    @OnClick({R.id.CloseGuestsSheet, R.id.CloseTimeSheet, R.id.CloseWhereSheet, R.id.CloseFilterSheet,
            R.id.Where_btn, R.id.Guests_btn, R.id.Time_btn, R.id.Filter_btn,
            R.id.increase, R.id.decrease, R.id.btn_save_anytime, R.id.btn_save_filter, R.id.btn_save_num_guests, R.id.btn_search, R.id.card_search})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CloseFilterSheet:
                bottomSheet_filter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_filter(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseTimeSheet:
                bottomSheet_anytime.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_when(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.CloseWhereSheet:
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_where(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseGuestsSheet:
                bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_num_guests(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.Where_btn:
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewmodel_explore.setState_where(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Time_btn:
                bottomSheet_anytime.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewmodel_explore.setState_when(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Guests_btn:
                bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewmodel_explore.setState_num_guests(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.Filter_btn:
                bottomSheet_filter.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewmodel_explore.setState_filter(BottomSheetBehavior.STATE_EXPANDED);
                FilterBtn.setBackgroundResource(R.drawable.ic_filter_clicked);
                break;

            case R.id.increase:
                int x1 = (Integer.parseInt(GuestsCount.getText().toString()));
                x1++;
                GuestsCount.setText(x1 + "");
                break;
            case R.id.decrease:
                int x12 = (Integer.parseInt(GuestsCount.getText().toString()));
                if (x12 == 1) {

                } else {
                    x12--;
                    GuestsCount.setText(Integer.toString(x12));
                }

                break;
            case R.id.btn_save_anytime:
                getDataTime();
                TimeBtn.setText(time_requred);
                bottomSheet_anytime.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_when(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_save_num_guests:
                num_guests = Integer.parseInt(GuestsCount.getText().toString());
                search.setNum_guests(num_guests);
                GuestsBtn.setText(GuestsCount.getText() + getResources().getString(R.string.guests));
                bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_num_guests(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_save_filter:
                bottomSheet_filter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewmodel_explore.setState_filter(BottomSheetBehavior.STATE_COLLAPSED);
                Filter f = new Filter();
                break;
            case R.id.btn_search:
                if (!expanded) {
                    AppSearchState(false);

                } else {

                    AppSearchState(true);
                }

                break;

            case R.id.card_search:
                if (!expanded) {
                    AppSearchState(false);
                }

                break;

            default:


        }

    }

    private void AppSearchState(Boolean Expnded) {
        if (Expnded) {
            AppBar.setExpanded(false);
            viewmodel_explore.setState_search(false);
            SearchBtn.setBackgroundResource(R.drawable.ic_search);
            expanded = false;
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.blue));
            SearchCard.setCardElevation(5);
        } else {
            AppBar.setExpanded(true);
            viewmodel_explore.setState_search(true);
            SearchBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down);
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.dark_blue));
            SearchCard.setCardElevation(0);
            expanded = true;

        }
    }

    private void getDataTime() {
        if (spinner_when.getSelectedItemPosition() == 0) {
            if (selectallBox.isChecked())
                time_requred += selectallBox.getTag().toString() + "/";
            else if (semester1Box.isChecked())
                time_requred += semester1Box.getTag().toString() + "/";
            else if (semester2Box.isChecked())
                time_requred += semester2Box.getTag().toString() + "/";
            else if (semester3Box.isChecked())
                time_requred += semester3Box.getTag().toString() + "/";

            search.getDate_require().setStudent_date(time_requred);
        } else {
            Date in = getDateFromDatePicker(checkinPicker);
            Date out = getDateFromDatePicker(checkoutPicker);
            date_require.setCheckIn(in);
            date_require.setCheckOut(out);
            search.getDate_require().setCheckIn(in);
            search.getDate_require().setCheckOut(out);
        }

    }

    @OnItemSelected({R.id.sheet_spin_faculty, R.id.spinner})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sheet_spin_faculty) {
            roomate_fac_name = faculties[position];
        } else if (parent.getId() == R.id.spinner) {
            if (position == 0) {
                foreigner_view.setVisibility(View.INVISIBLE);
                student_view.setVisibility(View.VISIBLE);
            } else {
                foreigner_view.setVisibility(View.VISIBLE);
                student_view.setVisibility(View.INVISIBLE);
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (RadioSFilterheet.getId() == group.getId()) {
            shared = checkedId != R.id.ifPrivate;
        }

    }

    @OnCheckedChanged({R.id.semester1, R.id.semester2, R.id.semester3, R.id.selectall})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.selectall) {
            if (isChecked) {
                boolean tr = true;
                semester1Box.setChecked(tr);
                semester2Box.setChecked(tr);
                semester3Box.setChecked(tr);
            } else {
                boolean fl = false;
                semester1Box.setChecked(fl);
                semester2Box.setChecked(fl);
                semester3Box.setChecked(fl);
                time_requred = "";
            }
        } else if (buttonView.getId() == R.id.smoking) {
            if (isChecked)
                issmoking = isChecked;
            else
                issmoking = !isChecked;

        }
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        StartRangePrice.setText(String.valueOf(Math.abs((double) minValue)));
        EndRangePrice.setText(String.valueOf(Math.abs((double) maxValue)));
    }

    private void SetDrawablesToButtons() {
        CloseFilterSheet.setBackground(close);
        CloseTimeSheet.setBackground(close);
        CloseWhereSheet.setBackground(close);
        CloseGuestsSheet.setBackground(close);
        WhereBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_anywhere), null, null, null);
        GuestsBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_guest), null, null, null);
        TimeBtn.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_calender), null, null, null);
        FilterBtn.setBackgroundResource(R.drawable.ic_filter);
        IncreaseGuests.setBackgroundResource(R.drawable.ic_increase);
        DecreaseGuests.setBackgroundResource(R.drawable.ic_decrease);
        SearchBtn.setBackgroundResource(R.drawable.ic_search);
    }


}
