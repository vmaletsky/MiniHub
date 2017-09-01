package com.example.minihub.data;

import android.net.Uri;
import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by v.maletskiy on 8/29/2017.
 */

public interface UserColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey String COLUMN_USER_ID = "id";

    @DataType(DataType.Type.TEXT) @NotNull String COLUMN_NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull String COLUMN_LOGIN = "login";

    @DataType(DataType.Type.TEXT) @NotNull String COLUMN_AVATAR_URL = "avatar_url";
}
