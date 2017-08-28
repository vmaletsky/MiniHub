package com.example.minihub.feed;

import com.example.minihub.data.FeedEvent;
import com.example.minihub.network.EventsAsyncTask;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;


/**
 * Created by v.maletskiy on 8/21/2017.
 */

public class FeedPresenter extends MvpBasePresenter<FeedView> {
    private String TAG = getClass().getSimpleName();

    EventsAsyncTask task;


    public void getEvents() {
        String token = getView().getAccessToken();
        task = new EventsAsyncTask(new EventsAsyncTask.FeedTaskListener() {
            @Override
            public void onFeedReceived(List<FeedEvent> events) {
                if (isViewAttached()) {
                    getView().setData(events);
                }
            }
        });
        task.execute(token);
        getView().showEvents();
    }

    @Override
    public void detachView(boolean retainInstance) {
        task.cancel(true);
    }
}
