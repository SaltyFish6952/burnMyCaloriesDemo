package com.example.salty_9a312.burnmycalories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Helper extends SQLiteOpenHelper {

    private static final String DBName = "caloriesRecorder.db";
    private static final String CALORIES = "Calories";


    private static final String CREATE_CALORIES_TABLE
            = "create table " + CALORIES + "(calories integer, date text primary key)";

    public DB_Helper(Context context, int version) {

        super(context, DBName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CALORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
