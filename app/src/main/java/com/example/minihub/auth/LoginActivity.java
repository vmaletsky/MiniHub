package com.example.minihub.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.minihub.MainActivity;
import com.example.minihub.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static String CLIENT_ID = "651300e25050131ec8ef";
    private static String CLIENT_SECRET = "04e7098776e98846d0170429971b847d2fbc8ad0";
    private static String REDIRECT_URI = "minihub://callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.login_button);
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
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                AuthAsyncTask task = new AuthAsyncTask();
                task.execute(code);

            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
                Toast.makeText(this, uri.getQueryParameter("error").toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class AuthAsyncTask extends AsyncTask<String, Void, AccessToken> {
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
            if (accessToken != null) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.access_token_pref_id), accessToken.getAccessToken()).apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
