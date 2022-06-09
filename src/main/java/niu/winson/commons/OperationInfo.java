package niu.winson.commons;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import niu.winson.aop.OperationLogAspect;
import niu.winson.entity.OperationLoggerConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * 获取必要信息公共操作类
 **/
public class OperationInfo {

    Logger log= LoggerFactory.getLogger(OperationInfo.class);
    /**
     * 获取SystemID,
     **/
    public String getSystemIdByHeader(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Token");
        String authorization = request.getHeader("Authorization");
        String systemid = request.getHeader("SystemID");
        String[] parts = null;
        if (StringUtils.isNotEmpty(token)) {
            parts = token.split("\\.");
        }
        if (StringUtils.isNotEmpty(authorization)) {
            parts = authorization.split("\\.");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (parts != null) {
                if (!parts[1].isEmpty()) {
                    byte[] decoded = Base64.decodeBase64(parts[1]);
                    String token_payload = new String(decoded);
                    JsonNode jsonNode = mapper.readTree(token_payload);
                    JsonNode system = jsonNode.get("systemId");
                    if(system==null){
                        return "token参数异常";
                    }
                    return system.textValue();
                }
            } else {
                if (StringUtils.isNotEmpty(systemid)) {
                    return systemid.trim();
                }
            }
        } catch (Exception e) {
            log.error("OperationInfo->getSystemIdByHeader: Exception \n" + e.getMessage());
            return "参数异常";
        }

        return "参数空";
    }

    /**
     * 获取SystemID,
     * 首先获取读取application.properties中niu.OperationLogger.System数值，
     * 如果为空，读取header中的SystemID
     * **/
    public String getSystemID(){
        try {
            if (!OperationLoggerConfig.SystemID.isEmpty()) {
                return getSystemIdByHeader();
            }

        }catch (Exception e){
            log.error("OperationInfo->getSystemID()="+e.getMessage());
            return "OperationInfo->getSystemID() error";
        }
        if(OperationLoggerConfig.SystemID.isEmpty()){
            log.warn("OperationInfo->getSystemID,header、application均为空\n");
            return "SystemID均为空";
        }else {
            return OperationLoggerConfig.SystemID;
        }
    }


    /***
     * 获取header中的 Token 或 Authorization 的 UserID
     * */
    public String getUserID() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Token");
        String authorization = request.getHeader("Authorization");
        String[] parts = null;
        if (StringUtils.isNotEmpty(token)) {
            parts = token.split("\\.");
        }
        if (StringUtils.isNotEmpty(authorization)) {
            parts = authorization.split("\\.");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (parts!=null) {
                if (!parts[1].isEmpty()) {
                    byte[] decoded = Base64.decodeBase64(parts[1]);
                    String token_payload = new String(decoded);
                    JsonNode jsonNode = mapper.readTree(token_payload);
                    JsonNode name = jsonNode.get("userId");
                    if(name==null){
                        return "Token参数异常";
                    }
                    String text_name=name.toString();
                    if(text_name.contains("\"")){
                        return name.textValue();
                    }
                    return name.toString();
                }
            }
        } catch (Exception e) {
            log.error("OperationInfo->getUserID,Token解析错误，未找到‘id’\n" + "->"+e.getMessage());
            return "Token参数异常";
        }

        return "Token参数空";
    }

}
