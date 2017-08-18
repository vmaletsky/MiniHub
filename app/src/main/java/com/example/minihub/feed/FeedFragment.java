package com.example.minihub.feed;

import android.content.Context;
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
import com.example.minihub.user_info.UserInfoFragment;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.minihub.ServiceGenerator.createService;



public class FeedFragment extends Fragment {
    String TAG = getClass().getSimpleName();

    @BindView(R.id.feed_list)
    public RecyclerView mFeedList;
    public FeedAdapter mFeedAdapter;


    private OnFragmentInteractionListener mListener;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFeedList.setLayoutManager(layoutManager);
        mFeedList.setHasFixedSize(true);
        ArrayList<FeedEvent> eventsList = new ArrayList<>();
        FeedEvent event1 = new FeedEvent();
        event1.name = "Event1";
        event1.time = "Today";
        FeedEvent event2 = new FeedEvent();
        event2.name = "Event2";
        event2.time = "Today";
        FeedEvent event3 = new FeedEvent();
        event3.time = "Today";
        event3.name = "Event3";

        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event3);

        mFeedAdapter = new FeedAdapter(eventsList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mFeedList.getContext(),
                layoutManager.getOrientation());
        mFeedList.addItemDecoration(dividerItemDecoration);
        mFeedList.setAdapter(mFeedAdapter);

        EventsAsyncTask task = new EventsAsyncTask();
        task.execute();

        Timber.d("Adapter size: " + mFeedAdapter.getItemCount());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class EventsAsyncTask extends AsyncTask<Void, Void, FeedEvent[]> {
        @Override
        protected FeedEvent[] doInBackground(Void... params) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(FeedFragment.this.getActivity());
            String authToken = sp.getString(getString(R.string.access_token_pref_id), null);
            GithubService service = ServiceGenerator.createService(GithubService.class, authToken);

            try {
                Response<FeedEvent[]> response = service.getPublicEvents().execute();
                Log.v(TAG, "Received events : " + String.valueOf(response.body().length));
            } catch (IOException e) {
                Log.v(TAG, e.getMessage());
            }

            return null;
        }
    }
}
