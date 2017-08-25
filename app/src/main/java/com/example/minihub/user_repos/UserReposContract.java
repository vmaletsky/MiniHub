package com.example.minihub.user_repos;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;

/**
 * Created by volod on 8/21/2017.
 */

public class UserReposContract {
    interface View extends BaseView<Presenter> {
        String getAccessToken();
    }

    interface Presenter extends BasePresenter {

    }
}
