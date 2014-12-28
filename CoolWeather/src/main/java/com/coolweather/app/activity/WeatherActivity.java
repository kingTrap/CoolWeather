package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.util.Common;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lovezyh on 14-12-28.
 */
public class WeatherActivity extends Activity{

    View weatherInfoView;
    TextView titleView ;
    Button btnRefresh;
    Button btnChangeCity;
    TextView tvPublish;
    TextView tvDate;
    TextView tvWeather;
    TextView tvTemp;

    String countryCode ;
    String countryName ;
    String weatherCode ;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_info_layout);
        findViews();
        setListeners();

        sp = getSharedPreferences("coolweather.xml" ,0);
        countryCode = sp.getString("country_code", null);
        countryName = sp.getString("country_name" , null);
        weatherCode = sp.getString("weather_code" , null);
        titleView.setText(countryName);

        queryWeatherInfo();
    }

    private void queryWeatherInfo() {

        weatherInfoView.setVisibility(View.GONE);
        tvPublish.setText(getString(R.string.publish_doing));

        if(weatherCode == null || weatherCode.equals("")){
            HttpUtil.sendHttpRequest(Common.generateCityUrl(countryCode) , new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(String response) {

                    final boolean result = Utility.handleWeatherCode(CoolWeatherDB.getInstance(WeatherActivity.this), response , WeatherActivity.this);

                    weatherCode = sp.getString("weather_code" , null);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(result && weatherCode != null){

                                queryWeatherInfo();

                            }else{
                                tvPublish.setText(getString(R.string.publish_failed));
                                Toast.makeText(WeatherActivity.this , WeatherActivity.this.getString(R.string.get_weather_code_failed),Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

                @Override
                public void onError(Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPublish.setText(getString(R.string.publish_failed));
                            Toast.makeText(WeatherActivity.this, WeatherActivity.this.getString(R.string.get_weather_code_failed), Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

        }else{

            HttpUtil.sendHttpRequest(Common.generateWeatherUrl(weatherCode) ,new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(String response) {

                    final boolean result = Utility.handleWeatherInfo(CoolWeatherDB.getInstance(WeatherActivity.this ), response , WeatherActivity.this);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(result){

                                weatherInfoView.setVisibility(View.VISIBLE);
                                tvPublish.setText(sp.getString("ptime" , ""));
                                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                tvDate.setText(sf.format(new Date()));
                                tvTemp.setText(String.format(getString(R.string.weather_range),sp.getString("temp1",""),sp.getString("temp2","")));
                                tvWeather.setText(sp.getString("weather",""));

                            }else{

                                tvPublish.setText(getString(R.string.publish_failed));
                                Toast.makeText(WeatherActivity.this , WeatherActivity.this.getString(R.string.get_weather_code_failed),Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

                @Override
                public void onError(Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvPublish.setText(getString(R.string.publish_failed));
                            Toast.makeText(WeatherActivity.this , WeatherActivity.this.getString(R.string.get_weather_code_failed),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }

    private void findViews(){

        weatherInfoView = findViewById(R.id.view_info);
        titleView = (TextView)findViewById(R.id.tv_title);
        btnRefresh = (Button)findViewById(R.id.btn_refresh);
        btnChangeCity = (Button)findViewById(R.id.btn_change);
        tvPublish = (TextView)findViewById(R.id.tv_publish);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvWeather = (TextView)findViewById(R.id.tv_weather);
        tvTemp = (TextView)findViewById(R.id.tv_temp);
    }

    private void setListeners(){

        btnChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mIntent = new Intent(WeatherActivity.this , ChooserActivity.class);
                startActivity(mIntent);
                finish();

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryWeatherInfo();
            }
        });
    }
}
