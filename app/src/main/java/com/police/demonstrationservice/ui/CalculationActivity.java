package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.HOUR_MINUTE;
import static com.police.demonstrationservice.Constants.STANDARD_CORRECTION_NOISE;
import static com.police.demonstrationservice.Constants.YEAR_MONTH_DAY_DATE_FORMAT;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.police.demonstrationservice.DateManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityCalculationBinding;

import java.util.Objects;

public class CalculationActivity extends AppCompatActivity {

    private ActivityCalculationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray_background));
        initButton();
        initTextView();
        initEditText();

        binding.standardNoise.setOnClickListener(e -> {
            Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.standard_noise)
                    + '/' + getResources().getResourceTypeName(R.drawable.standard_noise)
                    + '/' + getResources().getResourceEntryName(R.drawable.standard_noise));
            Intent intent = new Intent(this, ImageViewActivity.class);
            intent.setData(imageUri);
            startActivity(intent);
        });
    }

    private void initEditText() {
        binding.measurementEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.calculateButton.callOnClick();
                    return false;
                }
                return true;
            }
        });
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
        binding.calculateButton.setOnClickListener(e -> {
            if (Objects.requireNonNull(binding.backgroundNoiseEditText.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_background), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.measurementEditText.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_measurement), Toast.LENGTH_SHORT).show();
            } else {
                // 대상 소음도 구하기
                double backgroundNoise = Double.parseDouble(binding.backgroundNoiseEditText.getText().toString());
                double noise = Double.parseDouble(binding.measurementEditText.getText().toString());

                // 소음도의 차이는 소수 첫째 자리 까지 표시
                double differenceNoise = Math.round((noise - backgroundNoise) * 10) / 10.0;

                if (differenceNoise < 3.0) {
                    // 배경 소음도와 입력한 소음도의 차이가 3.0 미만일 경우 대상 소음도 0
                    noise = 0;
                } else if (differenceNoise <= 9.9) {
                    // 배경 소음도와 입력한 소음도의 차이가 3 dB 이상 10 dB 미만일 경우 보정
                    Double correction = STANDARD_CORRECTION_NOISE.get(differenceNoise);
                    assert correction != null;

                    double correctionNoise = correction;
                    noise += correctionNoise;
                }

                String text = Math.round(noise) + getString(R.string.space);
                binding.targetNoiseDetail.setText(text);
            }
        });
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
}
