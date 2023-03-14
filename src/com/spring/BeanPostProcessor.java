package com.spring;

/**
 * @version 1.0
 * @author: shore
 * @date: 2023/1/4 10:16
 */
public interface BeanPostProcessor {
   Object postProcessBeforeInitialization(String beanName,Object bean);

   Object postProcessAfterInitialization(String beanName,Object bean);
}
