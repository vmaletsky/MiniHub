package com.example.minihub.user_info;

import com.example.minihub.data.User;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by volod on 8/21/2017.
 */

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
