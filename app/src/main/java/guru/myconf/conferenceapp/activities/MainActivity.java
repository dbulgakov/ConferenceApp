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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar) Toolbar _toolbar;
    @Bind(R.id.nav_view) NavigationView _navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;
    private EventBus _bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(_toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toggle);

        toggle.syncState();
        _navigationView.setNavigationItemSelectedListener(this);

        checkAuth();
    }

    private void checkAuth(){
        if (!checkPreferencesManager()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {

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

        } else if (id == R.id.nav_about) {

        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
