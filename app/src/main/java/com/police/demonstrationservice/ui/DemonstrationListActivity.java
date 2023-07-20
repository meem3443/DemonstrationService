package com.police.demonstrationservice.ui;

import static com.police.demonstrationservice.Constants.INTENT_NAME_CURRENT;
import static com.police.demonstrationservice.Constants.INTENT_NAME_NOTIFICATION_TYPE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstrationservice.DemonstrationAdapter;
import com.police.demonstrationservice.R;
import com.police.demonstrationservice.database.DemonstrationDataBase;
import com.police.demonstrationservice.database.DemonstrationInfo;
import com.police.demonstrationservice.databinding.ActivityDemonstrationListBinding;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DemonstrationListActivity extends AppCompatActivity {

    private ActivityDemonstrationListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDemonstrationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(e -> finish());

        // DB 에서 read (Rxjava 비동기) 후 listener 를 통해 변경 알림 (onChanged())
        DemonstrationDataBase.getInstance(this)
                .demonstrationDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(list -> {
                    ArrayList<DemonstrationInfo> requestArrayList = new ArrayList<>(list);

                    if (requestArrayList.size() == 0) {
                        binding.noTextView.setVisibility(View.VISIBLE);
                    }

                    requestArrayList.sort((o1, o2) -> Long.compare(Long.parseLong(o2.getCurrentTime()), Long.parseLong(o1.getCurrentTime())));

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    binding.demonstrationRecyclerView.setLayoutManager(linearLayoutManager);
                    DemonstrationAdapter demonstrationAdapter = new DemonstrationAdapter(requestArrayList);
                    demonstrationAdapter.setListener((view, demonstrationInfo) -> {
                        Intent intent = new Intent(this, DownloadNotificationActivity.class);
                        intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, demonstrationInfo.getNotificationType());
                        intent.putExtra(INTENT_NAME_CURRENT, demonstrationInfo.getCurrentTime());
                        startActivity(intent);
                    });
                    binding.demonstrationRecyclerView.setAdapter(demonstrationAdapter);


                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            int position = viewHolder.getAbsoluteAdapterPosition();

                            if (direction == ItemTouchHelper.LEFT) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DemonstrationListActivity.this);
                                builder.setTitle("삭제");
                                builder.setMessage("정말 삭제하시겠습니까?");
                                builder.setPositiveButton("확인", (dialog, which) -> {
                                    // 삭제할 아이템
                                    DemonstrationInfo demonstrationInfo = requestArrayList.get(position);

                                    // Room 에서 삭제
                                    DemonstrationDataBase.getInstance(DemonstrationListActivity.this)
                                            .demonstrationDao()
                                            .deleteDemonstration(demonstrationInfo)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .doOnComplete(() -> {

                                            })
                                            .subscribe();

                                    // RecyclerView 에서 삭제
                                    requestArrayList.remove(position);
                                    demonstrationAdapter.notifyDataSetChanged();

                                    if (requestArrayList.size() == 0) {
                                        binding.noTextView.setVisibility(View.VISIBLE);
                                    }

                                    Toast.makeText(DemonstrationListActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                });
                                builder.setNegativeButton("취소", (dialog, which) -> demonstrationAdapter.notifyDataSetChanged());
                                builder.show();
                            } else {
                                demonstrationAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(DemonstrationListActivity.this, DownloadNotificationActivity.class);
                                intent.putExtra(INTENT_NAME_NOTIFICATION_TYPE, requestArrayList.get(position).getNotificationType());
                                intent.putExtra(INTENT_NAME_CURRENT, requestArrayList.get(position).getCurrentTime());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                                    .addSwipeLeftBackgroundColor(Color.RED)
                                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_sweep_24)
                                    .addSwipeLeftLabel("삭제")
                                    .setSwipeLeftLabelColor(Color.WHITE)
                                    .addSwipeRightBackgroundColor(Color.GREEN)
                                    .addSwipeRightActionIcon(R.drawable.baseline_list_24)
                                    .create()
                                    .decorate();

                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }).attachToRecyclerView(binding.demonstrationRecyclerView);
                }).subscribe();
    }
}
