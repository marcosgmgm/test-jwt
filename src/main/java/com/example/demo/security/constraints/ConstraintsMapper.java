package com.example.demo.security.constraints;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ConstraintsMapper {

    @JsonProperty("constraints")
    private List<DenyMatcher> denyMatcherList;

    @JsonProperty("publicConstraints")
    private List<PermitMatcher> permitMatcherList;



    public ConstraintsMapper(List<PermitMatcher> permitMatcherList, List<DenyMatcher> denyMatcherList) {
        this.permitMatcherList = permitMatcherList;
        this.denyMatcherList = denyMatcherList;
    }

    public ConstraintsMapper() {
    }

    public List<PermitMatcher> getPermitMatcherList() {
        return permitMatcherList;
    }

    public void setPermitMatcherList(List<PermitMatcher> permitMatcherList) {
        this.permitMatcherList = permitMatcherList;
    }

    public List<DenyMatcher> getDenyMatcherList() {
        return denyMatcherList;
    }

    public void setDenyMatcherList(List<DenyMatcher> denyMatcherList) {
        this.denyMatcherList = denyMatcherList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConstraintsMapper{");
        sb.append("denyMatcherList=").append(denyMatcherList);
        sb.append(", permitMatcherList=").append(permitMatcherList);
        sb.append('}');
        return sb.toString();
    }
}
