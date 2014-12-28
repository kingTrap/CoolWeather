package com.coolweather.app.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lovezyh on 14-12-25.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address , final HttpCallBackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try{

                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(50000);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(50000);
                    connection.connect();

                    InputStream is = connection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String read = null;
                    while( (read = bufferedReader.readLine()) != null){

                        sb.append(read);
                    }

                    if(listener != null){

                        listener.onFinish(sb.toString());
                    }
                }catch (Exception e){

                    e.printStackTrace();
                    if(listener != null){

                        listener.onError(e);
                    }
                } finally {
                    if(connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    public interface HttpCallBackListener{

        public void onFinish(String response);
        public void onError(Exception e);
    }
}
