package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.INTENT_NAME_CURRENT;
import static com.police.demonstrationservice.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_EIGHTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_ELEVEN;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FIFTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FIRST;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FOURTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_NINTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_NOT;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_SECOND;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_SEVENTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_SIXTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_TENTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_THIRD;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.police.demonstrationservice.NetworkManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityDownloadNotificationBinding;
import com.police.demonstrationservice.rest_api.notification.NotificationRequest;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestAction;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestCnt;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestItem;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class DownloadNotificationActivity extends AppCompatActivity {

    private ActivityDownloadNotificationBinding binding;

    private int notificationType;
    private Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDownloadNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray_background));

        notificationType = getIntent().getIntExtra(INTENT_NAME_NOTIFICATION_TYPE, 0);

        // 이미지 로딩
        String current = getIntent().getStringExtra(INTENT_NAME_CURRENT);

        // 고지 타입에 따라 서버에 고지서 요청
        switch (notificationType) {
            case NOTIFICATION_TYPE_NOT:
                NotificationRequest notificationRequest = new NotificationRequest("", "", "", current);
                NetworkManager.getInstance().getNotificationUriOne(this, notificationRequest, this::loadImage);
                break;
            case NOTIFICATION_TYPE_FIRST:
                NotificationRequest notificationRequest2 = new NotificationRequest("", "", "", "", "", "", "", "", "", current);
                NetworkManager.getInstance().getNotificationUriTwo(this, notificationRequest2, this::loadImage);
                binding.title.setText(getString(R.string.title_type_one));
                break;
            case NOTIFICATION_TYPE_SECOND:
                NotificationRequest notificationRequest3 = new NotificationRequest("", "", "", "", "", "", "", "", "", current);
                NetworkManager.getInstance().getNotificationUriThree(this, notificationRequest3, this::loadImage);
                binding.title.setText(getString(R.string.title_type_two));
                break;
            case NOTIFICATION_TYPE_THIRD:
                NotificationRequest notificationRequest4 = new NotificationRequest(getDemo(), current, "");
                NetworkManager.getInstance().getNotificationUriFour(this, notificationRequest4, this::loadImage);
                binding.title.setText(getString(R.string.title_type_three));
                break;
            case NOTIFICATION_TYPE_FOURTH:
                NotificationRequest notificationRequest5 = new NotificationRequest("", "", "", "", "", "", "", "", "", "", "", current, "");
                NetworkManager.getInstance().getNotificationUriFive(this, notificationRequest5, this::loadImage);
                binding.title.setText(getString(R.string.after_maintenance_order));
                break;
            case NOTIFICATION_TYPE_FIFTH:
                NotificationRequest notificationRequest6 = new NotificationRequest("", "", "", "", "", "", "", "", "", current);
                NetworkManager.getInstance().getNotificationUriSix(this, notificationRequest6, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_equivalent));
                break;
            case NOTIFICATION_TYPE_SIXTH:
                NotificationRequest notificationRequest7 = new NotificationRequest(getDemo(), current, "");
                NetworkManager.getInstance().getNotificationUriSeven(this, notificationRequest7, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_highest));
                break;
            case NOTIFICATION_TYPE_SEVENTH:
                NotificationRequest notificationRequest8 = new NotificationRequest("", "", "", "", "", "", "", "", getItems(), getAction(), current, "");
                NetworkManager.getInstance().getNotificationUriEight(this, notificationRequest8, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_equivalent));
                break;
            case NOTIFICATION_TYPE_EIGHTH:
                NotificationRequest notificationRequest9 = new NotificationRequest(getDemo(), getItems(), getAction(), current, "");
                NetworkManager.getInstance().getNotificationUriNine(this, notificationRequest9, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_highest));
                break;
            case NOTIFICATION_TYPE_NINTH:
                NotificationRequest notificationRequest10 = new NotificationRequest("", "", "", "", "", "", "", "", "", "", "", getItems(), getAction(), current, "");
                NetworkManager.getInstance().getNotificationUriTen(this, notificationRequest10, this::loadImage);
                binding.title.setText(getString(R.string.skip_stop_order));
                break;
            case NOTIFICATION_TYPE_TENTH:
                NotificationRequest notificationRequest11 = new NotificationRequest("", "", "", "", "", "", "", "", "", "", "", getItemsNotificationTypeTen(), getActionNotificationTypeTen(), current, "");
                NetworkManager.getInstance().getNotificationUriEleven(this, notificationRequest11, this::loadImage);
                binding.title.setText(getString(R.string.violation_after_a_stop_order));
                break;
            case NOTIFICATION_TYPE_ELEVEN:
                NotificationRequest notificationRequest12 = new NotificationRequest(
                        new NotificationRequestItem[]{
                                new NotificationRequestItem("", "", "", ""),
                                new NotificationRequestItem("", "", "", ""),
                                new NotificationRequestItem("", "", "", ""),
                                new NotificationRequestItem("", "", "", ""),
                                new NotificationRequestItem("", "", "", "")
                        },
                        "",
                        current
                );
                NetworkManager.getInstance().getNotificationUriTwelve(this, notificationRequest12, this::loadImage);
                binding.title.setText(getString(R.string.temporary_storage_return_certificate));
                break;
            default:
                break;
        }

//        // initTextView
//        String text = String.valueOf(current.charAt(0)) + current.charAt(1)
//                + current.charAt(2) + current.charAt(3) + getString(R.string.year)
//                + getString(R.string.space) + current.charAt(4) + current.charAt(5) + getString(R.string.month)
//                + getString(R.string.space) + current.charAt(6) + current.charAt(7) + getString(R.string.day)
//                + getString(R.string.space) + current.charAt(8) + current.charAt(9) + getString(R.string.hour)
//                + getString(R.string.space) + current.charAt(10) + current.charAt(11) + getString(R.string.minute);
//        binding.title.setText(text);

        initButton();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.downloadButton.setOnClickListener(e -> saveImageToGallery(imageUri));
    }

    // 이미지를 ImageView 에 보여주고, 로드 성공과 실패 시 이벤트 등록
    private void loadImage(Uri uri) {
        imageUri = uri;

        // Glide를 사용하여 이미지를 로딩
        Glide.with(this)
                .load(uri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // 이미지 로딩 실패 시 경고 토스트 메시지 출력 후 화면 종료
                        binding.progressBar.setVisibility(View.GONE); // 로딩 화면 숨김
                        Toast.makeText(getBaseContext(), getString(R.string.plz_retry_connect_network), Toast.LENGTH_SHORT).show();
                        finish();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 이미지 로딩 성공 시 프로그래스바 숨김, 화면 버튼 클릭 활성화
                        binding.progressBar.setVisibility(View.GONE); // 로딩 화면 숨김

                        // 이미지 뷰 클릭 이벤트
                        binding.imageView.setOnClickListener(e -> {
                            // ImageViewActivity 로 전환
                            Intent intent = new Intent(getBaseContext(), ImageViewActivity.class);
                            intent.setData(uri);
                            startActivity(intent);
                        });

                        binding.downloadButton.setEnabled(true);
                        return false;
                    }
                })
                .into(binding.imageView);
    }

    private void saveImageToGallery(Uri imageUri) {
        // 이미지 가져오기
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 이미지를 갤러리에 저장
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, binding.title.getText().toString() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, binding.title.getText().toString() + "/jpeg");

        ContentResolver contentResolver = getContentResolver();
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 이상인 경우
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            // Android 9 이하인 경우
            String picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            values.put(MediaStore.Images.Media.DATA, picturesDirectory + File.separator + binding.title.getText().toString() + ".jpg");
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Uri item = contentResolver.insert(collection, values);
        if (item == null) {
            // 저장 실패 처리
            Toast.makeText(this, getString(R.string.plz_retry), Toast.LENGTH_SHORT).show();
            return;
        }

        try (OutputStream outputStream = contentResolver.openOutputStream(item)) {
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, getString(R.string.complete_save), Toast.LENGTH_SHORT).show();
        finish();
    }

    private NotificationRequestCnt[] getDemo() {
        return new NotificationRequestCnt[]{
                new NotificationRequestCnt("", "", "", "", "", "", "", "", ""),
                new NotificationRequestCnt("", "", "", "", "", "", "", "", ""),
                new NotificationRequestCnt("", "", "", "", "", "", "", "", "")
        };
    }

    private NotificationRequestItem[] getItems() {
        return new NotificationRequestItem[]{
                new NotificationRequestItem("", "", "", ""),
                new NotificationRequestItem("", "", "", ""),
                new NotificationRequestItem("", "", "", "")
        };
    }

    private NotificationRequestAction[] getAction() {
        return new NotificationRequestAction[]{
                new NotificationRequestAction(""),
                new NotificationRequestAction("")
        };
    }

    private NotificationRequestItem[] getItemsNotificationTypeTen() {
        return new NotificationRequestItem[]{
                new NotificationRequestItem("", "", "", ""),
                new NotificationRequestItem("", "", "", ""),
                new NotificationRequestItem("", "", "", ""),
                new NotificationRequestItem("", "", "", "")
        };
    }

    private NotificationRequestAction[] getActionNotificationTypeTen() {
        return new NotificationRequestAction[]{
                new NotificationRequestAction(""),
                new NotificationRequestAction(""),
                new NotificationRequestAction("")
        };
    }
}
