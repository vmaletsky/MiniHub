package com.example.minihub.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.data.RepoContract;
import com.example.minihub.data.UsersContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Payload;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;
import com.example.minihub.feed.FeedActionStringsBuilder;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.example.minihub.feed.FeedPresenter.COL_ACTION;
import static com.example.minihub.feed.FeedPresenter.COL_AVATAR_URL;
import static com.example.minihub.feed.FeedPresenter.COL_CREATED_AT;
import static com.example.minihub.feed.FeedPresenter.COL_EVENT_ID;
import static com.example.minihub.feed.FeedPresenter.COL_EVENT_TYPE;
import static com.example.minihub.feed.FeedPresenter.COL_MERGED;
import static com.example.minihub.feed.FeedPresenter.COL_REF;
import static com.example.minihub.feed.FeedPresenter.COL_REF_TAG;
import static com.example.minihub.feed.FeedPresenter.COL_REPO_ID;
import static com.example.minihub.feed.FeedPresenter.COL_REPO_NAME;
import static com.example.minihub.feed.FeedPresenter.COL_SIZE;
import static com.example.minihub.feed.FeedPresenter.COL_USER_ID;
import static com.example.minihub.feed.FeedPresenter.COL_USER_LOGIN;
import static com.example.minihub.feed.FeedPresenter.COL_USER_NAME;

public class GithubRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;

    String TAG = getClass().getSimpleName();

    GithubRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "Widget onCreate");
    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = EventsContract.EventColumns.CONTENT_URI;
        String sortOrder = EventsContract.EventColumns.COLUMN_CREATED_AT + " DESC LIMIT 5";
        mCursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                sortOrder);
        Log.v(TAG, "Widget onDataSetChanged: " + mCursor.getCount());
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        FeedEvent event = new FeedEvent();
        event.id = mCursor.getLong(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_EVENT_ID));
        event.createdAt = mCursor.getString(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_CREATED_AT));
        event.type = mCursor.getString(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_TYPE));
        event.payload = new Payload();
        event.payload.action = mCursor.getString(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_PAYLOAD_ACTION));
        event.payload.ref_tag = mCursor.getString(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_PAYLOAD_REF_TAG));
        event.payload.ref = mCursor.getString(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_PAYLOAD_REF));
        event.payload.size = mCursor.getInt(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_PAYLOAD_SIZE));
        event.payload.merged = (mCursor.getInt(mCursor.getColumnIndex(EventsContract.EventColumns.COLUMN_PAYLOAD_MERGED)) == 1);
        event.repo = new Repository();
        event.repo.id = mCursor.getInt(mCursor.getColumnIndex(RepoContract.RepoColumns.COLUMN_REPO_ID));
        event.repo.name = mCursor.getString(mCursor.getColumnIndex(RepoContract.RepoColumns.COLUMN_NAME));
        event.actor = new User();
        event.actor.id = mCursor.getInt(mCursor.getColumnIndex(UsersContract.UserColumns.COLUMN_USER_ID));
        event.actor.login = mCursor.getString(mCursor.getColumnIndex(UsersContract.UserColumns.COLUMN_LOGIN));
        event.actor.name = mCursor.getString(mCursor.getColumnIndex(UsersContract.UserColumns.COLUMN_NAME));
        event.actor.avatarUrl = mCursor.getString(mCursor.getColumnIndex(UsersContract.UserColumns.COLUMN_AVATAR_URL));
        User actor = event.actor;

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        FeedActionStringsBuilder feedActionStringsBuilder = new FeedActionStringsBuilder(mContext, false);
        rv.setTextViewText(R.id.event_text_widget, actor.login + " " +
                feedActionStringsBuilder.getActionByEventType(event.type, event.payload) + " " + event.repo.name);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = sdf.parse(event.createdAt);
            rv.setTextViewText(R.id.time_text_widget, TimeAgo.using(date.getTime()));
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
