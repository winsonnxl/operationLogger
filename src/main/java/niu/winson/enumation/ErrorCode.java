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
    LOGIN_ERROR("101001", "你还未登陆,请及时登陆"),
    TOKEN_ERROR("101002", "登录凭证已过期，请重新登录"),
    //数据库操作错误码
    DATABASE_ERROR("202001","数据库操作错误！"),
    UPDATE_DATABASE_ERROR("202002","数据库更新失败"),
    INSERT_DATABASE_ERROR("202002","数据库插入失败"),
    SELECT_DATABASE_ERROR("202002","数据库查询失败"),
    DELETE_DATABASE_ERROR("202002","数据库删除失败"),
    //未知错误码
    UNKNOWN_ERROR("900001","未知错误异常");

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
