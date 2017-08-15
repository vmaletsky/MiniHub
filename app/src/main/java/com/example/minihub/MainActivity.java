package com.example.minihub;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FeedFragment.OnFragmentInteractionListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;

    FragmentManager mFragmentManager;
    private FeedFragment mFeedFragment;

    @Override
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

    private void showFeed() {
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

    private void showRepositoriesList() {
        RepositoriesFragment repositoriesFragment = new RepositoriesFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, repositoriesFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showUserInfo() {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        mFragmentManager.beginTransaction()
                .replace(R.id.feed_container, userInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    private void logout() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(getString(R.string.access_token_pref_id))
                .apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
