package com.example.minihub.data;

import android.net.Uri;
import android.provider.BaseColumns;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.ForeignKeyConstraint;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by v.maletskiy on 8/29/2017.
 */

public interface RepoColumns {
        @DataType(DataType.Type.INTEGER) @PrimaryKey String COLUMN_REPO_ID = "id";

        @DataType(DataType.Type.TEXT) @NotNull String COLUMN_NAME = "name";

        @DataType(DataType.Type.INTEGER) String COLUMN_USER_ID = "user_id";

}
