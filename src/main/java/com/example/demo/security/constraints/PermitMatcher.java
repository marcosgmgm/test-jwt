package com.example.demo.security.constraints;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class PermitMatcher {

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("methods")
    private Set<String> methods;

    public PermitMatcher(String pattern, Set<String> methods) {
        this.pattern = pattern;
        this.methods = methods;
    }

    public PermitMatcher(){}

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PermitMatcher{");
        sb.append("pattern='").append(pattern).append('\'');
        sb.append(", methods=").append(methods);
        sb.append('}');
        return sb.toString();
    }
}
