package guru.myconf.conferenceapp.activities;

import android.accounts.AccountsException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import guru.myconf.conferenceapp.pojos.Request.RegistrationRequest;
import guru.myconf.conferenceapp.pojos.Response.BasicResponse;
import guru.myconf.conferenceapp.utils.ProgressBarUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.link_login) TextView mLoginLink;
    @Bind(R.id.register_button) Button mRegisterButton;

    @Bind(R.id.name) EditText mInputName;
    @Bind(R.id.user_login) EditText mInputLogin;
    @Bind(R.id.password) EditText mInputPassword;
    @Bind(R.id.email) EditText mInputEmail;

    private final EventBus mBus = EventBus.getDefault();

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        mBus.register(this);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void attemptRegister()
    {
        String userName = mInputName.getText().toString();
        String userLogin = mInputLogin.getText().toString();
        String userPassword = mInputPassword.getText().toString();
        String userEmail = mInputEmail.getText().toString();

        if (!validateUserInfo(userName, userLogin, userPassword, userEmail)) {
            return;
        }

        ProgressBarUtility.showProgressBar(this, getString(R.string.registration_process_message));
        makeRegistrationRequest(userName.split("\\s+"), userLogin, userPassword, userEmail);
    }

    private void makeRegistrationRequest(String[] userName, String userLogin, String userPassword, String userEmail) {
        GeneralApiManager apiManager = new GeneralApiManager(this);

        ApiUrlManager apiService = apiManager.getApiService();

        RegistrationRequest request = new RegistrationRequest(userLogin, userPassword, userName[0], userName[1], userEmail);

        Call<BasicResponse> call = apiService.userRegister(request);

        call.enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> basicResponse) {
                try {
                    if (basicResponse.code() == 400) {
                        throw new AccountsException();
                    }

                    if (basicResponse.code() == 500){
                        throw new Exception();
                    }
                    Log.d("msg", basicResponse.body().getResponseMessage());

                    mBus.post(new ApiResultEvent(new Object()));
                }
                catch (Exception e){
                    mBus.post(new ApiErrorEvent(e));
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                mBus.post(new ApiErrorEvent(new ConnectException()));
            }
        });
    }

    @Subscribe
    public void onEvent(ApiResultEvent event) {
        ProgressBarUtility.dismissProgressBar();
        Toast.makeText(getBaseContext(), R.string.registration_finished_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Subscribe
    public void onEvent(ApiErrorEvent event) {
        Log.d("API ERROR: ", "" + event.getError());
        ProgressBarUtility.dismissProgressBar();

        Exception exception = event.getError();

        if (exception instanceof ConnectException) {
            Toast.makeText(getBaseContext(), R.string.error_no_internet, Toast.LENGTH_SHORT).show();
        } else if (exception instanceof AccountsException) {
            Toast.makeText(getBaseContext(), R.string.error_username_already_taken, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.error_server_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUserInfo(String userName, String userLogin, String userPassword, String userEmail) {
        boolean userInfoValid = true;
        View focusView = null;

        if (!isEmailValid(userEmail)) {
            mInputEmail.setError(getString(R.string.error_register_wrong_email));
            focusView = mInputEmail;
        }


        if (!isPasswordValid(userPassword)) {
            mInputPassword.setError(getString(R.string.error_register_wrong_length) + MIN_LENGTH + getString(R.string.error_symbol));
            focusView = mInputPassword;
        }

        if (!isLoginValid(userLogin)) {
            mInputLogin.setError(getString(R.string.error_register_wrong_length) + MIN_LENGTH + getString(R.string.error_symbol));
            focusView = mInputLogin;
        }

        if (!isNameValid(userName)) {
            mInputName.setError(getString(R.string.error_register_wrong_name));
            focusView = mInputName;
        }

        if (focusView != null) {
            focusView.requestFocus();
            userInfoValid = false;
        }

        return userInfoValid;
    }

    private boolean isLoginValid(String userLogin) {
        return !userLogin.isEmpty() && userLogin.length() >= MIN_LENGTH && userLogin.length() < MAX_LENGTH;
    }

    private boolean isEmailValid(String userEmail) {
        return !userEmail.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
    }

    private boolean isPasswordValid(String userPassword) {
        return !userPassword.isEmpty() && userPassword.length() >= MIN_LENGTH && userPassword.length() < MAX_LENGTH;
    }

    private boolean isNameValid(String userName) {
        return !userName.isEmpty() && userName.split("\\s+").length == 2;
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
        if (mBus.isRegistered(this))
            mBus.unregister(this);
    }
}
