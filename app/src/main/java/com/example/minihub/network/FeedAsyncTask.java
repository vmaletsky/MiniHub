package com.example.minihub.network;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.util.Log;

import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import retrofit2.Response;

public class FeedAsyncTask extends AsyncTask<String, Void, List<FeedEvent>> {
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private boolean mIsRefreshing;
    private int mPage;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ERROR_INCORRECT_DATA, ERROR_NO_NETWORK})
    public @interface Error{}

    // Declare the constants
    public static final int ERROR_INCORRECT_DATA = 0;
    public static final int ERROR_NO_NETWORK = 1;

    public OnLoad mListener;

    public OnError mErrorListener;

    public interface OnError {
        void onError(@Error int errorCode);
    }

    public interface OnLoad {
        void onLoaded(List<FeedEvent> events);
    }

    public FeedAsyncTask(Context context, boolean isRefreshing, int page) {
        mContext = context;
        mIsRefreshing = isRefreshing;
        mPage = page;
    }

    @Override
    protected List<FeedEvent> doInBackground(String... strings) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        String token = sp.getString(mContext.getString(R.string.access_token_pref_id), null);
        GithubService service = ServiceGenerator.createService(GithubService.class, token);
        List<FeedEvent> events = new ArrayList<>();
        try {
            Response<List<FeedEvent>> response = service.getUserEvents(mPage, 10).execute();
            if (response.isSuccessful()) {
                events = response.body();
            } else {
                mErrorListener.onError(ERROR_INCORRECT_DATA);
            }
        } catch (IOException e) {
            mErrorListener.onError(ERROR_NO_NETWORK);
        }
        return events;
    }


    @Override
    protected void onPostExecute(List<FeedEvent> feedEvents) {
        mListener.onLoaded(feedEvents);
    }


}
