package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovezyh on 14-12-24.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{

    /*
    *创建省表
     */
    public static final String CREATE_TABLE_PROVINCE = " create table province (_id integer primary key autoincrement , "

                                                        + "province_name text ,"
                                                        + "province_code text )";
    /*
    创建市表
     */

    public static final String CREATE_TABLE_CITY = " create table city ( _id integer primary key autoincrement , "
                                                    + "city_name text , "
                                                    + "city_code text , "
                                                    + "province_code text)" ;

    /*
    创建县表
     */

    public static final String CREATE_TABLE_COUNTRY = "create table country (_id integer primary key autoincrement ,"
                                                      + "country_name text , "
                                                      + "country_code text , "
                                                      + "city_code text)";

    public CoolWeatherOpenHelper(Context mContext, String mDBName, Object o, int mDBVersion){

        super(mContext , mDBName , null , mDBVersion);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(CREATE_TABLE_PROVINCE);
        sqLiteDatabase.execSQL(CREATE_TABLE_CITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_COUNTRY);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
