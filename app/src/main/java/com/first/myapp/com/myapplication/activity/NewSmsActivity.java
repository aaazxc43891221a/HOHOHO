package com.first.myapp.com.myapplication.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.first.myapp.com.myapplication.R;
import com.first.myapp.com.wheelview.OnWheelChangedListener;
import com.first.myapp.com.wheelview.WheelView;
import com.first.myapp.com.wheelview.adapters.ArrayWheelAdapter;
import com.first.myapp.com.wheelview.adapters.NumericWheelAdapter;

import java.util.Calendar;


/**
 * Created by chauvard on 10/14/16.
 */

public class NewSmsActivity extends AppCompatActivity {

    private WheelView day;
    private OnWheelChangedListener listener;
    private Calendar calendar;
    private WheelView month;
    private WheelView year;
    private int realYear;
    private int realMonth;
    private int realDay;
    private String[] day_of_week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    TextView txtAutoNaviInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page_new_sms);
        initView();
        setDefaulDateInWheel();
//        final GifView gifView = (GifView)findViewById(R.id.gif_view);
//        gifView.setMovieResource(R.raw.run);
//        gifView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gifView.setPaused(!gifView.isPaused());
//            }
//        });
    }



    private void initView() {
        day = (WheelView) findViewById(R.id.wheel_day_new_sms);
        month = (WheelView) findViewById(R.id.wheel_month_new_sms);
        year = (WheelView) findViewById(R.id.wheel_year_new_sms);
    }

    private void setDefaulDateInWheel() {
        calendar = Calendar.getInstance();
        int j = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e("kkk", "j: " + j);
        realYear = calendar.get(Calendar.YEAR);
        realMonth = calendar.get(Calendar.MONTH);
        realDay = calendar.get(Calendar.DAY_OF_MONTH);
        listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };

        //year
        int curYear = calendar.get(Calendar.YEAR);

        DateNumericAdapter yearAdapter = new DateNumericAdapter(this, 1980, curYear + 10, curYear - 1);
        year.setViewAdapter(yearAdapter);

        year.setCurrentItemByValue(curYear);
        year.addChangingListener(listener);


        //month
        int curMonth = calendar.get(Calendar.MONTH);
        Log.e("kkk", "setDefaulDateInWheel: " + curMonth);
        String months[] = new String[]{"一月", "二月", "三月", "四月", "五月",
                "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);

        //day
        Log.e("kkk", "calendar.get(Calendar.DAY_OF_WEEK)" + (calendar.get(Calendar.DAY_OF_WEEK)));
        int curDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        day.setViewAdapter(new DayDateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        updateDays(year, month, day);
        day.setCurrentItem(curDay);

    }

    private void updateDays(WheelView year, WheelView month, WheelView day) {
        calendar.set(Calendar.YEAR, 1980 + year.getCurrentItem());

        Log.e("kkk", "year: " + (1980 + year.getCurrentItem()));

        calendar.set(Calendar.MONTH, month.getCurrentItem());
        Log.e("kkk", "month: " + month.getCurrentItem());
        int curDay = day.getCurrentItem();
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int temp = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e("kkk", "i: " + i);
        calendar.set(Calendar.DAY_OF_MONTH, temp);
        day.setViewAdapter(new DayDateNumericAdapter(this, 1, maxDays, -1, initDayOfWeek(i)));
        int realDay = maxDays < curDay + 1 ? 0 : curDay;
        Log.e("kkk", "maxDays: " + maxDays + "    curDay + 1: " + (curDay + 1));
        day.setCurrentItem(realDay, true);
    }

    private String[] initDayOfWeek(int firstWeekDayOfMonth) {
        String[] strings = new String[7];
//        System.arraycopy(mBytes, 7, mDataBytes, 0, mDataLength - 6);
        System.arraycopy(day_of_week, firstWeekDayOfMonth - 1, strings, 0, day_of_week.length - firstWeekDayOfMonth + 1);
        Log.e("kkk", "strings: " + strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3] + " " + strings[4] + " " + strings[5] + " " + strings[6] + " ");
        System.arraycopy(day_of_week, 0, strings, day_of_week.length - firstWeekDayOfMonth + 1, firstWeekDayOfMonth - 1);
        Log.e("kkk", "strings: " + strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3] + " " + strings[4] + " " + strings[5] + " " + strings[6] + " ");
        return strings;
    }

    private class DayDateNumericAdapter extends DateNumericAdapter {
        int minValue;
        String[] strings;

        public DayDateNumericAdapter(Context context, int minValue, int maxValue, int current, String[] weeks) {
            super(context, minValue, maxValue, current);
            this.minValue = minValue;
            this.strings = weeks;
        }

        @Override
        public CharSequence getItemText(int index) {
            if (index >= 0 && index < getItemsCount()) {
                int value = minValue + index;
//                String str_year = calendar.get(Calendar.YEAR) + "";
//                int int_month = calendar.get(Calendar.MONTH);
//                String str_month = int_month <= 9 ? int_month + "0" : int_month + "";
//                String p_time = str_year + "-" + str_month + "-01";

                if (realYear == calendar.get(Calendar.YEAR) &&
                    realMonth == calendar.get(Calendar.MONTH)&&
                    realDay == index +1) {
                    return "今天";
                }else {
                String dayOfMonth = Integer.toString(value);
                return dayOfMonth + "号 " + strings[index % 7];}
            }
            return null;
        }
    }

    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;


        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
            }
            //设置wheelview样式
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public CharSequence getItemText(int index) {
            return super.getItemText(index);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;

            return super.getItem(index, cachedView, parent);
        }
    }

    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
