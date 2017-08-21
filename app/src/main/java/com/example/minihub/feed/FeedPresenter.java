package com.example.minihub.feed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.minihub.R;
import com.example.minihub.auth.LoginActivity;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public class FeedPresenter implements  FeedContract.Presenter {

    private void logout() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(getString(R.string.access_token_pref_id))
                .apply();
        Intent intent = new Intent(this, LoginActivity.class); // TODO: move to view
        startActivity(intent);
    }

    @Override
    public void start() {

    }

    @Override
    public void getUserEvents() {

    }
}
