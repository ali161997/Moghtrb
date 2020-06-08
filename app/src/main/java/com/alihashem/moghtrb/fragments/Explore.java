package com.alihashem.moghtrb.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alihashem.moghtrb.Interfaces.IOnBackPressed;
import com.alihashem.moghtrb.R;
import com.alihashem.moghtrb.activities.Book;
import com.alihashem.moghtrb.adapters.RoomAdapter;
import com.alihashem.moghtrb.models.HostID;
import com.alihashem.moghtrb.models.StudentTime;
import com.alihashem.moghtrb.models.VerticalSpaceItemDecoration;
import com.alihashem.moghtrb.viewmodels.ExploreViewModel;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Explore extends Fragment implements RoomAdapter.RecyclerViewClickListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        Spinner.OnItemSelectedListener,
        AppBarLayout.OnOffsetChangedListener,
        IOnBackPressed {
    private static final String TAG = "explore";
    private static final int VERTICAL_ITEM_SPACE = 10;
    private static StudentTime studentTime = new StudentTime();
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
    @BindView(R.id.semester1)
    MaterialRadioButton semester1Box;
    @BindView(R.id.semester2)
    MaterialRadioButton semester2Box;
    @BindView(R.id.semester3)
    MaterialRadioButton semester3Box;
    @BindView(R.id.semester12)
    MaterialRadioButton semester12Box;
    @BindView(R.id.radioTime)
    RadioGroup radioTimeSheet;
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
    @BindView(R.id.checkInValue)
    TextView checkInDate;
    @BindView(R.id.checkOutValue)
    TextView checkOutDate;
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
    @BindView(R.id.femaleButton)
    MaterialRadioButton femaleBtn;
    @BindView(R.id.maleButton)
    MaterialRadioButton maleBtn;
    @BindView(R.id.textSearch)
    TextView textSearch;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    MaterialDatePicker.Builder datePickerBuilder;
    Calendar myCalender;
    CalendarConstraints calendarConstraints;
    //------------------------
    private ArrayAdapter<String> adapterCity;
    private ArrayAdapter<String> adapterFaculties;
    private ArrayAdapter<String> adapterRegions;
    private String lang = null;
    private BottomNavigationView bottomNavigation;
    private CoordinatorLayout cordHome;
    private CoordinatorLayout cordExplore;
    private BottomSheetBehavior filterSheet;
    private BottomSheetBehavior whereSheet;
    private BottomSheetBehavior whenSheet;
    private BottomSheetBehavior guestSheet;
    private boolean needReset;
    //When Bottom Sheet Fields
    private View studentView;
    private View foreignerView;
    //Recycler View Home Fields
    private RoomAdapter roomAdapter;
    //View Models
    private ExploreViewModel viewModelExplore;
    private Intent intent;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModelExplore.getDates().observe(getViewLifecycleOwner(), str ->
        {
            TimeBtn.setText(String.format("%s", str));
            if (spinner_when.getSelectedItemPosition() == 1) {

                String[] strings = str.split("-");
                checkInDate.setText(strings[0]);
                checkInDate.setText(strings[1]);
            }
        });
        viewModelExplore.getListRegions().observe(getViewLifecycleOwner(), list -> {
            List<String> list1 = new ArrayList<>();
            String key = String.format("%s-name", lang);
            for (int i = 0; list.size() > i; i++)
                list1.add(list.get(i).get(key));
            adapterRegions = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, list1);
            selectRegionSpin.setAdapter(adapterRegions);

        });
        viewModelExplore.getNumGuests().observe(getViewLifecycleOwner(), num -> {
            GuestsBtn.setText(String.format("%s %s", num.toString(), getResources().getString(R.string.guestsCount)));
        });
        viewModelExplore.getListCities().observe(getViewLifecycleOwner(), list -> {
            adapterCity = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, list);
            selectCitySpin.setAdapter(adapterCity);
        });
        viewModelExplore.getStateWhen().observe(getViewLifecycleOwner(), integer -> {

            whenSheet.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
            } else endAnimBottomNavigation();

        });
        viewModelExplore.getStateWhere().observe(getViewLifecycleOwner(), integer -> {

            whereSheet.setState(integer);
            if (integer != 4) StartAnimBottomNavigation();
            else endAnimBottomNavigation();

        });
        viewModelExplore.getStateGuests().observe(getViewLifecycleOwner(), integer -> {

            guestSheet.setState(integer);
            if (integer != 4) StartAnimBottomNavigation();
            else endAnimBottomNavigation();


        });
        viewModelExplore.getStateFilter().observe(getViewLifecycleOwner(), integer -> {

            filterSheet.setState(integer);
            if (integer != 4) StartAnimBottomNavigation();
            else endAnimBottomNavigation();
        });
        viewModelExplore.getStateSearch().observe(getViewLifecycleOwner(), this::setSearchState);
        viewModelExplore.getFaculties().observe(getViewLifecycleOwner(), list -> {

            adapterFaculties = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, list);
            SpinnerFaculty.setAdapter(adapterFaculties);


        });
        viewModelExplore.getRooms().observe(getViewLifecycleOwner(), roomList -> {
            roomAdapter = new RoomAdapter(getContext(), roomList, this);
            ExploreRecyclerHome.setAdapter(roomAdapter);
            if (roomAdapter.getItemCount() > 0)
                tryAgainBtn.setVisibility(View.GONE);

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
        viewModelExplore.getGender().observe(getViewLifecycleOwner(), str ->
        {
            if (str.equals("male"))
                maleBtn.setChecked(true);
            else if (str.equals("female"))
                femaleBtn.setChecked(true);

        });
        viewModelExplore.getPlaceData().observe(getViewLifecycleOwner(), str -> {
            WhereBtn.setText(str);
            if (viewModelExplore.getSelectedCityIndex().getValue() != null)
                selectCitySpin.setSelection(viewModelExplore.getSelectedCityIndex().getValue());
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
        viewModelExplore = new ViewModelProvider(getActivity()).get(ExploreViewModel.class);
        viewModelExplore.setContext(getApplicationContext());
        lang = getResources().getConfiguration().locale.getLanguage();
        intent = new Intent(getActivity(), Book.class);
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initVariables();
        homeRecycler();
        sheetsCallbacks();
        stopDragAppBar();
        initPicker();
        SwipeRefreshHome.setOnRefreshListener(this);
        RadioFilterSheet.setOnCheckedChangeListener(this);
        radioTimeSheet.setOnCheckedChangeListener(this);
        AppBar.addOnOffsetChangedListener(this);
        if (viewModelExplore.getMakeRefresh().getValue()) {
            onRefresh();
        }
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
        if (HostID.getInstance().getHostId() != null) {
            viewModelExplore.setHostId(HostID.getInstance().getHostId());
            HostID.getInstance().setHostId(null);
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

        intent.putExtra("roomId", viewModelExplore.getRooms().getValue().get(position).getRoomId());
        intent.putExtra("type", spinner_when.getSelectedItem().toString());
        try {
            intent.putExtra("dates", viewModelExplore.getDates().getValue());
        } catch (Exception e) {
            Log.i(TAG, "recyclerViewListClicked: " + e.getMessage());
        }

        intent.putExtra("numGuests", viewModelExplore.getNumGuests().getValue());
        startActivity(intent);
    }

    private void initPicker() {
        myCalender = Calendar.getInstance(TimeZone.getDefault());
        myCalender.clear();
        long toDay = MaterialDatePicker.todayInUtcMilliseconds();
        myCalender.setTimeInMillis(toDay);
        myCalender.set(Calendar.MONTH, Calendar.JANUARY);
        long january = myCalender.getTimeInMillis();
        myCalender.set(Calendar.MONTH, Calendar.DECEMBER);
        long december = myCalender.getTimeInMillis();
        CalendarConstraints.Builder constraintBulder = new CalendarConstraints.Builder();
        constraintBulder.setStart(january);
        constraintBulder.setEnd(december);
        constraintBulder.setValidator(DateValidatorPointForward.now());


//-------
        datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker();
        datePickerBuilder.setSelection(new Pair<>(toDay, toDay));
        datePickerBuilder.setCalendarConstraints(constraintBulder.build());
        datePickerBuilder.setTitleText("select Dates");
        materialDatePicker = datePickerBuilder.build();
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            checkInDate.setText(getMilli(materialDatePicker.getSelection().first));
            checkOutDate.setText(getMilli(materialDatePicker.getSelection().second));

        });

    }

    private String getMilli(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(date));
    }


    private void initVariables() {
        filterSheet = BottomSheetBehavior.from(filterBottomSheet);
        whenSheet = BottomSheetBehavior.from(anytimeBottomSheet);
        guestSheet = BottomSheetBehavior.from(numGuestsBottomSheet);
        whereSheet = BottomSheetBehavior.from(anyWhereBottomSheet);
        bottomNavigation = getActivity().findViewById(R.id.navigation);
        studentView = getView().findViewById(R.id.student_view);
        foreignerView = getView().findViewById(R.id.foreigner_view);
        cordHome = getActivity().findViewById(R.id.cord_home);
        cordExplore = getView().findViewById(R.id.cordy);
        AppBar.setActivated(false);

    }

    private void homeRecycler() {
        ExploreRecyclerHome.setHasFixedSize(true);
        ExploreRecyclerHome.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        int height = bottomNavigation.getHeight();
        //Animations-------------------------
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, height + 10);
        animation.setFillAfter(true);
        animation.setDuration(500);
        bottomNavigation.startAnimation(animation);
        cordHome.setVisibility(View.INVISIBLE);

    }

    private void endAnimBottomNavigation() {
        bottomNavigation.clearAnimation();
        cordHome.setVisibility(View.VISIBLE);

    }

    private void stopDragAppBar() {
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

    private void sheetsCallbacks() {
        whereSheet.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    whereSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        filterSheet.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    filterSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        whenSheet.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    whenSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        guestSheet.addBottomSheetCallback(new BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    guestSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        tryAgainBtn.setVisibility(View.GONE);
        if (isInternetAvailable()) {
            showInternetStatus();
            SwipeRefreshHome.setRefreshing(false);

        } else {
            SwipeRefreshHome.setRefreshing(true);
            new Handler().postDelayed(() -> {
                if (roomAdapter != null) {
                    roomAdapter.notifyDataSetChanged();
                    if (roomAdapter.getItemCount() > 0)
                        tryAgainBtn.setVisibility(View.GONE);
                } else tryAgainBtn.setVisibility(View.VISIBLE);
                SwipeRefreshHome.setRefreshing(false);
                viewModelExplore.getMakeRefresh().setValue(false);
            }, 4000);
        }


    }

    private void showInternetStatus() {
        Snackbar snackbar = Snackbar
                .make(cordExplore, R.string.noInternet, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(bottomNavigation);
        snackbar.getView().setBackground(getResources().getDrawable(R.drawable.rounded_bottom));
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.blue));
        snackbar.setTextColor(getResources().getColor(R.color.white));
        snackbar.getView().setTextAlignment(Gravity.START);
        snackbar.show();
    }

    @OnClick({R.id.CloseGuestsSheet, R.id.CloseTimeSheet, R.id.CloseWhereSheet, R.id.CloseFilterSheet,
            R.id.Where_btn, R.id.Guests_btn, R.id.Time_btn, R.id.Filter_btn,
            R.id.tryAgainBtn,
            R.id.increase, R.id.decrease, R.id.btn_save_anytime, R.id.btn_save_filter, R.id.btn_save_num_guests,
            R.id.btn_search, R.id.card_search, R.id.selectDates, R.id.btn_save_anyWhere})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tryAgainBtn:
                onRefresh();
                break;
            case R.id.CloseFilterSheet:
                viewModelExplore.setStateFilter(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseTimeSheet:
                viewModelExplore.setStateWhen(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.CloseWhereSheet:
                viewModelExplore.setStateWhere(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseGuestsSheet:
                viewModelExplore.setStateGuests(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.Where_btn:
                viewModelExplore.setStateWhere(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.Time_btn:
                viewModelExplore.setStateWhen(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Guests_btn:
                viewModelExplore.setStateGuests(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Filter_btn:
                viewModelExplore.setStateSearch(true);
                viewModelExplore.setStateFilter(BottomSheetBehavior.STATE_EXPANDED);
                viewModelExplore.getGender().observe(getViewLifecycleOwner(), str ->
                {
                    if (str.toLowerCase().equals("male"))
                        maleBtn.setChecked(true);
                    else if (str.toLowerCase().equals("female"))
                        femaleBtn.setChecked(true);

                });
                break;

            case R.id.increase:
                Integer num = viewModelExplore.getNumGuestCounter().getValue();
                if (num < 10)
                    viewModelExplore.setNumGuestCounter(++num);
                break;
            case R.id.decrease:
                int number = viewModelExplore.getNumGuestCounter().getValue();
                if (number > 1)
                    viewModelExplore.setNumGuestCounter(--number);
                break;
            case R.id.btn_save_anytime:
                dateConstraints();
                break;
            case R.id.btn_save_num_guests:
                viewModelExplore.setStateGuests(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setNumGuests(viewModelExplore.getNumGuestCounter().getValue());
                break;
            case R.id.btn_save_filter:
                viewModelExplore.setStartPrice(rangeSeekSheet.getSelectedMinValue().doubleValue());
                viewModelExplore.setEndPrice(rangeSeekSheet.getSelectedMaxValue().doubleValue());
                if (maleBtn.isChecked()) {
                    viewModelExplore.setGender("male");
                } else if (femaleBtn.isChecked()) {
                    viewModelExplore.setGender("female");
                }
                // if (smoking.isChecked())

                viewModelExplore.setStateFilter(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.execute();
                onRefresh();
                break;
            case R.id.btn_search:
                if (viewModelExplore.getStateSearch().getValue())
                    viewModelExplore.setStateSearch(false);
                else
                    viewModelExplore.setStateSearch(true);


                break;

            case R.id.card_search:
                if (!viewModelExplore.getStateSearch().getValue())
                    viewModelExplore.setStateSearch(true);
                break;
            case R.id.selectDates:
                materialDatePicker.show(getChildFragmentManager(), TAG);
                break;
            case R.id.btn_save_anyWhere:
                viewModelExplore.setStateWhere(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setSelectedCityIndex(viewModelExplore.getSelectionCityIndex().getValue());
                viewModelExplore.setSelectedRegionIndex(viewModelExplore.getSelectionRegionIndex().getValue());
                viewModelExplore.execute();
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

    private void dateConstraints() {
        if (spinner_when.getSelectedItemPosition() == 0) {
            switch (radioTimeSheet.getCheckedRadioButtonId()) {
                case R.id.semester1:
                    studentTime.setSemester1(true);
                    studentTime.setSemester2(false);
                    studentTime.setSemester3(false);
                    studentTime.setSemester12(false);
                    break;
                case R.id.semester2:
                    studentTime.setSemester1(false);
                    studentTime.setSemester2(true);
                    studentTime.setSemester3(false);
                    studentTime.setSemester12(false);
                    break;
                case R.id.semester3:
                    studentTime.setSemester3(true);
                    studentTime.setSemester1(false);
                    studentTime.setSemester2(false);
                    studentTime.setSemester12(false);
                    break;
                case R.id.semester12:
                    studentTime.setSemester12(true);
                    studentTime.setSemester1(false);
                    studentTime.setSemester2(false);
                    studentTime.setSemester3(false);
            }
            if (studentTime.toString().equals(""))
                Toast.makeText(getActivity(), "you need to check dates", Toast.LENGTH_LONG).show();
            else {
                viewModelExplore.setStateWhen(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setDates(studentTime.toString());
            }
        } else {
            if (checkInDate.getText().toString().equals("") || checkOutDate.getText().toString().equals(""))
                Toast.makeText(getActivity(), "you need to Complete dates", Toast.LENGTH_LONG).show();
            else {
                viewModelExplore.setDates(checkInDate.getText().toString() + "-" + checkOutDate.getText().toString());
                viewModelExplore.setStateWhen(BottomSheetBehavior.STATE_COLLAPSED);
            }

        }
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
            textSearch.setVisibility(View.GONE);
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.dark_blue));
            SearchCard.setCardElevation(0);
            SearchCard.setClickable(false);
            needReset = true;
        } else {
            lockAppBarClosed();
            textSearch.setVisibility(View.VISIBLE);
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
                foreignerView.setVisibility(View.INVISIBLE);
                studentView.setVisibility(View.VISIBLE);
            } else {
                foreignerView.setVisibility(View.VISIBLE);
                studentView.setVisibility(View.INVISIBLE);
            }
        } else if (parent.getId() == R.id.spinSelectCity) {
            viewModelExplore.setSelectionCityIndex(parent.getSelectedItemPosition());

        } else if (parent.getId() == R.id.spinSelectRegion)
            viewModelExplore.setSelectionRegionIndex(parent.getSelectedItemPosition());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (R.id.radio_sheet_bottom == group.getId()) {


        }
        if (R.id.radioTime == group.getId()) {
            switch (checkedId) {
                case R.id.semester1:
                    studentTime.setSemester1(true);

                    break;
                case R.id.semester2:
                    studentTime.setSemester2(true);
                    break;
                case R.id.semester3:
                    studentTime.setSemester3(true);
                    break;
                case R.id.semester12:
                    studentTime.setSemester12(true);
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


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //appBarLayout.setNestedScrollingEnabled(false);

        if (Math.abs(appBarLayout.getTotalScrollRange()) == -1)//if expanded
            viewModelExplore.setStateSearch(true);
        else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) { //if collapsed
            viewModelExplore.setStateSearch(false);
        }
    }
}