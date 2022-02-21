package niu.winson;

import niu.winson.entity.OperationLoggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication
public class operationLoggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(operationLoggerApplication.class, args);
    }
}
