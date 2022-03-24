package com.wtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author niu
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication
public class OperationLoggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OperationLoggerApplication.class, args);
    }
}
