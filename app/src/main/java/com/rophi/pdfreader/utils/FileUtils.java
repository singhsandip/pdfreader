package com.rophi.pdfreader.utils;

import java.io.File;
import java.text.DecimalFormat;

public class FileUtils {

    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final long Mi = 1024 * 1024;
    private static final long KB = 1024;

    public static String getFileSize(File file) {

        if (!file.isFile()) {
            throw new IllegalArgumentException("Expected a file");
        }
        final double length = file.length();

        if (length > Mi) {
            return format.format(length / Mi) + " MB";
        }
        if (length > KB) {
            return format.format(length / KB) + " KB";
        }
        return format.format(length) + " B";
    }
}
