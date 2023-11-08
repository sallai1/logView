package com.sallai.logview.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @ClassName FileUtil
 * @Description TODO
 * @Author sallai
 * @Date 21:37 2023/10/29
 * @Version 1.0
 **/
public class FileUtil {
    public static String formatFileSize(File file) {
        long bytes = file.length();
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            double kilobytes = bytes / 1024.0;
            return formatDecimal(kilobytes, " KB");
        } else if (bytes < 1024 * 1024 * 1024) {
            double megabytes = bytes / (1024.0 * 1024);
            return formatDecimal(megabytes, " MB");
        } else {
            double gigabytes = bytes / (1024.0 * 1024 * 1024);
            return formatDecimal(gigabytes, " GB");
        }
    }

    private static String formatDecimal(double value, String unit) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(value) + unit;
    }
}
