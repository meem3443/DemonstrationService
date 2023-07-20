package com.police.demonstrationservice.ui.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.police.demonstrationservice.R;
import com.police.demonstrationservice.database.DemonstrationInfo;
import com.police.demonstrationservice.databinding.RecyclerviewDemonstrationBinding;

import java.util.ArrayList;

public class DemonstrationAdapter extends RecyclerView.Adapter<DemonstrationAdapter.ViewHolder> {

    // Adapter 에서 MainActivity 로 통신할 수 있는 listener
    public interface AdapterListener {
        void inputClick(View view, DemonstrationInfo demonstrationInfo);
    }

    private AdapterListener listener = null;

    // RecyclerView 를 표시하기 위한 ArrayList -> 시위 정보
    private final ArrayList<DemonstrationInfo> demonstrationList;

    // string.xml 을 사용하기 위한 resources
    private Resources resources;

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        demonstrationList.remove(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerviewDemonstrationBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.binding = RecyclerviewDemonstrationBinding.bind(itemView);
        }
    }

    public DemonstrationAdapter(ArrayList<DemonstrationInfo> dataSet) {
        demonstrationList = dataSet;
    }

    @NonNull
    @Override
    public DemonstrationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_demonstration, parent, false);
        DemonstrationAdapter.ViewHolder viewHolder = new DemonstrationAdapter.ViewHolder(view);

        resources = parent.getResources();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 날짜 반영
        String current = demonstrationList.get(position).getCurrentTime();
        String yearMonth = getYearMonth(current);
        String day = getDay(current);

        if (Integer.parseInt(day) < 10) {
            day = resources.getString(R.string.number_zero) + day;
        }
        day += resources.getString(R.string.dot);
        holder.binding.number.setText(day);
        holder.binding.yearMonth.setText(yearMonth);

        // 제목 반영
        holder.binding.type.setText(demonstrationList.get(position).getType());

        // 구분선 반영
        if (position - 1 < 0) {
            holder.binding.yearMonth.setVisibility(View.VISIBLE);
            holder.binding.line.setVisibility(View.VISIBLE);
        } else {
            String compare = demonstrationList.get(position - 1).getCurrentTime();
            if (!getYearMonth(compare).equals(yearMonth)) {
                holder.binding.yearMonth.setVisibility(View.VISIBLE);
                holder.binding.line.setVisibility(View.VISIBLE);
            } else {
                holder.binding.yearMonth.setVisibility(View.GONE);
                holder.binding.line.setVisibility(View.GONE);
            }
        }

        // click event
        holder.binding.cardView.setOnClickListener(e -> listener.inputClick(holder.itemView, demonstrationList.get(position)));
    }

    @Override
    public int getItemCount() {
        return demonstrationList.size();
    }

    public String getYearMonth(String current) {
        return String.valueOf(current.charAt(0)) + current.charAt(1)
                + current.charAt(2) + current.charAt(3) + resources.getString(R.string.dot)
                + current.charAt(4) + current.charAt(5);
    }

    public String getDay(String current) {
        return String.valueOf(current.charAt(6)) + current.charAt(7);
    }
}