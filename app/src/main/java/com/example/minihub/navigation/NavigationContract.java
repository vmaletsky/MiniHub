package com.example.minihub.navigation;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;

/**
 * Created by volod on 8/23/2017.
 */

public interface NavigationContract {
    interface View extends BaseView<Presenter> {
        void openLoginActivity();
        void showUserInfo();
        void showRepositoriesList();
        void showFeed();
    }

    interface Presenter extends BasePresenter {
        void logout();
    }
}
