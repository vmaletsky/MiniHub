package com.example.minihub.network;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;

import java.io.IOException;
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
            events = response.body();
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }
        return events;
    }


    @Override
    protected void onPostExecute(List<FeedEvent> feedEvents) {
        cacheEvents(feedEvents);
    }

    private void cacheEvents(List<FeedEvent> events) {
        Vector<ContentValues> eventValuesVector = new Vector<ContentValues>(events.size());
        HashMap<Integer, Repository> repos = new HashMap<>();
        HashMap<Integer, User> users = new HashMap<>();
        for (FeedEvent event : events) {
            Log.v(TAG, " Event id " + event.id + " Repo id : " + event.repo.id + " user id : " + event.actor.id);
            users.put(event.actor.id, event.actor);
            repos.put(event.repo.id, event.repo);
            ContentValues eventValues = new ContentValues();
            eventValues.put(EventsContract.EventColumns.COLUMN_EVENT_ID, event.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_REPO_ID, event.repo.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_TYPE, event.type);
            eventValues.put(EventsContract.EventColumns.COLUMN_USER_ID, event.actor.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_CREATED_AT, event.createdAt);
            eventValuesVector.add(eventValues);
        }

        Vector<ContentValues> userValuesVector = new Vector<>(users.size());
        for (User user : users.values()) {
            ContentValues userValues = new ContentValues();
            userValues.put(UsersContract.UserColumns.COLUMN_USER_ID, user.id);
            userValues.put(UsersContract.UserColumns.COLUMN_EMAIL, user.email);
            userValues.put(UsersContract.UserColumns.COLUMN_LOGIN, user.login);
            userValues.put(UsersContract.UserColumns.COLUMN_NAME, user.name);
            userValues.put(UsersContract.UserColumns.COLUMN_EMAIL, user.email);
            userValues.put(UsersContract.UserColumns.COLUMN_AVATAR_URL, user.avatarUrl);
            userValues.put(UsersContract.UserColumns.COLUMN_BIO, user.bio);
            userValues.put(UsersContract.UserColumns.COLUMN_FOLLOWERS, user.followers);
            userValues.put(UsersContract.UserColumns.COLUMN_FOLLOWING, user.following);
            userValues.put(UsersContract.UserColumns.COLUMN_LOCATION, user.location);
            userValues.put(UsersContract.UserColumns.COLUMN_PUBLIC_REPOS, user.publicRepos);
            userValuesVector.add(userValues);
        }

        Vector<ContentValues> reposValuesVector = new Vector<>(repos.size());
        for (Repository repo : repos.values()) {
            ContentValues repoValues = new ContentValues();
            repoValues.put(RepoContract.RepoColumns.COLUMN_REPO_ID, repo.id);
            repoValues.put(RepoContract.RepoColumns.COLUMN_NAME, repo.name);
            repoValues.put(RepoContract.RepoColumns.COLUMN_LANGUAGE, repo.language);
            repoValues.put(RepoContract.RepoColumns.COLUMN_WATCHERS_COUNT, repo.watchersCount);
            repoValues.put(RepoContract.RepoColumns.COLUMN_STARGAZERS_COUNT, repo.stargazersCount);
            repoValues.put(RepoContract.RepoColumns.COLUMN_FORKS_COUNT, repo.forksCount);
            repoValues.put(RepoContract.RepoColumns.COLUMN_TOPICS,  implode(",", repo.topics));
            reposValuesVector.add(repoValues);
        }

        if (eventValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[eventValuesVector.size()];
            eventValuesVector.toArray(cvArray);

            // delete old data
            if (mIsRefreshing) {
                mContext.getContentResolver().delete(EventsContract.EventColumns.CONTENT_URI, null, null);
            }
            mContext.getContentResolver().bulkInsert(EventsContract.EventColumns.CONTENT_URI, cvArray);
        }

        if (userValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[userValuesVector.size()];
            userValuesVector.toArray(cvArray);

            // delete old data
            if (mIsRefreshing) {
                mContext.getContentResolver().delete(UsersContract.UserColumns.CONTENT_URI, null, null);
            }

            mContext.getContentResolver().bulkInsert(UsersContract.UserColumns.CONTENT_URI, cvArray);
        }
        if (reposValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[reposValuesVector.size()];
            reposValuesVector.toArray(cvArray);

            // delete old data
            if (mIsRefreshing) {
                mContext.getContentResolver().delete(RepoContract.RepoColumns.CONTENT_URI, null, null);
            }
            mContext.getContentResolver().bulkInsert(RepoContract.RepoColumns.CONTENT_URI, cvArray);
        }

        Log.d(TAG, "Sync complete. " + eventValuesVector.size() + " events inserted");
        Log.d(TAG, userValuesVector.size() + " users inserted");
        Log.d(TAG, reposValuesVector.size() + " repos inserted");
    }

    public static String implode(String separator, String... data) {
        if (data == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length - 1; i++) {
            //data.length - 1 => to not add separator at the end
            if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
                sb.append(data[i]);
                sb.append(separator);
            }
        }
        sb.append(data[data.length - 1].trim());
        return sb.toString();
    }
}
