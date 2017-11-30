package com.example.minihub.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.minihub.domain.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

/**
 * Created by volod on 8/26/2017.
 */

public class ReposAsyncTask extends AsyncTask<String, Void, List<Repository>> {
    private String TAG = getClass().getSimpleName();

    public ReposListener listener;

    public interface ReposListener {
        void onReposLoaded(List<Repository> repos);
    }

    public ReposAsyncTask(ReposListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Repository> doInBackground(String... params) {
        String authToken = params[0];
        GithubService service = ServiceGenerator.createService(GithubService.class, authToken);
        Repository[] repos = null;
        try {
            Response<Repository[]> response = service.getWatchedRepos().execute();
            repos = response.body();
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }

        return Arrays.asList(repos);
    }

    @Override
    protected void onPostExecute(List<Repository> repositories) {
        listener.onReposLoaded(repositories);
    }
}