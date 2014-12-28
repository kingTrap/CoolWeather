package com.coolweather.app.util;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

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
}
