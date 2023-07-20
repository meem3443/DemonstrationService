package com.police.demonstrationservice.ui.input_notification;

import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_FIRST;
import static com.police.demonstrationservice.Constants.NOTIFICATION_TYPE_TENTH;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.police.demonstrationservice.R;
import com.police.demonstrationservice.databinding.FragmentTemporaryBinding;

public class TemporaryFragment extends Fragment {

    private FragmentTemporaryBinding binding;

    private int notificationType = NOTIFICATION_TYPE_FIRST;

    private int temporaryCount = 1;
    private int etcCount = 1;

    public FragmentTemporaryBinding getBinding() { return this.binding; }
    public void setNotificationType(int type) {
        notificationType = type;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTemporaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initButton();

        return root;
    }

    private void initButton() {
        binding.addTemporaryButton.setOnClickListener(e -> {
            switch (temporaryCount) {
                case 1:
                    binding.temporaryRow2.setVisibility(View.VISIBLE);
                    binding.temporaryNumber1.setBackgroundResource(R.drawable.table_inside);
                    binding.inputPlace1.setBackgroundResource(R.drawable.table_inside);
                    temporaryCount++;
                    break;
                case 2:
                    binding.temporaryRow3.setVisibility(View.VISIBLE);
                    binding.temporaryNumber2.setBackgroundResource(R.drawable.table_inside);
                    binding.inputPlace2.setBackgroundResource(R.drawable.table_inside);
                    temporaryCount++;
                    break;
                case 3:
                    if (notificationType == NOTIFICATION_TYPE_TENTH) {
                        binding.temporaryRow4.setVisibility(View.VISIBLE);
                        binding.temporaryNumber3.setBackgroundResource(R.drawable.table_inside);
                        binding.inputPlace3.setBackgroundResource(R.drawable.table_inside);
                        temporaryCount++;
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.cant_add_more), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    Toast.makeText(requireContext(), getString(R.string.cant_add_more), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });

        binding.addEtcButton.setOnClickListener(e -> {
            switch (etcCount) {
                case 1:
                    binding.etcRow2.setVisibility(View.VISIBLE);
                    binding.etcNumber1.setBackgroundResource(R.drawable.table_inside);
                    binding.inputWhatToDo1.setBackgroundResource(R.drawable.table_inside);
                    etcCount++;
                    break;
                case 2:
                    if (notificationType == NOTIFICATION_TYPE_TENTH) {
                        binding.etcRow3.setVisibility(View.VISIBLE);
                        binding.etcNumber2.setBackgroundResource(R.drawable.table_inside);
                        binding.inputWhatToDo2.setBackgroundResource(R.drawable.table_inside);
                        etcCount++;
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.cant_add_more), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    Toast.makeText(requireContext(), getString(R.string.cant_add_more), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });
    }
}
