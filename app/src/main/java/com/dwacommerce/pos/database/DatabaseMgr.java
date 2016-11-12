package com.dwacommerce.pos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.dwacommerce.pos.dao.CategoryData;

import java.util.ArrayList;

/**
 * Created by admin on 13/09/16.
 */
public class DatabaseMgr {
    private static DatabaseMgr instance;
    private static SQLiteDatabase sqLiteDb;

    private DatabaseMgr() {

    }

    public static DatabaseMgr getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseMgr();
            init(context);
        }
        return instance;
    }

    private synchronized static boolean init(Context context) {
        sqLiteDb = new DatabaseHelper(context).getWritableDatabase();
        sqLiteDb.setPageSize(4 * 1024);
        return true;
    }

    /**
     * This method is used to insert data in the table.
     *
     * @param tableName
     * @param contentValues
     * @return
     */
    private synchronized int insertRows(String tableName, ContentValues[] contentValues) {
        int retCode = -1;
        try {
            sqLiteDb.beginTransaction();
            for (ContentValues contactValue : contentValues) {
                try {
                    if (contactValue == null)
                        return 0;
                    retCode = (int) sqLiteDb.insertWithOnConflict(tableName, null, contactValue, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return retCode;
    }

    /**
     * method to insert one row at a time in table
     *
     * @param tableName
     * @param contentValues
     * @return
     */
    private synchronized int insertRow(String tableName, ContentValues contentValues) {
        int retCode = -1;
        try {
            sqLiteDb.beginTransaction();
            try {
                if (contentValues == null)
                    return 0;
                retCode = (int) sqLiteDb.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (sqLiteDb != null) {
                sqLiteDb.setTransactionSuccessful();
                sqLiteDb.endTransaction();
            }
        }
        return retCode;
    }


    public long getNoOfRecords(String tableName) {
        Cursor cursor;
        long noOfEntries = 0;
        try {
            noOfEntries = DatabaseUtils.queryNumEntries(sqLiteDb, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noOfEntries;
    }

    public void insertDataToCategoryTable(ArrayList<CategoryData> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                CategoryData parentCategoryData = list.get(i);
                insertRow(CategoryData.TABLE_NAME, createContentValuesFromObject(parentCategoryData));
                if (parentCategoryData.subCategories != null) {
                    for (int j = 0; j < parentCategoryData.subCategories.size(); j++) {
                        CategoryData childCategoryData = parentCategoryData.subCategories.get(j);
                        childCategoryData.parentCategoryId = parentCategoryData.id;
                        insertRow(CategoryData.TABLE_NAME, createContentValuesFromObject(childCategoryData));
                        if (childCategoryData.subCategories != null) {
                            for (int k = 0; k < childCategoryData.subCategories.size(); k++) {
                                CategoryData grandChildCategoryData = childCategoryData.subCategories.get(k);
                                grandChildCategoryData.parentCategoryId = childCategoryData.id;
                                insertRow(CategoryData.TABLE_NAME, createContentValuesFromObject(grandChildCategoryData));
                                if (grandChildCategoryData.subCategories != null) {
                                    for (int l = 0; l < grandChildCategoryData.subCategories.size(); l++) {
                                        CategoryData postGrandChildCategoryData = grandChildCategoryData.subCategories.get(l);
                                        postGrandChildCategoryData.parentCategoryId = grandChildCategoryData.id;
                                        insertRow(CategoryData.TABLE_NAME, createContentValuesFromObject(postGrandChildCategoryData));
                                    }

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ContentValues createContentValuesFromObject(CategoryData categoryData) {
        ContentValues contentValues = null;
        try {
            contentValues = new ContentValues();
            contentValues.put(CategoryData.FLD_CATEGORY_IMAGE_URL, categoryData.imageUrl);
            contentValues.put(CategoryData.FLD_CATEGORY_ID, categoryData.id);
            contentValues.put(CategoryData.PARENT_CATEGORY_ID, categoryData.parentCategoryId);
            contentValues.put(CategoryData.FLD_CATEGORY_NAME, categoryData.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentValues;
    }

    public ArrayList<CategoryData> getCategoryById(long parentCategoryId) {
        ArrayList<CategoryData> categorylist = new ArrayList<CategoryData>();
        try {
            Cursor cursor = sqLiteDb.query(CategoryData.TABLE_NAME, null, CategoryData.PARENT_CATEGORY_ID + " = ?", new String[]{"" + parentCategoryId}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    CategoryData categoryData = new CategoryData();
                    categoryData.id = cursor.getLong(cursor.getColumnIndex(CategoryData.FLD_CATEGORY_ID));
                    categoryData.name = cursor.getString(cursor.getColumnIndex(CategoryData.FLD_CATEGORY_NAME));
                    categoryData.parentCategoryId = cursor.getLong(cursor.getColumnIndex(CategoryData.PARENT_CATEGORY_ID));
                    categoryData.imageUrl = cursor.getString(cursor.getColumnIndex(CategoryData.FLD_CATEGORY_IMAGE_URL));
                    categorylist.add(categoryData);
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorylist;
    }

    public void clearDB() {
        sqLiteDb.execSQL("delete from " + CategoryData.TABLE_NAME);
    }
}
