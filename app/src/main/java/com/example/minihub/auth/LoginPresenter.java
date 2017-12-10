package com.example.minihub.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.minihub.R;
import com.example.minihub.domain.AccessToken;
import com.example.minihub.domain.User;
import com.example.minihub.network.AuthAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;


public class LoginPresenter extends MvpBasePresenter<LoginView> {
    AuthAsyncTask authAsyncTask;

    UserInfoTask userInfoTask;

    SharedPreferences mSharedPreferences;

    Context mContext;

    LoginPresenter(SharedPreferences sp, Context context) {
        this.mSharedPreferences = sp;
        this.mContext = context;
    }

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
                saveUserInfo(user);
            }
        });
        userInfoTask.execute(getView().getAccessToken());
    }

    public void saveUserInfo(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mContext.getString(R.string.current_user_name), user.name)
                .putString(mContext.getString(R.string.current_user_login), user.login)
                .putString(mContext.getString(R.string.current_user_avatar_url), user.avatarUrl)
                .apply();
    }

}
