package com.zql.filepickerlib.util;

import android.content.Context;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zql.filepickerlib.config.NavigationSortMode;
import com.zql.filepickerlib.mimeType.MimeTypeCategory;
import com.zql.filepickerlib.mimeType.MimeTypeHelper;
import com.zql.filepickerlib.model.FileSystemObject;



public class CompareHelper {
    private final static String TAG = "CompareHelper";

    public static int doCompare(final FileSystemObject fso1, final FileSystemObject fso2, final NavigationSortMode mode) {

        //Name (ascending)
        if (mode == NavigationSortMode.NAME_ASC) {
            return compareName(fso1.getName(), fso2.getName());
        }
        //Name (descending)
        if (mode == NavigationSortMode.NAME_DESC) {
            return compareName(fso1.getName(), fso2.getName()) * -1;
        }

        //Date (ascending)
        if (mode == NavigationSortMode.DATE_ASC) {
            return fso1.getLastModifiedTime().compareTo(fso2.getLastModifiedTime());
        }
        //Date (descending)
        if (mode == NavigationSortMode.DATE_DESC) {
            return fso1.getLastModifiedTime().compareTo(fso2.getLastModifiedTime()) * -1;
        }

        //Size (ascending)
        if (mode == NavigationSortMode.SIZE_ASC) {
            return Long.compare(fso1.getSize(), fso2.getSize());
        }
        //Size (descending)
        if (mode == NavigationSortMode.SIZE_DESC) {
            return Long.compare(fso1.getSize(), fso2.getSize()) * -1;
        }

        //Type (ascending)
        if (mode == NavigationSortMode.TYPE_ASC) {
            // Shouldn't need context here, mimetypes should be loaded
            return compareType(null, fso1, fso2);
        }
        //Type (descending)
        if (mode == NavigationSortMode.TYPE_DESC) {
            // Shouldn't need context here, mimetypes should be loaded
            return compareType(null, fso1, fso2) * -1;
        }

        //Comparison between files directly
        return fso1.compareTo(fso2);
    }

    public static int compareName(final String name1, final String name2) {
        int minLength = name1.length() > name2.length() ? name2.length() : name1.length();
        for (int i = 0; i < minLength; i++) {
            int result = compareChar(name1.charAt(i), name2.charAt(i));
            if (result != 0) {
                return result;
            }
        }
        return name1.length() - name2.length();
    }

    private static int compareChar(char ch1, char ch2) {
        int charType1 = CharterHelper.getCharType(ch1);
        int charType2 = CharterHelper.getCharType(ch2);
        if (charType1 == charType2) {
            switch (charType1) {
                case CharterHelper.CHAR_TYPE_PUNCTUATION:
                case CharterHelper.CHAR_TYPE_NUMBER:
                    return ch1 - ch2;
                case CharterHelper.CHAR_TYPE_LETTER:
                    return Character.toLowerCase(ch1) - Character.toLowerCase(ch2);
                case CharterHelper.CHAR_TYPE_CHINESE:
                    return Pinyin.toPinyin(ch1).compareTo(Pinyin.toPinyin(ch2));
                case CharterHelper.CHAR_TYPE_OTHER:
                default:
                    return ch1 - ch2;
            }
        } else {
            return charType1 - charType2;
        }
    }

//    private static int compareWithGBK(char ch1, char ch2) {
//        try {
//            byte[] bytes1 = String.valueOf(ch1).getBytes("GBK");
//            byte[] bytes2 = String.valueOf(ch2).getBytes("GBK");
//            if (bytes1.length == bytes2.length) {
//                for (int i = 0; i < bytes1.length; i++) {
//                    if (bytes1[i] - bytes2[i] != 0) {
//                        return bytes1[i] - bytes2[i];
//                    }
//                }
//            } else {
//                return bytes1.length - bytes2.length;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//        return ch1 - ch2;
//    }

    public static int compareType(Context context, FileSystemObject fso1, FileSystemObject fso2) {
        MimeTypeCategory mtc1 = MimeTypeHelper.getMimeTypeHelperInstance().getCategory(context, fso1);
        MimeTypeCategory mtc2 = MimeTypeHelper.getMimeTypeHelperInstance().getCategory(context, fso2);

        return mtc1.compareTo(mtc2);
    }

}
