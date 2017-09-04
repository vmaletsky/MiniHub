package com.example.minihub.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by volod on 9/5/2017.
 */

public class AuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private GithubAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new GithubAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
