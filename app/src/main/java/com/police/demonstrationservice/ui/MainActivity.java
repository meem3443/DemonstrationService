package com.police.demonstrationservice.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.police.demonstrationservice.DateManager;
import com.police.demonstrationservice.KeyStoreManager;
import com.police.demonstrationservice.NetworkManager;
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

        KeyStoreManager.getInstance().getPublicKey(getApplicationContext(), (key) -> {
            // 하루마다 날짜 업데이트, 날짜 변경 시 일출 일몰 시간 업데이트
            DateManager.getInstance().updateDate(MainActivity.this, key);
        });

        initButton();
    }

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