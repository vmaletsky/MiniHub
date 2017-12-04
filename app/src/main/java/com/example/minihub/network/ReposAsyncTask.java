package com.example.minihub.network;

import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.util.Log;

import com.example.minihub.domain.Repository;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

public class ReposAsyncTask extends AsyncTask<String, Void, List<Repository>> {
    private String TAG = getClass().getSimpleName();

    public ReposListener listener;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ERROR_INCORRECT_DATA, ERROR_NO_NETWORK})
    public @interface Error{}

    // Declare the constants
    public static final int ERROR_INCORRECT_DATA = 0;
    public static final int ERROR_NO_NETWORK = 1;

    public ErrorListener mErrorListener;

    public interface ErrorListener {
        void onError(@FeedAsyncTask.Error int errorCode);
    }

    public interface ReposListener {
        void onReposLoaded(List<Repository> repos);
    }

    public ReposAsyncTask(ReposListener listener, ErrorListener errorListener) {
        this.listener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    protected List<Repository> doInBackground(String... params) {
        String authToken = params[0];
        GithubService service = ServiceGenerator.createService(GithubService.class, authToken);
        List<Repository> repos = new ArrayList<>();
        try {
            Response<List<Repository>> response = service.getWatchedRepos().execute();
            if (response.isSuccessful()) {
                repos = response.body();
            } else {
                mErrorListener.onError(FeedAsyncTask.ERROR_INCORRECT_DATA);
            }
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
            mErrorListener.onError(FeedAsyncTask.ERROR_NO_NETWORK);
        }

        return repos;
    }

    @Override
    protected void onPostExecute(List<Repository> repositories) {
        listener.onReposLoaded(repositories);
    }
}