package com.zql.filepickerlib.util;


import android.content.Context;

import com.zql.filepickerlib.dependency.IFilePickerToast;


public class Toast {

    private static IFilePickerToast filePickerToast;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    public static void init(IFilePickerToast toast) {
        if (toast != null) {
            Toast.filePickerToast = toast;
        }
    }

    public static void showSuccessText(Context context, CharSequence text, int duration) {
        if (filePickerToast != null) {
            filePickerToast.showSuccessText(context, text, duration);
        } else {
            android.widget.Toast.makeText(context, text, duration).show();
        }
    }

    public static void showFailureText(Context context, CharSequence text, int duration) {
        if (filePickerToast != null) {
            filePickerToast.showFailureText(context, text, duration);
        } else {
            android.widget.Toast.makeText(context, text, duration).show();
        }
    }

    public static void showText(Context context, CharSequence text, int duration) {
        if (filePickerToast != null) {
            filePickerToast.showText(context, text, duration);
        } else {
            android.widget.Toast.makeText(context, text, duration).show();
        }
    }

    public static void showBottomToast(Context context, CharSequence text, int duration) {
        if (filePickerToast != null) {
            filePickerToast.showBottomToast(context, text, duration);
        } else {
            android.widget.Toast.makeText(context, text, duration).show();
        }
    }

    public static void detach() {
        filePickerToast = null;
    }
}
