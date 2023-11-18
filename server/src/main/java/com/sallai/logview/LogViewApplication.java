package com.sallai.logview;

import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author sallai
 */
@SpringBootApplication
@EnableConfigurationProperties
public class LogViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogViewApplication.class, args);
    }
}
