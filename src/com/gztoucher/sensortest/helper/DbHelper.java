package com.gztoucher.sensortest.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库工具类
 *
 * @author 李玉江[QQ:1023694760]
 * @version 2015/4/3
 *          Created by IntelliJ IDEA 14.1
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "sensor.db";
    private static DbHelper instance;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sql = "CREATE TABLE IF NOT EXISTS sensor_test ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type INTEGER NOT NULL,"
                + "x FLOAT NOT NULL,"
                + "y FLOAT NOT NULL,"
                + "z FLOAT NOT NULL,"
                + "timeline DATE NOT NULL)";

        db.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
