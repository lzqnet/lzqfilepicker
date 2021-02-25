package com.zql.filepickerlib.dependency;

import android.os.Parcelable;


public interface IFilePickerLogger extends Parcelable {
    void e(String tag, String msg, Throwable e);

    void e(String tag, Throwable e);

    void e(String tag, String msg);

    void w(String tag, String msg);

    void i(String tag, String msg);

    void d(String tag, String msg);

    void v(String tag, String msg);
}
