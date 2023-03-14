package com.spring;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @version 1.0
 * @author: shore
 * @date: 2023/1/3 13:41
 * 获取指定包下的所有Class对象
 */
public class PackageClassUtils {


    public PackageClassUtils(){
        ClassLoader classLoader = PackageClassUtils.class.getClassLoader();

    }


    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     * @throws
     */
    public static List<String> getClassName(String packageName, boolean childPackage) throws IOException {
        List<String> fileNames = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> urls = loader.getResources(packagePath);

        return fileNames;
    }

}
