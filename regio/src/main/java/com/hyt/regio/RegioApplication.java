package com.hyt.regio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
@ServletComponentScan //登录拦截器注解
public class RegioApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegioApplication.class, args);
        log.info("项目启动成功");
    }
}
