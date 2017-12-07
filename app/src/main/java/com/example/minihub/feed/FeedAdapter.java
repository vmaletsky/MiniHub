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
    private String TAG = getClass().getSimpleName();
    private Context mContext;


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
        }
    }

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
                    getActionByEventType(event.type, event.payload) + " " + event.repo.name));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = sdf.parse(event.createdAt);
                timeTextView.setText(TimeAgo.using(date.getTime()));
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }

            Glide.with(mContext)
                    .load(actor.avatarUrl)
                    .override(152, 152)
                    .centerCrop()
                    .into(avatar);
        }
    }

    public String getActionByEventType(String eventType, Payload payload) {
        Resources res = mContext.getResources();
        StringBuilder sb = new StringBuilder();
        switch (eventType) {
            case "PushEvent": {
                String[] string = {
                        makeStringBold(res.getQuantityString(R.plurals.pushed_commits, payload.size, payload.size)),
                        mContext.getString(R.string.to),
                        getBranchFromRef(payload.ref),
                        mContext.getString(R.string.at)
                };
                return TextUtils.join(" ", Arrays.asList(string));
            }
            case "IssueCommentEvent": return makeStringBold(getIssueCommentAction(payload)) + " "
                    + mContext.getString(R.string.on_issue_in);
            case "PullRequestEvent": {
                return makeStringBold(getPullRequestAction(payload) + " "
                        + mContext.getString(R.string.pull_request)) + " "
                        + mContext.getString(R.string.in);
            }
            case "CommitCommentEvent": return mContext.getString(R.string.commented_on_commit)
                    + " " + mContext.getString(R.string.in);
            case "IssuesEvent": return makeStringBold(payload.action) + " " + mContext.getString(R.string.issue_in);
            case "CreateEvent": return makeStringBold(mContext.getString(R.string.created) + " " + payload.ref_tag) +
                    " " + mContext.getString(R.string.in);
            case "PullRequestReviewEvent":
                return makeStringBold(getPullRequestReviewAction(payload)) + " "
                    + mContext.getString(R.string.in);
            case "PullRequestReviewCommentEvent":
                return makeStringBold(getPullRequestReviewCommentAction(payload)) + " " +
                    mContext.getString(R.string.in);
        }
        return eventType;
    }

    private String makeStringBold(String str) {
        return "<b>" + str + "</b>";
    }

    private String getPullRequestReviewAction(Payload payload) {
        switch (payload.action) {
            case "submitted": return mContext.getString(R.string.pull_request_review_action_reviewed);
            case "edited": return mContext.getString(R.string.pull_request_review_action_edited);
            case "dismissed": return mContext.getString(R.string.pull_request_review_action_dismissed);
        }
        return null;
    }

    private String getPullRequestReviewCommentAction(Payload payload) {
        switch (payload.action) {
            case "created": return mContext.getString(R.string.pull_request_review_comment_commented);
            case "edited": return mContext.getString(R.string.pull_request_review_comment_edited);
            case "deleted": return mContext.getString(R.string.pull_request_review_comment_deleted);
        }
        return null;
    }

    public String getIssueCommentAction(Payload payload) {
        if (payload.action.equals("created")) {
            return mContext.getString(R.string.commented);
        } else return payload.action  + mContext.getString(R.string.comment);
    }

    public String getPullRequestAction(Payload payload) {
        if (payload.action.equals("closed")) {
            if (payload.merged) return mContext.getString(R.string.merged);
            else return mContext.getString(R.string.closed);
        } else {
            return payload.action;
        }
    }

    public static String getBranchFromRef(String ref) {
        String[] splitted = ref.split("/");
        return splitted[splitted.length-1];
    }
}
