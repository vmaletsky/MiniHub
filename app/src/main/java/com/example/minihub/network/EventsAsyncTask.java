package com.example.minihub.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.minihub.domain.FeedEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

/**
 * Created by volod on 8/26/2017.
 */

class EventsAsyncTask extends AsyncTask<String, Void, FeedEvent[]> {

    public EventsAsyncTask(FeedTaskListener listener) {
        this.listener = listener;
    }

    public FeedTaskListener listener;

    public interface FeedTaskListener {
        void onFeedReceived(List<FeedEvent> events);
    }

    String TAG = getClass().getSimpleName();
    @Override
    protected FeedEvent[] doInBackground(String... params) {
        String token = params[0];
        GithubService service = ServiceGenerator.createService(GithubService.class, token);
        FeedEvent[] events = new FeedEvent[] { };
        try {
            Response<FeedEvent[]> response = service.getUserEvents().execute();
            events = response.body();
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }

        return events;
    }

    @Override
    protected void onPostExecute(FeedEvent[] feedEvents) {
        listener.onFeedReceived(Arrays.asList(feedEvents));
    }
}