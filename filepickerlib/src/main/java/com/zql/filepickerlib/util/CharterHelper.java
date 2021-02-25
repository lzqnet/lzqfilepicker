package com.zql.filepickerlib.util;

import com.github.promeg.pinyinhelper.Pinyin;


public class CharterHelper {
    public final static int CHAR_TYPE_PUNCTUATION = 1;
    public final static int CHAR_TYPE_NUMBER = 2;
    public final static int CHAR_TYPE_LETTER = 3;
    public final static int CHAR_TYPE_CHINESE = 4;
    public final static int CHAR_TYPE_OTHER = 5;

    private final static String TAG = "CharterHelper";

    public static boolean isLetter(char ch) {
        return isLowerCase(ch) || isUpperCase(ch);
    }

    private static boolean isUpperCase(char ch) {
        return ch >= 65 && ch <= 90;
    }

    private static boolean isLowerCase(char ch) {
        return ch >= 97 && ch <= 122;
    }

    private static boolean isNumber(char ch) {
        return ch >= 48 && ch <= 57;
    }

    private static boolean isPunctuation(char ch) {
        String str = "[`~!@#$^&*()=|{}':;,[].<>/?！￥¥《》「」…（）—【】‘；：”“。，、？～]$";
        int index = str.indexOf(ch);
        return index >= 0;
    }

    public static int getCharType(char ch) {
        if (isPunctuation(ch)) {
            return CHAR_TYPE_PUNCTUATION;
        } else if (isNumber(ch)) {
            return CHAR_TYPE_NUMBER;
        } else if (isLetter(ch)) {
            return CHAR_TYPE_LETTER;
        } else if(Pinyin.isChinese(ch)){
            return CHAR_TYPE_CHINESE;
        }
        return CHAR_TYPE_OTHER;
    }
}
