package com.example.minihub.user_repos;

import com.example.minihub.BasePresenter;
import com.example.minihub.BaseView;
import com.example.minihub.data.Repository;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

/**
 * Created by volod on 8/21/2017.
 */

public interface UserReposView extends MvpView {
    String getAccessToken();

    void showRepos(List<Repository> repos);
}
