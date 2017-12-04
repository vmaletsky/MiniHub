package com.example.minihub.auth;

import android.os.AsyncTask;
import android.util.Log;

import com.example.minihub.network.GithubService;
import com.example.minihub.network.ServiceGenerator;
import com.example.minihub.domain.User;

import java.io.IOException;

import retrofit2.Response;

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
