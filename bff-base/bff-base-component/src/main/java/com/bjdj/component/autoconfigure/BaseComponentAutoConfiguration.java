package com.bjdj.component.autoconfigure;

import com.bjdj.component.properties.BaseComponentProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(BaseComponentProperties.class)
public class BaseComponentAutoConfiguration {}
