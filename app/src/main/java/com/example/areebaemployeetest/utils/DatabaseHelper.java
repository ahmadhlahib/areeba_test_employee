package com.example.areebaemployeetest.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 1;

    // Define your table creation query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    "employee (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "title TEXT, " +
                    "firstName TEXT, " +
                    "lastName TEXT, " +
                    "userName TEXT, " +
                    "password TEXT, " +
                    "email TEXT, " +
                    "dob TEXT, " +
                    "age INTEGER, " +
                    "phone TEXT, " +
                    "cell TEXT, " +
                    "gender TEXT, " +
                    "picture TEXT," +
                    "country,\n" +
                    "city,\n" +
                    "state,\n" +
                    "streetName,\n" +
                    " streetNumber,\n" +
                    "longitude,\n" +
                    "lattitude );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the table creation query
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
