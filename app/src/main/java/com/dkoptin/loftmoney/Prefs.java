package com.dkoptin.loftmoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    public static final String TOKEN = "token";

    private SharedPreferences preferences;
    private Context context;

    public Prefs(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
}
