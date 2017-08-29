package com.example.minihub.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by v.maletskiy on 8/29/2017.
 */

public class UserContract {

    public static final String CONTENT_AUTHORITY = "com.example.minihub.data";

    public static final String PATH_USERS = "users";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class UserEntry implements BaseColumns {
        public static final String COLUMN_USER_ID = "id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_LOGIN = "login";

        public static final String COLUMN_AVATAR_URL = "avatar_url";
    }
}
