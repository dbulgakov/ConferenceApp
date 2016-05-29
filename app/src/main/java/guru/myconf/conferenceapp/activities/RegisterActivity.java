package guru.myconf.conferenceapp.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.register_button) Button _registerButton;

    @Bind(R.id.name) EditText _inputName;
    @Bind(R.id.user_login) EditText _inputLogin;
    @Bind(R.id.password) EditText _inputPassword;
    @Bind(R.id.email) EditText _inputEmail;

    private EventBus _bus = EventBus.getDefault();
    private ProgressDialog _progressDialog;

    private static final int MIN_LENGH = 4;
    private static final int MAX_LENGH = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        //_bus.register(this);

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void attemptRegister()
    {
        String userName = _inputName.getText().toString();
        String userLogin = _inputLogin.getText().toString();
        String userPassword = _inputPassword.getText().toString();
        String userEmail = _inputEmail.getText().toString();

        if (!validateUserInfo(userName, userLogin, userPassword, userEmail)) {
            return;
        }
    }



    private boolean validateUserInfo(String userName, String userLogin, String userPassword, String userEmail) {
        boolean userInfoValid = true;
        View focusView = null;

        if (!isEmailValid(userEmail)) {
            _inputEmail.setError(getString(R.string.error_register_wrong_email));
            focusView = _inputEmail;
        }


        if (!isPasswordValid(userPassword)) {
            _inputPassword.setError(getString(R.string.error_register_wrong_length) + MIN_LENGH + getString(R.string.error_symbol));
            focusView = _inputPassword;
        }

        if (!isLoginValid(userLogin)) {
            _inputLogin.setError(getString(R.string.error_register_wrong_length) + MIN_LENGH + getString(R.string.error_symbol));
            focusView = _inputLogin;
        }

        if (!isNameValid(userName)) {
            _inputName.setError(getString(R.string.error_register_wrong_name));
            focusView = _inputName;
        }

        if (focusView != null) {
            focusView.requestFocus();
            userInfoValid = false;
        }

        return userInfoValid;
    }

    private boolean isLoginValid(String userLogin) {
        return !userLogin.isEmpty() && userLogin.length() > MIN_LENGH && userLogin.length() < MAX_LENGH;
    }

    private boolean isEmailValid(String userEmail) {
        return !userEmail.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches();
    }

    private boolean isPasswordValid(String userPassword) {
        return !userPassword.isEmpty() && userPassword.length() > MIN_LENGH && userPassword.length() < MAX_LENGH;
    }

    private boolean isNameValid(String userName) {
        return userName.isEmpty() && userName.split("\\s+").length == 2;
    }
}
