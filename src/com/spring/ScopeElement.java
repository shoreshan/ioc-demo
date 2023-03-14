package com.spring;

/**
 * @author shore
 * @date 2023-01-03 20:32
 */

public enum ScopeElement {

    PROTOTYPE("prototype"), SINGLETON("singleton");

    private String scope;

    ScopeElement(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}
