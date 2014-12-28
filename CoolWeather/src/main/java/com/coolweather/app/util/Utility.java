package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovezyh on 14-12-25.
 */
public class Utility {

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB  db , String response){

        if(response == null || response.equals("") || db == null)
            return false;
        else{

            String[] spilts = response.split(",");

            if(spilts != null && spilts.length != 0){

                for(String s : spilts){

                    String[] temp = s.split("\\|");
                    Province p = new Province();
                    p.setProvinceCode(temp[0]);
                    p.setProvinceName(temp[1]);

                    db.saveProvince(p);
                }

            }
        }

        return true;

    }

    public synchronized  static boolean handleCityResponse(CoolWeatherDB db , String response , String provinceCode){

        if(response == null || response.equals("") || db == null ){

            return false;
        }else{

            String[] spilts = response.split(",");
            if(spilts != null && spilts.length != 0){

                for(String s : spilts){

                    String[] temp = s.split("\\|");
                    City c = new City();
                    c.setProvinceCode(provinceCode);
                    c.setCityCode(temp[0]);
                    c.setCityName(temp[1]);

                    db.saveCity(c);
                }
            }
        }

        return true;
    }

    public synchronized  static boolean handleCountryResponse(CoolWeatherDB db , String response , String cityCode){

        if(response == null || response.equals("") || db == null ){

            return false;
        }else{

            String[] spilts = response.split(",");
            if(spilts != null && spilts.length != 0){

                for(String s : spilts){

                    String[] temp = s.split("\\|");
                    Country c = new Country();
                    c.setCityCode(cityCode);
                    c.setCountryCode(temp[0]);
                    c.setCountryName(temp[1]);

                    db.saveCountry(c);
                }
            }
        }

        return true;
    }

    public synchronized  static boolean handleWeatherInfo(CoolWeatherDB db , String response , Context mContext){

        if(response == null || response.equals("") || db == null){

            return false;
        }else{
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject info = jsonObject.getJSONObject("weatherinfo");
                String city = info.getString("city");
                String cityid = info.getString("cityid");
                String temp1 = info.getString("temp1");
                String temp2 = info.getString("temp2");
                String weather = info.getString("weather");
                String img1 = info.getString("img1");
                String img2 = info.getString("img2");
                String ptime = info.getString("ptime");

                SharedPreferences preferences = mContext.getSharedPreferences("coolweather.xml" , 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("city" , city);
                editor.putString("cityid" ,cityid);
                editor.putString("temp1" ,temp1);
                editor.putString("temp2" , temp2);
                editor.putString("weather" , weather);
                editor.putString("img1" ,img1);
                editor.putString("img2" , img2);
                editor.putString("ptime" ,ptime);
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
                return  false;
            }

            return true;
        }
    }

    public synchronized  static boolean handleWeatherCode(CoolWeatherDB db , String response , Context mContext){

        if(response == null || response .equals("") || db == null){

            return false;
        }else{

            String [] weatherCode = response.split("\\|");
            if(weatherCode != null && weatherCode.length != 0){

                SharedPreferences preferences = mContext.getSharedPreferences("coolweather.xml" , 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("weather_code" , weatherCode[1]);
                editor.commit();
            }
            return true;
        }
    }


}
