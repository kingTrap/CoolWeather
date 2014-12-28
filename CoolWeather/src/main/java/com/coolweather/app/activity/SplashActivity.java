package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.coolweather.app.R;

/**
 * Created by lovezyh on 14-12-28.
 */
public class SplashActivity extends Activity {

    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);

        mHandler.postDelayed(mRunnable , 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mHandler != null){
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            SharedPreferences sp = SplashActivity.this.getSharedPreferences("coolweather.xml", 0);
            if(sp.getString("country_code", null) != null){

                Intent mIntent = new Intent(SplashActivity.this , WeatherActivity.class);
                startActivity(mIntent);
            }else{

                Intent mIntent = new Intent(SplashActivity.this , ChooserActivity.class);
                startActivity(mIntent);
            }
            finish();
        }
    };
}
