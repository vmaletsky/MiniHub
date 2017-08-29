package com.example.minihub.user_info;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minihub.R;
import com.example.minihub.domain.User;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import de.hdodenhof.circleimageview.*;


public class UserInfoFragment extends MvpFragment<UserInfoView, UserInfoPresenter> implements UserInfoView {
    String TAG = getClass().getSimpleName();

    TextView mUserName;

    TextView mUserLogin;

    CircleImageView mUserAvatar;

    public UserInfoFragment() {

    }

    @Override
    public UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.example.minihub.R.layout.fragment_user_info, container, false);
        mUserName = (TextView) view.findViewById(R.id.user_name);
        mUserAvatar = (CircleImageView) view.findViewById(R.id.user_pic);
        mUserLogin = (TextView) view.findViewById(R.id.user_login);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getUserInfo();
    }

    @Override
    public void setUserName(String name) {
        mUserName.setText(name);
    }

    @Override
    public void setUserLogin(String login) {
        mUserLogin.setText(login);
    }

    @Override
    public void setUserAvatar(String avatarUrl) {
        Glide.with(this)
                .load(avatarUrl)
                .override(152, 152)
                .centerCrop()
                .into(mUserAvatar);
    }

    @Override
    public User getCurrentUser() { // TODO: this is bad
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userName = sp.getString(getString(R.string.current_user_name), null);
        String userLogin = sp.getString(getString(R.string.current_user_login), null);
        String userAvatar = sp.getString(getString(R.string.current_user_avatar_url), null);
        User user = new User();
        user.avatarUrl = userAvatar;
        user.login = userLogin;
        user.name = userName;
        return user;
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }
}
