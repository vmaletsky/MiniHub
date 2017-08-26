package com.example.minihub.user_info;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.minihub.GithubService;
import com.example.minihub.R;
import com.example.minihub.ServiceGenerator;
import com.example.minihub.data.User;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by volod on 8/26/2017.
 */

class UserInfoTask extends AsyncTask<String, Void, User> {
    private String TAG = getClass().getSimpleName();

    public  UserInfoListener listener;

    public interface UserInfoListener {
        void onUserInfoLoaded(User user);
    }

    public UserInfoTask(UserInfoListener listener) {
        this.listener = listener;
    }

    @Override
    protected User doInBackground(String... params) {
        String authToken = params[0];
        User user = new User();
        GithubService githubService = ServiceGenerator.createService(GithubService.class, authToken);
        try {
            Response<User> response = githubService.getAuthenticatedUser().execute();
            user = response.body();
            Log.v(TAG, user.login);
        } catch (IOException e) {

        }

        return user;
    }


    @Override
    protected void onPostExecute(User user) {
        listener.onUserInfoLoaded(user);
    }
}
