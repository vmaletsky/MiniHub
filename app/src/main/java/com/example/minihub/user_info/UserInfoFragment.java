package com.example.minihub.user_info;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.minihub.*;
import com.example.minihub.R;
import com.example.minihub.data.User;

import java.io.IOException;

import de.hdodenhof.circleimageview.*;
import retrofit2.Response;


public class UserInfoFragment extends Fragment {
    String TAG = getClass().getSimpleName();

    TextView mUserName;

    TextView mUserLogin;

    CircleImageView mUserAvatar;

    public UserInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.example.minihub.R.layout.fragment_user_info, container, false);
        mUserName = (TextView) view.findViewById(R.id.user_name);
        mUserAvatar = (CircleImageView) view.findViewById(R.id.user_pic);
        mUserLogin = (TextView) view.findViewById(R.id.user_login);
        EventsAsyncTask task = new EventsAsyncTask();
        task.execute();
        return view;
    }

    private void setUserName(String name) {
        mUserName.setText(name);
    }

    private void setUserLogin(String login) {
        mUserLogin.setText(login);
    }

    private void setUserAvatar(String avatarUrl) {
        Glide.with(this)
                .load(avatarUrl)
                .override(152, 152)
                .centerCrop()
                .into(mUserAvatar);
    }

    class EventsAsyncTask extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UserInfoFragment.this.getActivity());
            String authToken = sp.getString(getString(R.string.access_token_pref_id), null);
            User user = new User();
            GithubService githubService = ServiceGenerator.createService(GithubService.class, authToken);
            try {
                Response<User> response = githubService.getAuthenticatedUser().execute();
                user = response.body();
                Log.v(TAG,  user.login);
            } catch (IOException e) {
                Toast.makeText(UserInfoFragment.this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return user;
        }


        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            setUserName(user.name);
            setUserLogin(user.login);
            setUserAvatar(user.avatarUrl);
        }
    }
}
