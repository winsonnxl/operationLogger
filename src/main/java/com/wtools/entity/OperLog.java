package com.wtools.entity;

import org.springframework.stereotype.Component;


@Component
public class OperLog {
    private String operUserID;//操作者ID
    private String operMethod;//操作的Function
    private String operIP;//操作人IP
    private String operURL;//操作URL
    private String operArgs;//操作参数
    private String operType;//操作类型
    private String operTime;//操作时间
    private String operReuslt;//操作状态，是否成功
    private String operApiName;//接口名称，读取@注解中的Name
    private String operSystemID;

    @Override
    public String toString() {
        return "OperLog{" +
                "operUserID='" + operUserID + '\'' +
                ", operMethod='" + operMethod + '\'' +
                ", operIP='" + operIP + '\'' +
                ", operURL='" + operURL + '\'' +
                ", operArgs='" + operArgs + '\'' +
                ", operType='" + operType + '\'' +
                ", operTime='" + operTime + '\'' +
                ", operReuslt='" + operReuslt + '\'' +
                ", operApiName='" + operApiName + '\'' +
                '}';
    }

    public String getOperApiName() {
        return operApiName;
    }

    public void setOperApiName(String operApiName) {
        this.operApiName = operApiName;
    }

    public String getOperMethod() {
        return operMethod;
    }

    public void setOperMethod(String operMethod) {
        this.operMethod = operMethod;
    }

    public String getOperIP() {
        return operIP;
    }

    public void setOperIP(String operIP) {
        this.operIP = operIP;
    }

    public String getOperURL() {
        return operURL;
    }

    public void setOperURL(String operURL) {
        this.operURL = operURL;
    }

    public String getOperArgs() {
        return operArgs;
    }

    public void setOperArgs(String operArgs) {
        this.operArgs = operArgs;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getOperReuslt() {
        return operReuslt;
    }

    public void setOperReuslt(String operReuslt) {
        this.operReuslt = operReuslt;
    }

    public String getOperUserID() {
        return operUserID;
    }

    public void setOperUserID(String operUserID) {
        this.operUserID = operUserID;
    }

    public String getOperSystemID() {
        return operSystemID;
    }

    public void setOperSystemID(String operSystemID) {
        this.operSystemID = operSystemID;
    }
}
