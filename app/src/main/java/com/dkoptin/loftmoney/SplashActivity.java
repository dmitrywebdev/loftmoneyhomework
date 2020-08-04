package com.dkoptin.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkToken();
    }

    private void checkToken() {
        final String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Prefs.TOKEN,"");

        if (TextUtils.isEmpty(token)) {
            routeToLogin();
        } else {
            routeToMain();
        }
    }

    private void routeToMain() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    private void routeToLogin() {
        Intent LoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(LoginIntent);
    }
}