package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.FRAGMENT_TYPE_INPUT_MEASUREMENT;
import static com.police.demonstrationservice.Constants.FRAGMENT_TYPE_TEMPORARY;
import static com.police.demonstrationservice.Constants.INTENT_NAME_CURRENT;
import static com.police.demonstrationservice.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstrationservice.Constants.INTENT_NAME_REQUEST_INFO;
import static com.police.demonstrationservice.Constants.NOISE_TYPE_HIGHEST;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_EIGHTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FIFTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FIRST;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FOURTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_NINTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_SECOND;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_SEVENTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_SIXTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_TENTH;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_THIRD;
import static com.police.demonstrationservice.Constants.SIMPLE_DATE_FORMAT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.police.demonstrationservice.manager.DateManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityInputNotificationBinding;
import com.police.demonstrationservice.rest_api.notification.NotificationRequest;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestAction;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestCnt;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestItem;
import com.police.demonstrationservice.ui.input_notification.InputFragment;
import com.police.demonstrationservice.ui.input_notification.TemporaryFragment;

import java.util.Objects;

public class InputNotificationActivity extends AppCompatActivity {
    private ActivityInputNotificationBinding binding;

    // 최고소음 위반 일 때 사용할 Fragment
    private InputFragment fragment1;
    private InputFragment fragment2;
    private InputFragment fragment3;

    // 일시 보관일 때 사용할 Fragment
    private TemporaryFragment fragmentTemp;

    private int fragmentType = FRAGMENT_TYPE_INPUT_MEASUREMENT;
    private int notificationType = 1;

    private Fragment currentFragment = null;

    @Override
    protected void onResume() {
        super.onResume();

        fragment2.getBinding().inputName.setEnabled(false);
        fragment2.getBinding().inputPhoneNumber.setEnabled(false);
        fragment3.getBinding().inputName.setEnabled(false);
        fragment3.getBinding().inputPhoneNumber.setEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInputNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentTemp = new TemporaryFragment();

        fragment1 = new InputFragment();
        fragment2 = new InputFragment();
        fragment3 = new InputFragment();

        currentFragment = fragment1;

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray_background));
        initButton();
        notificationType = getIntent().getIntExtra(INTENT_NAME_NOTIFICATION_TYPE, 1);

        // 초기 Fragment 표시
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout, fragment1)
                .add(R.id.frameLayout, fragment2)
                .hide(fragment2)
                .add(R.id.frameLayout, fragment3)
                .hide(fragment3)
                .add(R.id.frameLayout, fragmentTemp)
                .hide(fragmentTemp)
                .commitAllowingStateLoss();

        initTitle();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());

        binding.saveButton.setOnClickListener(e -> {
            // 입력 예외 처리
            if (fragment1.getBinding().inputPlace.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_measurement_place), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(fragment1.getBinding().windSpeedNatural.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_wind_speed), Toast.LENGTH_SHORT).show();
            } else if (fragment1.getBinding().measurementNoiseDetail.getText().toString().equals(getString(R.string.example_noise))) {
                Toast.makeText(this, getString(R.string.plz_input_measurement_noise), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_organizer_name), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_organizer_phone_number), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(fragment1.getBinding().measurementDistanceDetail.getText()).toString().equals("")) {
                Toast.makeText(this, "측정 거리를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (!fragment1.getCanNotify()) {
                Toast.makeText(this, getString(R.string.plz_confirm_measurement_noise), Toast.LENGTH_SHORT).show();
            } else {
                String winterSpeed = Objects.requireNonNull(fragment1.getBinding().windSpeedNatural.getText()).toString();
                if (!Objects.requireNonNull(fragment1.getBinding().windSpeedDecimal.getText()).toString().equals("")) {
                    winterSpeed += getString(R.string.dot) + fragment1.getBinding().windSpeedDecimal.getText().toString();
                }

                String current = DateManager.getInstance().getCurrentDate(SIMPLE_DATE_FORMAT);
                String measurementNoise = fragment1.getBinding().measurementNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];
                String standardNoise = fragment1.getBinding().standardNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];

                switch (notificationType) {
                    case NOTIFICATION_TYPE_FIRST:
                    case NOTIFICATION_TYPE_SECOND:
                    case NOTIFICATION_TYPE_FIFTH:
                        Intent intent = new Intent(this, SendActivity.class);
                        // API 파라미터에 필요한 인자들 사용하여 생성
                        NotificationRequest request = new NotificationRequest(
                                fragment1.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment1.getBinding().endTime.getText().toString(),
                                fragment1.getTimeZone(),
                                fragment1.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment1.getBinding().inputDetailPlace.getText()),
                                fragment1.getBinding().measurementDistanceDetail.getText().toString(),
                                fragment1.getPlaceZone(),
                                winterSpeed,
                                standardNoise,
                                measurementNoise,
                                Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString(),
                                current.replaceAll(getString(R.string.dash), "")
                        );

                        intent.putExtra(INTENT_NAME_REQUEST_INFO, request);
                        intent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                        intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                        intent.putExtra(INTENT_NAME_CURRENT, current);

                        startActivity(intent);
                        break;
                    case NOTIFICATION_TYPE_THIRD:
                    case NOTIFICATION_TYPE_SIXTH:
                        if (Objects.requireNonNull(fragment2.getBinding().inputPlace.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_place), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment2.getBinding().windSpeedNatural.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_input_wind_speed), Toast.LENGTH_SHORT).show();
                        } else if (fragment2.getBinding().measurementNoiseDetail.getText().toString().equals(getString(R.string.example_noise))) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment2.getBinding().measurementDistanceDetail.getText()).toString().equals("")) {
                            Toast.makeText(this, "두 번째 기록의 측정 거리를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (!fragment2.getCanNotify()) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_confirm_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment3.getBinding().inputPlace.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_place), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment3.getBinding().windSpeedNatural.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_input_wind_speed), Toast.LENGTH_SHORT).show();
                        } else if (fragment3.getBinding().measurementNoiseDetail.getText().toString().equals(getString(R.string.example_noise))) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment3.getBinding().measurementDistanceDetail.getText()).toString().equals("")) {
                            Toast.makeText(this, "세 번째 기록의 측정 거리를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (!fragment3.getCanNotify()) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_confirm_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else {
                            String currentTime = DateManager.getInstance().getCurrentDate(SIMPLE_DATE_FORMAT);

                            Intent sendIntent = new Intent(this, SendActivity.class);
                            // API 파라미터에 필요한 인자들 사용하여 생성
                            NotificationRequest notificationRequest = new NotificationRequest(
                                    getDemo(),
                                    currentTime.replaceAll(getString(R.string.dash), ""),
                                    Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString()
                            );

                            sendIntent.putExtra(INTENT_NAME_REQUEST_INFO, notificationRequest);
                            sendIntent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                            sendIntent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                            sendIntent.putExtra(INTENT_NAME_CURRENT, currentTime);

                            startActivity(sendIntent);
                        }
                        break;
                    case NOTIFICATION_TYPE_FOURTH:
                        String[] orderTime = fragment1.getBinding().orderTime.getText().toString().split(getString(R.string.colon));
                        String hour = orderTime[0].trim();

                        Log.d("테스트", hour);
                        String minute = orderTime[1].trim();

                        String noiseDivision = getString(R.string.equivalence_noise);

                        if (fragment1.getNoiseType() == NOISE_TYPE_HIGHEST) {
                            noiseDivision = getString(R.string.highest_noise);
                        }

                        Intent intent4 = new Intent(this, SendActivity.class);
                        // API 파라미터에 필요한 인자들 사용하여 생성
                        NotificationRequest request4 = new NotificationRequest(
                                hour,
                                minute,
                                fragment1.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment1.getBinding().endTime.getText().toString(),
                                fragment1.getTimeZone(),
                                fragment1.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment1.getBinding().inputDetailPlace.getText()),
                                fragment1.getBinding().measurementDistanceDetail.getText().toString(),
                                fragment1.getPlaceZone(),
                                noiseDivision,
                                winterSpeed,
                                standardNoise,
                                measurementNoise,
                                current.replaceAll(getString(R.string.dash), ""),
                                Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString()
                        );

                        intent4.putExtra(INTENT_NAME_REQUEST_INFO, request4);
                        intent4.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                        intent4.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                        intent4.putExtra(INTENT_NAME_CURRENT, current);

                        startActivity(intent4);
                        break;
                    case NOTIFICATION_TYPE_SEVENTH:
                        Intent intent5 = new Intent(this, SendActivity.class);
                        // API 파라미터에 필요한 인자들 사용하여 생성
                        NotificationRequest request5 = new NotificationRequest(
                                fragment1.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment1.getBinding().endTime.getText().toString(),
                                fragment1.getTimeZone(),
                                fragment1.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment1.getBinding().inputDetailPlace.getText()),
                                fragment1.getBinding().measurementDistanceDetail.getText().toString(),
                                fragment1.getPlaceZone(),
                                winterSpeed,
                                standardNoise,
                                measurementNoise,
                                getItems(),
                                getAction(),
                                current.replaceAll(getString(R.string.dash), ""),
                                Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString()
                        );

                        intent5.putExtra(INTENT_NAME_REQUEST_INFO, request5);
                        intent5.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                        intent5.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                        intent5.putExtra(INTENT_NAME_CURRENT, current);

                        startActivity(intent5);
                        break;
                    case NOTIFICATION_TYPE_EIGHTH:
                        if (Objects.requireNonNull(fragment2.getBinding().inputPlace.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_place), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment2.getBinding().windSpeedNatural.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_input_wind_speed), Toast.LENGTH_SHORT).show();
                        } else if (fragment2.getBinding().measurementNoiseDetail.getText().toString().equals(getString(R.string.example_noise))) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment2.getBinding().measurementDistanceDetail.getText()).toString().equals("")) {
                            Toast.makeText(this, "두 번째 기록의 측정 거리를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (!fragment2.getCanNotify()) {
                            Toast.makeText(this, getString(R.string.second_record) + getString(R.string.space) + getString(R.string.plz_confirm_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment3.getBinding().inputPlace.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_place), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment3.getBinding().windSpeedNatural.getText()).toString().equals("")) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_input_wind_speed), Toast.LENGTH_SHORT).show();
                        } else if (fragment3.getBinding().measurementNoiseDetail.getText().toString().equals(getString(R.string.example_noise))) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_input_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else if (Objects.requireNonNull(fragment3.getBinding().measurementDistanceDetail.getText()).toString().equals("")) {
                            Toast.makeText(this, "세 번째 기록의 측정 거리를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else if (!fragment3.getCanNotify()) {
                            Toast.makeText(this, getString(R.string.third_record) + getString(R.string.space) + getString(R.string.plz_confirm_measurement_noise), Toast.LENGTH_SHORT).show();
                        } else {
                            String currentTime = DateManager.getInstance().getCurrentDate(SIMPLE_DATE_FORMAT);

                            Intent sendIntent = new Intent(this, SendActivity.class);
                            // API 파라미터에 필요한 인자들 사용하여 생성
                            NotificationRequest notificationRequest = new NotificationRequest(
                                    getDemo(),
                                    getItems(),
                                    getAction(),
                                    currentTime.replaceAll(getString(R.string.dash), ""),
                                    Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString()
                            );

                            sendIntent.putExtra(INTENT_NAME_REQUEST_INFO, notificationRequest);
                            sendIntent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                            sendIntent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                            sendIntent.putExtra(INTENT_NAME_CURRENT, currentTime);

                            startActivity(sendIntent);
                        }
                        break;
                    case NOTIFICATION_TYPE_NINTH:
                        String[] orderTime2 = fragment1.getBinding().orderTime.getText().toString().split(getString(R.string.colon));
                        String hour2 = orderTime2[0].trim();
                        String minute2 = orderTime2[1].trim();

                        String noiseDivision2 = getString(R.string.equivalence_noise);

                        if (fragment1.getNoiseType() == NOISE_TYPE_HIGHEST) {
                            noiseDivision2 = getString(R.string.highest_noise);
                        }

                        Intent intent9 = new Intent(this, SendActivity.class);
                        // API 파라미터에 필요한 인자들 사용하여 생성
                        NotificationRequest request9 = new NotificationRequest(
                                hour2,
                                minute2,
                                fragment1.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment1.getBinding().endTime.getText().toString(),
                                fragment1.getTimeZone(),
                                fragment1.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment1.getBinding().inputDetailPlace.getText()),
                                fragment1.getBinding().measurementDistanceDetail.getText().toString(),
                                fragment1.getPlaceZone(),
                                noiseDivision2,
                                winterSpeed,
                                standardNoise,
                                measurementNoise,
                                getItems(),
                                getAction(),
                                current.replaceAll(getString(R.string.dash), ""),
                                Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString()
                        );

                        intent9.putExtra(INTENT_NAME_REQUEST_INFO, request9);
                        intent9.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                        intent9.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                        intent9.putExtra(INTENT_NAME_CURRENT, current);

                        startActivity(intent9);
                        break;
                    case NOTIFICATION_TYPE_TENTH:
                        String[] orderTime3 = fragment1.getBinding().orderTime.getText().toString().split(getString(R.string.colon));
                        String hour3 = orderTime3[0].trim();
                        String minute3 = orderTime3[1].trim();

                        Intent intent10 = new Intent(this, SendActivity.class);
                        // API 파라미터에 필요한 인자들 사용하여 생성
                        NotificationRequest request10 = new NotificationRequest(
                                hour3,
                                minute3,
                                fragment1.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment1.getBinding().endTime.getText().toString(),
                                fragment1.getTimeZone(),
                                fragment1.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment1.getBinding().inputDetailPlace.getText()),
                                fragment1.getBinding().measurementDistanceDetail.getText().toString(),
                                fragment1.getPlaceZone(),
                                winterSpeed,
                                measurementNoise,
                                getItemsNotificationTypeTen(),
                                getActionNotificationTypeTen(),
                                current.replaceAll(getString(R.string.dash), ""),
                                Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString()
                        );

                        intent10.putExtra(INTENT_NAME_REQUEST_INFO, request10);
                        intent10.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                        intent10.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                        intent10.putExtra(INTENT_NAME_CURRENT, current);

                        startActivity(intent10);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initTitle() {
        fragment1.setNotificationType(notificationType);
        switch (notificationType) {
            case NOTIFICATION_TYPE_FIRST:
                binding.title.setText(getString(R.string.exceed_highest_noise));
                break;
            case NOTIFICATION_TYPE_SECOND:
                binding.title.setText(getString(R.string.exceed_equivalent_noise));
                break;
            case NOTIFICATION_TYPE_THIRD:
                initSpinner();
                binding.title.setText(getString(R.string.highest_noise_violation));
                break;
            case NOTIFICATION_TYPE_FOURTH:
                binding.title.setText(getString(R.string.after_maintenance_order));
                break;
            case NOTIFICATION_TYPE_FIFTH:
                binding.title.setText(getString(R.string.skip_maintenance_order_equivalent));
                break;
            case NOTIFICATION_TYPE_SIXTH:
                initSpinner();
                binding.title.setText(getString(R.string.skip_maintenance_order_highest));
                break;
            case NOTIFICATION_TYPE_SEVENTH:
                initChangeFragmentButton();
                binding.title.setText(getString(R.string.skip_maintenance_order_equivalent));
                break;
            case NOTIFICATION_TYPE_EIGHTH:
                initChangeFragmentButton();
                initSpinner();
                binding.title.setText(getString(R.string.skip_maintenance_order_highest));
                break;
            case NOTIFICATION_TYPE_NINTH:
                initChangeFragmentButton();
                binding.title.setText(getString(R.string.skip_stop_order));
                break;
            case NOTIFICATION_TYPE_TENTH:
                initChangeFragmentButton();
                binding.title.setText(getString(R.string.violation_after_a_stop_order));
                break;
            default:
                break;
        }
    }

    private void initSpinner() {
        binding.spinner.setVisibility(View.VISIBLE);
        final String[] adapterList = getResources().getStringArray(R.array.spinner_count);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, adapterList);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).commitAllowingStateLoss();
                    getSupportFragmentManager().beginTransaction().show(fragment1).commitAllowingStateLoss();
                    currentFragment = fragment1;
                } else if (position == 1) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).commitAllowingStateLoss();
                    getSupportFragmentManager().beginTransaction().show(fragment2).commitAllowingStateLoss();
                    currentFragment = fragment2;

                    fragment2.getBinding().inputName.setText(Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString());
                    fragment2.getBinding().inputPhoneNumber.setText(Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                } else if (position == 2) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).commitAllowingStateLoss();
                    getSupportFragmentManager().beginTransaction().show(fragment3).commitAllowingStateLoss();
                    currentFragment = fragment3;

                    fragment3.getBinding().inputName.setText(Objects.requireNonNull(fragment1.getBinding().inputName.getText()).toString());
                    fragment3.getBinding().inputPhoneNumber.setText(Objects.requireNonNull(fragment1.getBinding().inputPhoneNumber.getText()).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initChangeFragmentButton() {
        fragmentTemp.setNotificationType(notificationType);

        binding.inputMeasurementButton.setVisibility(View.VISIBLE);
        binding.temporaryButton.setVisibility(View.VISIBLE);
        binding.line.setVisibility(View.VISIBLE);

        binding.inputMeasurementButton.setOnClickListener(e -> {
            fragmentType = FRAGMENT_TYPE_INPUT_MEASUREMENT;
            binding.inputMeasurementButton.setBackgroundResource(R.drawable.content_button);
            binding.inputMeasurementButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            binding.temporaryButton.setBackgroundResource(R.drawable.unclick_button);
            binding.temporaryButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));

            getSupportFragmentManager().beginTransaction().hide(currentFragment).commitAllowingStateLoss();
            getSupportFragmentManager().beginTransaction().show(fragment1).commitAllowingStateLoss();
            currentFragment = fragment1;
        });

        binding.temporaryButton.setOnClickListener(e -> {
            fragmentType = FRAGMENT_TYPE_TEMPORARY;
            binding.temporaryButton.setBackgroundResource(R.drawable.content_button);
            binding.temporaryButton.setTextColor(ContextCompat.getColor(this, R.color.main_color));
            binding.inputMeasurementButton.setBackgroundResource(R.drawable.unclick_button);
            binding.inputMeasurementButton.setTextColor(ContextCompat.getColor(this, R.color.un_click));

            getSupportFragmentManager().beginTransaction().hide(currentFragment).commitAllowingStateLoss();
            getSupportFragmentManager().beginTransaction().show(fragmentTemp).commitAllowingStateLoss();
            currentFragment = fragmentTemp;
        });
    }

    private NotificationRequestCnt[] getDemo() {
        String winterSpeed1 = Objects.requireNonNull(fragment1.getBinding().windSpeedNatural.getText()).toString();
        if (!Objects.requireNonNull(fragment1.getBinding().windSpeedDecimal.getText()).toString().equals("")) {
            winterSpeed1 += getString(R.string.dot) + fragment1.getBinding().windSpeedDecimal.getText().toString();
        }

        String measurementNoise1 = fragment1.getBinding().measurementNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];
        String standardNoise1 = fragment1.getBinding().standardNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];

        NotificationRequestCnt notificationRequestCnt = new NotificationRequestCnt(
                "1",
                fragment1.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment1.getBinding().endTime.getText().toString(),
                fragment1.getTimeZone(),
                fragment1.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment1.getBinding().inputDetailPlace.getText()),
                fragment1.getBinding().measurementDistanceDetail.getText().toString(),
                fragment1.getPlaceZone(),
                winterSpeed1,
                standardNoise1,
                measurementNoise1
        );

        String winterSpeed2 = Objects.requireNonNull(fragment2.getBinding().windSpeedNatural.getText()).toString();
        if (!Objects.requireNonNull(fragment2.getBinding().windSpeedDecimal.getText()).toString().equals("")) {
            winterSpeed2 += getString(R.string.dot) + fragment2.getBinding().windSpeedDecimal.getText().toString();
        }

        String measurementNoise2 = fragment2.getBinding().measurementNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];
        String standardNoise2 = fragment2.getBinding().standardNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];

        NotificationRequestCnt notificationRequestCnt2 = new NotificationRequestCnt(
                "2",
                fragment2.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment2.getBinding().endTime.getText().toString(),
                fragment2.getTimeZone(),
                fragment2.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment2.getBinding().inputDetailPlace.getText()),
                fragment2.getBinding().measurementDistanceDetail.getText().toString(),
                fragment2.getPlaceZone(),
                winterSpeed2,
                standardNoise2,
                measurementNoise2
        );

        String winterSpeed3 = Objects.requireNonNull(fragment3.getBinding().windSpeedNatural.getText()).toString();
        if (!Objects.requireNonNull(fragment3.getBinding().windSpeedDecimal.getText()).toString().equals("")) {
            winterSpeed3 += getString(R.string.dot) + fragment3.getBinding().windSpeedDecimal.getText().toString();
        }

        String measurementNoise3 = fragment3.getBinding().measurementNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];
        String standardNoise3 = fragment3.getBinding().standardNoiseDetail.getText().toString().split(getString(R.string.split_space))[0];

        NotificationRequestCnt notificationRequestCnt3 = new NotificationRequestCnt(
                "3",
                fragment3.getBinding().startTime.getText().toString() + getString(R.string.space) + getString(R.string.tilde) + getString(R.string.space) + fragment3.getBinding().endTime.getText().toString(),
                fragment3.getTimeZone(),
                fragment3.getBinding().inputPlace.getText().toString() + getString(R.string.space) + Objects.requireNonNull(fragment3.getBinding().inputDetailPlace.getText()),
                fragment3.getBinding().measurementDistanceDetail.getText().toString(),
                fragment3.getPlaceZone(),
                winterSpeed3,
                standardNoise3,
                measurementNoise3
        );

        return new NotificationRequestCnt[]{notificationRequestCnt, notificationRequestCnt2, notificationRequestCnt3};
    }

    private NotificationRequestItem[] getItems() {
        return new NotificationRequestItem[]{
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem1.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity1.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote1.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace1.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem2.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity2.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote2.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace2.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem3.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity3.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote3.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace3.getText()).toString())
        };
    }

    private NotificationRequestAction[] getAction() {
        return new NotificationRequestAction[]{
                new NotificationRequestAction(Objects.requireNonNull(fragmentTemp.getBinding().inputWhatToDo1.getText()).toString()),
                new NotificationRequestAction(Objects.requireNonNull(fragmentTemp.getBinding().inputWhatToDo2.getText()).toString())
        };
    }

    private NotificationRequestItem[] getItemsNotificationTypeTen() {
        return new NotificationRequestItem[]{
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem1.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity1.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote1.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace1.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem2.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity2.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote2.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace2.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem3.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity3.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote3.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace3.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(fragmentTemp.getBinding().inputItem4.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputQuantity4.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputNote4.getText()).toString(),
                        Objects.requireNonNull(fragmentTemp.getBinding().inputPlace4.getText()).toString())
        };
    }

    private NotificationRequestAction[] getActionNotificationTypeTen() {
        return new NotificationRequestAction[]{
                new NotificationRequestAction(Objects.requireNonNull(fragmentTemp.getBinding().inputWhatToDo1.getText()).toString()),
                new NotificationRequestAction(Objects.requireNonNull(fragmentTemp.getBinding().inputWhatToDo2.getText()).toString()),
                new NotificationRequestAction(Objects.requireNonNull(fragmentTemp.getBinding().inputWhatToDo3.getText()).toString())
        };
    }
}
