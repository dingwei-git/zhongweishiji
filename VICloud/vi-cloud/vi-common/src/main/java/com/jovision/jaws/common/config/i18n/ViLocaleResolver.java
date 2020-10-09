package com.jovision.jaws.common.config.i18n;

import com.jovision.jaws.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @ClassName : ViLocaleResolver
 * @Description : 国际化配置
 * @Author : pangxh
 * @Date: 2020-08-22 16:25
 */

public class ViLocaleResolver  implements LocaleResolver {

    @Value("${languageType}")
    private String language;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {

        String l = language;
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(l)){
            String[] split = l.split("_");
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
