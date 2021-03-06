package com.huawei.vi.map.properties;

import com.jovision.jaws.common.properties.DSProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.videoinsightcollecter")
public class VicDsProperties extends DSProperties {
}
