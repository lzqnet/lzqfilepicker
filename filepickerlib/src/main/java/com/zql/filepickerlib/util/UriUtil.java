package com.zql.filepickerlib.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.regex.Pattern;

public class UriUtil {
    private static final String FILE_PROVIDER = ".filePickerProvider";
    private static final String TAG = "UriUtil";
    private static final String SCHEMA_REGEX = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][iI][lL][eE]:/*)([\\w.]+\\/?)\\S*";
    private static Pattern SCHEMA_PATTERN = Pattern.compile("^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][iI][lL][eE]:/*)([\\w.]+\\/?)\\S*");

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                fileUri = getUriForFile24(context, file);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    private static Uri getUriForFile24(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + FILE_PROVIDER, file);
    }

    /**
     * 判断是否http://\https://\ftp:// 开头的url
     *
     * @param url
     * @return
     */
    public static boolean isValidSchema(String url) {
        return !TextUtils.isEmpty(url) && SCHEMA_PATTERN.matcher(url).matches();
    }
}
