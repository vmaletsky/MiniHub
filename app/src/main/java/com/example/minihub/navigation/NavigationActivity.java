package com.example.minihub.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.minihub.R;
import com.example.minihub.auth.LoginActivity;
import com.example.minihub.feed.FeedFragment;
import com.example.minihub.user_info.UserInfoFragment;
import com.example.minihub.user_repos.UserReposFragment;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import butterknife.ButterKnife;

public class NavigationActivity extends MvpActivity<NavigationView, NavigationPresenter> implements NavigationView, AdapterView.OnItemClickListener {

    public DrawerLayout mDrawerLayout;

    public android.support.design.widget.NavigationView mNavigationView;

    ListView mMenuItems;

    private ActionBarDrawerToggle mDrawerToggle;

    FragmentManager mFragmentManager;
    private FeedFragment mFeedFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (android.support.design.widget.NavigationView) findViewById(R.id.navigation_view);
        mMenuItems = (ListView) findViewById(R.id.menu_items);
        mFragmentManager = getSupportFragmentManager();
        if (mNavigationView != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);

            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.setDrawerListener(mDrawerToggle);
            mNavigationView.setNavigationItemSelectedListener(new android.support.design.widget.NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    selectMenuItem(item);
                    return true;
                }
            });
        } else {
            String[] menuItems = getResources().getStringArray(R.array.menu_items);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_list_item, menuItems);
            mMenuItems.setAdapter(adapter);
            mMenuItems.setOnItemClickListener(this);
        }
        if (savedInstanceState == null) {
            mFeedFragment = new FeedFragment();
            mUserInfoFragment = new UserInfoFragment();
            mUserReposFragment = new UserReposFragment();


            mFragmentManager.beginTransaction()
                    .replace(R.id.feed_container, mFeedFragment, null)
                    .commit();
        } else {
            //Restore the fragment's instance
            mFeedFragment = (FeedFragment) getSupportFragmentManager().getFragment(savedInstanceState, "FeedFragment");
            mUserReposFragment = (UserReposFragment) getSupportFragmentManager().getFragment(savedInstanceState, "UserReposFragment");
            mUserInfoFragment = (UserInfoFragment) getSupportFragmentManager().getFragment(savedInstanceState, "UserInfoFragment");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @NonNull
    @Override
    public NavigationPresenter createPresenter() {
        return new NavigationPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event

        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFeedFragment != null && mFeedFragment.isAdded()) {
            mFragmentManager.putFragment(outState, "FeedFragment", mFeedFragment);
        }
        if (mUserInfoFragment != null && mUserInfoFragment.isAdded()) {
            mFragmentManager.putFragment(outState, "UserInfoFragment", mUserInfoFragment);
        }
        if (mUserReposFragment != null && mUserReposFragment.isAdded()) {
            mFragmentManager.putFragment(outState, "UserReposFragment", mUserReposFragment);
        }
    }

    @Override
    public void showFeed() {
        if (mFeedFragment == null) {
            mFeedFragment = new FeedFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, mFeedFragment)
                .commit();
    }

    private void selectMenuItem(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_feed: showFeed(); break;
            case R.id.item_repos : showRepositoriesList(); break;
            case R.id.item_user_info : showUserInfo(); break;
            case R.id.item_log_out: logout(); break;
        }
        mDrawerLayout.closeDrawers();
    }

    UserReposFragment mUserReposFragment;

    @Override
    public void showRepositoriesList() {
        if (mUserReposFragment == null) {
            mUserReposFragment = new UserReposFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, mUserReposFragment)
                .commit();
    }

    UserInfoFragment mUserInfoFragment;

    @Override
    public void showUserInfo() {
        if (mUserInfoFragment == null) {
            mUserInfoFragment = new UserInfoFragment();
        }
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, mUserInfoFragment)
                .commit();
    }

    public void removeAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(getString(R.string.access_token_pref_id));
    }

    public void logout() {
        removeAccessToken();
        openLoginActivity();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: showFeed(); break;
            case 1: showRepositoriesList(); break;
            case 2: showUserInfo(); break;
            case 3: logout(); break;
        }
    }
}
