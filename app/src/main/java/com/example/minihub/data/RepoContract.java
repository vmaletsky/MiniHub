package com.example.minihub.data;

import android.provider.BaseColumns;

/**
 * Created by Daryna on 03.09.17.
 */

public class RepoContract {
    public class RepoColumns implements BaseColumns {
        String COLUMN_REPO_ID = "id";

        String COLUMN_NAME = "name";

        String COLUMN_USER_ID = "user_id";
    }
}