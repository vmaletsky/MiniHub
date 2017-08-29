package com.example.minihub.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.minihub.domain.AccessToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by volod on 8/27/2017.
 */

public class AuthAsyncTask extends AsyncTask<String, Void, AccessToken> {
    private static String CLIENT_ID = "651300e25050131ec8ef";
    private static String CLIENT_SECRET = "04e7098776e98846d0170429971b847d2fbc8ad0";
    private static String REDIRECT_URI = "minihub://callback";

    public OnLoginListener listener;

    public interface OnLoginListener {
        void onLogin(AccessToken token);
    }

    public AuthAsyncTask(OnLoginListener listener) {
        this.listener = listener;
    }

    String TAG = this.getClass().getSimpleName();
    @Override
    protected AccessToken doInBackground(String... params) {
        AccessToken token = null;
        String code = params[0];
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(GsonConverterFactory.create());
        try {
            Retrofit retrofit = builder.build();
            LoginService service = retrofit.create(LoginService.class);
            Call<AccessToken> call = service.getAccessToken(CLIENT_ID, CLIENT_SECRET, code);
            token = call.execute().body();
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }

        return token;
    }

    @Override
    protected void onPostExecute(AccessToken accessToken) {
        listener.onLogin(accessToken);
    }
}