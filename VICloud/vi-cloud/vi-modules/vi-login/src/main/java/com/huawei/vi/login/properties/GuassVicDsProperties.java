package com.huawei.vi.login.properties;

import com.jovision.jaws.common.properties.DSProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.videoinsightcollecter.gauss")
public class GuassVicDsProperties extends DSProperties {
}
