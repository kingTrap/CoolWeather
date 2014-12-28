package com.coolweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.Common;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lovezyh on 14-12-26.
 */
public class ChooserActivity extends Activity implements AdapterView.OnItemClickListener{

    TextView titleText;
    ListView mListView;
    ArrayAdapter<String> mAdapter ;

    List<String> data = new ArrayList<String>();

    List<Province> mProvinces = new ArrayList<Province>();
    List<City> mCities = new ArrayList<City>();
    List<Country> mCountries = new ArrayList<Country>();

    ProgressDialog mProgressDialog ;

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2 ;
    public static final int LEVEL_COUNTRY = 3;

    int level = LEVEL_PROVINCE;

    Province mCurrentProvince ;
    City mCurrentCity;
    Country mCurrentCountry ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_layout);

        findViews();

        queryProvices();
    }

    private void findViews(){

        titleText = (TextView)findViewById(R.id.tv_title);
        mListView = (ListView)findViewById(R.id.listview);

        mAdapter = new ArrayAdapter<String>(ChooserActivity.this ,android.R.layout.simple_list_item_1 , data);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);

    }

    private void queryProvices(){

        mProvinces = CoolWeatherDB.getInstance(this).getProvinces();
        level = LEVEL_PROVINCE;
        if(mProvinces .isEmpty()){
            String url = Common.generateCityUrl(null);
            openDialog();
            HttpUtil.sendHttpRequest(url , new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(String response) {

                    boolean result = Utility.handleProvinceResponse(CoolWeatherDB.getInstance(ChooserActivity.this) , response);

                    Runnable mRefreshRunnable = new HandleResponseRunnable(result);

                    runOnUiThread(mRefreshRunnable);

                }

                @Override
                public void onError(Exception e) {

                    Runnable mRefreshRunnable = new HandleResponseRunnable(false );

                    runOnUiThread(mRefreshRunnable);

                }
            });

        }else{
            data.clear();
            for(int i = 0 ; i < mProvinces.size() ; i++){

                data.add(mProvinces.get(i).getProvinceName());
            }
            mListView.setSelection(0);
            titleText.setText(getString(R.string.province_title));
            mAdapter.notifyDataSetChanged();
        }

    }

    private void queryCities(final Province mProvince){

        if(mProvince == null)
            return ;
        mCities = CoolWeatherDB.getInstance(this).getCities(mProvince.getProvinceCode());
        level = LEVEL_CITY;
        if(mCities .isEmpty()){
            String url = Common.generateCityUrl(mProvince.getProvinceCode());
            openDialog();
            HttpUtil.sendHttpRequest(url , new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(String response) {

                    boolean result = Utility.handleCityResponse(CoolWeatherDB.getInstance(ChooserActivity.this), response, mProvince.getProvinceCode());

                    Runnable mRefreshRunnable = new HandleResponseRunnable(result , mProvince);

                    runOnUiThread(mRefreshRunnable);

                }

                @Override
                public void onError(Exception e) {

                    Runnable mRefreshRunnable = new HandleResponseRunnable(false , mProvince);

                    runOnUiThread(mRefreshRunnable);

                }
            });

        }else{
            data.clear();
            for(int i = 0 ; i < mCities.size() ; i++){

                data.add(mCities.get(i).getCityName());
            }
            mListView.setSelection(0);
            mCurrentProvince = mProvince;
            titleText.setText(mCurrentProvince.getProvinceName());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void queryCountries(final City mCity ){

        if(mCity == null){
            return ;
        }
        mCountries = CoolWeatherDB.getInstance(this).getCountries(mCity.getCityCode());
        level = LEVEL_COUNTRY;
        if(mCountries .isEmpty()){

            String url = Common.generateCityUrl(mCity.getCityCode());
            openDialog();
            HttpUtil.sendHttpRequest(url , new HttpUtil.HttpCallBackListener() {
                @Override
                public void onFinish(String response) {

                    boolean result = Utility.handleCountryResponse(CoolWeatherDB.getInstance(ChooserActivity.this), response, mCity.getCityCode());

                    Runnable mRefreshRunnable = new HandleResponseRunnable(result , mCity);

                    runOnUiThread(mRefreshRunnable);

                }

                @Override
                public void onError(Exception e) {

                    Runnable mRefreshRunnable = new HandleResponseRunnable(false ,mCity);

                    runOnUiThread(mRefreshRunnable);

                }
            });

        }else{
            data.clear();
            for(int i = 0 ; i < mCountries.size() ; i++){

                data.add(mCountries.get(i).getCountryName());
            }
            mListView.setSelection(0);
            mCurrentCity = mCity;
            titleText.setText(mCurrentProvince.getProvinceName() + "->" + mCurrentCity.getCityName());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void openDialog(){

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.progress_loading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    private void closeDialog(){

        if(mProgressDialog != null && mProgressDialog.isShowing()){

            mProgressDialog .dismiss();
        }
        mProgressDialog = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String name = data.get(i);
        switch (level){

            case LEVEL_PROVINCE :
                Province p = null;
                for(int k = 0 ; k < mProvinces.size() ; k++){

                    if(mProvinces.get(k).getProvinceName().equals(name)){

                        p = mProvinces.get(k);
                        break;
                    }
                }

                queryCities(p);
                break;
            case LEVEL_CITY :

                City city = null;
                for(int k = 0 ; k < mCities.size() ; k++){

                    if(mCities.get(k).getCityName().equals(name)){

                        city = mCities.get(k);
                        break;

                    }
                }
                queryCountries(city);
                break;

            case LEVEL_COUNTRY :

                break;
        }

    }

    class HandleResponseRunnable implements Runnable {

        boolean result = false;
        City city ;
        Province province;

        public HandleResponseRunnable(boolean result){

            this.result = result;
        }
        public HandleResponseRunnable(boolean result ,City city){

            this.result = result;
            this.city = city;
        }

        public HandleResponseRunnable(boolean result , Province p){

            this.result = result;
            this.province = p ;
        }

        @Override
        public void run() {

            String showResult = "";
            closeDialog();

            if(result){
                switch (level){

                    case LEVEL_PROVINCE:
                        queryProvices();
                        showResult = getString(R.string.load_province_success);
                        break;
                    case LEVEL_CITY :
                        queryCities(province);
                        showResult = getString(R.string.load_city_success);
                        break;
                    case LEVEL_COUNTRY :
                        queryCountries(city);
                        showResult = getString(R.string.load_country_success);
                        break;
                }

            }else{
                switch (level){

                    case LEVEL_PROVINCE:

                        showResult = getString(R.string.load_province_failed);
                        break;
                    case LEVEL_CITY :

                        level = LEVEL_PROVINCE;
                        showResult = getString(R.string.load_city_failed);
                        break;
                    case LEVEL_COUNTRY :

                        showResult = getString(R.string.load_country_failed);
                        level = LEVEL_CITY;
                        break;
                }

            }

            Toast.makeText(ChooserActivity.this , showResult ,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            switch (level){

                case LEVEL_CITY:
                    queryProvices();
                    return true;
                case LEVEL_COUNTRY :
                    queryCities(mCurrentProvince);
                    return true;
                case LEVEL_PROVINCE :

                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
