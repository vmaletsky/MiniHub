package com.example.minihub.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.minihub.domain.Repository;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by volod on 8/26/2017.
 */

class ReposAsyncTask extends AsyncTask<String, Void, Repository[]> {
    private String TAG = getClass().getSimpleName();

    public ReposListener listener;

    public interface ReposListener {
        void onReposLoaded(Repository[] repos);
    }

    public ReposAsyncTask(ReposListener listener) {
        this.listener = listener;
    }

    @Override
    protected Repository[] doInBackground(String... params) {
        String authToken = params[0];
        GithubService service = ServiceGenerator.createService(GithubService.class, authToken);
        Repository[] repos = null;
        try {
            Response<Repository[]> response = service.getWatchedRepos().execute();
            repos = response.body();
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }

        return repos;
    }

    @Override
    protected void onPostExecute(Repository[] repositories) {
        listener.onReposLoaded(repositories);
    }
}