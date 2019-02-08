package com.example.demo.security.constraints;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Set;

public class DenyMatcher {

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("roles")
    private Map<String, Set<String>> roleMapping;

    public DenyMatcher(String pattern, Map<String, Set<String>> roleMapping) {
        this.pattern = pattern;
        this.roleMapping = roleMapping;
    }

    public DenyMatcher() {

    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Map<String, Set<String>> getRoleMapping() {
        return roleMapping;
    }

    public void setRoleMapping(Map<String, Set<String>> roleMapping) {
        this.roleMapping = roleMapping;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DenyMatcher{");
        sb.append("pattern='").append(pattern).append('\'');
        sb.append(", roleMapping=").append(roleMapping);
        sb.append('}');
        return sb.toString();
    }
}
