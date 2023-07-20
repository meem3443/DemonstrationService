package com.police.demonstrationservice.manager;

import static android.content.Context.MODE_PRIVATE;
import static com.police.demonstrationservice.Constants.API_SUNSET_BASE_URL;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_DATE_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECORD_DATE_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_SUNRISE_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_SUNSET_KEY;
import static com.police.demonstrationservice.Constants.YEAR_MONTH_DAY_DATE_FORMAT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.police.demonstrationservice.rest_api.sunset.SunsetApi;
import com.police.demonstrationservice.rest_api.xml_data.Sunset;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DateManager {
    private static DateManager instance;

    private DateManager() {
    }

    public static DateManager getInstance() {
        if (instance == null) {
            instance = new DateManager();
        }
        return instance;
    }

    public String getCurrentDate(String pattern) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date currentDate = new Date(System.currentTimeMillis());
        return formatter.format(currentDate);
    }

    public String getSunsetTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_RECORD_DATE_KEY, MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_SUNSET_KEY, "");
    }

    public String getSunriseTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_RECORD_DATE_KEY, MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_SUNRISE_KEY, "");
    }

    public void updateDate(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_RECORD_DATE_KEY, MODE_PRIVATE);
        String recordDate = sharedPreferences.getString(SHARED_PREFERENCES_DATE_KEY, "");
        String currentDate = DateManager.getInstance().getCurrentDate(YEAR_MONTH_DAY_DATE_FORMAT).replaceAll("-", "");

        // 하루마다 업데이트
        if (recordDate.equals("") || currentDate.equals("") || Integer.parseInt(recordDate) < Integer.parseInt(currentDate)) {
            TikXml parser = new TikXml.Builder().exceptionOnUnreadXml(false).build();

            // Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_SUNSET_BASE_URL)
                    .addConverterFactory(TikXmlConverterFactory.create(parser))
                    .build();

            SunsetApi sunsetApi = retrofit.create(SunsetApi.class);

            sunsetApi.getSunset(key, DateManager.getInstance().getCurrentDate(YEAR_MONTH_DAY_DATE_FORMAT).replaceAll("-", ""), "서울")
                    .enqueue(new Callback<Sunset>() {
                        @Override
                        public void onResponse(Call<Sunset> call, Response<Sunset> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String sunrise = response.body().getBody().getItems().getItem().getSunrise();
                                String sunset = response.body().getBody().getItems().getItem().getSunset();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(SHARED_PREFERENCES_DATE_KEY, currentDate);
                                editor.putString(SHARED_PREFERENCES_SUNRISE_KEY, sunrise);
                                editor.putString(SHARED_PREFERENCES_SUNSET_KEY, sunset);
                                editor.apply();
                            } else {
                                Log.d("오류", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Sunset> call, Throwable t) {
                            Log.d("오류", t.getMessage());
                        }
                    });
        }
    }
}
