package com.dkoptin.loftmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dkoptin.loftmoney.remote.AuthResponse;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtnView;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtnView = findViewById(R.id.loginBtnView);

        configureButton();
    }

    private void configureButton() {
        loginBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String socialUserId = String.valueOf(new Random().nextInt());
                compositeDisposable.add(((LoftApp) getApplication()).getAuthApi().performLogin(socialUserId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<AuthResponse>() {
                            @Override
                            public void accept(AuthResponse authResponse) throws Exception {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                editor.putString(Prefs.TOKEN, authResponse.getAccessToken());
                                editor.apply();

                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(mainIntent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getApplicationContext(),throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        });
    }

}