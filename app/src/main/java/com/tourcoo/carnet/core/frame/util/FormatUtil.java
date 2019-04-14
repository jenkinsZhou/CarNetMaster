package com.tourcoo.carnet.core.frame.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author :zhoujian
 * @description : 快速格式化工具
 * @company :翼迈科技
 * @date 2019年03月04日下午 03:54
 * @Email: 971613168@qq.com
 */
public class FormatUtil {

    /**
     * 格式化星期
     *
     * @param millis
     * @return 1-星期日...7-星期六
     */
    public static int formatWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int index = calendar.get(Calendar.DAY_OF_WEEK) + 1;
        return index;
    }

    /**
     * 格式化时间(类似yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        return formatTime(new Date(time), format);
    }

    /**
     * 格式化时间(类似yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    /**
     * 格式化数据
     *
     * @param dataSize 数据字节数
     * @return
     */
    public static String formatDataSize(long dataSize) {
        return formatDataSize(dataSize, "###.00");
    }

    /**
     * 格式化文件大小格式
     *
     * @param dataSize 字节数
     * @param pattern  格式
     * @return
     */
    public static String formatDataSize(long dataSize, String pattern) {
        DecimalFormat var2 = new DecimalFormat(pattern);
        return dataSize < 1024L ? dataSize + "Bytes" : (dataSize < 1048576L ? var2.format((double) ((float) dataSize / 1024.0F))
                + "KB" : (dataSize < 1073741824L ? var2.format((double) ((float) dataSize / 1024.0F / 1024.0F))
                + "MB" : (dataSize < 0L ? var2.format((double) ((float) dataSize / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "Error")));
    }

    /**
     * 格式化数据保留maxLength位小数--四舍五入
     *
     * @param value
     * @param maxLength
     * @return
     */
    public static String formatDoubleSize(double value, int maxLength) {
        return formatDoubleSize(value, maxLength, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 格式化数据保留maxLength位小数
     *
     * @param value
     * @param maxLength
     * @param roundingMode 保留小数规则-如四舍五入 BigDecimal.ROUND_HALF_UP {@link BigDecimal#ROUND_DOWN}
     * @return
     */
    public static String formatDoubleSize(double value, int maxLength, int roundingMode) {
        String result = value + "";
        BigDecimal bg = new BigDecimal(value);
        try {
            result = bg.setScale(maxLength, roundingMode).doubleValue() + "";
        } catch (Exception e) {
        }
        return result;
    }
}
