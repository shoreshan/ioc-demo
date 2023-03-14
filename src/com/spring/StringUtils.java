package com.spring;

/**
 * @author shore
 * @date 2023-01-03 20:49
 */

public class StringUtils {


    /**
     * 判断字符串是否为空
     * @param arg0
     * @return
     */
    public static boolean isEmpty(String arg0){
        if (arg0 == null || arg0.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

}
