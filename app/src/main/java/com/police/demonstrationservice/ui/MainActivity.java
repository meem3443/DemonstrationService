package com.police.demonstrationservice.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import com.police.demonstrationservice.manager.DateManager;
import com.police.demonstrationservice.manager.KeyStoreManager;
import com.police.demonstrationservice.manager.NetworkManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // 뒤로 가기 버튼 클릭 체크
    private boolean doubleBackToExitPressedOnce = false;

    public interface CallBackListener {
        void finishGetKey(String key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check permission
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                            KeyStoreManager.getInstance().getPublicKey(getApplicationContext(), (key) -> {
                                // 하루마다 날짜 업데이트, 날짜 변경 시 일출 일몰 시간 업데이트
                                DateManager.getInstance().updateDate(MainActivity.this, key);
                            });
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                            KeyStoreManager.getInstance().getPublicKey(getApplicationContext(), (key) -> {
                                // 하루마다 날짜 업데이트, 날짜 변경 시 일출 일몰 시간 업데이트
                                DateManager.getInstance().updateDate(MainActivity.this, key);
                            });
                        } else {
                            // No location access granted.
                            finish();
                            Toast.makeText(this, "권한을 허용해주세요.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    }
            );

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_the_back_button), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void initButton() {
        binding.calculationButton.setOnClickListener(e -> {
            if (NetworkManager.getInstance().isNetworkConnected(getApplicationContext())) {
                // 인터넷 연결 상태
                startActivity(new Intent(this, CalculationActivity.class));
            } else {
                // 인터넷 연결이 없음
                Toast.makeText(this, getString(R.string.plz_check_connected_internet), Toast.LENGTH_SHORT).show();
            }
        });

        binding.messageButton.setOnClickListener(e -> startActivity(new Intent(this, MessageActivity.class)));

        binding.notificationButton.setOnClickListener(e -> {
            if (NetworkManager.getInstance().isNetworkConnected(getApplicationContext())) {
                // 인터넷 연결 상태
                startActivity(new Intent(this, SelectNotificationTypeActivity.class));
            } else {
                // 인터넷 연결이 없음
                Toast.makeText(this, getString(R.string.plz_check_connected_internet), Toast.LENGTH_SHORT).show();
            }
        });

        binding.notificationListButton.setOnClickListener(e -> startActivity(new Intent(this, DemonstrationListActivity.class)));
    }
}