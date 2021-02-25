package com.zql.filepickerlib.util;


import com.zql.filepickerlib.dependency.IFilePickerLogger;


public class Log {
    private static IFilePickerLogger filePickerLogger;

    public static void init(IFilePickerLogger logger) {
        if (logger != null) {
            Log.filePickerLogger = logger;
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (filePickerLogger != null) {
            filePickerLogger.e(tag, msg, e);
        }
    }

    public static void e(String tag, Throwable e) {
        if (filePickerLogger != null) {
            filePickerLogger.e(tag, e);
        }
    }

    public static void e(String tag, String msg) {
        if (filePickerLogger != null) {
            filePickerLogger.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (filePickerLogger != null) {
            filePickerLogger.w(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (filePickerLogger != null) {
            filePickerLogger.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (filePickerLogger != null) {
            filePickerLogger.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (filePickerLogger != null) {
            filePickerLogger.v(tag, msg);
        }
    }

    public static void detach() {
        filePickerLogger = null;
    }

}
