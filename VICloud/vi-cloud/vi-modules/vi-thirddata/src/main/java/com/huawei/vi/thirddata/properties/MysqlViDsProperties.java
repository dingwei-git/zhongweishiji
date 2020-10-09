package com.huawei.vi.thirddata.properties;

import com.jovision.jaws.common.properties.DSProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource.videoinsight.mysql")
public class MysqlViDsProperties extends DSProperties {
}
