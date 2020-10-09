package com.jovision.jaws.common.anantation;

import com.jovision.jaws.common.config.i18n.I18nConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({I18nConfig.class})
public @interface EnableI18n {
}
