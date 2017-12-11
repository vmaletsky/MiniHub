package com.example.minihub.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class UsersContract {
    public static final String CONTENT_AUTHORITY = "com.example.minihub.data";

    public static final String PATH_USERS = "users";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class UserColumns implements BaseColumns {

        public static final String TABLE_NAME = "users";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;


        public static final String COLUMN_USER_ID = "user_id";

        public static final String COLUMN_NAME = "user_name";

        public static final String COLUMN_LOGIN = "login";

        public static final String COLUMN_AVATAR_URL = "avatar_url";

        public static final String COLUMN_FOLLOWING = "following";

        public static final String COLUMN_FOLLOWERS = "followers";

        public static final String COLUMN_PUBLIC_REPOS = "public_repos";

        public static final String COLUMN_EMAIL = "email";

        public static final String COLUMN_LOCATION = "location";

        public static final String COLUMN_BIO = "bio";

        public static Uri buildUsersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
