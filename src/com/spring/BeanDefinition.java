package com.spring;

/**
 * @author shore
 * @date 2023-01-03 20:26
 */

public class BeanDefinition {
    private Class type;
    private String scope;

    public BeanDefinition(Class type) {
        this.type = type;
    }

    public BeanDefinition() {
    }

    public BeanDefinition(Class type, String scope) {
        this.type = type;
        this.scope = scope;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
