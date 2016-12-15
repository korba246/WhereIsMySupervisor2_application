package com.example.korba.gdziejestmojpromotor2.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.korba.gdziejestmojpromotor2.model.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korba on 15.12.16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UsersManager";

    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD +  " TEXT"+ ")";
        db.execSQL(CREATE_USERS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.get_email());
        values.put(KEY_PASSWORD, user.get_password());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public Users getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_EMAIL, KEY_PASSWORD }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        Users user = new Users(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        return user;
    }


    public int updateUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.get_email());
        values.put(KEY_PASSWORD, user.get_password());
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
    }


    public void deleteUser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
        db.close();
    }


    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count =cursor.getCount();
        cursor.close();

        return count;
    }

}
