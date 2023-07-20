package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.INTENT_NAME_CURRENT;
import static com.police.demonstrationservice.Constants.INTENT_NAME_NOTIFICATION_TYPE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_ORGANIZER_PHONE_NUMBER;
import static com.police.demonstrationservice.Constants.INTENT_NAME_REQUEST_INFO;
import static com.police.demonstrationservice.Constants.SIMPLE_DATE_FORMAT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.police.demonstrationservice.DateManager;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityInputNotificationTemporaryBinding;
import com.police.demonstrationservice.rest_api.notification.NotificationRequest;
import com.police.demonstrationservice.rest_api.notification.NotificationRequestItem;

import java.util.Objects;

public class InputNotificationTemporaryActivity extends AppCompatActivity {

    private ActivityInputNotificationTemporaryBinding binding;

    private int notificationType = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInputNotificationTemporaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initButton();
        notificationType = getIntent().getIntExtra(INTENT_NAME_NOTIFICATION_TYPE, 1);
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());

        binding.saveButton.setOnClickListener(e -> {
            if (Objects.requireNonNull(binding.inputName.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_organizer_name), Toast.LENGTH_SHORT).show();
            } else if (Objects.requireNonNull(binding.inputPhoneNumber.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_organizer_phone_number), Toast.LENGTH_SHORT).show();
            } else {
                String current = DateManager.getInstance().getCurrentDate(SIMPLE_DATE_FORMAT);

                NotificationRequest request = new NotificationRequest(
                        getItems(),
                        binding.inputName.getText().toString(),
                        current.replaceAll(getString(R.string.dash), "")
                );

                Intent intent = new Intent(this, SendActivity.class);

                intent.putExtra(INTENT_NAME_REQUEST_INFO, request);
                intent.putExtra(INTENT_NAME_ORGANIZER_PHONE_NUMBER, Objects.requireNonNull(binding.inputPhoneNumber.getText()).toString());
                intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, notificationType);
                intent.putExtra(INTENT_NAME_CURRENT, current);

                startActivity(intent);
            }
        });
    }

    private NotificationRequestItem[] getItems() {
        return new NotificationRequestItem[]{
                new NotificationRequestItem(
                        Objects.requireNonNull(binding.inputItem1.getText()).toString(),
                        Objects.requireNonNull(binding.inputQuantity1.getText()).toString(),
                        Objects.requireNonNull(binding.inputNote1.getText()).toString(),
                        Objects.requireNonNull(binding.inputAbnormality1.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(binding.inputItem2.getText()).toString(),
                        Objects.requireNonNull(binding.inputQuantity2.getText()).toString(),
                        Objects.requireNonNull(binding.inputNote2.getText()).toString(),
                        Objects.requireNonNull(binding.inputAbnormality2.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(binding.inputItem3.getText()).toString(),
                        Objects.requireNonNull(binding.inputQuantity3.getText()).toString(),
                        Objects.requireNonNull(binding.inputNote3.getText()).toString(),
                        Objects.requireNonNull(binding.inputAbnormality3.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(binding.inputItem4.getText()).toString(),
                        Objects.requireNonNull(binding.inputQuantity4.getText()).toString(),
                        Objects.requireNonNull(binding.inputNote4.getText()).toString(),
                        Objects.requireNonNull(binding.inputAbnormality4.getText()).toString()),
                new NotificationRequestItem(
                        Objects.requireNonNull(binding.inputItem5.getText()).toString(),
                        Objects.requireNonNull(binding.inputQuantity5.getText()).toString(),
                        Objects.requireNonNull(binding.inputNote5.getText()).toString(),
                        Objects.requireNonNull(binding.inputAbnormality5.getText()).toString())
        };
    }
}
