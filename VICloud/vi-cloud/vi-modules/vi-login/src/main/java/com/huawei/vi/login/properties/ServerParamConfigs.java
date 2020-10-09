package com.huawei.vi.login.properties;

import com.jovision.jaws.common.properties.ServerParamConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "serverparamconfig.config")
public class ServerParamConfigs extends ServerParamConfig {

}
