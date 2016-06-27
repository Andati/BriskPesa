package com.briskpesa.briskpesademo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rodgers on 03/05/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "briskpesademo.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_user (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "phone varchar(20) NOT NULL, status int(2) NOT NULL, date_time timestamp NOT NULL DEFAULT (DATETIME(current_timestamp, 'localtime')))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_user");
        onCreate(db);
        db.close();
    }

    public long insertUser (String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("phone", phone);
        contentValues.put("status", 1);

        long affectedColumnId = db.insert("tbl_user", null, contentValues);
        db.close();
        return affectedColumnId;
    }

    public String getPhoneNumber(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from tbl_user where id=1", null);
        if (res.getCount() < 1) {
            db.close();
            res.close();
            return null;
        } else {
            res.moveToFirst();
            String phone = res.getString(1);
            db.close();
            res.close();
            return phone;
        }
    }

    public void deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS tbl_user");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_user (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "phone varchar(20) NOT NULL, status int(2) NOT NULL, date_time timestamp NOT NULL DEFAULT (DATETIME(current_timestamp, 'localtime')))");
        db.close();
    }

    public boolean activateUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 0);
        db.update("tbl_user", contentValues, "id = ? ", new String[]{Integer.toString(1)});
        return true;
    }

}
