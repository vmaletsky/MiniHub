package com.example.minihub.user_info;

import com.example.minihub.domain.User;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface UserInfoView extends MvpView {
    void setUserName(String name);

    void setUserLogin(String login);

    void setUserAvatar(String url);

    String getAccessToken();

    User getCurrentUser();
}
