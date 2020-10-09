/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.huawei.vi.thirddata.service.binary.pojo;

/**
 * 二进制数据转化，查询字段结果
 *
 * @version 1.0, 2018年7月18日
 * @since 2019-09-19
 */
public class FieldInfo {
    /**
     * 查询结果字段名称
     */
    private String fieldName;

    /**
     * 查询结果字段类型
     */
    private byte fieldType;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public byte getFieldType() {
        return fieldType;
    }

    public void setFieldType(byte fieldType) {
        this.fieldType = fieldType;
    }
}
