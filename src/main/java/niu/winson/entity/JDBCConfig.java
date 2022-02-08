package niu.winson.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:/application.properties")
public class JDBCConfig {
    @Value("${niu.OperationLogger.database.DriverClassName}")
    private String DriverClassName;

    @Value("${niu.OperationLogger.database.url}")
    private String url;

    @Value("${niu.OperationLogger.database.UserName}")
    private String UserName;

    @Value("${niu.OperationLogger.database.Password}")
    private String Password;

    @Value("${niu.OperationLogger.FailLog}")
    private Boolean FailLog;

    public Boolean getFailLog() {
        return FailLog;
    }

    public void setFailLog(Boolean failLog) {
        FailLog = failLog;
    }

    public String getDriverClassName() {
        return DriverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        DriverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
