package com.example.to_doapplication.ui.calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_doapplication.databinding.ItemCalendarDayBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    public interface OnDateClickListener {
        void onDateClick(long dateMillis);
    }

    public static class DayItem {
        public long millis;
        public String dayOfWeek;
        public String dayOfMonth;
        public int year, month, day;

        public DayItem(long millis, String dayOfWeek, String dayOfMonth, int year, int month, int day) {
            this.millis = millis;
            this.dayOfWeek = dayOfWeek;
            this.dayOfMonth = dayOfMonth;
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    private final List<DayItem> days = new ArrayList<>();
    private long selectedMillis;
    private final OnDateClickListener listener;

    public CalendarAdapter(OnDateClickListener listener) {
        this.listener = listener;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        selectedMillis = cal.getTimeInMillis();

        cal.add(Calendar.DAY_OF_YEAR, -30);
        for (int i = 0; i < 60; i++) {
            long millis = cal.getTimeInMillis();
            String dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
            String dayOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            int y = cal.get(Calendar.YEAR);
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);

            days.add(new DayItem(millis, dayOfWeek != null ? dayOfWeek.toUpperCase() : "", dayOfMonth, y, m, d));
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public long getSelectedMillis() {
        return selectedMillis;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCalendarDayBinding binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CalendarViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        DayItem dayItem = days.get(position);
        holder.bind(dayItem);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        private final ItemCalendarDayBinding binding;

        public CalendarViewHolder(@NonNull ItemCalendarDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DayItem dayItem) {
            binding.tvDayOfWeek.setText(dayItem.dayOfWeek);
            binding.tvDayOfMonth.setText(dayItem.dayOfMonth);

            Calendar sel = Calendar.getInstance();
            sel.setTimeInMillis(selectedMillis);

            boolean isSelected = sel.get(Calendar.YEAR) == dayItem.year &&
                    sel.get(Calendar.MONTH) == dayItem.month &&
                    sel.get(Calendar.DAY_OF_MONTH) == dayItem.day;

            if (isSelected) {
                binding.containerCalendarDay.setBackgroundColor(Color.parseColor("#8687E7"));
                binding.tvDayOfWeek.setTextColor(Color.WHITE);
                binding.tvDayOfMonth.setTextColor(Color.WHITE);
            } else {
                binding.containerCalendarDay.setBackgroundColor(Color.parseColor("#121212"));
                binding.tvDayOfWeek.setTextColor(Color.parseColor("#AFAFAF"));
                binding.tvDayOfMonth.setTextColor(Color.WHITE);
            }

            binding.getRoot().setOnClickListener(v -> {
                selectedMillis = dayItem.millis;
                notifyDataSetChanged();
                if (listener != null) listener.onDateClick(selectedMillis);
            });
        }
    }
}
