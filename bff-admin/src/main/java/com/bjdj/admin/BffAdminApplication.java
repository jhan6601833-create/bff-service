package com.bjdj.admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class BffAdminApplication {
    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled", "false");
        System.setProperty("nacos.server.grpc.port.offset", "0");
        SpringApplication.run(BffAdminApplication.class, args);

        log.info("service start success .....");
    }
}
