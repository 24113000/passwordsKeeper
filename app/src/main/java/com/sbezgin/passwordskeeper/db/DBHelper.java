package com.sbezgin.passwordskeeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sbezgin on 08.06.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "passwordsKeeper";
    private static final String TABLE = "Path";
    private static final String COLUMN_NAME = "Path";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE + "(" + COLUMN_NAME + " TEXT )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing
    }

    public void setPath(String path) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, path);

        Cursor cursor = db.query(TABLE, new String[]{COLUMN_NAME}, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            db.insert(TABLE, null, values);
        } else {
            db.update(COLUMN_NAME, values, null, null);
        }
        cursor.close();
        db.close();
    }

    public String getPath() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{COLUMN_NAME}, null, null, null, null, null);
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }

    public void cleanTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE, null, null);
    }
}
