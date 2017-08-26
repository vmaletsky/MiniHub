package com.example.minihub.user_info;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by volod on 8/21/2017.
 */

public interface UserInfoView extends MvpView {
    void setUserName(String name);

    void setUserLogin(String login);

    void setUserAvatar(String url);

    String getAccessToken();
}
