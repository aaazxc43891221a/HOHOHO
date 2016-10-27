package com.first.myapp.com.myapplication.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.first.myapp.com.myapplication.R;
import com.first.myapp.com.wheelview.OnWheelChangedListener;
import com.first.myapp.com.wheelview.WheelView;
import com.first.myapp.com.wheelview.adapters.ArrayWheelAdapter;
import com.first.myapp.com.wheelview.adapters.NumericWheelAdapter;

import java.util.Calendar;
import java.util.HashMap;


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
    private String[] day_of_week = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
    boolean b  = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page_new_sms);
        initView();
        setDefaulDateInWheel();
        final GifView gifView = (GifView)findViewById(R.id.gif_view);
        gifView.setMovieResource(R.raw.run);
        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifView.setPaused(!gifView.isPaused());
            }
        });
    }

    private void initView() {
        day = (WheelView) findViewById(R.id.wheel_day_new_sms);
        month = (WheelView) findViewById(R.id.wheel_month_new_sms);
        year = (WheelView) findViewById(R.id.wheel_year_new_sms);
    }

    private void setDefaulDateInWheel() {
        calendar = Calendar.getInstance();
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

        DateNumericAdapter yearAdapter = new DateNumericAdapter(this, curYear - 30, curYear + 10, curYear - 1);
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
        Log.e("kkk", "calendar.get(Calendar.DAY_OF_WEEK)" + (calendar.get(Calendar.DAY_OF_WEEK) ));
        int curDay = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        day.setCurrentItem(curDay);
        updateDays(year, month, day);

    }

    private void updateDays(WheelView year, WheelView month, WheelView day) {
//        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        int curDay = day.getCurrentItem();
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, -1));
        int realDay = maxDays < curDay + 1 ? 0 : curDay;
        Log.e("kkk", "maxDays: " + maxDays + "    curDay + 1: " + (curDay + 1));
        day.setCurrentItem(realDay, true);
    }

    private String[] initDayOfWeek(int firstWeekDayOfMonth) {
        String[] strings = new String[7];
//        System.arraycopy(mBytes, 7, mDataBytes, 0, mDataLength - 6);
        System.arraycopy(day_of_week,firstWeekDayOfMonth,strings,0,day_of_week.length-firstWeekDayOfMonth);
        System.arraycopy(day_of_week,0,strings,firstWeekDayOfMonth,firstWeekDayOfMonth);
        return strings;
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
