package com.example.vonlion.kupao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类Created by hbs on 2016/2/4.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "user.db";
    private static final int  version = 1;
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
        // TODO Auto-generated constructor stub
    }


    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "create table if not exists usertb(_id integer primary key autoincrement,"
                + "name text,"
                + "date text,"
                + "time text,"
                + "distance text,"
                + "theyCount text,"
                + "energy text,"
                + "speed text,"
                + "motionState text,"
                + "position text"
                + ");";
        db.execSQL(sql);
        String sqlChart = "create table if not exists charttb(_id integer primary key autoincrement,"
                + "username text,"
                + "starttime text,"
                + "curspeed text,"
                + "curtime text"
                + ");";
        db.execSQL(sqlChart);
        String sqlTrace = "create table if not exists tracetb(_id integer primary key autoincrement,"
                + "username text,"
                + "starttime text,"
                + "latitude text,"
                + "longitude text,"
                + "speed text"
                + ");";
        db.execSQL(sqlTrace);

        //用户资料表
        String sqldata = "create table if not exists userdata(_id integer primary key autoincrement,"
                + "username text,"
                + "nickname text,"
                + "height text,"
                + "weight text,"
                + "goal text,"
                + "age text,"
                + "adress text,"
                + "signature text"
                + ")";
        db.execSQL(sqldata);
    }

    public void onUpgrade(SQLiteDatabase arg0, int oldversion, int newversion) {
        // TODO Auto-generated method stub

    }

}
