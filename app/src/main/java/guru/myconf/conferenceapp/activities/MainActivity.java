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

import com.avast.android.dialogs.fragment.SimpleDialogFragment;

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
import guru.myconf.conferenceapp.fragments.ConferenceListFragment;
import guru.myconf.conferenceapp.pojos.Response.ConferenceResponse;
import guru.myconf.conferenceapp.pojos.Response.ConferencesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConferenceAdapter.OnConferenceSelected{

    @Bind(R.id.toolbar) Toolbar _toolbar;
    @Bind(R.id.nav_view) NavigationView _navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;

    static final int LOGIN_STATUS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife binding
        ButterKnife.bind(this);

        // Action bar init
        setSupportActionBar(_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toggle);
        _navigationView.getMenu().getItem(1).setChecked(true);
        toggle.syncState();
        _navigationView.setNavigationItemSelectedListener(this);


        /*
        if (checkAuth() && savedInstanceState == null){
            startMainFragment();
        }
        */
        Intent intent = new Intent(this, ConferenceInfoActivity.class);
        startActivity(intent);
    }


    private boolean checkAuth(){
        if (!checkPreferencesManager()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_STATUS);
            return false;
        }
        return true;
    }

    private void startMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, ConferenceListFragment.newInstance())
                .commitAllowingStateLoss();
    }


    private boolean checkPreferencesManager(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.contains(getString(R.string.auth_token_key));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_STATUS) {
            startMainFragment();
        }
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
            SimpleDialogFragment.createBuilder(this, getSupportFragmentManager()).setTitle(getString(R.string.about_app_fragment_title)).setMessage(getString(R.string.about_app_fragment_body_text)).setPositiveButtonText(getString(R.string.about_app_fragment_button_text)).useDarkTheme().show();
        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void OnConferenceSelected(int conferenceId) {
        Log.d("id", "" + conferenceId);
    }
}
