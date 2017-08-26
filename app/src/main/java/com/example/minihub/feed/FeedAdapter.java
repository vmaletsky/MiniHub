package com.example.minihub.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minihub.R;
import com.example.minihub.data.FeedEvent;
import com.example.minihub.data.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by v.maletskiy on 7/18/2017.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {


    public FeedAdapter(List<FeedEvent> eventsList) {
        this.mEventsList = eventsList;
    }

    private List<FeedEvent> mEventsList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_element, parent, false);
        return new ViewHolder(itemView);
    }

    public void setEvents(List<FeedEvent> events) {
        mEventsList = events;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedEvent event = mEventsList.get(position);
        User actor = event.actor;
        holder.eventTextView.setText(actor.login + " made action  " + event.type + " to " + event.repo.name);
        Glide.with(holder.itemView.getContext())
                .load(actor.avatarUrl)
                .override(152, 152)
                .centerCrop()
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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
