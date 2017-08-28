package com.example.minihub.auth;

import com.example.minihub.data.AccessToken;
import com.example.minihub.data.User;
import com.example.minihub.network.AuthAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {
    AuthAsyncTask authAsyncTask;

    UserInfoTask userInfoTask;

    void requestAuthToken(String code) {
        authAsyncTask = new AuthAsyncTask(new AuthAsyncTask.OnLoginListener() {
            @Override
            public void onLogin(AccessToken token) {
                if (isViewAttached()) {
                    getView().saveAuthToken(token);
                }
                getUserInfo();
                getView().openNavigationActivity();
            }
        });
        authAsyncTask.execute(code);
    }


    void getUserInfo() {
        userInfoTask = new UserInfoTask(new UserInfoTask.UserInfoListener() {
            @Override
            public void onUserInfoLoaded(User user) {
                if (isViewAttached()) {
                    getView().saveUserInfo(user);
                }
            }
        });
        userInfoTask.execute(getView().getAccessToken());
    }

}
