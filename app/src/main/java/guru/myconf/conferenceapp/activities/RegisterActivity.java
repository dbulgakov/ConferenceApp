package guru.myconf.conferenceapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import guru.myconf.conferenceapp.R;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        _loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
