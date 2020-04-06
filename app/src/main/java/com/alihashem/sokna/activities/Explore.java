package com.alihashem.sokna.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alihashem.sokna.Interfaces.IOnBackPressed;
import com.alihashem.sokna.R;
import com.alihashem.sokna.Repository.Uploading_explore;
import com.alihashem.sokna.adapters.RoomAdapter;
import com.alihashem.sokna.models.DatePickerCheckIn;
import com.alihashem.sokna.models.DatePickerCheckOut;
import com.alihashem.sokna.models.HostID;
import com.alihashem.sokna.models.VerticalSpaceItemDecoration;
import com.alihashem.sokna.viewmodels.view_model_explore;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Explore extends Fragment implements RoomAdapter.RecyclerViewClickListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        CheckBox.OnCheckedChangeListener,
        Spinner.OnItemSelectedListener,
        IOnBackPressed {
    private static final String TAG = "explore";
    private static final int VERTICAL_ITEM_SPACE = 10;
    @BindView(R.id.CloseFilterSheet)
    Button CloseFilterSheet;
    @BindView(R.id.CloseGuestsSheet)
    Button CloseGuestsSheet;
    @BindView(R.id.CloseTimeSheet)
    Button CloseTimeSheet;
    @BindView(R.id.CloseWhereSheet)
    Button CloseWhereSheet;
    @BindView(R.id.Where_btn)
    MaterialButton WhereBtn;
    @BindView(R.id.Guests_btn)
    MaterialButton GuestsBtn;
    @BindView(R.id.Time_btn)
    MaterialButton TimeBtn;
    @BindView(R.id.Filter_btn)
    MaterialButton FilterBtn;
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
    @BindView(R.id.btn_save_num_guests)
    Button BtnGuestsSave;
    @BindView(R.id.selectall)
    CheckBox selectAllBox;
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
    @BindView(R.id.sheet_spin_faculty)
    Spinner SpinnerFaculty;
    @BindView(R.id.spinnerAnyTime)
    Spinner spinner_when;
    @BindView(R.id.start_price)
    TextView StartRangePrice;
    @BindView(R.id.end_price)
    TextView EndRangePrice;
    @BindView(R.id.toolbar)
    Toolbar ToolBarHome;
    @BindView(R.id.btn_search)
    MaterialButton SearchBtn;
    @BindView(R.id.card_search)
    MaterialCardView SearchCard;
    @BindView(R.id.smoking)
    CheckBox smoking;
    @BindView(R.id.num_guests_count)
    TextView GuestsCount;
    @BindView(R.id.range_seek_sheet)
    CrystalRangeSeekbar rangeSeekSheet;
    @BindView(R.id.radio_sheet_bottom)
    RadioGroup RadioFilterSheet;
    @BindView(R.id.btnCheckIn)
    Button checkIn;
    @BindView(R.id.btnCheckOut)
    Button checkOut;
    @BindView(R.id.spinSelectCity)
    Spinner selectCitySpin;
    @BindView(R.id.spinSelectRegion)
    Spinner selectRegionSpin;
    @BindView(R.id.btn_save_anyWhere)
    Button btnSaveAnyWhere;
    @BindView(R.id.tryAgainBtn)
    Button tryAgainBtn;
    @BindView(R.id.collapsingTool)
    CollapsingToolbarLayout collapsingTool;
    //------------------------
    private ArrayAdapter<String> adapterCity;
    private ArrayAdapter<String> adapterFaculties;
    private ArrayAdapter<String> adapterreg;
    private String lang = null;
    // @BindView(R.id.linearRecycler)
    // LinearLayout linearRecycler;
    //-------------------
    private BottomNavigationView bottom_navigation;
    private CoordinatorLayout bottomCord;
    private BottomSheetBehavior bottomSheet_filter;
    private BottomSheetBehavior bottomSheet_anyWhere;
    private BottomSheetBehavior bottomSheet_anytime;
    private BottomSheetBehavior bottomSheet_num_guests;
    private boolean needReset;
    //Animations-------------------------
    private TranslateAnimation animation;
    //When Bottom Sheet Fields
    private View student_view;
    private View foreigner_view;
    //Recycler View Home Fields
    private RoomAdapter roomAdapter;
    //View Models
    private String[] StudentTime = new String[]{"", "", "", ""};
    private view_model_explore viewModelExplore;
    private HashMap<String, String> searchText;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelExplore.getCheckInData().observe(getViewLifecycleOwner(), str ->
                checkIn.setText(str)
        );
        viewModelExplore.getCheckOutData().observe(getViewLifecycleOwner(), str ->
                checkOut.setText(str)
        );
        viewModelExplore.getListCities().observe(getViewLifecycleOwner(), list -> {

            adapterCity = new ArrayAdapter<>(getActivity(), R.layout.spinner_where_item, list);
            selectCitySpin.setAdapter(adapterCity);

        });
        viewModelExplore.getState_when().observe(getViewLifecycleOwner(), integer -> {

            bottomSheet_anytime.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
            } else EndAnimBottomNavigation();

        });
        viewModelExplore.getState_where().observe(getViewLifecycleOwner(), integer -> {

            bottomSheet_anyWhere.setState(integer);
            if (integer != 4) StartAnimBottomNavigation();
            else EndAnimBottomNavigation();

        });
        viewModelExplore.getState_num_guests().observe(getViewLifecycleOwner(), integer -> {

            bottomSheet_num_guests.setState(integer);
            if (integer != 4) StartAnimBottomNavigation();
            else EndAnimBottomNavigation();


        });
        viewModelExplore.getState_filter().observe(getViewLifecycleOwner(), integer -> {

            bottomSheet_filter.setState(integer);
            if (integer != 4) StartAnimBottomNavigation();
            else EndAnimBottomNavigation();
        });
        viewModelExplore.getState_search().observe(getViewLifecycleOwner(), this::setSearchState);
        viewModelExplore.getFaculties().observe(getViewLifecycleOwner(), list -> {

            adapterFaculties = new ArrayAdapter<>(getActivity(), R.layout.spinner_where_item, list);
            SpinnerFaculty.setAdapter(adapterFaculties);


        });
        viewModelExplore.getrooms().observe(getViewLifecycleOwner(), roomList -> {

            roomAdapter = new RoomAdapter(getContext(), roomList, this);
            ExploreRecyclerHome.setAdapter(roomAdapter);
            if (roomAdapter.getItemCount() > 0)
                tryAgainBtn.setVisibility(View.INVISIBLE);

        });
        viewModelExplore.getSearchText().observe(getViewLifecycleOwner(), text -> {

            if (text.containsKey("StudentTime")) {
                spinner_when.setSelection(0);
                TimeBtn.setText("");
                TimeBtn.setText(String.format("%s", text.get("StudentTime")));
                TimeBtn.setIcon(getResources().getDrawable(R.drawable.ic_checked_black_24dp));
                TimeBtn.setIconTint(getResources().getColorStateList(R.color.green));
            }
            if (text.containsKey("ForeignerTime")) {
                spinner_when.setSelection(1);
                TimeBtn.setText("");
                TimeBtn.setText(String.format("%s", text.get("ForeignerTime")));
                TimeBtn.setIcon(getResources().getDrawable(R.drawable.ic_checked_black_24dp));
                TimeBtn.setIconTint(getResources().getColorStateList(R.color.green));
                checkIn.setText(viewModelExplore.getCheckInData().getValue());
                checkOut.setText(viewModelExplore.getCheckOutData().getValue());
            }
            if (text.containsKey("NumGuests")) {
                GuestsBtn.setText(String.format("%s", text.get("NumGuests")));
                GuestsBtn.setIcon(getResources().getDrawable(R.drawable.ic_checked_black_24dp));
                GuestsBtn.setIconTint(getResources().getColorStateList(R.color.green));
            }
            if (text.containsKey("PlaceRequired")) {
                selectCitySpin.setSelection(viewModelExplore.getSelectedCityIndex().getValue());
                selectRegionSpin.setSelection(viewModelExplore.getSelectedRegionIndex().getValue());
                WhereBtn.setText(String.format("%s", text.get("PlaceRequired")));
                WhereBtn.setIcon(getResources().getDrawable(R.drawable.ic_checked_black_24dp));
                WhereBtn.setIconTint(getResources().getColorStateList(R.color.green));
            }
            if (text.containsKey("gender")) {
                if (text.get("gender").equals("male")) {
                    RadioFilterSheet.check(R.id.maleButton);
                } else if (text.get("gender").equals("female")) {
                    RadioFilterSheet.check(R.id.femaleButton);
                }
            }
            if (text.containsKey("noSmoking")) {
                if (text.get("noSmoking").equals("yes")) {
                    smoking.setChecked(true);
                } else smoking.setChecked(false);
            }
            if (text.containsKey("PreferredFaculty")) {
                SpinnerFaculty.setSelection(viewModelExplore.getFaculties().getValue().indexOf(text.get("PreferredFaculty")));
            }
        });
        viewModelExplore.getNumGuestCounter().observe(getViewLifecycleOwner(), num -> GuestsCount.setText(String.format("%s", num)));
        viewModelExplore.getStartPrice().observe(getViewLifecycleOwner(), str -> {

            rangeSeekSheet.setMinStartValue((float) (double) str);
            StartRangePrice.setText(String.valueOf(str));

        });
        viewModelExplore.getEndPrice().observe(getViewLifecycleOwner(), str -> {
            rangeSeekSheet.setMaxStartValue((float) (double) str);
            EndRangePrice.setText(String.valueOf(str));
        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {

            return null;
        }
        View layout = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, layout);
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
        viewModelExplore = ViewModelProviders.of(getActivity()).get(view_model_explore.class);
        viewModelExplore.setContext(getApplicationContext());
        lang = getResources().getConfiguration().locale.getLanguage();
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
        SheetsCallbacks();
        StopDragAppSearchBar();
        SwipeRefreshHome.setOnRefreshListener(this);
        RadioFilterSheet.setOnCheckedChangeListener(this);
        rangeSeekSheet.setMinValue((float) (double) viewModelExplore.getMinSeek().getValue());
        rangeSeekSheet.setMaxValue((float) (double) viewModelExplore.getMaxSeek().getValue());
        rangeSeekSheet.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            StartRangePrice.setText(String.valueOf(minValue));
            EndRangePrice.setText(String.valueOf(maxValue));
        });
        rangeSeekSheet.setOnRangeSeekbarFinalValueListener((minValue, maxValue) -> {
            viewModelExplore.setStartPrice(minValue.doubleValue());
            viewModelExplore.setEndPrice(maxValue.doubleValue());
        });
        AppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {

            if (Math.abs(appBarLayout.getTotalScrollRange()) == -1)
                viewModelExplore.setState_search(true);
            else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                viewModelExplore.setState_search(false);
            }
        });

        if (viewModelExplore.getmakeRefesh().getValue()) {
            onRefresh();
        }
        if (HostID.getInstance().getHostId() != null) {
            viewModelExplore.setHostId(HostID.getInstance().getHostId());
        }
        Uploading_explore uploading_explore = new Uploading_explore();
        //uploading_explore.settoUpload();
    }


    @Override
    public void recyclerViewListClicked(View v, int position) {

        Intent intent = new Intent(getActivity(), Booking.class);
        intent.putExtra("roomId", viewModelExplore.getrooms().getValue().get(position).getRoomId());
        startActivity(intent);
    }

    private void InitializeVariables() {
        bottomSheet_filter = BottomSheetBehavior.from(filterBottomSheet);
        bottomSheet_anytime = BottomSheetBehavior.from(anytimeBottomSheet);
        bottomSheet_num_guests = BottomSheetBehavior.from(numGuestsBottomSheet);
        bottomSheet_anyWhere = BottomSheetBehavior.from(anyWhereBottomSheet);
        bottom_navigation = getActivity().findViewById(R.id.navigation);
        student_view = getView().findViewById(R.id.student_view);
        foreigner_view = getView().findViewById(R.id.foreigner_view);
        bottomCord = getActivity().findViewById(R.id.cord_home);
        AppBar.setActivated(false);
        searchText = new HashMap<>();

    }

    private void HomeRecycler() {
        ExploreRecyclerHome.setHasFixedSize(true);
        ExploreRecyclerHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        roomAdapter = new RoomAdapter(getContext(), viewModelExplore.getrooms().getValue(), this);
        ExploreRecyclerHome.setAdapter(roomAdapter);
        ExploreRecyclerHome.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
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
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }

    private void StartAnimBottomNavigation() {
        int height = bottom_navigation.getHeight();
        animation = new TranslateAnimation(0, 0, 0, height + 10);
        animation.setFillAfter(true);
        animation.setDuration(500);
        bottom_navigation.startAnimation(animation);
        bottomCord.setVisibility(View.INVISIBLE);

    }

    private void EndAnimBottomNavigation() {
        bottomCord.setVisibility(View.VISIBLE);
        bottom_navigation.clearAnimation();

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
        bottomSheet_anyWhere.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    needReset = false;

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    needReset = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheet_filter.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_filter.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    needReset = false;

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    needReset = true;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheet_anytime.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_anytime.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    needReset = false;
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    needReset = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheet_num_guests.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    needReset = false;
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    needReset = true;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onRefresh() {
        tryAgainBtn.setVisibility(View.INVISIBLE);
        if (isInternetAvailable()) {
            showInternetStatus();
            SwipeRefreshHome.setRefreshing(false);

        } else {
            SwipeRefreshHome.setRefreshing(true);
            new Handler().postDelayed(() -> {
                roomAdapter.notifyDataSetChanged();
                viewModelExplore.setMakeRefresh(false);
                SwipeRefreshHome.setRefreshing(false);
                if (roomAdapter.getItemCount() == 0)
                    tryAgainBtn.setVisibility(View.VISIBLE);
                else
                    tryAgainBtn.setVisibility(View.INVISIBLE);

            }, 4000);
        }


    }

    private void showInternetStatus() {
        Snackbar snack = Snackbar.make(bottomCord,
                "No Internet Connection..!", Snackbar.LENGTH_LONG);
        snack.getView().setFitsSystemWindows(true);
        snack.getView().setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
        snack.setBackgroundTint(getResources().getColor(R.color.white));
        snack.setTextColor(getResources().getColor(R.color.white));
        snack.show();
    }

    @OnClick({R.id.CloseGuestsSheet, R.id.CloseTimeSheet, R.id.CloseWhereSheet, R.id.CloseFilterSheet,
            R.id.Where_btn, R.id.Guests_btn, R.id.Time_btn, R.id.Filter_btn,
            R.id.tryAgainBtn,
            R.id.increase, R.id.decrease, R.id.btn_save_anytime, R.id.btn_save_filter, R.id.btn_save_num_guests,
            R.id.btn_search, R.id.card_search, R.id.btnCheckIn, R.id.btnCheckOut, R.id.btn_save_anyWhere})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tryAgainBtn:
                onRefresh();
                break;
            case R.id.CloseFilterSheet:
                viewModelExplore.setState_filter(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseTimeSheet:
                viewModelExplore.setState_when(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.CloseWhereSheet:
                viewModelExplore.setState_where(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseGuestsSheet:
                viewModelExplore.setState_num_guests(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.Where_btn:
                viewModelExplore.setState_where(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.Time_btn:
                viewModelExplore.setState_when(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Guests_btn:
                viewModelExplore.setState_num_guests(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Filter_btn:
                viewModelExplore.setState_search(true);
                viewModelExplore.setState_filter(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.increase:
                Integer num = viewModelExplore.getNumGuestCounter().getValue();
                if (num <= 10)
                    viewModelExplore.setNumGuestCounter(++num);
                break;
            case R.id.decrease:
                int number = viewModelExplore.getNumGuestCounter().getValue();
                if (number > 1)
                    viewModelExplore.setNumGuestCounter(--number);
                break;
            case R.id.btn_save_anytime:
                if (spinner_when.getSelectedItemPosition() == 0) {
                    searchText.remove("ForeignerTime");
                    String r = "";
                    for (String s : StudentTime)
                        if (!s.equals("")) {
                            r = r.concat("-" + s);

                        }
                    searchText.put("StudentTime", r);
                } else {
                    searchText.remove("StudentTime");
                    viewModelExplore.setCheckInData(checkIn.getText().toString());
                    viewModelExplore.setCheckInData(checkOut.getText().toString());
                    searchText.put("ForeignerTime", checkIn.getText().toString() + "-" + checkOut.getText().toString());

                }
                viewModelExplore.setState_when(BottomSheetBehavior.STATE_COLLAPSED);
                setSearchText();

                break;
            case R.id.btn_save_num_guests:
                searchText.put("NumGuests", viewModelExplore.getNumGuestCounter().getValue().toString());
                viewModelExplore.setState_num_guests(BottomSheetBehavior.STATE_COLLAPSED);
                setSearchText();
                viewModelExplore.setNumGuests(viewModelExplore.getNumGuestCounter().getValue());
                break;
            case R.id.btn_save_filter:
                viewModelExplore.setStartPrice(rangeSeekSheet.getSelectedMinValue().doubleValue());
                viewModelExplore.setEndPrice(rangeSeekSheet.getSelectedMaxValue().doubleValue());
                if (RadioFilterSheet.getCheckedRadioButtonId() == R.id.maleButton) {
                    searchText.put("gender", "male");
                    viewModelExplore.setGender("male");
                } else {
                    searchText.put("gender", "female");
                    viewModelExplore.setGender("female");
                }
                if (smoking.isChecked())
                    searchText.put("NoSmoking", "yes");
                else searchText.put("noSmoking", "no");
                searchText.put("PreferredFaculty", SpinnerFaculty.getSelectedItem().toString());
                viewModelExplore.setState_filter(BottomSheetBehavior.STATE_COLLAPSED);
                setSearchText();
                onRefresh();
                roomAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_search:
                if (viewModelExplore.getState_search().getValue())
                    viewModelExplore.setState_search(false);
                else
                    viewModelExplore.setState_search(true);


                break;

            case R.id.card_search:
                if (!viewModelExplore.getState_search().getValue())
                    viewModelExplore.setState_search(true);
                break;
            case R.id.btnCheckIn:
                DialogFragment dialogin = new DatePickerCheckIn();
                dialogin.show(getFragmentManager(), "in");
                break;
            case R.id.btnCheckOut:
                DialogFragment dialogout = new DatePickerCheckOut();
                dialogout.show(getFragmentManager(), "out");

                break;
            case R.id.btn_save_anyWhere:
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_where(BottomSheetBehavior.STATE_COLLAPSED);
                searchText.put("PlaceRequired", selectCitySpin.getSelectedItem() + "/" + selectRegionSpin.getSelectedItem());
                viewModelExplore.setPlace(selectCitySpin.getSelectedItemPosition(), selectRegionSpin.getSelectedItemPosition());
                setSearchText();
                onRefresh();
                break;


            default:


        }

    }

    private void lockAppBarClosed() {
        AppBar.setExpanded(false);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) AppBar.getLayoutParams();
        lp.height = (int) dpToPixel(65);
    }

    private float dpToPixel(float dp) {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private void unlockAppBarOpen() {
        AppBar.setExpanded(true);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) AppBar.getLayoutParams();
        lp.height = collapsingTool.getLayoutParams().height;
    }

    private void setSearchState(boolean state) {
        if (state) {
            unlockAppBarOpen();
            SearchBtn.setIcon(getResources().getDrawable(R.drawable.ic_arrow_drop_down));
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.dark_blue));
            SearchCard.setCardElevation(0);
            SearchCard.setClickable(false);
            needReset = true;
        } else {

            lockAppBarClosed();
            SearchBtn.setIcon(getResources().getDrawable(R.drawable.ic_search));
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.blue));
            SearchCard.setCardElevation(5);
            needReset = false;
            SearchCard.setClickable(true);
        }

    }

    @OnItemSelected({R.id.sheet_spin_faculty, R.id.spinnerAnyTime, R.id.spinSelectRegion, R.id.spinSelectCity})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerAnyTime) {
            if (position == 0) {
                foreigner_view.setVisibility(View.INVISIBLE);
                student_view.setVisibility(View.VISIBLE);
            } else {
                foreigner_view.setVisibility(View.VISIBLE);
                student_view.setVisibility(View.INVISIBLE);
            }
        } else if (parent.getId() == R.id.spinSelectCity) {
            viewModelExplore.setSelectedCityIndex(parent.getSelectedItemPosition());
            viewModelExplore.getListRigions().observe(getViewLifecycleOwner(), list -> {
                List<String> list1 = new ArrayList<>();
                String key = String.format("%s-name", lang);
                for (int i = 0; list.size() > i; i++)
                    list1.add(list.get(i).get(key));
                adapterreg = new ArrayAdapter<>(getActivity(), R.layout.spinner_where_item, list1);
                selectRegionSpin.setAdapter(adapterreg);

            });

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (R.id.radio_sheet_bottom == group.getId()) {
            if (checkedId == R.id.maleButton)
                viewModelExplore.setGender("male");
            else if (checkedId == R.id.femaleButton)
                viewModelExplore.setGender("female");


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
            }


        } else if (buttonView.getId() == R.id.semester1) {
            if (isChecked)
                StudentTime[1] = "Semester1";
            else {
                StudentTime[1] = "";
                selectAllBox.setChecked(false);

            }
        } else if (buttonView.getId() == R.id.semester2) {
            if (isChecked)
                StudentTime[2] = "Semester2";
            else {
                StudentTime[2] = "";
                selectAllBox.setChecked(false);

            }
        } else if (buttonView.getId() == R.id.semester3) {
            if (isChecked)
                StudentTime[3] = "Semester3";
            else {
                StudentTime[3] = "";
                selectAllBox.setChecked(false);

            }
        }

    }

    @Override
    public boolean onBackPressed() {
        if (needReset) {
            //action not popBackStack
            viewModelExplore.resetStates();
            return true;
        } else {
            return false;
        }
    }

    private void setSearchText() {
        viewModelExplore.setSearchText(searchText);
    }

}