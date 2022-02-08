package niu.winson.entity;


import niu.winson.enumation.ErrorCode;


// 统一请求的返回对象
public class ResultVO<T> {

   //"错误代码"
    private String error_code;//错误代码

    @Override
    public String toString() {
        return "ResultVO{" +
                "error_code='" + error_code + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", data=" + data +
                '}';
    }

    //"消息"
    private String error_msg;//错误信息

   //"返回数据"
    private T data;//传输数据

    public ResultVO(String errorCode,String errorMsg, T data) {
        setError_code(errorCode);
        setError_msg(errorMsg);
        setData(data);
    }

    public ResultVO(ErrorCode errorCode, T data) {
        setError_code(errorCode.getCode());
        setError_msg(errorCode.getMsg());
        setData(data);
    }

    public ResultVO(ErrorCode errorCode) {
        setError_code(errorCode.getCode());
        setError_msg(errorCode.getMsg());
    }


    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
