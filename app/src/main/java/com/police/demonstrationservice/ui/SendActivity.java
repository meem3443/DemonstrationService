package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.INTENT_NAME_CURRENT;
import static com.police.demonstrationservice.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstrationservice.Constants.INTENT_NAME_REQUEST_INFO;
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
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_DOCUMENT_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_MESSAGE_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_NOTIFICATION_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECENT_HOUR_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECENT_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECENT_MINUTE_KEY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.police.demonstrationservice.database.DemonstrationDataBase;
import com.police.demonstrationservice.database.DemonstrationInfo;
import com.police.demonstrationservice.databinding.ActivitySendBinding;
import com.police.demonstrationservice.rest_api.notification.NotificationRequest;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SendActivity extends AppCompatActivity {

    private ActivitySendBinding binding;

    private int notificationType = 1;
    private NotificationRequest notificationRequest;
    private String phoneNumber = "";
    private String textMessage = "";

    private Uri imageUri;

    public interface CallbackListener {
        void completeGetImageUri(Uri uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 고지 타입, request 정보, 전화번호 읽기
        notificationType = getIntent().getIntExtra(INTENT_NAME_NOTIFICATION_TYPE, 1);
        notificationRequest = getIntent().getParcelableExtra(INTENT_NAME_REQUEST_INFO);
        phoneNumber = getIntent().getStringExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER);

        Log.d("테스트", String.valueOf(notificationType));

        // 메시지 보낼 때, 사용할 텍스트 메시지 읽기
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_MESSAGE_KEY, MODE_PRIVATE);
        if (notificationType == NOTIFICATION_TYPE_NOT) {
            textMessage = sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATION_KEY, "");
        } else if (notificationType == NOTIFICATION_TYPE_ELEVEN) {
            textMessage = "";
        } else {
            textMessage = sharedPreferences.getString(SHARED_PREFERENCES_DOCUMENT_KEY, "");
        }

        // statusBar 색 조정
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray_background));

        initTextView();
        initButton();

        // 고지 타입에 따라 서버에 고지서 요청
        switch (notificationType) {
            case NOTIFICATION_TYPE_NOT:
                NetworkManager.getInstance().getNotificationUriOne(this, notificationRequest, this::loadImage);
                break;
            case NOTIFICATION_TYPE_FIRST:
                NetworkManager.getInstance().getNotificationUriTwo(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.title_type_one));
                break;
            case NOTIFICATION_TYPE_SECOND:
                NetworkManager.getInstance().getNotificationUriThree(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.title_type_two));
                break;
            case NOTIFICATION_TYPE_THIRD:
                NetworkManager.getInstance().getNotificationUriFour(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.title_type_three));
                break;
            case NOTIFICATION_TYPE_FOURTH:
                NetworkManager.getInstance().getNotificationUriFive(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.after_maintenance_order));
                break;
            case NOTIFICATION_TYPE_FIFTH:
                NetworkManager.getInstance().getNotificationUriSix(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_equivalent));
                break;
            case NOTIFICATION_TYPE_SIXTH:
                NetworkManager.getInstance().getNotificationUriSeven(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_highest));
                break;
            case NOTIFICATION_TYPE_SEVENTH:
                NetworkManager.getInstance().getNotificationUriEight(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_equivalent));
                break;
            case NOTIFICATION_TYPE_EIGHTH:
                NetworkManager.getInstance().getNotificationUriNine(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.skip_maintenance_order_highest));
                break;
            case NOTIFICATION_TYPE_NINTH:
                NetworkManager.getInstance().getNotificationUriTen(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.skip_stop_order));
                break;
            case NOTIFICATION_TYPE_TENTH:
                NetworkManager.getInstance().getNotificationUriEleven(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.violation_after_a_stop_order));
                break;
            case NOTIFICATION_TYPE_ELEVEN:
                NetworkManager.getInstance().getNotificationUriTwelve(this, notificationRequest, this::loadImage);
                binding.title.setText(getString(R.string.temporary_storage_return_certificate));
                break;
            default:
                break;
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.notifyButton.setOnClickListener(e -> {
            // DB 에서 read (Rxjava 비동기) 100 개 넘는지 확인
            DemonstrationDataBase.getInstance(this)
                    .demonstrationDao()
                    .getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(list -> {
                        if (list.size() >= 100) {
                            DemonstrationDataBase.getInstance(this)
                                    .demonstrationDao()
                                    .deleteDemonstration(list.get(0))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe();
                        }
                    }).subscribe();


            // Room 명령서 발부 리스트 저장 (add)
            DemonstrationInfo demonstrationInfo = new DemonstrationInfo(
                    binding.title.getText().toString(),
                    notificationRequest.getCurrenttime(),
                    notificationType
            );

            DemonstrationDataBase.getInstance(getApplicationContext())
                    .demonstrationDao()
                    .addDemonstration(demonstrationInfo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();

            // 최근 고지 시간 저장
            String currentHour = String.valueOf(notificationRequest.getCurrenttime().charAt(8))
                    + notificationRequest.getCurrenttime().charAt(9);
            String currentMinute = String.valueOf(notificationRequest.getCurrenttime().charAt(10))
                    + notificationRequest.getCurrenttime().charAt(11);

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_RECENT_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_RECENT_HOUR_KEY, currentHour);
            editor.putString(SHARED_PREFERENCES_RECENT_MINUTE_KEY, currentMinute);
            editor.apply();

            // MMS를 보내기 위한 Intent 생성
            // FileUriExposedException (Android 7.0 이상에서 발생하는 예외) 를 피하기 위하여
            // FileProvider 사용
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_TEXT, textMessage); // 텍스트 메시지 내용
            intent.putExtra("address", phoneNumber); // 수신자 전화번호
            intent.setType("image/*"); // 이미지 MIME 타입
            intent.putExtra(Intent.EXTRA_STREAM, imageUri); // 이미지 파일의 경로

            // MMS 앱 실행
            if (intent.resolveActivity(getPackageManager()) != null) {
                messageLauncher.launch(intent);
            }
        });
    }

    private void initTextView() {
        String current = getIntent().getStringExtra(INTENT_NAME_CURRENT);
        String[] currentInfo = current.split(getString(R.string.dash));

        String date = currentInfo[0] + getString(R.string.year) + getString(R.string.space) + currentInfo[1] + getString(R.string.month) + getString(R.string.space) + currentInfo[2] + getString(R.string.day);
        String time = currentInfo[3] + getString(R.string.hour) + getString(R.string.space) + currentInfo[4] + getString(R.string.minute);

        binding.dateDetail.setText(date);
        binding.timeDetail.setText(time);
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

                        binding.notifyButton.setEnabled(true);
                        return false;
                    }
                })
                .into(binding.imageView);
    }

    // registerForActivityResult call back 설정
    ActivityResultLauncher<Intent> messageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        // 시위 관리 화면으로 이동 -> Activity Task 정리
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    });
}
