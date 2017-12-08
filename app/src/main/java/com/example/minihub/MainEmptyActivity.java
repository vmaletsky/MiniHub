package com.example.minihub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.minihub.auth.LoginActivity;
import com.example.minihub.navigation.NavigationActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainEmptyActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        Intent intent;
        if (userLoggedIn()) {
            Log.v(TAG, "User is logged in");
            intent = new Intent(this, NavigationActivity.class);
        } else {
            Log.v(TAG, "User is not logged in");
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
    }

    private boolean userLoggedIn() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = sp.getString(getString(R.string.access_token_pref_id), null);
        return accessToken != null;
    }
}
