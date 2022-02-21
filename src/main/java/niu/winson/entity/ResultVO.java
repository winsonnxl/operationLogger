package niu.winson.entity;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import niu.winson.enumation.ErrorCode;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

// 统一请求的返回对象
public class ResultVO<T> {

    //"对应平台编码"
    private String system_id;

   //"错误代码"
    private String error_code;//错误代码

    //"消息"
    private String error_msg;//错误信息

    //"返回数据"
    private T data;//传输数据


    @Override
    public String toString() {
        return "ResultVO{" +
                "system_id='" + system_id + '\'' +
                ", error_code='" + error_code + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", data=" + data +
                '}';
    }

    /*
    *核对请求接口是不是为当前系统
    * */
    private Boolean CheckSystemID(){
       try {
        if(getSystemID().equals(OperationLoggerConfig.SystemID) || OperationLoggerConfig.SystemID.equals("NULL")){
            return true;
        }else{
            setSystem_id("UNKNOW");
            setError_code("UNKNOW");
            setError_msg("非平台请求！");
            return false;
        }
       }catch (Exception e){
           System.out.println(e.getMessage());
           setSystem_id("UNKNOW");
           setError_code("UNKNOW");
           setError_msg("非平台请求！");
           return false;
       }
    }

    /*
    * 通过接口请求，获取Token中的System编码
    * */
    private String getSystemID() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Token");
        String authorization = request.getHeader("Authorization");
        String systemid=request.getHeader("SystemID");
        String[] parts = null;
        if (token != null) {
            parts = token.split("\\.");
        }
        if (authorization != null) {
            parts = authorization.split("\\.");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (parts != null) {
                if (!parts[1].isEmpty()) {
                    byte[] decoded = Base64.decodeBase64(parts[1]);
                    String token_payload = new String(decoded);
                    JsonNode jsonNode = mapper.readTree(token_payload);
                    JsonNode name = jsonNode.get("system");
                    return name.textValue();
                }
            }else{
                if(systemid != null){
                    return systemid.trim().toString();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "参数异常";
        }

        return "参数空";
    }

    public ResultVO(String errorCode,String errorMsg, T data) {
        if(CheckSystemID()) {
            setSystem_id(OperationLoggerConfig.SystemID);
            setError_code(errorCode);
            setError_msg(errorMsg);
            setData(data);
        }
    }

    public ResultVO(ErrorCode errorCode, T data) {
        if(CheckSystemID()) {
            setSystem_id(OperationLoggerConfig.SystemID);
            setError_code(errorCode.getCode());
            setError_msg(errorCode.getMsg());
            setData(data);
        }
    }

    public ResultVO(ErrorCode errorCode) {
        if(CheckSystemID()) {
            setSystem_id(OperationLoggerConfig.SystemID);
            setError_code(errorCode.getCode());
            setError_msg(errorCode.getMsg());
        }
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

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id=system_id;
    }
}
