package com.bjdj.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @author: yulong.zhang
 * @date: 2025/4/10
 * @description:
 */
@Data
@Configuration
@NacosConfigurationProperties(
        dataId = "test.yaml",
        prefix = "temp",
        groupId = "${nacos.config.group}",
        autoRefreshed = true,
        type = ConfigType.YAML)
public class TestProperties {
    private String key;
}
