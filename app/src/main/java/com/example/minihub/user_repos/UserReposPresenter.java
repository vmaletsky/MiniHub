package com.example.minihub.user_repos;

import com.example.minihub.data.Repository;
import com.example.minihub.network.ReposAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Arrays;

/**
 * Created by volod on 8/21/2017.
 */

public class UserReposPresenter extends MvpBasePresenter<UserReposView> {
    ReposAsyncTask task;

    public void loadRepos() {
        task = new ReposAsyncTask(new ReposAsyncTask.ReposListener() {
            @Override
            public void onReposLoaded(Repository[] repos) {
                if (isViewAttached()) {
                    getView().showRepos(Arrays.asList(repos));
                }
            }
        });
        task.execute(getView().getAccessToken());
    }
}
