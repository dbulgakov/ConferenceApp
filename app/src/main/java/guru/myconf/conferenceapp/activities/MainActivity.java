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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.adapters.ConferenceAdapter;
import guru.myconf.conferenceapp.fragments.ConferenceListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConferenceAdapter.OnConferenceSelected{

    @Bind(R.id.toolbar) Toolbar _toolbar;
    @Bind(R.id.nav_view) NavigationView _navigationView;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;


    private TextView _userName;

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
        _navigationView.getMenu().getItem(0).setChecked(true);
        toggle.syncState();
        _navigationView.setNavigationItemSelectedListener(this);

        if (checkAuth()){
            startMainFragment();
            getUserName();
        }
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

        if (id == R.id.nav_conferences) {
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


    private void SaveConferenceId(int conferenceId){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("conferenceId", conferenceId);
        editor.apply();
    }


    @Override
    public void OnConferenceSelected(int conferenceId) {
        Intent intent = new Intent(MainActivity.this, ConferenceInfoActivity.class);
        SaveConferenceId(conferenceId);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ButterKnife.bind(this);
        updateUserName();
    }

    private String getUserName() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getString(getString(R.string.username_key), getString(R.string.username_error_value));
    }

    private void updateUserName(){
        View header = _navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.user_name);
        userName.setText(getUserName());
    }
}
