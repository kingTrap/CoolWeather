package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovezyh on 14-12-24.
 */
public class CoolWeatherDB {

    public static final String DB_NAME = "coolweather.db";
    public static final int DB_VERSION = 1 ;

    private static CoolWeatherDB instance = null;
    private SQLiteOpenHelper dbHelper ;
    private SQLiteDatabase db ;

    private CoolWeatherDB(Context mContext){

        dbHelper = new CoolWeatherOpenHelper(mContext , DB_NAME ,null ,DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }
    public static CoolWeatherDB getInstance(Context mContext){

        if(instance == null)
            instance = new CoolWeatherDB(mContext);

        return instance;
    }

    public void saveProvince(Province p){


        if(p == null)
            return ;

        ContentValues values = new ContentValues();
        values.put("province_name" , p.getProvinceName());
        values.put("province_code" , p.getProvinceCode());
        db.insert("province" , null , values);
    }

    public List<Province> getProvinces(){

        List<Province> provinces = new ArrayList<Province>();

        Cursor cursor = db.query("province" , null ,null,null,null,null,null,null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do{

                    Province p = new Province();
                    p.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    p.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                    p.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                    provinces.add(p);

                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        return provinces;
    }

    public void saveCity(City c){


        if(c == null)
            return ;

        ContentValues values = new ContentValues();
        values.put("city_name" , c.getCityName());
        values.put("city_code" , c.getCityCode());
        values.put("province_id" , c.getProvinceId());
        db.insert("city" , null , values);
    }

    public List<City> getCities(){

        List<City> cities = new ArrayList<City>();

        Cursor cursor = db.query("city" , null ,null,null,null,null,null,null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do{

                    City c = new City();
                    c.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    c.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                    c.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                    cities.add(c);

                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        return cities;
    }

    public void saveCountry(Country c){


        if(c == null)
            return ;

        ContentValues values = new ContentValues();
        values.put("country_name" , c.getCountryName());
        values.put("country_code" , c.getCountryCode());
        values.put("city_id" ,c.getCityId());
        db.insert("country" , null , values);
    }

    public List<Country> getCountries(){

        List<Country> countries = new ArrayList<Country>();

        Cursor cursor = db.query("country" , null ,null,null,null,null,null,null);

        if(cursor != null){

            if(cursor.moveToFirst()){

                do{

                    Country c = new Country();
                    c.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    c.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                    c.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                    countries.add(c);

                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        return countries;
    }
}
