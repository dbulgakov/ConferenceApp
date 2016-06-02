package guru.myconf.conferenceapp.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.adapters.SpeechAdapter;
import guru.myconf.conferenceapp.api.ApiUrlManager;
import guru.myconf.conferenceapp.api.GeneralApiManager;
import guru.myconf.conferenceapp.entities.Speech;
import guru.myconf.conferenceapp.events.ApiErrorEvent;
import guru.myconf.conferenceapp.events.ApiResultEvent;
import guru.myconf.conferenceapp.pojos.Response.ConferenceInfo;
import guru.myconf.conferenceapp.pojos.Response.ConferenceInfoResponse;
import guru.myconf.conferenceapp.pojos.Response.SpeechResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConferenceInfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int _conferenceId;
    private SpeechAdapter _speechAdapter;
    private EventBus _bus = EventBus.getDefault();

    @Bind(R.id.speech_toolbar) Toolbar _toolbar;
    @Bind(R.id.recycler_view_speeches) RecyclerView _recycleView;
    @Bind(R.id.confenrece_name) TextView _conferenceTitle;
    @Bind(R.id.confenrece_description) TextView _conferenceDescription;
    @Bind(R.id.confenrece_address) TextView _conferenceAddress;
    @Bind(R.id.conference_date) TextView _conferenceDate;
    @Bind(R.id.conference_image) ImageView _conferenceImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_info);

        // ButterKnife binding
        ButterKnife.bind(this);

        // Bus Registration
        _bus.register(this);

        // Getting caller cangeferenceId
        _conferenceId = getConferenceId();

        // Toolbar init
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting onClickListener to enable back button
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_bus.isRegistered(this))
                    _bus.unregister(this);
                finish();
            }
        });

        // Initializing Adapter
        _speechAdapter = new SpeechAdapter(this, new ArrayList<Speech>());

        // Initializing RC
        _recycleView.setLayoutManager(new LinearLayoutManager(this));
        _recycleView.setItemAnimator(new DefaultItemAnimator());
        _recycleView.setAdapter(_speechAdapter);

        getConferenceInfo(_conferenceId);
    }

    private void getConferenceInfo(int conferenceId) {
        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(this);
        ApiUrlManager apiService = apiManager.getApiService();

        // Removing old data
        _speechAdapter.removeItems();

        Call<ConferenceInfoResponse> call = apiService.getConferenceInfo(_conferenceId);

        call.enqueue(new Callback<ConferenceInfoResponse>() {

            @Override
            public void onResponse(Call<ConferenceInfoResponse> call, Response<ConferenceInfoResponse> response) {
                try {
                    ArrayList<Speech> speeches = new ArrayList<>();
                    ConferenceInfo conferenceInfo = response.body().getConferenceInfo();

                    // Converting into more convenient classes
                    for (SpeechResponse c : conferenceInfo.getSpeeches()) {
                        Speech tmp = new Speech(c.getId(), c.getTitle(), c.getDate(), c.getAddress());
                        speeches.add(tmp);
                    }

                    setData(conferenceInfo.getTitle(), conferenceInfo.getDescription(), conferenceInfo.getDate(),
                            conferenceInfo.getAddress(), conferenceInfo.getMainImageUrl());

                    _bus.post(new ApiResultEvent(speeches));

                    if (response.code() == 500){
                        throw new Exception();
                    }
                }
                catch (Exception e){
                    _bus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<ConferenceInfoResponse> call, Throwable t) {
                Log.d("response1", t.getMessage());
                _bus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
    }

    private int getConferenceId() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getInt("conferenceId", -1);
    }

    @Override
    public void onRefresh() {
        getConferenceInfo(_conferenceId);
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        if (event.getResponse() instanceof  ArrayList) {
            _speechAdapter.addItems((ArrayList<Speech>)event.getResponse());
        }
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        _bus.unregister(this);
        finish();
    }


    private void setData(String title, String description, String date, String address, String imageUrl) {
        _conferenceTitle.setText(title);
        _conferenceAddress.setText(address);
        _conferenceDescription.setText(description);
        _conferenceDate.setText(date);

        Picasso picasso = Picasso.with(this);
        picasso.setIndicatorsEnabled(false);

        picasso.load(imageUrl)
                .fit()
                .into(_conferenceImage);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!_bus.isRegistered(this))
            _bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        _bus.unregister(this);
    }
}
