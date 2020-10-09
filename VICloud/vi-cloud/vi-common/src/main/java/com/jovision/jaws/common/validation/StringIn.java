package com.jovision.jaws.common.validation;


import javax.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 自定义字符串选择范围
 * @Author: ABug
 * @Date: 2019/12/19 8:58 上午
 * @Version V1.0.0
 **/
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = StringInValidator.class)
@Documented
public @interface StringIn {

    String message() default "{com.jovision.jaws.common.validation.StringIn.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String[] value() default { };

}

class StringInValidator implements ConstraintValidator<StringIn, String> {

    /**
     * 取值范围
     */
    String[] ranges;

    @Override
    public void initialize(StringIn constraintAnnotation) {
        ranges = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        // 当前前端传过来的请求参数是空串，或者没传的时候，不进行后续正则校验
        if ("".equals(s) || s == null) {
            return false;
        }
        for(String value : ranges){
            if(s.equals(value)){
                return true;
            }
        }

        return false;
    }
}
