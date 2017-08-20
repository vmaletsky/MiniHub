package com.example.minihub.feed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.minihub.R;

import java.util.List;

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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventTextView;

        public TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.eventTextView = (TextView) itemView.findViewById(R.id.event_text);
            this.timeTextView = (TextView) itemView.findViewById(R.id.time_text);
        }
    }
}
