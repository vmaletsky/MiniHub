package com.example.minihub.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class EventsContract {

    public static final String CONTENT_AUTHORITY = "com.example.minihub.data";

    public static final String PATH_EVENTS = "events";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static class EventColumns implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;

        public static final String TABLE_NAME = "events";

        public static final String COLUMN_EVENT_ID = "event_id";

        public static final String COLUMN_TYPE = "event_name";

        public static final String COLUMN_USER_ID = "user_id";

        public static final String COLUMN_REPO_ID = "repo_id";

        public static final String COLUMN_CREATED_AT = "created_at";

        public static final String COLUMN_PAYLOAD_ACTION = "payload_action";

        public static final String COLUMN_PAYLOAD_SIZE = "payload_size";

        public static final String COLUMN_PAYLOAD_REF_TAG = "payload_ref_tag";

        public static final String COLUMN_PAYLOAD_REF = "payload_ref";

        public static final String COLUMN_PAYLOAD_MERGED = "payload_merged";

        public static Uri buildEventsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
