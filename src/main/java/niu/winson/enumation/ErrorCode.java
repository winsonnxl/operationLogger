package niu.winson.enumation;

import org.omg.CORBA.UNKNOWN;

public enum ErrorCode {
    /***
     * 1. 以下错误码的定义，需要提前与前端沟通
     * 2. 错误码按模块进行错误码规划
     * 3. 所有错误码枚举类均需要实现错误码接口类
     */
    SUCCESS("0","操作成功"),
    SYSTEM_BUSY("10000","系统繁忙,请稍后再试!"),
    FORM_VALIDATION_ERROR("10001","表单验证错误"),
    PARAMETER_MISSING_ERROR("10002","参数缺少错误"),
    // 用户登录方面错误码
    LOGIN_ERROR("101001", "未登录"),
    TOKEN_ERROR("101002", "登录凭证错误"),
    LOGIN_KEY_ERROR("101003", "账号密码错误"),
    TOKEN_TIMEOUT("101004","登录凭证超时"),
    ACCOUNT_CLOSED("101005","账号关闭"),
    ACCOUNT_LOGOFF("101006","账号注销"),
    //数据库操作错误码
    DATABASE_ERROR("202001","数据库操作错误！"),
    UPDATE_DATABASE_ERROR("202002","数据库更新失败"),
    INSERT_DATABASE_ERROR("202003","数据库插入失败"),
    SELECT_DATABASE_ERROR("202004","数据库查询失败"),
    DELETE_DATABASE_ERROR("202005","数据库删除失败"),

    NOTFOUND_ERROR("700001","未找到该条记录"),
    QUOTE_ERROR("700002", "该记录已被引用"),
    QRCODE_ERROR("700003", "生成二维码失败"),
    //未知错误码
    UNKNOWN_ERROR("900001","未知错误异常"),
    WORKING_ERROR("900002","业务执行异常"),
    TRANSACTION_ERROR("900003","事务执行异常");
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg(){return msg;}

    ErrorCode(String code,String msg) {
        this.code=code;
        this.msg=msg;
    }

}
