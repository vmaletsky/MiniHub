package com.example.minihub.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
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

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

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

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        GithubSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, EventsContract.CONTENT_AUTHORITY, true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = EventsContract.CONTENT_AUTHORITY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                EventsContract.CONTENT_AUTHORITY, bundle);
    }

}
