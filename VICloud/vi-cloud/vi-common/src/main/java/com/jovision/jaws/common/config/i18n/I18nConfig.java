package com.jovision.jaws.common.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.io.File;

/**
 * @ClassName : I18nConfig
 * @Description : ���ʻ��ļ�����
 * @Author : pangxh
 * @Date: 2020-08-15 15:57
 */
@Configuration
public class I18nConfig {

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath:i18n"+ File.separator+"vi");
        messageBundle.setDefaultEncoding("UTF-8");
        return messageBundle;
    }
    @Bean
    public LocaleResolver localeResolver(){
        return new ViLocaleResolver();
    }

    @Bean
    public MessageSourceUtil messageSourceUtil(){
        return new MessageSourceUtil(messageSource());
    }
}
