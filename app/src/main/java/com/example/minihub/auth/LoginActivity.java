package com.example.minihub.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.minihub.domain.User;
import com.example.minihub.navigation.NavigationActivity;
import com.example.minihub.R;
import com.example.minihub.domain.AccessToken;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    private static String CLIENT_ID = "651300e25050131ec8ef";
    private static String CLIENT_SECRET = "04e7098776e98846d0170429971b847d2fbc8ad0";
    private static String REDIRECT_URI = "minihub://callback";

    @BindView(R.id.login_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://github.com/login/oauth/authorize" +
                                "?client_id=" + CLIENT_ID +
                                "&client_secret=" + CLIENT_SECRET +
                                "scope=public_repo&redirect_uri=" + REDIRECT_URI));
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                presenter.requestAuthToken(code);
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
                Toast.makeText(this, uri.getQueryParameter("error"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveAuthToken(AccessToken token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.access_token_pref_id), token.getAccessToken())
                .apply();
    }

    @Override
    public void openNavigationActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return new LoginPresenter(sp, this);
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sp.getString(getString(R.string.access_token_pref_id), null);
        return token;
    }

}
