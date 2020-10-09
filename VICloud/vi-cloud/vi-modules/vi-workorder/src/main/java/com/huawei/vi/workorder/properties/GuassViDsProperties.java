package com.huawei.vi.workorder.properties;

import com.jovision.jaws.common.properties.DSProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.videoinsight.gauss")
public class GuassViDsProperties extends DSProperties {
}
