package com.jovision.jaws.common.datasource;

public enum DataSourceEnum {

    VideoInsight("videoInsight"),
    VideoinsightCollecter("videoinsightcollecter");


    private String value;

    DataSourceEnum(String value) {
            this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
