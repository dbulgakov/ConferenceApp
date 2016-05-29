package guru.myconf.conferenceapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import guru.myconf.conferenceapp.R;

public class MainActivity extends AppCompatActivity {

    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuth();
    }

    private void checkAuth(){
        if (!checkPreferencesManager()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {

        }
    }

    private boolean checkPreferencesManager(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.contains(getString(R.string.auth_token_key));
    }
}
