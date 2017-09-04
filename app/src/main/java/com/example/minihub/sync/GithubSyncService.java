package com.example.minihub.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by volod on 9/5/2017.
 */

public class GithubSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static GithubSyncAdapter sSyncAdapter = null;

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new GithubSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
