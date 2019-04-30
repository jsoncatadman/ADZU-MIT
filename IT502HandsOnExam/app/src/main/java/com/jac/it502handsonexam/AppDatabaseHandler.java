package com.jac.it502handsonexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  android.util.Log;

public class AppDatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "App Database Handler";
    private static final String TABLE_NAME = "tblPersonalInformation";
    private static final String COL0 = "ID";
    private static final String COL1 = "Full_name";
    private static final String COL2 = "Birth_day";
    private static final String COL3 = "Username";
    private static final String COL4 = "Password";
    private static final String COL5 = "Status";
    SQLiteDatabase db;
    ContentValues contentValues;
    Cursor data;
    String log_status = "off";

    public AppDatabaseHandler(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COL0 + " INTEGER primary key  AUTOINCREMENT,"
                +  COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT " + ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean logIn(String un, String pw) {
        db  = this.getWritableDatabase();
        String query = "SELECT " + COL0 + " FROM " + TABLE_NAME + " WHERE Username = '" + un + "' and Password = '" + pw + "';";
        data = db.rawQuery(query,null);
        log_status = "on";
        if (data != null && data.moveToFirst()) {
            String update_query = "UPDATE " + TABLE_NAME + " SET " + COL5 + " = '" + log_status + "' WHERE " + COL3 + " = '" + un + "';";
            Log.d(TAG, "Updating of login status.");
            db.execSQL(update_query);
            data.close();
            return true;
        }
        else {
            data.close();
            return false;
        }
    }

    public boolean logOut(String un, String pw) {
        db  = this.getWritableDatabase();
        String query = "SELECT " + COL0 + " FROM " + TABLE_NAME + " WHERE Username = '" + un + "' and Password = '" + pw + "';";
        data = db.rawQuery(query,null);
        if (data != null && data.moveToFirst()) {
            String update_query = "UPDATE " + TABLE_NAME + " SET " + COL5 + " = '" + log_status + "' WHERE " + COL3 + " = '" + un + "';";
            Log.d(TAG, "Updating of login status.");
            db.execSQL(update_query);
            data.close();
            return true;
        }
        else {
            data.close();
            return false;
        }
    }

    public void insertData(String fname, String bdate, String un, String pw) {
        db  = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(COL1, fname);
        contentValues.put(COL2, bdate);
        contentValues.put(COL3, un);
        contentValues.put(COL4, pw);
        contentValues.put(COL5, log_status);
        Log.d(TAG, "Adding of Data to TABLE:" + TABLE_NAME);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void updateData(String fname, String bdate, String un, String pw, int id) {
        db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(COL1, fname);
        contentValues.put(COL2, bdate);
        contentValues.put(COL3, un);
        contentValues.put(COL4, pw);
        contentValues.put(COL5, "off");
        Log.d(TAG, "Updating of Data to TABLE:" + TABLE_NAME);
        db.update(TABLE_NAME, contentValues, "ID="+id, null);
    }

    public Cursor getAccountInformation(String un, String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE Username = '" + un + "' and Password = '" + pw + "';";
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
