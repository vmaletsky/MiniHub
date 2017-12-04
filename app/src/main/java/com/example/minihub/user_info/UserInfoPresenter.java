package com.example.minihub.user_info;

import com.example.minihub.domain.User;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public class UserInfoPresenter extends MvpBasePresenter<UserInfoView> {
    void getUserInfo() {

        if (isViewAttached()) {
            User user = getView().getCurrentUser();
            getView().setUserName(user.name);
            getView().setUserAvatar(user.avatarUrl);
            getView().setUserLogin(user.login);
        }
    }
}
