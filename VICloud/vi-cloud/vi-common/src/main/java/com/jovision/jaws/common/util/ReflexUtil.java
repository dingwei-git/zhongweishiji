/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 */

package com.jovision.jaws.common.util;

import com.huawei.utils.CollectionUtil;
import com.huawei.utils.CommonUtil;
import com.huawei.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 和反射相关的工具类
 *
 * @since 2019-08-15
 */
public final class ReflexUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ReflexUtil.class);

    private ReflexUtil() {
    }

    /**
     * 动态调用T的set方法修改实体TT 当前此方法不具有修改父类熟悉的能力
     *
     * @param tt bean
     * @param propertyName fieldName
     * @param value new Value
     * @param <TT> 泛型T
     */
    public static <TT> void dynamicSet(TT tt, String propertyName, Object value) {
        if (CommonUtil.isNull(tt)) {
            LOG.error("parameter is null");
            return;
        }
        try {
            Field field = tt.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            try {
                field.set(tt, value);
            } catch (IllegalAccessException e) {
                LOG.error("IllegalAccessException {}", e.getMessage());
            }
        } catch (NoSuchFieldException e) {
            LOG.error("there is NoSuchFieldException;");
        }
    }

    /**
     * 动态调用T的set方法批量修改实体TT
     *
     * @param tt bean
     * @param fieldList field集合
     * @param valueList value集合
     * @param <TT> 泛型
     */
    public static <TT> void dynamicSet(TT tt, List<String> fieldList, List<Object> valueList) {
        if (CommonUtil.isNull(tt)) {
            LOG.error("parameter is null");
            return;
        }
        if (CollectionUtil.isEmpty(fieldList) || CollectionUtil.isEmpty(valueList)) {
            LOG.error("parameter is empty");
            return;
        }
        if (fieldList.size() != valueList.size()) {
            LOG.error("fieldList size !=valueList size fieldList:{}, valueList:{}", fieldList.size(), valueList.size());
            return;
        }
        int count = fieldList.size();
        for (int ii = 0; ii < count; ii++) {
            dynamicSet(tt, fieldList.get(ii), valueList.get(ii));
        }
    }

    /**
     * 通过bean和属性名获取属性值
     *
     * @param fieldName 属性名
     * @param tt bean
     * @param <TT> 泛型
     * @return Object 返回的属性值
     */
    public static <TT> Object getFieldValue(String fieldName, TT tt) {
        if (StringUtil.isEmpty(fieldName)) {
            LOG.error("ReflexUtil getFieldValue() fieldName is empty");
            return fieldName;
        }
        if (CommonUtil.isNull(tt)) {
            LOG.error("ReflexUtil getFieldValue() object is null");
            return null;
        }
        Object reValue = null;
        try {
            Field declaredField = tt.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(declaredField.getName(), tt.getClass());
            Method readMethod = propertyDescriptor.getReadMethod();
            reValue = readMethod.invoke(tt);
        } catch (NoSuchFieldException e) {
            LOG.error("NoSuchFieldException {}", e.getMessage());
        } catch (IntrospectionException e) {
            LOG.error("IntrospectionException {}", e.getMessage());
        } catch (IllegalAccessException e) {
            LOG.error("IllegalAccessException {}", e.getMessage());
        } catch (InvocationTargetException e) {
            LOG.error("InvocationTargetException {}", e.getMessage());
        }
        return reValue;
    }
}
