package guru.myconf.conferenceapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.adapters.ConferenceAdapter;
import guru.myconf.conferenceapp.entities.Conference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar) Toolbar _toolbar;
    @Bind(R.id.nav_view) NavigationView _navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;
    @Bind(R.id.recycler_view) RecyclerView _recyclerView;

    private EventBus _bus = EventBus.getDefault();

    private ConferenceAdapter _conferenceAdapter;
    private RecyclerView.LayoutManager _layoutManager;

    private ArrayList<Conference> conflist= new ArrayList<Conference>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conflist.add(new Conference(1, "hello", "hello6", "http://static1.squarespace.com/static/569cafff841aba3e9730c245/569e1ae3df40f36b9acc04a3/56e2e3ed9f7266cdfff01e5c/1461336372285/conferences.jpg"));
        conflist.add(new Conference(2, "hello2", "hello7", "http://static1.squarespace.com/static/569cafff841aba3e9730c245/569e1ae3df40f36b9acc04a3/56e2e3ed9f7266cdfff01e5c/1461336372285/conferences.jpg"));
        conflist.add(new Conference(3, "hello3", "hello8", "http://static1.squarespace.com/static/569cafff841aba3e9730c245/569e1ae3df40f36b9acc04a3/56e2e3ed9f7266cdfff01e5c/1461336372285/conferences.jpg"));
        conflist.add(new Conference(4, "hello4", "hello9", "http://static1.squarespace.com/static/569cafff841aba3e9730c245/569e1ae3df40f36b9acc04a3/56e2e3ed9f7266cdfff01e5c/1461336372285/conferences.jpg"));

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
        _conferenceAdapter = new ConferenceAdapter(this, conflist);
        _recyclerView.setAdapter(_conferenceAdapter);

        // Checking auth
        checkAuth();
    }

    private void checkAuth(){
        if (!checkPreferencesManager()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
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
}
