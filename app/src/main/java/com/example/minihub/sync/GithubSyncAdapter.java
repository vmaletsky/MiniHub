package com.example.minihub.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.User;
import com.example.minihub.network.GithubService;
import com.example.minihub.network.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import retrofit2.Response;

/**
 * Created by volod on 9/5/2017.
 */

public class GithubSyncAdapter extends AbstractThreadedSyncAdapter {

    private final String TAG = getClass().getSimpleName();

    public GithubSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync - starting sync");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getContext().getString(R.string.access_token_pref_id), null);
        GithubService service = ServiceGenerator.createService(GithubService.class, token);
        List<FeedEvent> events;
        try {
            Response<List<FeedEvent>> response = service.getUserEvents().execute();
            events = response.body();
            cacheEvents(events);
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }
    }

    private void cacheEvents(List<FeedEvent> events) {
        Vector<ContentValues> eventValuesVector = new Vector<ContentValues>(events.size());
        Vector<ContentValues> userValuesVector = new Vector<>(events.size());

        HashMap<Integer, User> users = new HashMap<>();
        for (FeedEvent event : events) {
            users.put(event.actor.id, event.actor);
            ContentValues eventValues = new ContentValues();
            eventValues.put(EventsContract.EventColumns.COLUMN_EVENT_ID, event.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_REPO_ID, event.repo.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_TYPE, event.type);
            eventValues.put(EventsContract.EventColumns.COLUMN_USER_ID, event.actor.id);
            eventValues.put(EventsContract.EventColumns.COLUMN_CREATED_AT, event.createdAt);
            eventValuesVector.add(eventValues);
        }

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

        if (eventValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[eventValuesVector.size()];
            eventValuesVector.toArray(cvArray);

            // delete old data
            getContext().getContentResolver().delete(EventsContract.EventColumns.CONTENT_URI, null, null);

            getContext().getContentResolver().bulkInsert(EventsContract.EventColumns.CONTENT_URI, cvArray);
        }

        if (userValuesVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[userValuesVector.size()];
            userValuesVector.toArray(cvArray);

            // delete old data
            getContext().getContentResolver().delete(UsersContract.UserColumns.CONTENT_URI, null, null);

            getContext().getContentResolver().bulkInsert(UsersContract.UserColumns.CONTENT_URI, cvArray);
        }
        Log.d(TAG, "Sync complete. " + eventValuesVector.size() + " events inserted");
        Log.d(TAG, userValuesVector.size() + " users inserted");
    }
}
