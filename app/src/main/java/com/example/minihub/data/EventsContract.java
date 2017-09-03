package com.example.minihub.data;

import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Daryna on 03.09.17.
 */

public class EventsContract {

    public class EventColumns implements BaseColumns {
        String COLUMN_EVENT_ID = "id";

        String COLUMN_TYPE = "name";

        String COLUMN_USER_ID = "user_id";

        String COLUMN_REPO_ID = "repo_id";
    }
}
