package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_ETC;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_HOME;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME;
import static com.police.demonstrationservice.Constants.DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_DAY_HOME;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_DAY_PUBLIC;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_ETC;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_NIGHT_HOME;
import static com.police.demonstrationservice.Constants.DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC;
import static com.police.demonstrationservice.Constants.HOUR_MINUTE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_CURRENT;
import static com.police.demonstrationservice.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstrationservice.Constants.INTENT_NAME_REQUEST_INFO;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_NOT;
import static com.police.demonstrationservice.Constants.SIMPLE_DATE_FORMAT;
import static com.police.demonstrationservice.Constants.YEAR_MONTH_DAY_DATE_FORMAT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.police.demonstrationservice.DateManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityInputNotificationNoticeBinding;
import com.police.demonstrationservice.rest_api.notification.NotificationRequest;

import java.util.Objects;

public class InputNotificationNoticeActivity extends AppCompatActivity {

    private ActivityInputNotificationNoticeBinding binding;

    private String timeZone;
    private String placeZone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInputNotificationNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray_background));

        timeZone = getString(R.string.time_zone_day);
        placeZone = getString(R.string.place_zone_home);

        calculateNoise();

        initTextView();
        initButton();
    }

    private void initTextView() {
        // 일몰 시각 반영
        String sunsetTime = DateManager.getInstance().getSunsetTime(this);
        String text = String.valueOf(sunsetTime.charAt(0)) + sunsetTime.charAt(1) + getString(R.string.hour) + getString(R.string.space) + sunsetTime.charAt(2) + sunsetTime.charAt(3) + getString(R.string.minute);
        binding.sunsetTimeDetail.setText(text);

        // 현재 날짜 반영
        String[] currentDate = DateManager.getInstance().getCurrentDate(YEAR_MONTH_DAY_DATE_FORMAT).split(getString(R.string.dash));
        String t = currentDate[0] + getString(R.string.year) + getString(R.string.space) + currentDate[1] + getString(R.string.month) + getString(R.string.space) + currentDate[2] + getString(R.string.day);
        binding.dateDetail.setText(t);

        // 낮 / 밤 이미지 반영
        String sunriseTime = DateManager.getInstance().getSunriseTime(this);
        int current = Integer.parseInt(DateManager.getInstance().getCurrentDate(HOUR_MINUTE).replaceAll(getString(R.string.dash), ""));
        int sunset = Integer.parseInt(sunsetTime.trim());
        int sunrise = Integer.parseInt(sunriseTime.trim());

        if (current > sunset || current < sunrise) {
            binding.dayNightImage.setImageResource(R.drawable.moon);
        }
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());

        binding.dayButton.setOnClickListener(e -> {
            timeZone = getString(R.string.time_zone_day);
            binding.dayButton.setBackgroundResource(R.drawable.content_button);
            binding.dayButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            binding.nightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.nightButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.lateNightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.lateNightButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            calculateNoise();
        });

        binding.nightButton.setOnClickListener(e -> {
            timeZone = getString(R.string.time_zone_night);
            binding.dayButton.setBackgroundResource(R.drawable.unclick_button);
            binding.dayButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.nightButton.setBackgroundResource(R.drawable.content_button);
            binding.nightButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            binding.lateNightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.lateNightButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            calculateNoise();
        });

        binding.lateNightButton.setOnClickListener(e -> {
            timeZone = getString(R.string.time_zone_late_night);
            binding.dayButton.setBackgroundResource(R.drawable.unclick_button);
            binding.dayButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.nightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.nightButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.lateNightButton.setBackgroundResource(R.drawable.content_button);
            binding.lateNightButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            calculateNoise();
        });

        binding.homeButton.setOnClickListener(e -> {
            placeZone = getString(R.string.place_zone_home);
            binding.homeButton.setBackgroundResource(R.drawable.content_button);
            binding.homeButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            binding.publicButton.setBackgroundResource(R.drawable.unclick_button);
            binding.publicButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.etcButton.setBackgroundResource(R.drawable.unclick_button);
            binding.etcButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            calculateNoise();
        });

        binding.publicButton.setOnClickListener(e -> {
            placeZone = getString(R.string.place_zone_public);
            binding.homeButton.setBackgroundResource(R.drawable.unclick_button);
            binding.homeButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.publicButton.setBackgroundResource(R.drawable.content_button);
            binding.publicButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            binding.etcButton.setBackgroundResource(R.drawable.unclick_button);
            binding.etcButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            calculateNoise();
        });

        binding.etcButton.setOnClickListener(e -> {
            placeZone = getString(R.string.place_zone_etc);
            binding.homeButton.setBackgroundResource(R.drawable.unclick_button);
            binding.homeButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.publicButton.setBackgroundResource(R.drawable.unclick_button);
            binding.publicButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));
            binding.etcButton.setBackgroundResource(R.drawable.content_button);
            binding.etcButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            calculateNoise();
        });

        binding.saveButton.setOnClickListener(e -> {
            if (Objects.requireNonNull(binding.inputName.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_organizer_name), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.inputPhoneNumber.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_organizer_phone_number), Toast.LENGTH_SHORT).show();
            } else {
                String current = DateManager.getInstance().getCurrentDate(SIMPLE_DATE_FORMAT);

                Intent intent = new Intent(this, SendActivity.class);

                // API 파라미터에 필요한 인자들 사용하여 생성
                NotificationRequest request = new NotificationRequest(
                        Objects.requireNonNull(binding.inputName.getText()).toString(),
                        binding.equivalenceNoiseDetail.getText().toString(),
                        binding.highestNoiseDetail.getText().toString(),
                        current.replaceAll(getString(R.string.dash), "")
                );

                intent.putExtra(INTENT_NAME_REQUEST_INFO, request);
                intent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(binding.inputPhoneNumber.getText()).toString());
                intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, NOTIFICATION_TYPE_NOT);
                intent.putExtra(INTENT_NAME_CURRENT, current);

                startActivity(intent);
            }
        });
    }

    private void calculateNoise() {
        int equivalentNoise = 0;
        int highestNoise = 0;

        if (timeZone.equals(getString(R.string.time_zone_day))) {
            if (placeZone.equals(getString(R.string.place_zone_home))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_DAY_HOME;
                highestNoise = DEFAULT_HIGHEST_NOISE_DAY_HOME;
            } else if (placeZone.equals(getString(R.string.place_zone_public))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC;
                highestNoise = DEFAULT_HIGHEST_NOISE_DAY_PUBLIC;
            } else if (placeZone.equals(getString(R.string.place_zone_etc))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_DAY_ETC;
                highestNoise = DEFAULT_HIGHEST_NOISE_ETC;
            }
        } else if (timeZone.equals(getString(R.string.time_zone_night))) {
            if (placeZone.equals(getString(R.string.place_zone_home))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME;
                highestNoise = DEFAULT_HIGHEST_NOISE_NIGHT_HOME;
            } else if (placeZone.equals(getString(R.string.place_zone_public))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC;
                highestNoise = DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC;
            } else if (placeZone.equals(getString(R.string.place_zone_etc))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC;
                highestNoise = DEFAULT_HIGHEST_NOISE_ETC;
            }
        } else if (timeZone.equals(getString(R.string.time_zone_late_night))) {
            if (placeZone.equals(getString(R.string.place_zone_home))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME;
                highestNoise = DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME;
            } else if (placeZone.equals(getString(R.string.place_zone_public))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC;
                highestNoise = DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC;
            } else if (placeZone.equals(getString(R.string.place_zone_etc))) {
                equivalentNoise = DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC;
                highestNoise = DEFAULT_HIGHEST_NOISE_ETC;
            }
        }

        String equivalenceText = equivalentNoise + getString(R.string.space) + getString(R.string.decibel);
        String highestText = highestNoise + getString(R.string.space) + getString(R.string.decibel);

        binding.equivalenceNoiseDetail.setText(equivalenceText);
        binding.highestNoiseDetail.setText(highestText);
    }
}
