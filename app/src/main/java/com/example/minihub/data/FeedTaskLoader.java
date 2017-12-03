package com.example.minihub.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by Daryna on 21.10.17.
 */

public class FeedTaskLoader extends AsyncTaskLoader {


    public FeedTaskLoader(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {
        return null;
    }
}

