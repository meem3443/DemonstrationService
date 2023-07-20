package com.police.demonstrationservice.rest_api.sunset;

import com.police.demonstrationservice.rest_api.xml_data.Sunset;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunsetApi {
    @GET("/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo")
    Call<Sunset> getSunset(
            @Query(value = "serviceKey", encoded = true) String serviceKey,
            @Query("locdate") String locdate,
            @Query("location") String location
    );
}
