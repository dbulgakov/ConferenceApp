package guru.myconf.conferenceapp.activities;

import android.accounts.AccountsException;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import guru.myconf.conferenceapp.adapters.CommentAdapter;
import guru.myconf.conferenceapp.adapters.SpeechAdapter;
import guru.myconf.conferenceapp.api.ApiUrlManager;
import guru.myconf.conferenceapp.api.GeneralApiManager;
import guru.myconf.conferenceapp.entities.Comment;
import guru.myconf.conferenceapp.entities.Speech;
import guru.myconf.conferenceapp.events.ApiErrorEvent;
import guru.myconf.conferenceapp.events.ApiPostCommentError;
import guru.myconf.conferenceapp.events.ApiPostCommentResult;
import guru.myconf.conferenceapp.events.ApiResultEvent;
import guru.myconf.conferenceapp.pojos.Request.PostCommentRequest;
import guru.myconf.conferenceapp.pojos.Response.BasicResponse;
import guru.myconf.conferenceapp.pojos.Response.ConferenceComment;
import guru.myconf.conferenceapp.pojos.Response.ConferenceCommentsResponse;
import guru.myconf.conferenceapp.pojos.Response.ConferenceInfo;
import guru.myconf.conferenceapp.pojos.Response.ConferenceInfoResponse;
import guru.myconf.conferenceapp.pojos.Response.SpeechResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class ConferenceInfoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int mConferenceId;
    private SpeechAdapter mSpeechAdapter;
    private CommentAdapter mCommentAdapter;
    private EventBus mBus = EventBus.getDefault();

    @Bind(R.id.speech_toolbar) Toolbar mToolbar;
    @Bind(R.id.recycler_view_speeches) RecyclerView mRecycleViewSpeeches;
    @Bind(R.id.recycler_view_comments) RecyclerView mRecycleViewComments;
    @Bind(R.id.conference_name) TextView mConferenceTitle;
    @Bind(R.id.conference_description) TextView mConferenceDescription;
    @Bind(R.id.conference_address) TextView mConferenceAddress;
    @Bind(R.id.conference_date) TextView mConferenceDate;
    @Bind(R.id.conference_image) ImageView mConferenceImage;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    @Bind(R.id.add_comment_button) Button mAddCommentButton;
    @Bind(R.id.add_comment_text) TextView mAddCommentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_info);

        // ButterKnife binding
        ButterKnife.bind(this);

        // Bus Registration
        mBus.register(this);

        // Getting caller conference Id
        mConferenceId = getConferenceId();

        // Toolbar init
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting onClickListener to enable back button
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBus.isRegistered(this))
                    mBus.unregister(this);
                finish();
            }
        });

        // Add comment button init
        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkComment(mAddCommentText.getText().toString())){
                    addConferenceComment(mConferenceId, mAddCommentText.getText().toString());
                } else {
                    mAddCommentText.setError("Необходимо ввести текст комментария.");
                }
            }
        });


        // Turning progressbar on
        mProgressBar.setIndeterminate(true);

        // Initializing Adapters
        mSpeechAdapter = new SpeechAdapter(this, new ArrayList<Speech>());
        mCommentAdapter = new CommentAdapter(this, new ArrayList<Comment>());

        // Initializing RCs
        mRecycleViewSpeeches.setLayoutManager(new LinearLayoutManager(this));
        mRecycleViewSpeeches.setItemAnimator(new DefaultItemAnimator());
        mRecycleViewSpeeches.setAdapter(mSpeechAdapter);

        mRecycleViewComments.setLayoutManager(new LinearLayoutManager(this));
        mRecycleViewComments.setItemAnimator(new DefaultItemAnimator());
        mRecycleViewComments.setAdapter(mCommentAdapter);

        UpdateData();
    }

    private void getConferenceInfo(int conferenceId) {
        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(this);
        ApiUrlManager apiService = apiManager.getApiService();

        // Removing old data
        mSpeechAdapter.removeItems();

        Call<ConferenceInfoResponse> call = apiService.getConferenceInfo(mConferenceId);

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
                            conferenceInfo.getAddress(), conferenceInfo.getBiggerImageUrl());

                    mBus.post(new ApiResultEvent(speeches));

                    if (response.code() == 500){
                        throw new Exception();
                    }
                }
                catch (Exception e){
                    mBus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<ConferenceInfoResponse> call, Throwable t) {
                mBus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
    }

    private int getConferenceId() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getInt("conferenceId", -1);
    }

    private String getAuthKey() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getString(getString(R.string.auth_token_key), "");
    }

    private void getConferenceComments(int conferenceId) {
        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(this);
        ApiUrlManager apiService = apiManager.getApiService();

        // Removing old data
        mCommentAdapter.removeItems();

        Call<ConferenceCommentsResponse> call = apiService.getConferenceComments(mConferenceId);

        call.enqueue(new Callback<ConferenceCommentsResponse>() {

            @Override
            public void onResponse(Call<ConferenceCommentsResponse> call, Response<ConferenceCommentsResponse> response) {
                try {
                    if (response.code() == 500 || response.body().getResponseComments() == null){
                        throw new Exception();
                    }

                    ArrayList<Comment> comments = new ArrayList<>();

                    // Converting into more convenient classes
                    for (ConferenceComment c : response.body().getResponseComments()) {
                        comments.add(c.convertToEntityComment());
                    }

                    mBus.post(new ApiResultEvent(comments));
                }
                catch (Exception e){
                    mBus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<ConferenceCommentsResponse> call, Throwable t) {
                mBus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
    }

    private void addConferenceComment(int conferenceId, String text) {
        // Initializing apiManager to perform requests
        GeneralApiManager apiManager = new GeneralApiManager(this);
        ApiUrlManager apiService = apiManager.getApiService();

        // Removing old data
        mCommentAdapter.removeItems();

        String authToken = getAuthKey();

        Call<BasicResponse> call = apiService.addComment(mConferenceId, new PostCommentRequest(authToken, text));

        call.enqueue(new Callback<BasicResponse>() {

            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                try {
                    Log.d("eee", "" + response.code());
                    mBus.post(new ApiPostCommentResult());

                    if (response.code() == 401){
                        throw new AccountsException();
                    }


                    if (response.code() == 500){
                        throw new Exception();
                    }
                }
                catch (Exception e){
                    mBus.post(new ApiPostCommentError(e));
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                mBus.post(new ApiPostCommentError(new ConnectException()));
            }
        });
    }


    private void UpdateData()
    {
        getConferenceInfo(mConferenceId);
        getConferenceComments(mConferenceId);
    }


    @Override
    public void onRefresh() {
        UpdateData();
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        if (event.getResponse() instanceof  ArrayList && ((ArrayList) event.getResponse()).size() > 0) {
            if (((ArrayList) event.getResponse()).toArray()[0] instanceof Speech)
                mSpeechAdapter.addItems((ArrayList<Speech>)event.getResponse());
            else {
                if (((ArrayList) event.getResponse()).size() > 0) {
                    mRecycleViewComments.setVisibility(View.VISIBLE);
                } else {
                    mRecycleViewComments.setVisibility(View.GONE);
                }
                mCommentAdapter.addItems((ArrayList<Comment>)event.getResponse());
            }
        }
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("error", event.getError().getMessage());
        Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        mBus.unregister(this);
        finish();
    }

    @Subscribe
    public void onEvent(ApiPostCommentResult event) {
        Toast.makeText(this, "Комментарий успешно опубликован.", Toast.LENGTH_SHORT).show();
        getConferenceComments(mConferenceId);
    }

    @Subscribe
    public void onEvent(ApiPostCommentError event) {
        if (event.getError() instanceof AccountsException) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear().apply();
            Toast.makeText(this, R.string.error_wrong_credentials, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        }
        mBus.unregister(this);
        finish();
    }


    private void setData(String title, String description, String date, String address, String imageUrl) {
        mConferenceTitle.setText(title);
        mConferenceAddress.setText(address);
        mConferenceDescription.setText(description);
        mConferenceDate.setText(date);

        final Picasso picasso = Picasso.with(this);
        picasso.setIndicatorsEnabled(false);

        com.squareup.picasso.Callback callback = new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setIndeterminate(false);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mProgressBar.setIndeterminate(false);
                mProgressBar.setVisibility(View.GONE);
                picasso.load(R.drawable.error_image_big).into(mConferenceImage);
            }
        };


        picasso.load(imageUrl)
                .into(mConferenceImage, callback);
    }


    private boolean checkComment(String text) {
        return !text.isEmpty();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
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
