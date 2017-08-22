package com.example.minihub.navigation;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by volod on 8/23/2017.
 */

public class NavigationPresenter implements NavigationContract.Presenter {

    private final NavigationContract.View mNavigationView;
    private final SharedPreferences mSharedPreferences;

    public NavigationPresenter(@NonNull NavigationContract.View navigationView, @NonNull SharedPreferences sharedPreferences) {
        mNavigationView = navigationView;
        mSharedPreferences = sharedPreferences;
    }

    private void removeAccessToken() {

    }

    public void start() {

    }



    @Override
    public void logout() {
        removeAccessToken();
        mNavigationView.openLoginActivity();
    }
}
