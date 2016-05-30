package guru.myconf.conferenceapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.ConnectException;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;
import guru.myconf.conferenceapp.api.ApiUrlManager;
import guru.myconf.conferenceapp.api.GeneralApiManager;
import guru.myconf.conferenceapp.events.ApiErrorEvent;
import guru.myconf.conferenceapp.events.ApiResultEvent;
import guru.myconf.conferenceapp.pojos.Request.LoginRequest;
import guru.myconf.conferenceapp.pojos.Response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import guru.myconf.conferenceapp.utils.ProgressBarUtility;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.user_login) EditText _inputUserLogin;
    @Bind(R.id.password) EditText _inputPassword;
    @Bind(R.id.login_button) Button _loginButton;
    @Bind(R.id.link_register) TextView _registerLink;

    private EventBus _bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        _registerLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
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
        String userLogin = _inputUserLogin.getText().toString();
        String userPassword = _inputPassword.getText().toString();

        if (!validateCredentials(userLogin, userPassword)) {
            return;
        }

        ProgressBarUtility.showProgressBar(this, getString(R.string.login_process_message));
        makeLoginRequest(userLogin, userPassword);
    }

    private boolean validateCredentials(String userLogin, String userPassword) {

        boolean areCredentialsValid = true;
        View focusView = null;

        if (!isPasswordValid(userPassword)) {
            _inputPassword.setError(getString(R.string.error_invalid_password));
            focusView = _inputPassword;
        }

        if (!isEmailValid(userLogin)) {
            _inputUserLogin.setError(getString(R.string.error_invalid_email));
            focusView = _inputUserLogin;
        }

        if (focusView != null) {
            focusView.requestFocus();
            areCredentialsValid = false;
        }

        return areCredentialsValid;
    }

    private void makeLoginRequest(String userLogin, String password) {

        GeneralApiManager apiManager = new GeneralApiManager(this);

        ApiUrlManager apiService = apiManager.getApiService();

        Call<LoginResponse> call = apiService.userLogin(new LoginRequest(userLogin, password));

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
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        ProgressBarUtility.dismissProgressBar();
        saveToken(event.getResponse().toString());
        finish();
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("API ERROR: ", "" + event.getError());
        ProgressBarUtility.dismissProgressBar();

        Exception exception = event.getError();

        if (exception instanceof ConnectException) {
            Toast.makeText(getBaseContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (exception instanceof NullPointerException) {
            Toast.makeText(getBaseContext(), R.string.error_wrong_credentials, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.error_server_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToken(String token) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getString(R.string.auth_token_key), token);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        _bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (!_bus.isRegistered(this))
            _bus.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (_bus.isRegistered(this))
            _bus.unregister(this);
        super.onPause();
    }
}

