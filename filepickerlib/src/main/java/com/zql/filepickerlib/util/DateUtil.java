package com.zql.filepickerlib.util;

import android.content.Context;


import com.zql.filepickerlib.dependency.IFilePickerDateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtil {

    private static IFilePickerDateFormat filePickerDateFormat;
    private static DateFormat mDateFormatCurrentYear = new SimpleDateFormat("MM-dd", Locale.getDefault());
    private static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * Method that formats a filetime date with the specific system settings
     *
     * @param filetime The filetime date
     * @return String The filetime date formatted
     */
    public static String getFormatFileTimeDefault(Date filetime) {
        Date nowDate = new Date(System.currentTimeMillis());
        if (filetime.getYear() == nowDate.getYear()) {
            return mDateFormatCurrentYear.format(filetime);
        } else {
            return mDateFormat.format(filetime);
        }
    }


    public static void init(IFilePickerDateFormat filePickerDateFormat) {
        if (filePickerDateFormat != null) {
            DateUtil.filePickerDateFormat = filePickerDateFormat;
        }
    }

    public static String getFormatTime(Context context, Date fileTime) {
        if (DateUtil.filePickerDateFormat != null) {
            return DateUtil.filePickerDateFormat.getFormatTime(context, fileTime.getTime());
        } else {
            return getFormatFileTimeDefault(fileTime);
        }
    }

    public static void detach() {
        DateUtil.filePickerDateFormat = null;
    }

}
