package com.example.coolweather;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.coolweather.R.color.design_default_color_primary;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    private TextView cityName;
    private TextView weatherText;
    private TextView updateTime;
    private TextView temNow;
    private TextView todayData;
    private TextView temMax;
    private TextView temMin;
    private TextView infoText;
    private TextView winSpeed;
    private TextView winText;
    private TextView humidityText;
    private TextView pm25Text;
    private TextView pressureText;
    private TextView comfrotText;
    private ScrollView weatherLayout;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    public DrawerLayout drawerLayout;
    private Button navButton;
    private ImageView bingPicImg;


    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        
        initView();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bing_pic = prefs.getString("bing_pic", null);
        if (bing_pic!=null){
            Glide.with(this).load(bing_pic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.weatherId;
            showWeatherInfo(weather);
        }else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }

    private void loadBingPic() {
        String bingPicUrl ="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        cityName = (TextView) findViewById(R.id.title_city);
        weatherText = (TextView) findViewById(R.id.weather_text);
        updateTime = (TextView) findViewById(R.id.title_update_time);
        temNow = (TextView) findViewById(R.id.tem_now_text);
        todayData = (TextView) findViewById(R.id.date_text);
        temMax = (TextView) findViewById(R.id.max_text);
        temMin = (TextView) findViewById(R.id.min_text);
        infoText = (TextView) findViewById(R.id.info_text);
        winSpeed = (TextView) findViewById(R.id.win_spend_text);
        winText = (TextView) findViewById(R.id.win_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        pressureText = (TextView) findViewById(R.id.pressure_text);
        comfrotText = (TextView) findViewById(R.id.comfort_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(design_default_color_primary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

    }

    public void requestWeather(final String weatherId) {

        String weatherUrl = "https://tianqiapi.com/api?unescape=1&version=v6&appid=38263775&appsecret=e1SAE8Yx&cityid="+weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather !=null ){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId=weather.weatherId;
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();

                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        loadBingPic();
    }
    private void showWeatherInfo(Weather weather) {
        cityName.setText(weather.city);
        weatherText.setText(weather.wea);
        updateTime.setText("更新于"+weather.update_time);
        temNow.setText(weather.nowTem+"℃");
        todayData.setText(weather.date);
        temMax.setText(weather.maxTem+"℃");
        temMin.setText(weather.minTem+"℃");
        infoText.setText(weather.wea);
        winSpeed.setText(weather.win_speed);
        winText.setText(weather.win);
        humidityText.setText(weather.humidity);
        pm25Text.setText(weather.air_pm25);
        pressureText.setText(weather.pressure);
        comfrotText.setText(weather.air_tips);
        weatherLayout.setVisibility(View.VISIBLE);
    }

//    private void showWeatherInfo(Weather weather) {
//        String cityName = weather.basic.cityName;
//        String updateTime =weather.basic.update.updateTime.split(" ")[1];
//        String degree = weather.now.temperature+"℃";
//        String weatherInfo = weather.now.more.info;
//        titleCity.setText(cityName);
//        titleUpdateTime.setText(updateTime);
//        degreeText.setText(degree);
//        weatherInfoText.setText(weatherInfo);
//
//        forecastLayout.removeAllViews();
//        for (Forecast forecast: weather.forecastList){
//            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
//            TextView dateText = view.findViewById(R.id.date_text);
//            TextView infoText = view.findViewById(R.id.info_text);
//            TextView maxText = view.findViewById(R.id.max_text);
//            TextView minText = view.findViewById(R.id.min_text);
//
//            dateText.setText(forecast.date);
//            infoText.setText(forecast.more.info);
//            maxText.setText(forecast.temperature.max);
//            minText.setText(forecast.temperature.min);
//
//            forecastLayout.addView(view);
//
//        }
//        if (weather.aqi !=null){
//            aqiText.setText(weather.aqi.city.aqi);
//            pm25Text.setText(weather.aqi.city.pm25);
//
//        }
//
//        String comfort = "舒适度："+weather.suggestion.comfort.info;
//        String carWash = "洗车指数："+weather.suggestion.carWash.info;
//        String sport = "运动建议："+weather.suggestion.sport.info;
//        comfortText.setText(comfort);
//        carWashText.setText(carWash);
//        sportText.setText(sport);
//        weatherLayout.setVisibility(View.VISIBLE);
//
//    }
}
