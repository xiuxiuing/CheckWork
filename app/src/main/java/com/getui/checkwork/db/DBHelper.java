package com.getui.checkwork.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.getui.checkwork.bean.RecordBean;

/**
 * Created by wang on 16/7/2.
 */
public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "work.db";
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_WORK = "create table if not exists " + "work" + "(id integer primary key autoincrement, time text)";
    private static final String DROP_TABLE_WORK = "drop table if exists work";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static DBHelper instance;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL(CREATE_TABLE_WORK);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE_WORK);
        } catch (Exception e) {

        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDowngrade(db, oldVersion, newVersion);
    }

    public void insertTime(String table, ContentValues cv) {
        instance.getWritableDatabase().insert(table, null, cv);
    }

    public List<RecordBean> queryTime() {
        Cursor cursor = null;
        List<RecordBean> lists = new ArrayList<RecordBean>();
        try {
            cursor = instance.getWritableDatabase().rawQuery("select * from work", new String[] {});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        RecordBean bean = new RecordBean();
                        bean.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                        lists.add(bean);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {

        }
        return lists;
    }
}
