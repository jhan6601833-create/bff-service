package com.bjdj;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: yulong.zhang
 * @date: 2025/4/10
 * @description:
 */
@Slf4j
@EnableDubbo
@SpringBootApplication
@MapperScan(basePackages = "com.bjdj.mapper")
public class Application {
    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");
        System.setProperty("nacos.server.grpc.port.offset", "0");
        SpringApplication.run(Application.class, args);

        log.info("service start success .....");
    }
}
