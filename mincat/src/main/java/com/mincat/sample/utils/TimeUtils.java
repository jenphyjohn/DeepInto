package com.mincat.sample.utils;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Ming
 * @描述 倒计时
 */
public class TimeUtils {


    public static final String pattern = "yyyy-MM-dd HH:mm:ss";

    public static void countTime(TextView mShi, TextView mFen, TextView mMiao) {

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);

        StringBuffer buffer = new StringBuffer();
        String substring = format.substring(0, 11);
        buffer.append(substring);


        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour % 2 == 0) {
            buffer.append((hour + 2));
            buffer.append(":00:00");
        } else {
            buffer.append((hour + 1));
            buffer.append(":00:00");
        }

        String totalTime = buffer.toString();
        try {
            java.util.Date date = df.parse(totalTime);
            java.util.Date date1 = df.parse(format);
            long defferenttime = date.getTime() - date1.getTime();
            long days = defferenttime / (1000 * 60 * 60 * 24);
            long hours = (defferenttime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minute = (defferenttime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = defferenttime % 60000;
            long second = Math.round((float) seconds / 1000);
            mShi.setText("0" + hours + "");
            if (minute >= 10) {
                mFen.setText(minute + "");
            } else {
                mFen.setText("0" + minute + "");
            }
            if (second >= 10) {
                mMiao.setText(second + "");
            } else {
                mMiao.setText("0" + second + "");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
