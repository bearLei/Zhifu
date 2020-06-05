package com.ubtech.zhifu.utils;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class StringUtils {
    public static String getTextCenter(String paramString1, String paramString2, String paramString3) {
        try {
            int i = paramString1.indexOf(paramString2) + paramString2.length();
            return paramString1.substring(i, paramString1.indexOf(paramString3, i));
        } catch (Exception p) {
            p.printStackTrace();
            return "error";
        }
    }

}
