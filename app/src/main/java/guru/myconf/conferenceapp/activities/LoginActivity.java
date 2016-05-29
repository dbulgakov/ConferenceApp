package guru.myconf.conferenceapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.api.ConferenceGuruApi;
import guru.myconf.conferenceapp.events.ApiErrorEvent;
import guru.myconf.conferenceapp.events.ApiResultEvent;
import guru.myconf.conferenceapp.pojos.Request.LoginRequest;
import guru.myconf.conferenceapp.pojos.Response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.username) EditText _inputUsername;
    @Bind(R.id.password) EditText _inputPassword;
    @Bind(R.id.login_button) Button _loginButton;

    private EventBus _bus = EventBus.getDefault();
    private ProgressDialog _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        _progressDialog = initializeProcessDialog();
        _loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        _bus.register(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private boolean isEmailValid(String email) {
        return !email.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }

    private void attemptLogin() {
        String userName = _inputUsername.getText().toString();
        String userPassword = _inputPassword.getText().toString();

        if (!validateCredentials(userName, userPassword)) {
            return;
        }

        _progressDialog.show();
        makeLoginRequest(userName, userPassword);
    }

    private boolean validateCredentials(String userName, String userPassword) {

        boolean areCredentialsValid = true;
        View focusView = null;

        if (!isPasswordValid(userPassword)) {
            _inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = _inputPassword;
        }

        if (!isEmailValid(userName)) {
            _inputUsername.setError(getString(R.string.error_invalid_email));
            focusView = _inputUsername;
        }

        if (focusView != null) {
            focusView.requestFocus();
            areCredentialsValid = false;
        }

        return areCredentialsValid;
    }

    private String makeLoginRequest(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.conferenceguru_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConferenceGuruApi apiService = retrofit.create(ConferenceGuruApi.class);

        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.userLogin(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    String token = response.body().getResponseToken();
                    _bus.post(new ApiResultEvent(token));

                    if (response.code() == 500){
                        throw new Exception();
                    }

                }
                catch (Exception e){
                    _bus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                _bus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
        return null;
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        _progressDialog.dismiss();
        saveToken(event.getResponse().toString());
        finish();
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("API ERROR: ", "" + event.getError());
        _progressDialog.dismiss();

        Exception exception = event.getError();

        if (exception instanceof ConnectException) {
            Toast.makeText(getBaseContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (exception instanceof NullPointerException) {
            Toast.makeText(getBaseContext(), R.string.error_wrong_credentials, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.error_server_error, Toast.LENGTH_SHORT).show();
        }
    }


    private ProgressDialog initializeProcessDialog()
    {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_process_message));
        return progressDialog;
    }

    public void saveToken(String token) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.auth_token_key), token);
        editor.apply();
    }
}

