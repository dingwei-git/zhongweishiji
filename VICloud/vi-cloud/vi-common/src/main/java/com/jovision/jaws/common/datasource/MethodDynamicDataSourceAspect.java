package com.jovision.jaws.common.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Order(-1)
@Component
public class MethodDynamicDataSourceAspect {

    @Pointcut("@annotation(com.jovision.jaws.common.datasource.DataSourceSelect) || @within(com.jovision.jaws.common.datasource.DataSourceSelect)")
    public void pointCut(){};

    @Before("pointCut()")
    public void switchDataSource(JoinPoint point) throws Exception {

        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();

        // 使用目标注解类型，如果没有，则使用其接口指定类型
        for (Class<?> clazz : target.getInterfaces()) {
            resolveDataSource(clazz, signature.getMethod());
        }
        resolveDataSource(target, signature.getMethod());
    }

    /**
     * 获取目标对象方法注解和类型注解中的注解
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();

            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSourceSelect.class)) {
                DataSourceSelect cds = clazz.getAnnotation(DataSourceSelect.class);
                DataSourceContextHolder.setDB(cds.value().getValue());
            }

            // 方法注解覆盖，以方法注解为最后值
            Method meName = clazz.getMethod(method.getName(), types);
            if (meName.isAnnotationPresent(DataSourceSelect.class)) {
                DataSourceSelect cds = meName.getAnnotation(DataSourceSelect.class);
                DataSourceContextHolder.setDB(cds.value().getValue());
            }
        } catch (NoSuchMethodException e) {
        }
    }


    @After("pointCut()")
    public void afterSwitchDS(JoinPoint point){
        DataSourceContextHolder.clearDB();
    }

}
