package com.dwacommerce.pos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dwacommerce.pos.dao.CategoryData;

/**
 * Created by admin on 13/09/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DWACommerce";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createCategoryTable(sqLiteDatabase);
    }

    private void createCategoryTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "
                + CategoryData.TABLE_NAME + "("
                + CategoryData.FLD_CATEGORY_ID + " LONG UNIQUE ON CONFLICT REPLACE,"
                + CategoryData.FLD_CATEGORY_NAME + " TEXT,"
                + CategoryData.PARENT_CATEGORY_ID + " LONG,"
                + CategoryData.FLD_CATEGORY_IMAGE_URL + " TEXT);");
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS category_id_index on " + CategoryData.TABLE_NAME + "(" + CategoryData.FLD_CATEGORY_ID + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
