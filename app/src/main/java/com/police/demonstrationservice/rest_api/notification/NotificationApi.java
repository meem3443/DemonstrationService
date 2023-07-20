package com.police.demonstrationservice.rest_api.notification;

import com.police.demonstrationservice.BuildConfig;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationApi {
    @POST("/one")
    Observable<ResponseBody> getNotificationImageOne(@Body NotificationRequest request);

    @POST("/two")
    Observable<ResponseBody> getNotificationImageTwo(@Body NotificationRequest request);

    @POST("/three")
    Observable<ResponseBody> getNotificationImageThree(@Body NotificationRequest request);

    @POST("four")
    Observable<ResponseBody> getNotificationImageFour(@Body NotificationRequest request);

    @POST("five")
    Observable<ResponseBody> getNotificationImageFive(@Body NotificationRequest request);

    @POST("six")
    Observable<ResponseBody> getNotificationImageSix(@Body NotificationRequest request);

    @POST("seven")
    Observable<ResponseBody> getNotificationImageSeven(@Body NotificationRequest request);

    @POST("eight")
    Observable<ResponseBody> getNotificationImageEight(@Body NotificationRequest request);

    @POST("nine")
    Observable<ResponseBody> getNotificationImageNine(@Body NotificationRequest request);

    @POST("ten")
    Observable<ResponseBody> getNotificationImageTen(@Body NotificationRequest request);

    @POST("eleven")
    Observable<ResponseBody> getNotificationImageEleven(@Body NotificationRequest request);

    @POST("twelve")
    Observable<ResponseBody> getNotificationImageTwelve(@Body NotificationRequest request);

    @POST(BuildConfig.END_POINT_PUBLIC)
    Single<String> getPublicKey();
}