package com.example.minihub.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.minihub.R;
import com.example.minihub.auth.LoginActivity;
import com.example.minihub.feed.FeedFragment;
import com.example.minihub.user_info.UserInfoFragment;
import com.example.minihub.user_repos.UserReposFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationActivity extends AppCompatActivity implements NavigationContract.View {
    @BindView(R.id.drawer_layout)
    public DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    public NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;

    FragmentManager mFragmentManager;
    private FeedFragment mFeedFragment;

    private NavigationContract.Presenter mPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectMenuItem(item);
                return true;
            }
        });
        mFeedFragment = new FeedFragment();
        mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction()
                .add(R.id.feed_container, mFeedFragment, null)
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFeed() {
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, mFeedFragment)
                .commit();
    }

    private void selectMenuItem(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_feed: showFeed(); break;
            case R.id.item_repos : showRepositoriesList(); break;
            case R.id.item_user_info : showUserInfo(); break;
            case R.id.item_log_out: mPresenter.logout(); break;
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void showRepositoriesList() {
        UserReposFragment repositoriesFragment = new UserReposFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, repositoriesFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showUserInfo() {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, userInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    public void removeAccessToken() {

    }


    @Override
    public void setPresenter(NavigationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
