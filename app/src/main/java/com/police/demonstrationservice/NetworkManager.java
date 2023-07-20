package com.police.demonstrationservice;

import static com.police.demonstrationservice.Constants.PACKAGE_NAME;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.police.demonstrationservice.rest_api.notification.NotificationApi;
import com.police.demonstrationservice.rest_api.notification.NotificationRequest;
import com.police.demonstrationservice.ui.SendActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {
    private static NetworkManager instance;

    private Uri notificationUri;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    // 안내문 발송 : rxjava3 - retrofit2 -> POST 요청 보내고 응답 받기
    public void getNotificationUriOne(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // RxJava
        notificationApi.getNotificationImageOne(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    // 최고 소음 초과 (유지) : rxjava3 - retrofit2 -> POST 요청 보내고 응답 받기
    public void getNotificationUriTwo(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageTwo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    // 등가 소음 초과 (유지) : rxjava3 - retrofit2 -> POST 요청 보내고 응답 받기
    public void getNotificationUriThree(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageThree(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriFour(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageFour(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriFive(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageFive(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriSix(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageSix(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriSeven(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageSeven(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriEight(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageEight(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriNine(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageNine(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriTen(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageTen(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriEleven(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageEleven(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    public void getNotificationUriTwelve(Context context, NotificationRequest request, SendActivity.CallbackListener listener) {
        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        NotificationApi notificationApi = retrofit.create(NotificationApi.class);

        // Rxjava
        notificationApi.getNotificationImageTwelve(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        // 구독 시 처리할 내용
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        // 결과 데이터 처리
                        Uri imageUri;
                        try {
                            imageUri = convertResponseToUri(responseBody, context);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        notificationUri = imageUri;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 에러 처리
                        Log.d("오류", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        // 작업 완료 처리
                        listener.completeGetImageUri(notificationUri);
                        Log.d("테스트", "완료");
                    }
                });
    }

    // ResponseBody Type 에서 Uri 로 변환 후 return
    private Uri convertResponseToUri(ResponseBody responseBody, Context context) throws IOException {
        // ResponseBody에서 이미지 데이터를 읽기
        InputStream inputStream = responseBody.byteStream();

        // 이미지를 기기 내부에 저장할 파일을 생성
        File tempFile = createFile(context);

        // 파일에 이미지 데이터를 저장
        writeInputStreamToFile(inputStream, tempFile);

        // 파일의 경로를 FileProvider를 사용하여 Uri로 변환
        return FileProvider.getUriForFile(context, PACKAGE_NAME, tempFile);
    }

    private File createFile(Context context) throws IOException {
        // 파일을 저장할 경로 및 파일 이름 설정 (file 이름은 측정 정보 ID 로 설정)
        File tempDir = new File(context.getFilesDir(), "temp");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }

        String fileName = "temp.png";

        // 파일 생성
        File tempFile = new File(tempDir, fileName);

        // 기존에 파일이 있을 경우 삭제
        if (tempFile.exists()) {
            tempFile.delete();
        }

        tempFile.createNewFile();

        return tempFile;
    }


    // 파일 쓰기 - 이미지 데이터 저장 (png)
    private void writeInputStreamToFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
