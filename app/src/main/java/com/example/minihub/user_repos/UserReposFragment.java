package com.example.minihub.user_repos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minihub.R;
import com.example.minihub.data.Repository;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserReposFragment extends MvpFragment<UserReposView, UserReposPresenter> implements UserReposView {

    String TAG = getClass().getSimpleName();

    ReposAdapter mAdapter;

    @BindView(R.id.user_repos_list)
    RecyclerView mReposList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_repositories, container, false);
        ButterKnife.bind(this, view);
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadRepos();
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }

    @Override
    public void showRepos(List<Repository> repos) {
        mAdapter = new ReposAdapter(repos);
        mReposList.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mReposList.setLayoutManager(layoutManager);
        mReposList.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReposList.getContext(),
                layoutManager.getOrientation());
        mReposList.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public UserReposPresenter createPresenter() {
        return new UserReposPresenter();
    }


}
