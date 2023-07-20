package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.INTENT_NAME_DEFAULT_MESSAGE;
import static com.police.demonstrationservice.Constants.INTENT_NAME_MESSAGE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.ActivityAttachedMessageBinding;

import java.util.Objects;

public class AttachedMessageActivity extends AppCompatActivity {

    private ActivityAttachedMessageBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttachedMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.messageEditText.setText(getIntent().getStringExtra(INTENT_NAME_DEFAULT_MESSAGE));

        initButton();
    }

    private void initButton() {
        binding.backButton.setOnClickListener(e -> finish());

        binding.saveButton.setOnClickListener(e -> {
            if (Objects.requireNonNull(binding.messageEditText.getText()).toString().equals("")) {
                Toast.makeText(this, getString(R.string.plz_input_content), Toast.LENGTH_SHORT).show();
            } else {
                // 입력 값이 있을 경우 setResult 로 입력 값 전달 후 종료
                Intent intent = new Intent();
                intent.putExtra(INTENT_NAME_MESSAGE, Objects.requireNonNull(binding.messageEditText.getText()).toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
