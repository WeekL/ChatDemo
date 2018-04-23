package weekl.chatdemo.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final String TAG = "DateUtil";

    /**
     * 具体日期格式：年-月-日
     */
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat mYearDateFormat =
            new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 日期格式：月-日
     */
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat mDateFormat =
            new SimpleDateFormat("MM-dd");


    /**
     * 时间格式：时:分:秒
     */
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat mDetailedTime =
            new SimpleDateFormat("HH:mm:SS");

    /**
     * 时间格式：时:分
     */
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat mTimeFormat =
            new SimpleDateFormat("HH:mm");

    /**
     * 以long类型的时间来获取格式化后的时间
     *
     * @param time
     * @return
     */
    public static String getTimeFromTime(long time) {
        return mTimeFormat.format(new Date(time));
    }

    /**
     * 以long类型的时间来获取格式化后的星期时间
     *
     * @param time
     * @return
     */
    public static String getWeekDayFromTime(long time) {
        int week = getDayOfWeek(time);
        switch (week) {
            case 1:
                return "星期天";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
        return "Unknown";
    }

    /**
     * 以long类型的时间来获取格式化后的日期
     *
     * @param time
     * @return
     */
    public static String getDateFromTime(long time) {
        return mDateFormat.format(new Date(time));
    }

    /**
     * 以long类型的时间来获取格式化后的具体日期
     *
     * @param time
     * @return
     */
    public static String getYearDateFromTime(long time) {
        return mYearDateFormat.format(new Date(time));
    }

    /**
     * 判断传入的时间是一周中的哪一天
     *
     * @param time
     * @return
     */
    public static int getDayOfWeek(long time) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 计算两个long类型之间差了多少分钟
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getMinOffset(long time1, long time2) {
        return (int) Math.abs((time1 - time2) / (60 * 1000));
    }

    /**
     *
     * 计算两个时间的天数差
     * 先获取两个时间当前的起始时间，求出相对时间差
     * 再用相对时间差/一天的毫秒数，得出天数差
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getDayOffset(long time1, long time2) {
        //获取time1当天的起始时间
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(time1));
        calendar1.set(Calendar.HOUR_OF_DAY,0);
        calendar1.set(Calendar.MINUTE,0);
        calendar1.set(Calendar.SECOND,0);
        calendar1.set(Calendar.MILLISECOND,0);
        long start1 = calendar1.getTime().getTime();

        //获取time2当天的起始时间
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date(time2));
        calendar2.set(Calendar.HOUR_OF_DAY,0);
        calendar2.set(Calendar.MINUTE,0);
        calendar2.set(Calendar.SECOND,0);
        calendar2.set(Calendar.MILLISECOND,0);

        long start2 = calendar2.getTime().getTime();
        int offset = Math.abs((int) (start1 - start2));
        int oneDay = 24 * 60 * 60 * 1000;
        int dayOffset = offset / oneDay;
        return dayOffset;
    }

    /**
     * 计算两个long类型之间差了多少年
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int getYearOffset(long time1, long time2) {
        String y1 = mYearDateFormat.format(new Date(time1));
        String y2 = mYearDateFormat.format(new Date(time2));

        int year1 = Integer.parseInt(y1.split("-")[0]);
        int year2 = Integer.parseInt(y2.split("-")[0]);

        return Math.abs(year1 - year2);
    }
}
