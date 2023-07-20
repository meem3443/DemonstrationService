package com.police.demonstrationservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.police.demonstrationservice.rest_api.notification.NotificationApi;
import com.police.demonstrationservice.ui.MainActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class KeyStoreManager {
    private static final String API_KEY_ALIAS = "my_api_key";
    private static final String API_KEY_PREFS_NAME = "my_api_key_prefs";
    private static final String API_KEY_PREFS_KEY = "api_key";

    private static KeyStoreManager instance;

    private KeyStoreManager() {
    }

    public static KeyStoreManager getInstance() {
        if (instance == null) {
            instance = new KeyStoreManager();
        }
        return instance;
    }

    public void getPublicKey(Context context, MainActivity.CallBackListener callBackListener) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    API_KEY_PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String apiKey = sharedPreferences.getString(API_KEY_PREFS_KEY, null);

            if (apiKey == null) {
                // Gson 객체 생성
                Gson gson = new GsonBuilder().setLenient().create();

                // Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BuildConfig.SERVER_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build();

                NotificationApi notificationApi = retrofit.create(NotificationApi.class);
                // Rxjava
                notificationApi.getPublicKey()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            @Override
                            public void onSuccess(@NonNull String s) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(API_KEY_PREFS_KEY, s);
                                editor.apply();
                                callBackListener.finishGetKey(s);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("오류", e.getMessage());
                            }
                        });
            } else {
                callBackListener.finishGetKey(apiKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGoogleKey(Context context) {

    }
}
