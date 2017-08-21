package com.example.minihub.auth;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;

/**
 * Created by v.maletskiy on 8/21/2017.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void login();
    }
}
