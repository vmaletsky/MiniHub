package com.example.minihub.feed;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.minihub.R;
import com.example.minihub.Utilities;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Payload;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;
import com.example.minihub.network.FeedAsyncTask;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

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
import static com.example.minihub.network.FeedAsyncTask.ERROR_INCORRECT_DATA;
import static com.example.minihub.network.FeedAsyncTask.ERROR_NO_NETWORK;

public class FeedAdapter extends RecyclerViewCursorAdapter<FeedAdapter.ViewHolder> {
    public interface OnLastElementReached {
        void onLastElementReached();
    }

    private String TAG = getClass().getSimpleName();
    private Context mContext;

    public OnLastElementReached mOnLastElementReachedListener;

    private LayoutInflater mInflater;

    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }

        return VIEWTYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount() + 1;
    }

    protected FeedAdapter(Context context) {
        super(context);
        this.mContext = context;
        this.feedActionStringsBuilder = new FeedActionStringsBuilder(this.mContext, true);
        this.mInflater = LayoutInflater.from(mContext);
        setupCursorAdapter(null, 0, R.layout.feed_element, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_ITEM) {
            return new ViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent), VIEWTYPE_ITEM);
        } else if (viewType == VIEWTYPE_LOADER) {
            View view = mInflater.inflate(R.layout.loader_item_layout, parent, false);
            return new ViewHolder(view, VIEWTYPE_LOADER);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mCursorAdapter.getCount()) {
            mCursorAdapter.getCursor().moveToPosition(position);
            setViewHolder(holder);
            mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
            if (position == mCursorAdapter.getCount() - 1) {
                mOnLastElementReachedListener.onLastElementReached();
            }
        }
    }

    FeedActionStringsBuilder feedActionStringsBuilder;

    class ViewHolder extends RecyclerViewCursorViewHolder {


        public CircleImageView avatar;

        public TextView eventTextView;

        public TextView timeTextView;

        public ProgressBar progressBar;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEWTYPE_ITEM) {
                this.avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
                this.eventTextView = (TextView) itemView.findViewById(R.id.event_text);
                this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
            } else if (viewType == VIEWTYPE_LOADER) {
                this.progressBar = (ProgressBar) itemView.findViewById(R.id.item_progress_bar);
            }
        }

        @Override
        public void bindCursor(Cursor cursor) {
            FeedEvent event = new FeedEvent();
            event.id = cursor.getLong(COL_EVENT_ID);
            event.createdAt = cursor.getString(COL_CREATED_AT);
            event.type = cursor.getString(COL_EVENT_TYPE);
            event.payload = new Payload();
            event.payload.action = cursor.getString(COL_ACTION);
            event.payload.ref_tag = cursor.getString(COL_REF_TAG);
            event.payload.ref = cursor.getString(COL_REF);
            event.payload.size = cursor.getInt(COL_SIZE);
            event.payload.merged = (cursor.getInt(COL_MERGED) == 1);
            event.repo = new Repository();
            event.repo.id = cursor.getInt(COL_REPO_ID);
            event.repo.name = cursor.getString(COL_REPO_NAME);
            event.actor = new User();
            event.actor.id = cursor.getInt(COL_USER_ID);
            event.actor.login = cursor.getString(COL_USER_LOGIN);
            event.actor.name = cursor.getString(COL_USER_NAME);
            event.actor.avatarUrl = cursor.getString(COL_AVATAR_URL);
            User actor = event.actor;
            eventTextView.setText(Html.fromHtml(actor.login + " " +
                    feedActionStringsBuilder.getActionByEventType(event.type, event.payload) + " " + event.repo.name));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = sdf.parse(event.createdAt);
                timeTextView.setText(TimeAgo.using(date.getTime()));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }

            RequestOptions myOptions = new RequestOptions()
                    .override(152, 152)
                    .centerCrop();


            Glide.with(mContext)
                    .load(actor.avatarUrl)
                    .apply(myOptions)
                    .into(avatar);
        }
    }


}
