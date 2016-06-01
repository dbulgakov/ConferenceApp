package guru.myconf.conferenceapp.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;

public class ConferenceInfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int _conferenceId;


    @Bind(R.id.speech_toolbar) Toolbar _toolbar;
    @Bind(R.id.swipe_refresh_layout_conference_info) SwipeRefreshLayout _swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_info);

        // ButterKnife binding
        ButterKnife.bind(this);

        // Getting caller cangeferenceId
        _conferenceId = getConferenceId();

        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _swipeRefreshLayout.setRefreshing(true);

        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private int getConferenceId() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getInt("conferenceId", -1);
    }

    @Override
    public void onRefresh() {

    }
}
