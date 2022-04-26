package niu.winson.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import niu.winson.annotation.OperationLogger;
import niu.winson.dao.OperationLoggerDao;
import niu.winson.entity.OperLog;
import niu.winson.entity.OperationLoggerConfig;
import niu.winson.entity.ResultVO;
import niu.winson.enumation.ErrorCode;
import niu.winson.enumation.OperationType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Winson niu
 */
@Aspect
@Component
public class OperationLogAspect {
    private static final String UNKNOWN = "unknown";
    private final OperLog operlog = new OperLog();

    @Autowired
    OperationLoggerDao operationLoggerDao;
    @Autowired
    OperationLoggerConfig operationLoggerConfig;

    @Pointcut("@annotation(niu.winson.annotation.OperationLogger)")
    public void OperationLogger() {
    }


    @Around("OperationLogger() && @annotation(operationLogger)")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint, OperationLogger operationLogger) throws Throwable,Exception {
        //try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //获取方法名（Function name）
            operlog.setOperMethod(getMethodName(proceedingJoinPoint));
            //获取请求RUL
            operlog.setOperURL(request.getRequestURL().toString());
            //获取操作者IP
            operlog.setOperIP(getIp(request));
            //获取操作者用户ID，暂时用占位符代替
            operlog.setOperUserID(getUserID(request));
            //System.out.println("OperUserID="+operlog.getOperUserID());
            operlog.setOperArgs(getArgs(proceedingJoinPoint));
            operlog.setOperType(operationLogger.Type().getValue());
            operlog.setOperApiName(operationLogger.Name());
            operlog.setOperTime(formatter.format(date));
            operlog.setOperSystemID(operationLoggerConfig.getSystemID());
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        //} catch (Exception e) {
           // System.out.println("OperationLogAspect->aroundMethod:Exception \n"+e.getMessage());
           // return null;
       // }
    }

    @AfterReturning(pointcut = "OperationLogger()", returning = "msg")
    public void afterReturningMethod(ResultVO<Object> msg) throws Exception{
        //try {
            if (msg.getError_code() == ErrorCode.SUCCESS.getCode() || operationLoggerConfig.getFailLog()) {
                operlog.setOperReuslt(msg.toString());
                if(operationLoggerDao.insertOperationLogger(operlog)!=1){
                    System.out.println("@AfterReturnning: 数据插入失败！\n");
                }
            }
       // } catch (Exception e) {
        //    System.out.println("@AfterReturnning: 数据库操作失败！\n" + e.getMessage());
       // }
    }

    @After("OperationLogger()")
    public void afterMethod() {
    }

    /**
     * 获取注解
     *
     * @param proceedingJoinPoint
     * @return 注解名称
     */
    private OperationLogger getAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        return ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(OperationLogger.class);
    }

    /**
     * 获取操作类型
     *
     * @param proceedingJoinPoint
     * @return
     */
    private OperationType getOperationType(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        return getAnnotation(proceedingJoinPoint).Type();
    }

    /**
     * 获取方法名称
     *
     * @param proceedingJoinPoint
     * @return
     */
    private String getMethodName(ProceedingJoinPoint proceedingJoinPoint) throws Exception{
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        return methodSignature.getMethod().getDeclaringClass().getName() + "." + methodSignature.getMethod().getName();
    }

    /**
     * 获取真实ip
     */
    private String getIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                System.out.println("OperationLogAspect->getIp->Exception \n"+e.getMessage());
            }
        }
        return ip;
    }

    /**
     * 获取传入参数
     */
    private String getArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args)) {
            List<Object> logArgs = Arrays.stream(args).filter(arg -> (!(arg instanceof HttpServletRequest) || !(arg instanceof HttpServletResponse))).collect(Collectors.toList());
            return logArgs.toString();
        }
        return "参数空";
    }

    /***
     * 获取header中的 Token 或 Authorization 的 UserID
     * */
    private String getUserID(HttpServletRequest request) {
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
            System.out.println("Token解析错误，未找到‘id’\n" + "->"+e.getMessage());
            return "Token参数异常";
        }

        return "Token参数空";
    }
}
