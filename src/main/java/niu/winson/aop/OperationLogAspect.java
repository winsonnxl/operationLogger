package niu.winson.aop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import niu.winson.annotation.OperationLogger;
import niu.winson.dao.OperationLoggerDao;
import niu.winson.entity.OperationLoggerConfig;
import niu.winson.entity.OperLog;
import niu.winson.entity.ResultVO;
import niu.winson.enumation.OperationType;
import org.apache.commons.lang3.ArrayUtils;
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

@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    OperationLoggerDao operationLoggerDao;

    @Autowired
    OperationLoggerConfig operationLoggerConfig;

    private static final String UNKNOWN = "unknown";

    private OperLog operlog=new OperLog();

    @Pointcut("@annotation(niu.winson.annotation.OperationLogger)")
    public void operationLogger() {
    }


    @Around("operationLogger() && @annotation(OperationLogger)")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint, OperationLogger OperationLogger) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        operlog.setOperMethod(getMethodName(proceedingJoinPoint));//获取方法名（Function name）
        operlog.setOperURL(request.getRequestURL().toString());//获取请求RUL
        operlog.setOperIP(getIp(request));//获取操作者IP
        operlog.setOperUserID(getUserID(request));//获取操作者用户ID，暂时用占位符代替
        operlog.setOperArgs(getArgs(proceedingJoinPoint));
        operlog.setOperType(OperationLogger.Type().getValue());
        operlog.setOperApiName(OperationLogger.Name());
        operlog.setOperTime(formatter.format(date));
        Object obj = proceedingJoinPoint.proceed();
        return obj;
/*抓取Swagger2 @ApiOperation 部分
        ApiOperation apiOperation = null;
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        apiOperation = ms.getMethod().getDeclaredAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            String value = apiOperation.value();
            System.out.println(value);
        }
*/
    }

    @AfterReturning(pointcut = "operationLogger()", returning = "msg")
    public void afterReturningMethod(ResultVO msg)  {
        try {
            Integer db_result=0;
            if (msg.getError_code() == "0" || operationLoggerConfig.getFailLog()) {
                operlog.setOperReuslt(msg.toString());
                db_result=operationLoggerDao.insertOperationLogger(operlog);
            }
        }catch (Exception e){
            System.out.println("@AfterReturnning: 数据库操作失败！\n"+e.getMessage());
        }
    }

    @After("operationLogger()")
    public void afterMethod(){
    }

    /**
     * 获取注解
     *
     * @param proceedingJoinPoint
     * @return
     */
    private OperationLogger getAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        return ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(OperationLogger.class);
    }

    /**
     * 获取操作类型
     *
     * @param proceedingJoinPoint
     * @return
     */
    private OperationType getOperationType(ProceedingJoinPoint proceedingJoinPoint) {
        return getAnnotation(proceedingJoinPoint).Type();
    }

    /**
     * 获取方法名称
     *
     * @param proceedingJoinPoint
     * @return
     */
    private String getMethodName(ProceedingJoinPoint proceedingJoinPoint) {
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
               e.printStackTrace();
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
    private String getUserID(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Token");
        String authorization = request.getHeader("Authorization");
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
                    JsonNode name = jsonNode.get("id");
                    return name.textValue();
                }
            }
        } catch (Exception e) {
            System.out.println("Token解析错误，未找到‘id’\n"+e.getMessage());
            return "参数异常";
        }

        return "参数空";
    }
}

