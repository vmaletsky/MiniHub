package com.example.minihub.feed;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minihub.R;
import com.example.minihub.Utilities;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.Payload;
import com.example.minihub.domain.Repository;
import com.example.minihub.domain.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private String TAG = getClass().getSimpleName();

    private CursorAdapter mCursorAdapter;

    private Context mContext;

    public FeedAdapter(Context context, Cursor c) {
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = LayoutInflater.from(context).inflate(R.layout.feed_element, parent, false);

                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);

                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                ViewHolder holder = (ViewHolder) view.getTag();
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
                holder.eventTextView.setText(Html.fromHtml(actor.login  +
                        Utilities.getActionByEventType(event.type, event.payload) + event.repo.name));
                Glide.with(context)
                        .load(actor.avatarUrl)
                        .override(152, 152)
                        .centerCrop()
                        .into(holder.avatar);
            }
        };
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }


    public void swapCursor(Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    public void dataSetChanged() {
        mCursorAdapter.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatar;

        public TextView eventTextView;

        public TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            this.eventTextView = (TextView) itemView.findViewById(R.id.event_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
