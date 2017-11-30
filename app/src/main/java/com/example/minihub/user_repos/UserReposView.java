package com.example.minihub.user_repos;

import com.example.minihub.domain.Repository;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface UserReposView extends MvpView {
    String getAccessToken();

    void onLoadFinished();

    void showRepos(List<Repository> repos);

    void setRefreshing(boolean isRefreshing);
}
