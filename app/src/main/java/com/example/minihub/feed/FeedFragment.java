package com.example.minihub.feed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minihub.GithubService;
import com.example.minihub.R;
import com.example.minihub.ServiceGenerator;
import com.example.minihub.auth.LoginActivity;
import com.example.minihub.data.FeedEvent;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.minihub.ServiceGenerator.createService;



public class FeedFragment extends Fragment implements FeedContract.View {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.feed_list)
    public RecyclerView mFeedList;
    public FeedAdapter mFeedAdapter;

    public FeedContract.Presenter mPresenter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);
        Timber.plant(new Timber.Tree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                Log.v(tag, message);
            }
        });
        Timber.d("Adapter size: " + mFeedAdapter.getItemCount());
        return view;
    }

    @Override
    public void setPresenter(FeedContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showEvents() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFeedList.setLayoutManager(layoutManager);
        mFeedList.setHasFixedSize(true);
        ArrayList<FeedEvent> eventsList = new ArrayList<>();
        mFeedAdapter = new FeedAdapter(eventsList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFeedList.getContext(),
                layoutManager.getOrientation());
        mFeedList.addItemDecoration(dividerItemDecoration);
        mFeedList.setAdapter(mFeedAdapter);
        mPresenter.getUserEvents();
    }

    @Override
    public void removeAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(getString(R.string.access_token_pref_id))
                .apply();
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public String getAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String accessToken = sp.getString(getString(R.string.access_token_pref_id), null);
        return accessToken;
    }
}
