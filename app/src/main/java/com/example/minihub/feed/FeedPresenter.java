package com.example.minihub.feed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.minihub.GithubService;
import com.example.minihub.R;
import com.example.minihub.ServiceGenerator;
import com.example.minihub.auth.LoginActivity;
import com.example.minihub.data.FeedEvent;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.IOException;

import retrofit2.Response;


/**
 * Created by v.maletskiy on 8/21/2017.
 */

public class FeedPresenter extends MvpBasePresenter<FeedView> {
    private String TAG = getClass().getSimpleName();

    public FeedPresenter(@NonNull FeedView feedView,
                         @NonNull SharedPreferences sharedPreferences) {
        this.mFeedView = feedView;
    }

    private FeedView mFeedView;

    @Override
    public void start() {

    }

    @Override
    public void getUserEvents() {
        EventsAsyncTask task = new EventsAsyncTask();
        task.execute();
    }

    class EventsAsyncTask extends AsyncTask<Void, Void, FeedEvent[]> {
        @Override
        protected FeedEvent[] doInBackground(Void... params) {
            String authToken = mFeedView.getAccessToken();
            GithubService service = ServiceGenerator.createService(GithubService.class, authToken);

            try {
                Response<FeedEvent[]> response = service.getUserEvents().execute();
                Log.v(TAG, "Received events : " + response.raw().toString());
            } catch (IOException e) {
                Log.v(TAG, e.getMessage());
            }

            return null;
        }
    }
}
