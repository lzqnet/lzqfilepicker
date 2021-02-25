package com.zql.filepickerlib.dependency;

import android.content.Context;
import android.os.Parcelable;


public interface IFilePickerToast extends Parcelable {
    void showSuccessText(Context context, CharSequence text, int duration);

    void showFailureText(Context context, CharSequence text, int duration);

    void showText(Context context, CharSequence text, int duration);

    void showBottomToast(Context context, CharSequence text, int duration);
}
