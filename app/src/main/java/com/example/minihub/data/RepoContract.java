package com.example.minihub.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public final class RepoContract {
    public static final String CONTENT_AUTHORITY = "com.example.minihub.data";

    public static final String PATH_REPOS = "repos";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static class RepoColumns implements BaseColumns {

        public static final String TABLE_NAME = "repos";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPOS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REPOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REPOS;


        public static final String COLUMN_REPO_ID = "id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_USER_ID = "user_id";

        public static final String COLUMN_LANGUAGE = "language";

        public static final String COLUMN_TOPICS = "topics";

        public static final String COLUMN_WATCHERS_COUNT = "watchers_count";

        public static final String COLUMN_STARGAZERS_COUNT = "stargazers_count";

        public static final String COLUMN_FORKS_COUNT = "forks_count";

        public static final String COLUMN_STARGAZERS_URL = "stargazers_url";

        public static Uri buildReposUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}