package com.example.minihub.feed;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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

import de.hdodenhof.circleimageview.CircleImageView;

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
            event.id = cursor.getLong(FeedFragment.COL_EVENT_ID);
            event.createdAt = cursor.getString(FeedFragment.COL_CREATED_AT);
            event.type = cursor.getString(FeedFragment.COL_EVENT_TYPE);
            event.payload = new Payload();
            event.payload.action = cursor.getString(FeedFragment.COL_ACTION);
            event.payload.ref_tag = cursor.getString(FeedFragment.COL_REF_TAG);
            event.payload.ref = cursor.getString(FeedFragment.COL_REF);
            event.payload.size = cursor.getInt(FeedFragment.COL_SIZE);
            event.payload.merged = (cursor.getInt(FeedFragment.COL_MERGED) == 1);
            event.repo = new Repository();
            event.repo.id = cursor.getInt(FeedFragment.COL_REPO_ID);
            event.repo.name = cursor.getString(FeedFragment.COL_REPO_NAME);
            event.actor = new User();
            event.actor.id = cursor.getInt(FeedFragment.COL_USER_ID);
            event.actor.login = cursor.getString(FeedFragment.COL_USER_LOGIN);
            event.actor.name = cursor.getString(FeedFragment.COL_USER_NAME);
            event.actor.avatarUrl = cursor.getString(FeedFragment.COL_AVATAR_URL);

            User actor = event.actor;
            eventTextView.setText(Html.fromHtml(actor.login +
                    Utilities.getActionByEventType(event.type, event.payload) + event.repo.name));
            Glide.with(mContext)
                    .load(actor.avatarUrl)
                    .override(152, 152)
                    .centerCrop()
                    .into(avatar);
        }
    }
}
