package com.police.demonstrationservice.ui.input_notification;

import static android.app.Activity.RESULT_OK;
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
import static com.police.demonstrationservice.Constants.NOISE_TYPE_EQUIVALENT;
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
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECENT_HOUR_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECENT_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECENT_MINUTE_KEY;
import static com.police.demonstrationservice.Constants.STANDARD_CORRECTION_NOISE;
import static com.police.demonstrationservice.Constants.YEAR_MONTH_DAY_DATE_FORMAT;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.police.demonstrationservice.manager.DateManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.FragmentInputBinding;
import com.police.demonstrationservice.ui.Current_Place_M;

import java.util.Calendar;
import java.util.Objects;

public class InputFragment extends Fragment {

    private FragmentInputBinding binding;

    private int notificationType = NOTIFICATION_TYPE_FIRST;
    private String timeZone;
    private String placeZone;
    private int noiseType = NOISE_TYPE_EQUIVALENT;

    private boolean canNotify = false;

    public void setNotificationType(int type) {
        notificationType = type;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public String getPlaceZone() {
        return this.placeZone;
    }

    public boolean getCanNotify() {
        return this.canNotify;
    }

    public int getNoiseType() {
        return this.noiseType;
    }

    public FragmentInputBinding getBinding() {
        return this.binding;
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Bundle bundle = intent.getBundleExtra("result");
                    if (bundle != null) {

                        binding.inputPlace.setText(bundle.getString("M_add"));
                        binding.measurementDistanceDetail.setText(bundle.getString("D_add"));

                    }
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        timeZone = getString(R.string.time_zone_day);
        placeZone = getString(R.string.place_zone_home);

        initTextView();
        initButton();
        initSpinner();
        calculateNoise();

        // 고지 타입 4번, 9번일 경우 '소음도 구분' 항목 visible
        if (notificationType == NOTIFICATION_TYPE_FOURTH ||
                notificationType == NOTIFICATION_TYPE_NINTH) {
            binding.noiseLayout.setVisibility(View.VISIBLE);
            binding.orderLayout.setVisibility(View.VISIBLE);
        }

        // 고지 타입 4번, 9번일 경우 '이전 명령 기록' 항목 visible
        if (notificationType == NOTIFICATION_TYPE_FOURTH
                || notificationType == NOTIFICATION_TYPE_NINTH
                || notificationType == NOTIFICATION_TYPE_TENTH) {
            binding.orderLayout.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_RECENT_KEY, Context.MODE_PRIVATE);
            String hour = sharedPreferences.getString(SHARED_PREFERENCES_RECENT_HOUR_KEY, "00");
            String minute = sharedPreferences.getString(SHARED_PREFERENCES_RECENT_MINUTE_KEY, "00");

            String text = hour + " : " + minute;
            binding.orderTime.setText(text);
        }

        return root;
    }

    private void initTextView() {
        // 일몰 시각 반영
        String sunsetTime = DateManager.getInstance().getSunsetTime(requireContext());
        String text = String.valueOf(sunsetTime.charAt(0)) + sunsetTime.charAt(1) + getString(R.string.hour) + getString(R.string.space) + sunsetTime.charAt(2) + sunsetTime.charAt(3) + getString(R.string.minute);
        binding.sunsetTimeDetail.setText(text);

        // 현재 날짜 반영
        String[] currentDate = DateManager.getInstance().getCurrentDate(YEAR_MONTH_DAY_DATE_FORMAT).split(getString(R.string.dash));
        String t = currentDate[0] + getString(R.string.year) + getString(R.string.space) + currentDate[1] + getString(R.string.month) + getString(R.string.space) + currentDate[2] + getString(R.string.day);
        binding.dateDetail.setText(t);

        // 낮 / 밤 이미지 반영
        String sunriseTime = DateManager.getInstance().getSunriseTime(requireContext());
        int current = Integer.parseInt(DateManager.getInstance().getCurrentDate(HOUR_MINUTE).replaceAll(getString(R.string.dash), ""));
        int sunset = Integer.parseInt(sunsetTime.trim());
        int sunrise = Integer.parseInt(sunriseTime.trim());

        if (current > sunset || current < sunrise) {
            binding.dayNightImage.setImageResource(R.drawable.moon);
        }

        if (notificationType == NOTIFICATION_TYPE_SECOND
                || notificationType == NOTIFICATION_TYPE_FOURTH
                || notificationType == NOTIFICATION_TYPE_FIFTH
                || notificationType == NOTIFICATION_TYPE_SEVENTH
                || notificationType == NOTIFICATION_TYPE_NINTH) {
            binding.highest.setText(getString(R.string.equivalence));
        }

        if (notificationType == NOTIFICATION_TYPE_TENTH) {
            binding.highest.setVisibility(View.GONE);
        }
    }

    private void initButton() {
        binding.dayButton.setOnClickListener(e -> {
            timeZone = getString(R.string.time_zone_day);
            binding.dayButton.setBackgroundResource(R.drawable.content_button);
            binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            binding.nightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.nightButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.lateNightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.lateNightButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            calculateNoise();
        });

        binding.nightButton.setOnClickListener(e -> {
            timeZone = getString(R.string.time_zone_night);
            binding.dayButton.setBackgroundResource(R.drawable.unclick_button);
            binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.nightButton.setBackgroundResource(R.drawable.content_button);
            binding.nightButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            binding.lateNightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.lateNightButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            calculateNoise();
        });

        binding.lateNightButton.setOnClickListener(e -> {
            timeZone = getString(R.string.time_zone_late_night);
            binding.dayButton.setBackgroundResource(R.drawable.unclick_button);
            binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.nightButton.setBackgroundResource(R.drawable.unclick_button);
            binding.nightButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.lateNightButton.setBackgroundResource(R.drawable.content_button);
            binding.lateNightButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            calculateNoise();
        });

        binding.homeButton.setOnClickListener(e -> {
            placeZone = getString(R.string.place_zone_home);
            binding.homeButton.setBackgroundResource(R.drawable.content_button);
            binding.homeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            binding.publicButton.setBackgroundResource(R.drawable.unclick_button);
            binding.publicButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.etcButton.setBackgroundResource(R.drawable.unclick_button);
            binding.etcButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            calculateNoise();
        });

        binding.publicButton.setOnClickListener(e -> {
            placeZone = getString(R.string.place_zone_public);
            binding.homeButton.setBackgroundResource(R.drawable.unclick_button);
            binding.homeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.publicButton.setBackgroundResource(R.drawable.content_button);
            binding.publicButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            binding.etcButton.setBackgroundResource(R.drawable.unclick_button);
            binding.etcButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            calculateNoise();
        });

        binding.etcButton.setOnClickListener(e -> {
            placeZone = getString(R.string.place_zone_etc);
            binding.homeButton.setBackgroundResource(R.drawable.unclick_button);
            binding.homeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.publicButton.setBackgroundResource(R.drawable.unclick_button);
            binding.publicButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));
            binding.etcButton.setBackgroundResource(R.drawable.content_button);
            binding.etcButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            calculateNoise();
        });

        binding.calculateButton.setOnClickListener(e -> {
            // 키패드 내리기
            View currentFocus = requireActivity().getCurrentFocus();
            if (currentFocus != null && currentFocus.getWindowToken() != null) {
                // 포커스가 있는 뷰에서 작업 수행
                InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            if (Objects.requireNonNull(binding.inputBackgroundNoise.getText()).toString().equals("")) {
                Toast.makeText(requireContext(), getString(R.string.plz_input_background), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.inputMeasurementNoise.getText()).toString().equals("")) {
                Toast.makeText(requireContext(), getString(R.string.plz_input_measurement), Toast.LENGTH_SHORT).show();
            } else {
                // 대상 소음도 구하기
                double backgroundNoise = 0.0;
                double noise = 0.0;

                try {
                    backgroundNoise = Double.parseDouble(binding.inputBackgroundNoise.getText().toString());
                    noise = Double.parseDouble(binding.inputMeasurementNoise.getText().toString());
                } catch (NumberFormatException exception) {
                    Toast.makeText(requireContext(), getString(R.string.plz_input_number), Toast.LENGTH_SHORT).show();
                    return;
                }

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

                int measurementNoise = (int) Math.round(noise);
                String text = measurementNoise + getString(R.string.space) + getString(R.string.decibel);
                binding.measurementNoiseDetail.setText(text);

                // 10번째 고지서일 경우 기준 소음도 x
                if (notificationType == NOTIFICATION_TYPE_TENTH) {
                    return;
                }
                int standardNoise = Integer.parseInt(binding.standardNoiseDetail.getText().toString().split(getString(R.string.split_space))[0]);
                if (standardNoise < measurementNoise) {
                    canNotify = true;
                    binding.measurementNoiseDetail.setTextColor(requireActivity().getColor(R.color.red));
                } else {
                    canNotify = false;
                    binding.measurementNoiseDetail.setTextColor(requireActivity().getColor(R.color.main_color));
                }
            }
        });

        binding.equivalenceButton.setOnClickListener(e -> {
            noiseType = NOISE_TYPE_EQUIVALENT;
            binding.highest.setText(getString(R.string.equivalence));
            binding.equivalenceButton.setBackgroundResource(R.drawable.content_button);
            binding.equivalenceButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            binding.highestButton.setBackgroundResource(R.drawable.unclick_button);
            binding.highestButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));

            calculateNoise();
        });

        binding.highestButton.setOnClickListener(e -> {
            noiseType = NOISE_TYPE_HIGHEST;
            binding.highest.setText(getString(R.string.highest));
            binding.highestButton.setBackgroundResource(R.drawable.content_button);
            binding.highestButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color));
            binding.equivalenceButton.setBackgroundResource(R.drawable.unclick_button);
            binding.equivalenceButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.un_click));

            calculateNoise();
        });

        binding.Locater.setOnClickListener(e -> {
            Intent intent = new Intent(getActivity(), Current_Place_M.class);
            launcher.launch(intent);
        });
    }

    private void initSpinner() {
        Calendar calendar = Calendar.getInstance();

        binding.startTime.setOnClickListener(e -> {
            // 시작 시간 입력 TimePicker
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, selectedHour, selectedMinute) -> {
                // 시작 시간 입력 시 시작 시간 textView 에 반영
                binding.startTime.setText(getTimeText(selectedHour, selectedMinute));
                // 시작 시간 입력 시 마침 시간 textView 에 10분 추가 하여 반영
                binding.endTime.setText(getTimeText(selectedHour, selectedMinute + 10));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(getString(R.string.input_start_time));
            timePickerDialog.show();
        });

        binding.endTime.setOnClickListener(e -> {
            // 마침 시간 입력 TimePicker
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, selectedHour, selectedMinute) -> {
                // 마침 시간 입력 시 마침 시간 textView 에 반영
                binding.endTime.setText(getTimeText(selectedHour, selectedMinute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(getString(R.string.input_end_time));
            timePickerDialog.show();
        });

        binding.orderTime.setOnClickListener(e -> {
            // 이전 명령 시간 입력 TimePicker
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (timePicker, selectedHour, selectedMinute) -> {
                // 이전 명령 시간 입력 시 시작 시간 textView 에 반영
                binding.orderTime.setText(getTimeText(selectedHour, selectedMinute));
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle(getString(R.string.order_time));
            timePickerDialog.show();
        });
    }

    private void calculateNoise() {
        if (notificationType == NOTIFICATION_TYPE_TENTH) {
            binding.standardNoise.setVisibility(View.GONE);
            binding.standardNoiseDetail.setVisibility(View.GONE);
            canNotify = true;
            return;
        }

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

        String text = getString(R.string.space) + getString(R.string.decibel);
        switch (notificationType) {
            case NOTIFICATION_TYPE_FIRST:
            case NOTIFICATION_TYPE_THIRD:
            case NOTIFICATION_TYPE_SIXTH:
            case NOTIFICATION_TYPE_EIGHTH:
                text = highestNoise + text;
                break;

            case NOTIFICATION_TYPE_SECOND:
            case NOTIFICATION_TYPE_FIFTH:
            case NOTIFICATION_TYPE_SEVENTH:
                text = equivalentNoise + text;
                break;
            case NOTIFICATION_TYPE_FOURTH:
            case NOTIFICATION_TYPE_NINTH:
                if (noiseType == NOISE_TYPE_EQUIVALENT) {
                    text = equivalentNoise + text;
                } else if (noiseType == NOISE_TYPE_HIGHEST) {
                    text = highestNoise + text;
                }
            default:
                break;
        }

        binding.standardNoiseDetail.setText(text);
    }

    private String getTimeText(int selectedHour, int selectedMinute) {
        // 1. 분이 60 이상일 경우에 시간 계산
        // 2. 시 혹은 분이 10 미만일 경우에 앞에 "0" 을 붙여 "01" 과 같은 String 으로 변환
        // 3. '[시간] + " : " + [분]' 의 형태로 String 값을 반환

        int minute = selectedMinute % 60;
        int hour = (selectedHour + (int) (selectedMinute / 60)) % 24;

        String h = String.valueOf(hour);
        String m = String.valueOf(minute);

        if (minute < 10) {
            m = getString(R.string.number_zero) + minute;
        }

        if (hour < 10) {
            h = getString(R.string.number_zero) + hour;
        }

        return h + getString(R.string.space) + getString(R.string.colon) + getString(R.string.space) + m;
    }
}
