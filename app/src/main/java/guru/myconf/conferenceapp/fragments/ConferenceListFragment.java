package guru.myconf.conferenceapp.fragments;

import android.app.Activity;
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


    private ConferenceAdapter.OnConferenceSelected mClickListener;
    private ConferenceAdapter mConferenceAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Context mContext;

    private EventBus mBus = EventBus.getDefault();

    public static ConferenceListFragment newInstance() {
        return new ConferenceListFragment();
    }

    public ConferenceListFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (context instanceof ConferenceAdapter.OnConferenceSelected) {
            mClickListener = (ConferenceAdapter.OnConferenceSelected) context;
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
        mBus.register(this);

        if (savedInstanceState == null)
        {
            mSwipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout_main);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setRefreshing(true);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_conferences);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setHasFixedSize(true);
            mConferenceAdapter = new ConferenceAdapter(activity, new ArrayList<Conference>(), mClickListener, mSwipeRefreshLayout);

            mRecyclerView.setAdapter(mConferenceAdapter);
        }

        getConferences();
    }

    public void getConferences() {

        // Turning on progressbar
        mSwipeRefreshLayout.setRefreshing(true);

        // Removing old data
        mConferenceAdapter.removeItems();

        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(mContext);

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
                    mBus.post(new ApiResultEvent(conferences));

                    if (response.code() == 500){
                        throw new Exception();
                    }
                }
                catch (Exception e){
                    mBus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<ConferencesResponse> call, Throwable t) {
                mBus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        if (event.getResponse() instanceof  ArrayList) {
            if (((ArrayList) event.getResponse()).toArray()[0] instanceof Conference)
                mConferenceAdapter.addItems((ArrayList<Conference>)event.getResponse());
        }
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("API ERROR: ", "" + event.getError());
        Toast.makeText(mContext, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        getConferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mBus.isRegistered(this))
            mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }
}
