package com.zql.filepickerlib.util;

import android.content.Context;
import android.text.TextUtils;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.util.HashMap;
import java.util.Map;


public class UIHelper {

    private static Map<String, Template> mTmplCache = new HashMap<>();

    private static Template getTmpl(String str) {
        Template template = mTmplCache.get(str);
        if (template == null) {
            template = Mustache.compiler().compile(str);
            mTmplCache.put(str, template);
        }
        return template;
    }

    /**
     * 为了便于I18n统一下发模板 统一采用Mustache来编译模板和生成最终字符串
     * 默认情况下：模板作者（devops/pm）需要做好特殊字符的转义工作，例如 ' " @ 等
     * 否则可能会导致最终编译失败 或者文字样式不符合要求
     *
     * @param context     Context
     * @param stringResId 字符串资源id
     * @param dataMap     字符串资源对应的key和value map
     * @return 格式化完成的字符串
     * @see \https://github.com/samskivert/jmustache

     */
    public static String mustacheFormat(Context context, int stringResId, Map<String, String> dataMap) {
        String targetTplString = context.getString(stringResId);
        if (TextUtils.isEmpty(targetTplString) || dataMap == null || dataMap.isEmpty()) {
            return targetTplString;
        }
        Template tmpl = getTmpl(targetTplString);
        return tmpl.execute(dataMap);
    }

    /**
     * 同上述函数 提供一个快捷方式 因为大部分格式化可能只要格式化一个key
     *
     * @param context     Context
     * @param stringResId 字符串资源id
     * @param key         字符串资源中的key
     * @param value       实际的value
     * @return 格式化完成的字符串
     */
    public static String mustacheFormat(Context context, int stringResId, String key, String value) {
        String targetTplString = context.getString(stringResId);
        if (key == null || value == null) {
            return targetTplString;
        }
        Map<String, String> dataMap = new HashMap<>(1);
        dataMap.put(key, value);
        Template tmpl = getTmpl(targetTplString);
        return tmpl.execute(dataMap);
    }
}
