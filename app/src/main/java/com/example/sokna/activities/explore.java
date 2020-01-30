package com.example.sokna.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.sokna.Interfaces.IOnBackPressed;
import com.example.sokna.R;
import com.example.sokna.Repository.Uploading_explore;
import com.example.sokna.adapters.RoomAdapter;
import com.example.sokna.models.VerticalSpaceItemDecoration;
import com.example.sokna.viewmodels.view_model_explore;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.facebook.FacebookSdk.getApplicationContext;
public class explore extends Fragment implements RoomAdapter.RecyclerViewClickListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener,
        CheckBox.OnCheckedChangeListener,
        Spinner.OnItemSelectedListener,
        IOnBackPressed {
    private static final String TAG = "explore";
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

    //-------------------
    private BottomNavigationView bottom_navigation;
    private CoordinatorLayout bottomCord;
    private CardView cardView_bottom;

    //------------------------
    private BottomSheetBehavior bottomSheet_filter;
    private BottomSheetBehavior bottomSheet_anyWhere;
    private BottomSheetBehavior bottomSheet_anytime;
    private BottomSheetBehavior bottomSheet_num_guests;
    private boolean needReset;
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
    private boolean shared;
    private String facultyPreferred;
    private boolean ifSmoking;
    private String[] StudentTime = new String[]{"", "", "", ""};
    private String foreignerCheckIn;
    private String foreignerCheckOut;
    private view_model_explore viewModelExplore;
    private Calendar checkInCalendar;
    private DatePickerDialog.OnDateSetListener dateCheckIn;
    private Calendar checkOutCalendar;
    private DatePickerDialog.OnDateSetListener dateCheckOut;
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
        viewModelExplore = ViewModelProviders.of(getActivity()).get(view_model_explore.class);


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
        AppSearchState();
        StopDragAppSearchBar();
        SetupAnWhere();
        SwipeRefreshHome.setOnRefreshListener(this);
        RadioFilterSheet.setOnCheckedChangeListener(this);
        StartRangePrice.setText(String.valueOf(viewModelExplore.getMinMaxSeek().getValue().get(0)));
        EndRangePrice.setText(String.valueOf(viewModelExplore.getMinMaxSeek().getValue().get(1)));
        rangeSeekSheet.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            StartRangePrice.setText(String.valueOf(minValue));
            EndRangePrice.setText(String.valueOf(maxValue));
        });
        SwipeRefreshHome.setRefreshing(true);
        new Handler().postDelayed(() -> {
            SwipeRefreshHome.setRefreshing(false);
            roomAdapter.notifyDataSetChanged();
        }, 6000);

        if (isInternetAvailable()) {
            showInternetStatus();

        }
        SetDateFromPicker();
        Uploading_explore uploading_explore = new Uploading_explore();
        uploading_explore.settoUpload();


    }

    private void SetDateFromPicker() {
        dateCheckIn = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            checkInCalendar.set(Calendar.YEAR, year);
            checkInCalendar.set(Calendar.MONTH, monthOfYear);
            checkInCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            foreignerCheckIn = String.format("%s/%s/%s", year, monthOfYear, dayOfMonth);
            checkIn.setText(foreignerCheckIn);
        };
        dateCheckOut = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            checkOutCalendar.set(Calendar.YEAR, year);
            checkOutCalendar.set(Calendar.MONTH, monthOfYear);
            checkOutCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            foreignerCheckOut = String.format("%s/%s/%s", year, monthOfYear, dayOfMonth);
            checkOut.setText(foreignerCheckOut);
        };
    }
    private void SaveStatesBottomSheets() {
        viewModelExplore.getState_when().observe(this, integer -> {

            bottomSheet_anytime.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }

        });
        viewModelExplore.getState_where().observe(this, integer -> {

            bottomSheet_anyWhere.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }
        });
        viewModelExplore.getState_num_guests().observe(this, integer -> {

            bottomSheet_num_guests.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }

        });
        viewModelExplore.getState_filter().observe(this, integer -> {

            bottomSheet_filter.setState(integer);
            if (integer != 4) {
                StartAnimBottomNavigation();
                StartAnimTopBar();

            }
        });
        viewModelExplore.getState_search().observe(this, this::setSearchState);

    }
    @Override
    public void recyclerViewListClicked(View v, int position) {

        Intent intent = new Intent(getActivity(), booking.class);
        intent.putExtra("room_selected", viewModelExplore.getrooms().getValue().get(position));
        startActivity(intent);
    }
    private void InitializeVariables() {
        bottom_navigation = getActivity().findViewById(R.id.navigation);
        bottomCord = getView().findViewById(R.id.cordy);
        cardView_bottom = getActivity().findViewById(R.id.card_bottom);
        student_view = getView().findViewById(R.id.student_view);
        foreigner_view = getView().findViewById(R.id.foreigner_view);
        bottomCord = getActivity().findViewById(R.id.cord_home);
        checkInCalendar = Calendar.getInstance();
        checkOutCalendar = Calendar.getInstance();
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
        ExploreRecyclerHome.setLayoutManager(new LinearLayoutManager(getActivity()));

        roomAdapter = new RoomAdapter(getContext(), viewModelExplore.getrooms().getValue(), this);

        viewModelExplore.getrooms().observe(this, roomList -> roomAdapter.notifyDataSetChanged());

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
        return activeNetwork == null ||
                !activeNetwork.isConnectedOrConnecting();
    }
    private void StartAnimBottomNavigation() {
        int height = bottom_navigation.getHeight();
        animation = new TranslateAnimation(0, 0, 0, height);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        bottom_navigation.startAnimation(animation);
        cardView_bottom.setVisibility(View.INVISIBLE);
        bottomCord.setVisibility(View.INVISIBLE);
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
        bottomCord.setVisibility(View.VISIBLE);
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
                    needReset = false;

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                    needReset = true;
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
                    needReset = false;
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                    needReset = true;
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
                    needReset = false;
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                    needReset = true;
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
                    needReset = false;

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    StartAnimBottomNavigation();
                    StartAnimTopBar();
                    needReset = true;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void SetupAnWhere() {
        ArrayAdapter<String> adapterCity = new ArrayAdapter<>(getActivity(), R.layout.spinner_where_item, viewModelExplore.getListCities().getValue());
        selectCitySpin.setAdapter(adapterCity);

    }
    @Override
    public void onRefresh() {
        if (isInternetAvailable()) {
            showInternetStatus();
            SwipeRefreshHome.setRefreshing(false);

        } else {
            roomAdapter.notifyDataSetChanged();
            new Handler().postDelayed(() -> SwipeRefreshHome.setRefreshing(false), 2000);
        }


    }
    private void showInternetStatus() {
        Snackbar snack = Snackbar.make(bottomCord,
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
            R.id.increase, R.id.decrease, R.id.btn_save_anytime, R.id.btn_save_filter, R.id.btn_save_num_guests,
            R.id.btn_search, R.id.card_search, R.id.btnCheckIn, R.id.btnCheckOut, R.id.btn_save_anyWhere})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CloseFilterSheet:
                bottomSheet_filter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_filter(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseTimeSheet:
                bottomSheet_anytime.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_when(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.CloseWhereSheet:
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_where(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.CloseGuestsSheet:
                bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_num_guests(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.Where_btn:
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewModelExplore.setState_where(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Time_btn:
                bottomSheet_anytime.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewModelExplore.setState_when(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.Guests_btn:
                bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewModelExplore.setState_num_guests(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.Filter_btn:
                bottomSheet_filter.setState(BottomSheetBehavior.STATE_EXPANDED);
                viewModelExplore.setState_filter(BottomSheetBehavior.STATE_EXPANDED);
                FilterBtn.setBackgroundResource(R.drawable.ic_filter_clicked);
                break;

            case R.id.increase:
                int x1 = (Integer.parseInt(GuestsCount.getText().toString()));
                x1++;
                GuestsCount.setText(String.format("%s", x1));
                break;
            case R.id.decrease:
                int number = (Integer.parseInt(GuestsCount.getText().toString()));
                if (number > 1) {
                    number--;
                    GuestsCount.setText(String.format("%s", number));
                }
                break;
            case R.id.btn_save_anytime:

                if (spinner_when.getSelectedItemPosition() == 0) {
                    viewModelExplore.setTime(StudentTime);
                    viewModelExplore.setTime(null, null);
                    TimeBtn.setText("");
                    for (String s : StudentTime)
                        if (!s.equals(""))
                            TimeBtn.append(s + "-");

                } else {
                    viewModelExplore.setTime(foreignerCheckIn, foreignerCheckOut);
                    viewModelExplore.setTime(null);
                    TimeBtn.setText("");
                    TimeBtn.setText(String.format("%s:%s", foreignerCheckIn, foreignerCheckOut));

                }
                bottomSheet_anytime.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_when(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_save_num_guests:
                int c = (Integer.parseInt(GuestsCount.getText().toString()));
                viewModelExplore.setNumGuest(c);
                GuestsBtn.setText(String.format("%s Guests", c));
                bottomSheet_num_guests.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_num_guests(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_save_filter:
                rangeSeekSheet.setOnRangeSeekbarFinalValueListener((minValue, maxValue) ->
                        viewModelExplore.setFilter((double) minValue, (double) maxValue, shared, ifSmoking, facultyPreferred)

                );
                bottomSheet_filter.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_filter(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_search:
                if (viewModelExplore.getState_search().getValue()) {
                    viewModelExplore.setState_search(false);

                } else {
                    viewModelExplore.setState_search(true);

                }

                break;

            case R.id.card_search:
                if (!viewModelExplore.getState_search().getValue()) {
                    viewModelExplore.setState_search(true);
                }
                break;
            case R.id.btnCheckIn:
                DatePickerDialog datePickerDialogin = new DatePickerDialog(getActivity(), dateCheckIn, checkInCalendar
                        .get(Calendar.YEAR), checkInCalendar.get(Calendar.MONTH),
                        checkInCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogin.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialogin.show();
                break;
            case R.id.btnCheckOut:
                DatePickerDialog datePickerDialogOut = new DatePickerDialog(getActivity(), dateCheckOut, checkOutCalendar
                        .get(Calendar.YEAR), checkOutCalendar.get(Calendar.MONTH),
                        checkOutCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogOut.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialogOut.show();
                break;
            case R.id.btn_save_anyWhere:
                bottomSheet_anyWhere.setState(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setState_where(BottomSheetBehavior.STATE_COLLAPSED);
                viewModelExplore.setPlace(selectCitySpin.getSelectedItem().toString(), selectRegionSpin.getSelectedItem().toString());
                WhereBtn.setText(String.format("%s / %s", selectCitySpin.getSelectedItem().toString(), selectRegionSpin.getSelectedItem().toString()));
                break;


            default:


        }

    }

    private void AppSearchState() {

    }

    private void setSearchState(boolean state) {
        if (state) {
            AppBar.setExpanded(state);
            SearchBtn.setBackgroundResource(R.drawable.ic_arrow_drop_down);
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.dark_blue));
            SearchCard.setCardElevation(0);
            needReset = true;
        } else {
            AppBar.setExpanded(state);
            SearchBtn.setBackgroundResource(R.drawable.ic_search);
            SearchCard.setCardBackgroundColor(getResources().getColor(R.color.blue));
            SearchCard.setCardElevation(5);
            needReset = false;
        }

    }

    @OnItemSelected({R.id.sheet_spin_faculty, R.id.spinnerAnyTime, R.id.spinSelectRegion, R.id.spinSelectCity})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sheet_spin_faculty) {
            facultyPreferred = faculties[position];

        } else if (parent.getId() == R.id.spinnerAnyTime) {
            if (position == 0) {
                foreigner_view.setVisibility(View.INVISIBLE);
                student_view.setVisibility(View.VISIBLE);
            } else {
                foreigner_view.setVisibility(View.VISIBLE);
                student_view.setVisibility(View.INVISIBLE);
            }
        } else if (parent.getId() == R.id.spinSelectCity) {
            if (parent.getSelectedItem().equals("Select City")) {
                btnSaveAnyWhere.setEnabled(false);
                btnSaveAnyWhere.setBackgroundColor(getResources().getColor(R.color.com_facebook_primary_button_disabled_text_color));
            } else {
                btnSaveAnyWhere.setEnabled(true);
                btnSaveAnyWhere.setBackgroundColor(getResources().getColor(R.color.dark_blue));
            }


            viewModelExplore.setSelectedCity(parent.getSelectedItem().toString());
            if (parent.getSelectedItem().equals("Select City")) {
                selectRegionSpin.setAdapter(null);
            } else {
                ArrayAdapter<String> adapterreg = new ArrayAdapter<>(getActivity(),
                        R.layout.spinner_where_item,
                        viewModelExplore.getListRigions().getValue());
                selectRegionSpin.setAdapter(adapterreg);

            }

        } else if (parent.getId() == R.id.spinSelectRegion) {
            if (parent.getSelectedItem().equals("select Region") || viewModelExplore.getSelectedCity().getValue().equals("Select City")) {
                btnSaveAnyWhere.setEnabled(false);
                btnSaveAnyWhere.setBackgroundColor(getResources().getColor(R.color.com_facebook_primary_button_disabled_text_color));

            } else {
                btnSaveAnyWhere.setEnabled(true);
                btnSaveAnyWhere.setBackgroundColor(getResources().getColor(R.color.dark_blue));
            }
        }


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (RadioFilterSheet.getId() == group.getId()) {
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
            }


        } else if (buttonView.getId() == R.id.smoking) {
            ifSmoking = !isChecked;


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
}