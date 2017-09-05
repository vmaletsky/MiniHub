package com.example.minihub.feed;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minihub.R;
import com.example.minihub.data.EventsContract;
import com.example.minihub.domain.FeedEvent;
import com.example.minihub.domain.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by v.maletskiy on 7/18/2017.
 */

public class FeedAdapter extends CursorAdapter {

    public FeedAdapter(Context context, Cursor c) {
        super(context, c);
    }

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
        event.id = cursor.getInt(FeedFragment.COL_EVENT_ID);
        event.createdAt = cursor.getString(FeedFragment.COL_CREATED_AT);
        event.type = cursor.getString(FeedFragment.COL_EVENT_TYPE);
        event.repo.id = cursor.getInt(FeedFragment.COL_REPO_ID);
        event.repo.name = cursor.getString(FeedFragment.COL_REPO_NAME);
        event.actor.id = cursor.getInt(FeedFragment.COL_USER_ID);
        event.actor.login = cursor.getString(FeedFragment.COL_USER_LOGIN);
        event.actor.name = cursor.getString(FeedFragment.COL_USER_NAME);
        event.actor.avatarUrl = cursor.getString(FeedFragment.COL_AVATAR_URL);

        User actor = event.actor;
        holder.eventTextView.setText(actor.login + " made action  " + event.type + " to " + event.repo.name);
        Glide.with(context)
                .load(actor.avatarUrl)
                .override(152, 152)
                .centerCrop()
                .into(holder.avatar);
    }

    static class ViewHolder {

        public CircleImageView avatar;

        public TextView eventTextView;

        public TextView timeTextView;

        public ViewHolder(View itemView) {
            this.avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            this.eventTextView = (TextView) itemView.findViewById(R.id.event_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
