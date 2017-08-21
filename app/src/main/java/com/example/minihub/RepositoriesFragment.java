package com.example.minihub;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minihub.data.Repository;

import java.io.IOException;

import retrofit2.Response;

public class RepositoriesFragment extends Fragment {

    String TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ReposAsyncTask asyncTask = new ReposAsyncTask();
        asyncTask.execute();
        return inflater.inflate(R.layout.fragment_repositories, container, false);
    }

    class ReposAsyncTask extends AsyncTask<Void, Void, Repository[]> {
        @Override
        protected Repository[] doInBackground(Void... params) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(RepositoriesFragment.this.getActivity());
            String authToken = sp.getString(getString(R.string.access_token_pref_id), null);
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
            for (Repository r : repositories) {
                Log.v(TAG, r.name);
            }
        }
    }
}
