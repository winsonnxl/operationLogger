package com.wtools.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author niu
 */
@Component
public class OperationLoggerConfig {
    @Value("${niu.OperationLogger.database.DriverClassName}")
    private String DriverClassName;

    @Value("${niu.OperationLogger.database.url}")
    private String url;

    @Value("${niu.OperationLogger.database.UserName}")
    private String UserName;

    @Value("${niu.OperationLogger.database.Password}")
    private String Password;

    /**
     * 配合ResultVO封装使用。TRUE表示接口执行接口成功或失败都记录。FALSE表示接口执行成功才记录。
     * */
    @Value("${niu.OperationLogger.FailLog}")
    private Boolean FailLog;

    /**
    *非必填项，系统编码
    */
    public static String SystemID;

    /**
     * 非必填项，适配MySQL数据库，同一个MySQL Server下有多个库，用于指定库下创建table
     * */
    public static String DatabaseName;

    public static String getDatabaseName() {
        return DatabaseName;
    }

    @Value("${niu.OperationLogger.DatabaseName:NULL}")
    public static void setDatabaseName(String databaseName) {
        DatabaseName = databaseName;
    }

    public String getSystemID() {
        return SystemID;
    }


    @Value("${niu.OperationLogger.System:NULL}")
    public void setSystemID(String systemID) {
        SystemID = systemID;
    }

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
