package com.example.minihub.data;

import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by v.maletskiy on 9/1/2017.
 */

public interface EventColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey String COLUMN_EVENT_ID = "id";

    @DataType(DataType.Type.TEXT) @NotNull String COLUMN_TYPE = "name";

    @DataType(DataType.Type.INTEGER) @NotNull String COLUMN_USER_ID = "user_id";

    @DataType(DataType.Type.INTEGER) @NotNull String COLUMN_REPO_ID = "repo_id";
}
