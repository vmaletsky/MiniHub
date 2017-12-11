package com.example.minihub.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;



public class GithubRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GithubRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
