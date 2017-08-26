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
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;


/**
 * Created by v.maletskiy on 8/21/2017.
 */

public class FeedPresenter extends MvpBasePresenter<FeedView> {
    private String TAG = getClass().getSimpleName();

    EventsAsyncTask task;


    public void getEvents() {
        String token = getView().getAccessToken();
        task = new EventsAsyncTask(new EventsAsyncTask.FeedTaskListener() {
            @Override
            public void onFeedReceived(List<FeedEvent> events) {
                if (isViewAttached()) {
                    getView().setData(events);
                }
            }
        });
        task.execute(token);
        getView().showEvents();
    }

    @Override
    public void detachView(boolean retainInstance) {
        task.cancel(true);
    }
}
