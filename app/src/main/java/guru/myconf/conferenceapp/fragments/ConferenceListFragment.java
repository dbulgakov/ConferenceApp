package guru.myconf.conferenceapp.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;
import java.util.ArrayList;

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

public class ConferenceListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private ConferenceAdapter.OnConferenceSelected _clickListener;
    private RecyclerView.LayoutManager _layoutManager;
    private ConferenceAdapter _conferenceAdapter;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private RecyclerView _recyclerView;
    private Context _context;

    private EventBus _bus = EventBus.getDefault();

    public static ConferenceListFragment newInstance() {
        return new ConferenceListFragment();
    }

    public ConferenceListFragment() {
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;

        if (context instanceof ConferenceAdapter.OnConferenceSelected) {
            _clickListener = (ConferenceAdapter.OnConferenceSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " OnConferenceSelected is not implemented");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.conference_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        final Activity activity = getActivity();

        //EventBus
        _bus.register(this);

        if (savedInstanceState == null)
        {
            _swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout_main);
            _swipeRefreshLayout.setOnRefreshListener(this);
            _swipeRefreshLayout.setRefreshing(true);

            _recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_conferences);
            _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            _recyclerView.setHasFixedSize(true);
            _conferenceAdapter = new ConferenceAdapter(activity, new ArrayList<Conference>(), _clickListener, _swipeRefreshLayout);

            _recyclerView.setAdapter(_conferenceAdapter);
        }

        getConferences();
    }

    public void getConferences() {

        // Turning on progressbar
        _swipeRefreshLayout.setRefreshing(true);

        // Removing old data
        _conferenceAdapter.removeItems();

        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(_context);

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
        if (event.getResponse() instanceof  ArrayList) {
            _conferenceAdapter.addItems((ArrayList<Conference>)event.getResponse());
        }
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("API ERROR: ", "" + event.getError());
        Toast.makeText(_context, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        _swipeRefreshLayout.setRefreshing(false);
        _swipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        getConferences();
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
