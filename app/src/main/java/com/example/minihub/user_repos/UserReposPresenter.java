package com.example.minihub.user_repos;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.domain.Repository;
import com.example.minihub.network.ReposAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class UserReposPresenter extends MvpBasePresenter<UserReposView> implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REPOS_LOADER = 100;
    private String TAG = getClass().getSimpleName();

    private ReposAsyncTask task;

    private Context mContext;

    private ReposAdapter mReposAdapter;

    private LoaderManager mLoaderManager;

    static final int COL_REPO_ID = 0;
    static final int COL_REPO_NAME = 1;
    static final int COL_REPO_USER_ID = 2;
    static final int COL_LANGUAGE = 3;
    static final int COL_TOPICS = 4;
    static final int COL_WATCHERS_COUNT = 5;
    static final int COL_STARGAZERS_COUNT = 6;
    static final int COL_FORKS_COUNT = 7;
    static final int COL_STARGAZERS_URL = 8;

    UserReposPresenter(Context context, ReposAdapter adapter, LoaderManager loaderManager) {
        mContext = context;
        mReposAdapter = adapter;
        mLoaderManager = loaderManager;
    }

    public void loadRepos() {
        task = new ReposAsyncTask(new ReposAsyncTask.ReposListener() {
            @Override
            public void onReposLoaded(List<Repository> repos) {
                if (isViewAttached()) {
                    cacheRepos(repos);
                    getView().onLoadFinished();
                }
            }
        }, new ReposAsyncTask.ErrorListener() {
            @Override
            public void onError(int errorCode) {
                getView().setErrorMessage(errorCode);
                getView().setRefreshing(false);
            }
        });
        task.execute(getView().getAccessToken());
    }

    private void cacheRepos(List<Repository> repos) {

        Vector<ContentValues> reposValuesVector = new Vector<>(repos.size());
        for (Repository repo : repos) {
            ContentValues values = new ContentValues();
            values.put(RepoContract.RepoColumns.COLUMN_REPO_ID, repo.id);
            values.put(RepoContract.RepoColumns.COLUMN_NAME, repo.name);
            values.put(RepoContract.RepoColumns.COLUMN_FORKS_COUNT, repo.forksCount);
            values.put(RepoContract.RepoColumns.COLUMN_LANGUAGE, repo.language);
            values.put(RepoContract.RepoColumns.COLUMN_STARGAZERS_COUNT, repo.stargazersCount);
            values.put(RepoContract.RepoColumns.COLUMN_WATCHERS_COUNT, repo.watchersCount);
            reposValuesVector.add(values);
        }

        if (reposValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[reposValuesVector.size()];
            reposValuesVector.toArray(cvArray);

            // delete old data
            mContext.getContentResolver().delete(RepoContract.RepoColumns.CONTENT_URI, null, null);
            mContext.getContentResolver().bulkInsert(RepoContract.RepoColumns.CONTENT_URI, cvArray);
            Log.v(TAG, "Inserted repos : " + reposValuesVector.size());
        }

    }

    void onDestroyView() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        mLoaderManager.destroyLoader(REPOS_LOADER);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == REPOS_LOADER) {
            Uri reposUri = RepoContract.RepoColumns.CONTENT_URI;
            CursorLoader loader = new CursorLoader(mContext,
                    reposUri,
                    REPOS_COLUMNS,
                    null,
                    null,
                    null);
            return loader;
        } else return null;
    }

    public String REPOS_COLUMNS[] = {
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_REPO_ID + " AS _id",
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_NAME,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_USER_ID,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_LANGUAGE,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_TOPICS,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_WATCHERS_COUNT,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_STARGAZERS_COUNT,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_FORKS_COUNT,
            RepoContract.RepoColumns.TABLE_NAME + "." + RepoContract.RepoColumns.COLUMN_STARGAZERS_URL
    };

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == REPOS_LOADER) {
            mReposAdapter.swapCursor(data);
            Log.v(TAG, "onLoadFinished, loaded repos : " + data.getCount());
            if (getView() != null) {
                getView().setRefreshing(false);
            }
            if (data.moveToFirst()) {
                mReposAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == REPOS_LOADER) {
            mReposAdapter.swapCursor(null);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        mLoaderManager.initLoader(REPOS_LOADER, null, this);
    }

    public void onResume() {
        mLoaderManager.initLoader(REPOS_LOADER, null, this);
    }
}
