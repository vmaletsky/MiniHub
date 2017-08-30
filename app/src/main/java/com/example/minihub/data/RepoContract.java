package com.example.minihub.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by v.maletskiy on 8/29/2017.
 */

public class RepoContract {

    public static final String CONTENT_AUTHORITY = "com.example.minihub";

    public static final String TABLE_NAME = "repos";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class RepoEntry implements BaseColumns {
        public static final String COLUMN_REPO_ID = "id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_USER_ID = "user_id";


    }
}
