package com.police.demonstrationservice.ui;

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

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivitySelectNotificationTypeBinding;

public class SelectNotificationTypeActivity extends AppCompatActivity {

    private ActivitySelectNotificationTypeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectNotificationTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initButton();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.firstButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationNoticeActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_NOT);
            startActivity(intent);
        });

        binding.secondButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_FIRST);
            startActivity(intent);
        });

        binding.thirdButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_SECOND);
            startActivity(intent);
        });

        binding.fourthButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_THIRD);
            startActivity(intent);
        });

        binding.fifthButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_FOURTH);
            startActivity(intent);
        });

        binding.sixthButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_FIFTH);
            startActivity(intent);
        });

        binding.seventhButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_SIXTH);
            startActivity(intent);
        });

        binding.eighthButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_SEVENTH);
            startActivity(intent);
        });

        binding.ninthButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_EIGHTH);
            startActivity(intent);
        });

        binding.tenthButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_NINTH);
            startActivity(intent);
        });

        binding.eleventhButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_TENTH);
            startActivity(intent);
        });

        binding.twelveButton.setOnClickListener(e -> {
            Intent intent = new Intent(this, InputNotificationTemporaryActivity.class);
            intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_ELEVEN);
            startActivity(intent);
        });
    }
}
