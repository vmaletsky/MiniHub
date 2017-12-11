package com.example.minihub.user_repos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minihub.R;
import com.example.minihub.Utilities;
import com.example.minihub.domain.Repository;
import com.example.minihub.network.FeedAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserReposFragment extends MvpFragment<UserReposView, UserReposPresenter> implements UserReposView, View.OnClickListener {

    private static final String LIST_STATE_KEY = "LIST_STATE_REPOS";
    String TAG = getClass().getSimpleName();

    ReposAdapter mAdapter;

    @BindView(R.id.user_repos_list)
    RecyclerView mReposList;

    @BindView(R.id.refresh_repos)
    SwipeRefreshLayout mLayoutRepos;
    private Parcelable mListState;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_repositories, container, false);
        ButterKnife.bind(this, view);
        mLayoutRepos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadRepos();
            }
        });
        mAdapter = new ReposAdapter(getContext());
        mReposList.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getContext());
        mReposList.setLayoutManager(mLayoutManager);
        mReposList.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mReposList.getContext(),
                mLayoutManager.getOrientation());
        mReposList.addItemDecoration(dividerItemDecoration);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mReposList.getLayoutManager().onRestoreInstanceState(mListState);
        }
        presenter.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mListState = mReposList.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void setErrorMessage(@FeedAsyncTask.Error int errorCode) {
        Snackbar.make(mReposList, Utilities.getErrorMessage(errorCode), BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(R.string.retry_action, this)
                .show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mReposList.getLayoutManager().onRestoreInstanceState(mListState);
        } else {
            presenter.loadRepos();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mReposList.getLayoutManager().onRestoreInstanceState(mListState);
        }
        presenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }

    @Override
    public void showRepos(List<Repository> repos) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFinished() {
        mLayoutRepos.setRefreshing(false);
    }

    @Override
    public UserReposPresenter createPresenter() {
        return new UserReposPresenter(getContext(), mAdapter, getActivity().getLoaderManager());
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mLayoutRepos.setRefreshing(isRefreshing);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        presenter.loadRepos();
    }
}
