package com.zql.filepickerlib;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.zql.filepickerlib.mimeType.MimeTypeHelper;
import com.zql.filepickerlib.mimeType.MimeTypeInfo;
import com.zql.filepickerlib.model.Directory;
import com.zql.filepickerlib.model.FileSystemObject;
import com.zql.filepickerlib.model.Symlink;
import com.zql.filepickerlib.util.FileHelper;
import com.zql.filepickerlib.util.Log;



public class DefaultIconProvider {
    private final static String TAG = "DefaultIconProvider";
    private final static int DEFAULT_FILE_ICON_NAME = R.drawable.drivefilepicker_widget_item_icon_default;
    private final static int DEFAULT_FOLDER_ICON_NAME = R.drawable.drivefilepicker_widget_item_icon_folder;
    private final static SparseIntArray sDrawableResource = new SparseIntArray(20);

    private interface IconCode {
        int ICON_CODE_DEFAULT = 1;
        int ICON_CODE_AE = 2;
        int ICON_CODE_AI = 3;
        int ICON_CODE_EXCEL = 4;
        int ICON_CODE_IMAGE = 5;
        int ICON_CODE_KEY = 6;
        int ICON_CODE_MUSIC = 7;
        int ICON_CODE_PDF = 8;
        int ICON_CODE_PPT = 9;
        int ICON_CODE_PSD = 10;
        int ICON_CODE_SKETCH = 11;
        int ICON_CODE_TXT = 12;
        int ICON_CODE_VIDEO = 13;
        int ICON_CODE_WORD = 14;
        int ICON_CODE_ZIP = 15;
        int ICON_CODE_APK = 16;
    }

    static {
        sDrawableResource.put(IconCode.ICON_CODE_DEFAULT, R.drawable.drivefilepicker_widget_item_icon_default);
        sDrawableResource.put(IconCode.ICON_CODE_AE, R.drawable.drivefilepicker_widget_item_drive_ae);
        sDrawableResource.put(IconCode.ICON_CODE_AI, R.drawable.drivefilepicker_widget_item_drive_ai);
        sDrawableResource.put(IconCode.ICON_CODE_EXCEL, R.drawable.drivefilepicker_widget_item_drive_excel);
        sDrawableResource.put(IconCode.ICON_CODE_IMAGE, R.drawable.drivefilepicker_widget_item_drive_image);
        sDrawableResource.put(IconCode.ICON_CODE_KEY, R.drawable.drivefilepicker_widget_item_drive_key);
        sDrawableResource.put(IconCode.ICON_CODE_MUSIC, R.drawable.drivefilepicker_widget_item_drive_music);
        sDrawableResource.put(IconCode.ICON_CODE_PDF, R.drawable.drivefilepicker_widget_item_drive_pdf);
        sDrawableResource.put(IconCode.ICON_CODE_PPT, R.drawable.drivefilepicker_widget_item_drive_ppt);
        sDrawableResource.put(IconCode.ICON_CODE_PSD, R.drawable.drivefilepicker_widget_item_drive_psd);
        sDrawableResource.put(IconCode.ICON_CODE_SKETCH, R.drawable.drivefilepicker_widget_item_drive_sketch);
        sDrawableResource.put(IconCode.ICON_CODE_TXT, R.drawable.drivefilepicker_widget_item_drive_txt);
        sDrawableResource.put(IconCode.ICON_CODE_VIDEO, R.drawable.drivefilepicker_widget_item_drive_video);
        sDrawableResource.put(IconCode.ICON_CODE_WORD, R.drawable.drivefilepicker_widget_item_drive_word);
        sDrawableResource.put(IconCode.ICON_CODE_ZIP, R.drawable.drivefilepicker_widget_item_drive_zip);
        sDrawableResource.put(IconCode.ICON_CODE_APK, R.drawable.drivefilepicker_widget_item_drive_apk);
    }

    public DefaultIconProvider() {

    }

    private int getIconResIdByCode(String iconCodeStr) {
        try {
            int code = Integer.valueOf(iconCodeStr.trim());
            return sDrawableResource.get(code, DEFAULT_FILE_ICON_NAME);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return DEFAULT_FILE_ICON_NAME;
    }

    public int getDefaultIconResId(Context context, FileSystemObject fso) {

        // Return the symlink ref mime/type icon
        if (fso instanceof Symlink && ((Symlink) fso).getLinkRef() != null) {
            return getDefaultIconResId(context, ((Symlink) fso).getLinkRef());
        }

        //Check if the argument is a folder
        if (fso instanceof Directory) {
            return DEFAULT_FOLDER_ICON_NAME;
        }

        //Get the extension and delivery
        String ext = FileHelper.getExtension(fso);
        if (ext != null) {
            MimeTypeInfo mimeTypeInfo = MimeTypeHelper.getMimeTypeHelperInstance()
                    .getMimeTypeFromPath(fso.getFullPath(), ext);

            if (mimeTypeInfo != null) {
                // Create a new drawable
                if (!TextUtils.isEmpty(mimeTypeInfo.mDrawable)) {
                    return getIconResIdByCode(mimeTypeInfo.mDrawable);
                }
                // Something was wrong here. The resource should exist, but it's not present.
                // Audit the wrong mime/type resource and return the best fso drawable (probably
                // default)
                Log.w(TAG, String.format("Something was wrong with the drawable of the fso: %s, mime: %s",
                        fso.toString(), mimeTypeInfo.toString()));
            }
        }

        // Check system file
        if (FileHelper.isSystemFile(fso)) {
            return DEFAULT_FILE_ICON_NAME;
        }
        // Check if the fso is executable (but not a symlink)
        if (fso.getPermissions() != null && !(fso instanceof Symlink)) {
            if (fso.getPermissions().getUser().isExecute() ||
                    fso.getPermissions().getGroup().isExecute() ||
                    fso.getPermissions().getOthers().isExecute()) {
                return DEFAULT_FILE_ICON_NAME;
            }
        }
        return DEFAULT_FILE_ICON_NAME;
    }

}
