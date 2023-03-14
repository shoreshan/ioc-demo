package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author: shore
 * @date: 2023/1/3 11:19
 */
public class MyApplicationContext {

    private Map<String, BeanDefinition> beanDefinitionContainer = new HashMap<>();  //BeanDefinition容器
    private Map<String, Object> singletonObjectContainer = new HashMap<>(); //单例对象容器
    private ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
    private ArrayList<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();  //


    public MyApplicationContext(Class clazz) {

        if (!clazz.isAnnotationPresent(ComponentScan.class)) {
            throw new RuntimeException("you should have default configuration class  use @ComponentScan ");
        }

        ComponentScan componentScan = (ComponentScan) clazz.getAnnotation(ComponentScan.class);
        //扫描路径
        String path = componentScan.value();
        //获取扫描路径下，所有的类全限定名
        List<String> list = new ArrayList();
        getClassReference(path, list, classLoader);
        for (String classReference : list) {
            try {
                System.out.println("--------------------" + classReference);
                Class<?> aClass = classLoader.loadClass(classReference);
                //Class<?> aClass = Class.forName(classReference);  经历的加载阶段更多
                if (aClass.isAnnotationPresent(Component.class)) {

                    if (BeanPostProcessor.class.isAssignableFrom(aClass)) {  //该类实现了BeanPostProcessor
                        BeanPostProcessor instance = (BeanPostProcessor) aClass.getConstructor().newInstance();
                        beanPostProcessorList.add(instance);
                    }

                    Component component = aClass.getAnnotation(Component.class);
                    String beanName = component.value();
                    if (StringUtils.isEmpty(beanName)) {
                        beanName = Introspector.decapitalize(aClass.getSimpleName());
                    }
                    BeanDefinition beanDefinition = new BeanDefinition(aClass);
                    if (aClass.isAnnotationPresent(Scope.class)) {
                        if (ScopeElement.PROTOTYPE.getScope().equals(aClass.getAnnotation(Scope.class).value())) {
                            beanDefinition.setScope(ScopeElement.PROTOTYPE.getScope());
                        } else {
                            beanDefinition.setScope(ScopeElement.SINGLETON.getScope());
                        }
                    } else {
                        beanDefinition.setScope(ScopeElement.SINGLETON.getScope());
                    }
                    //初始化BeanDefinition容器
                    beanDefinitionContainer.put(beanName, beanDefinition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //实例化单例对象容器
        for (String beanName : beanDefinitionContainer.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionContainer.get(beanName);
            if (ScopeElement.SINGLETON.getScope().equals(beanDefinition.getScope())) {
                if (singletonObjectContainer.get(beanName) == null){
                    singletonObjectContainer.put(beanName, createBean(beanName));
                }
            }
        }
    }

    /**
     * 获取指定包下所有的类全限定名
     *
     * @param path
     * @param list
     * @param classLoader
     */
    private void getClassReference(String path, List<String> list, ClassLoader classLoader) {
        path = path.replace(".", "/");
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String absolutePath = f.getAbsolutePath();
                int comIndex = absolutePath.indexOf("com");
                if (f.isFile()) {
                    int classIndex = absolutePath.indexOf(".class");
                    String substring = absolutePath.substring(comIndex, classIndex);
                    list.add(substring.replace(File.separator, "."));
                } else {
                    String dir = absolutePath.substring(comIndex);
                    getClassReference(dir.replace(File.separator, "."), list, classLoader);
                }
            }
        }
    }


    /**
     * 根据BeanName创建Bean对象
     *
     * @param beanName
     * @return
     */
    private Object createBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionContainer.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException("this beanName [" + beanName + "]  not have the beanDefinition ");
        }
        Class aClass = beanDefinition.getType();
        Object object = null;
        try {
            //1、调用构造器，创建对象
            object = aClass.getConstructor().newInstance();
            //2、DI 依赖注入
            for (Field declaredField : aClass.getDeclaredFields()) {
               if (declaredField.isAnnotationPresent(AutoWried.class)){
                   declaredField.setAccessible(true);
                   String dependencyBeanName = declaredField.getName();
                   declaredField.set(object,getBean(dependencyBeanName));
               }
            }
            //前置处理
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                 object = beanPostProcessor.postProcessBeforeInitialization(beanName, object);
            }
            //3、postConstruct
            for (Method declaredMethod : aClass.getDeclaredMethods()) {
              if (declaredMethod.isAnnotationPresent(PostConstruct.class)){
                  declaredMethod.invoke(object,null);
              }
            }
            //后置处理
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                object = beanPostProcessor.postProcessAfterInitialization(beanName, object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }


    /**
     * 获取Bean对象
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        Object result = null;
        BeanDefinition beanDefinition = beanDefinitionContainer.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException("this beanName [" + beanName + "]  not have the beanDefinition ");
        }
        String scope = beanDefinition.getScope();
        if (ScopeElement.SINGLETON.getScope().equals(scope)) {
            Object bean = singletonObjectContainer.get(beanName);
            if (bean == null) {
                bean = createBean(beanName);
                singletonObjectContainer.put(beanName, bean);
            }
            result = bean;
        } else {
            return createBean(beanName);
        }
        return result;
    }
}
