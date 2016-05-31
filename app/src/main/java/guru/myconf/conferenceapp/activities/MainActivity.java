package guru.myconf.conferenceapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.adapters.ConferenceAdapter;
import guru.myconf.conferenceapp.api.ApiUrlManager;
import guru.myconf.conferenceapp.api.GeneralApiManager;
import guru.myconf.conferenceapp.entities.Conference;
import guru.myconf.conferenceapp.events.ApiErrorEvent;
import guru.myconf.conferenceapp.events.ApiResultEvent;
import guru.myconf.conferenceapp.pojos.Response.ConferenceResponse;
import guru.myconf.conferenceapp.pojos.Response.ConferencesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar) Toolbar _toolbar;
    @Bind(R.id.nav_view) NavigationView _navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;
    @Bind(R.id.recycler_view) RecyclerView _recyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout _swipeRefreshLayout;

    private EventBus _bus = EventBus.getDefault();

    private ConferenceAdapter _conferenceAdapter;
    private RecyclerView.LayoutManager _layoutManager;

    private ArrayList<Conference> conflist= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife
        ButterKnife.bind(this);

        // Action bar init
        setSupportActionBar(_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toggle);
        _navigationView.getMenu().getItem(1).setChecked(true);
        toggle.syncState();
        _navigationView.setNavigationItemSelectedListener(this);


        // Card view init
        _layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);
        _conferenceAdapter = new ConferenceAdapter(this, new ArrayList<Conference>());
        _recyclerView.setAdapter(_conferenceAdapter);

        //EventBus
        _bus.register(this);

        //SwipeRefresh
        _swipeRefreshLayout.setOnRefreshListener(this);

        // Checking auth
        if (!checkAuth()){

            // Does not run in OnCreateMethod
            _swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            _swipeRefreshLayout.setRefreshing(true);
                                            getConferences();
                                        }
                                    }
            );
        }
    }


    public void getConferences() {
        // Turning on progressbar
        _swipeRefreshLayout.setRefreshing(true);

        // Removing old data
        _conferenceAdapter.removeItems();

        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(this);

        ApiUrlManager apiService = apiManager.getApiService();

        Call<ConferencesResponse> call = apiService.getConferences();

        call.enqueue(new Callback<ConferencesResponse>() {

            @Override
            public void onResponse(Call<ConferencesResponse> call, Response<ConferencesResponse> response) {
                try {
                    ArrayList<Conference> conferences = new ArrayList<>();

                    // Converting into more convenient classes
                    for (ConferenceResponse c : response.body().getResponseConferences()) {
                        Conference tmp = new Conference(c.getId(),
                                c.getTitle(), c.getDate(), getString(R.string.image_server_address) + c.getImageId());
                        conferences.add(tmp);
                    }
                    _bus.post(new ApiResultEvent(conferences));

                    if (response.code() == 500){
                        throw new Exception();
                    }
                }
                catch (Exception e){
                    _bus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<ConferencesResponse> call, Throwable t) {
                _bus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        _conferenceAdapter.addItems((ArrayList<Conference>) event.getResponse());
        _swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("API ERROR: ", "" + event.getError());
        _swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getBaseContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
    }


    private boolean checkAuth(){
        if (!checkPreferencesManager()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private boolean checkPreferencesManager(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.contains(getString(R.string.auth_token_key));
    }

    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_account) {
        } else if (id == R.id.nav_conferences) {

        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            if (settings.contains(getString(R.string.auth_token_key))) {
                SharedPreferences.Editor editor = settings.edit();
                editor.clear().apply();
            }
            checkAuth();

        } else if (id == R.id.nav_about) {

        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        _bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (!_bus.isRegistered(this))
            _bus.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (_bus.isRegistered(this))
            _bus.unregister(this);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        getConferences();
    }
}
