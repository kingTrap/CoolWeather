package com.coolweather.app.util;

/**
 * Created by lovezyh on 14-12-27.
 */
public class Common {

    public static final String CITY_ROOT_URL = "http://www.weather.com.cn/data/list3/city";

    public static String generateCityUrl (String code){

        if(code == null){
            return CITY_ROOT_URL + ".xml";
        }

        return CITY_ROOT_URL + code +".xml";
    }
}
