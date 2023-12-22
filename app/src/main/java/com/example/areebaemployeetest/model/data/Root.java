package com.example.areebaemployeetest.model.data;

import java.util.List;

public class Root{
    private List<Employee> results;
    private Info info;

    public Root(List<Employee> results, Info info) {
        this.results = results;
        this.info = info;
    }

    public List<Employee> getResults() {
        return results;
    }

    public void setResults(List<Employee> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
