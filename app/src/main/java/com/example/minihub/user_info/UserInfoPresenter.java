package com.example.minihub.user_info;

import com.example.minihub.data.User;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by volod on 8/21/2017.
 */

public class UserInfoPresenter extends MvpBasePresenter<UserInfoView> {
    UserInfoTask task;

    void getUserInfo() {
        task = new UserInfoTask(new UserInfoTask.UserInfoListener() {
            @Override
            public void onUserInfoLoaded(User user) {
                if (isViewAttached()) {
                    getView().setUserName(user.name);
                    getView().setUserAvatar(user.avatarUrl);
                    getView().setUserLogin(user.login);
                }
            }
        });
        task.execute(getView().getAccessToken());
    }
}
