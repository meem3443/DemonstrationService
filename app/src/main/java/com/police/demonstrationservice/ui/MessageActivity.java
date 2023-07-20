package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.INTENT_NAME_DEFAULT_MESSAGE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_MESSAGE;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_DOCUMENT_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_MESSAGE_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_NOTIFICATION_KEY;
import static com.police.demonstrationservice.Constants.SHARED_PREFERENCES_RECORD_DATE_KEY;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityMessageBinding;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_MESSAGE_KEY, MODE_PRIVATE);

        initTextView();
        initButton();
    }

    private void initTextView() {
        // 기존에 저장된 메시지 반영
        String notificationText = sharedPreferences.getString(SHARED_PREFERENCES_NOTIFICATION_KEY, "");
        String documentText = sharedPreferences.getString(SHARED_PREFERENCES_DOCUMENT_KEY, "");

        if (!notificationText.equals("")) {
            binding.notificationMessageDetail.setText(notificationText);
        }

        if (!documentText.equals("")) {
            binding.documentMessageDetail.setText(documentText);
        }

        // 메시지 새로 입력하는 화면으로 전환
        binding.notificationMessageDetail.setOnClickListener(e -> {
            Intent intent = new Intent(this, AttachedMessageActivity.class);
            intent.putExtra(INTENT_NAME_DEFAULT_MESSAGE, binding.notificationMessageDetail.getText().toString());
            addNotificationMessageLauncher.launch(intent);
        });
        binding.documentMessageDetail.setOnClickListener(e -> {
            Intent intent = new Intent(this, AttachedMessageActivity.class);
            intent.putExtra(INTENT_NAME_DEFAULT_MESSAGE, binding.documentMessageDetail.getText().toString());
            addDocumentMessageLauncher.launch(intent);
        });
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());
    }

    // registerForActivityResult call back 설정
    ActivityResultLauncher<Intent> addNotificationMessageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            // RESULT_OK
            Intent intent = data.getData();
            assert intent != null;

            binding.notificationMessageDetail.setText(intent.getStringExtra(INTENT_NAME_MESSAGE));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_NOTIFICATION_KEY, binding.notificationMessageDetail.getText().toString());
            editor.apply();

            Toast.makeText(this, getString(R.string.complete_save), Toast.LENGTH_SHORT).show();
        }
    });

    // registerForActivityResult call back 설정
    ActivityResultLauncher<Intent> addDocumentMessageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
        if (data.getResultCode() == Activity.RESULT_OK) {
            // RESULT_OK
            Intent intent = data.getData();
            assert intent != null;

            binding.documentMessageDetail.setText(intent.getStringExtra(INTENT_NAME_MESSAGE));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_DOCUMENT_KEY, binding.documentMessageDetail.getText().toString());
            editor.apply();

            Toast.makeText(this, getString(R.string.complete_save), Toast.LENGTH_SHORT).show();
        }
    });
}
