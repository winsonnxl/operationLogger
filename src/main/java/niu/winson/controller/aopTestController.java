package niu.winson.controller;

import niu.winson.annotation.OperationLogger;
import niu.winson.entity.OperLog;
import niu.winson.entity.ResultVO;
import niu.winson.enumation.OperationType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import niu.winson.commons.ReturnValue;
import java.util.HashMap;
import java.util.Map;

import static niu.winson.enumation.ErrorCode.DATABASE_ERROR;


@RestController
@RequestMapping(value = "/aop")
public class aopTestController {
//    @Autowired
//    OperationLoggerDao operationLoggerDao;

//    @Autowired
//    JDBCConfig jdbcConfig;

    @OperationLogger(Name = "捕捉useApiLog", Type = OperationType.UPDATE)
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ResultVO useApiLog(@RequestBody String args) {
//        ResultVO result=new ResultVO();
//        result.setError_code("0");
//        result.setError_msg("成功");
//        result.setShow_msg("ResultV0 成功！");
//        String msg=new String();
//        try {
//            ObjectMapper om = new ObjectMapper();
//            msg = om.writeValueAsString(result);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return msg;


        OperLog operLog=new OperLog();
        operLog.setOperUserID("803");
        operLog.setOperApiName("插入数据库API Name");
        operLog.setOperMethod("插入Method");
        try {
            //operationLoggerDao.insertOperationLogger(operLog);
            return ReturnValue.FAIL(DATABASE_ERROR);
        }catch (Exception e){
            e.getMessage();
            return ReturnValue.FAIL(DATABASE_ERROR);
        }

    }

    @OperationLogger(Name = "捕捉hello",Type= OperationType.INSERT)
    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public ResultVO hello(@RequestBody String args) {
//        ResultVO result=new ResultVO();
//        result.setMsg("I'am Hello");
        String data=new String();
        Map<String,Object> map=new HashMap<String,Object>();
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("ddd","djfkdfj");
        map1.put("ccc","ccccccc");
        String encoded = "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";
        byte[] decoded = Base64.decodeBase64(encoded);
        String getDecoded = new String(decoded);
        map.put("decoded", getDecoded);
        map.put("show_msg","Greate!!!");
        map.put("map1",map1);
        return ReturnValue.SUCCESS(map);
    }

    @OperationLogger(Name = "测试空参数",Type= OperationType.SELECT)
    @RequestMapping(value = "/getrequest",method = RequestMethod.GET)
    public ResultVO getEmptyArgs(){
        return ReturnValue.SUCCESS();
    }
}

