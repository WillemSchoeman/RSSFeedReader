package com.example.shumy.rss_feed_reader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            Log.d(TAG,"getView: called with null convertView");
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            Log.d(TAG,"getView: provided a convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);

        viewHolder.nameTextView.setText(currentApp.getName());
        viewHolder.artistTextView.setText(currentApp.getArtist());
        viewHolder.summaryTextView.setText(currentApp.getSummary());

        return convertView;
    }

    private class ViewHolder {
        final TextView nameTextView;
        final TextView artistTextView;
        final TextView summaryTextView;

        ViewHolder(View v) {
            this.nameTextView = v.findViewById(R.id.nameTextView);
            this.artistTextView = v.findViewById(R.id.artistTextView);
            this.summaryTextView = v.findViewById(R.id.summaryTV);
        }
    }
}

