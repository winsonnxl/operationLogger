package niu.winson.controller;

import niu.winson.dao.OperationLoggerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author niu
 */
@RestController
@RequestMapping(value = "/show")
public class TableController {
    Logger log= LoggerFactory.getLogger(TableController.class);
    @Autowired
    OperationLoggerDao operationLoggerDao;

    @RequestMapping(value = "/getlist",method= RequestMethod.POST)
    public String getResultList(){
        try {
            operationLoggerDao.selectInfo();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
       return "TableController";
    }
}
