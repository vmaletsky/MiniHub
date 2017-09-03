package com.example.minihub.data;

import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Daryna on 03.09.17.
 */

public class UsersContract {
    public class UserColumns implements BaseColumns {
        String COLUMN_USER_ID = "id";

        String COLUMN_NAME = "name";

        String COLUMN_LOGIN = "login";

        String COLUMN_AVATAR_URL = "avatar_url";
    }
}
