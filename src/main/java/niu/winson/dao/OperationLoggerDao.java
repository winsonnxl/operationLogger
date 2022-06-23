package niu.winson.dao;

import niu.winson.entity.OperLog;
import niu.winson.entity.OperationLoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author EDZ
 */
@Repository
public class OperationLoggerDao {
    Logger log= LoggerFactory.getLogger(OperationLoggerDao.class);
    @Autowired
    OperationLoggerConfig operationLoggerConfig;
    /**
     * 表名
     * */
    String tableName = "operation_logger";
    String create = "CREATE TABLE `operation_logger` (\n" +
            "  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '主键自增',\n" +
            "  `oper_user_id` varchar(50) NOT NULL COMMENT '操作者ID',\n" +
            "  `oper_api_name` varchar(200) NOT NULL COMMENT 'API名称,读取@注解中的Name',\n" +
            "  `oper_method` varchar(200) NOT NULL COMMENT '操作方法',\n" +
            "  `oper_ip` varchar(20) NOT NULL COMMENT '操作人IP地址',\n" +
            "  `oper_url` varchar(300) NOT NULL COMMENT '操作URL',\n" +
            "  `oper_type` varchar(10) NOT NULL COMMENT '操作类型',\n" +
            "  `oper_time` varchar(20) NOT NULL COMMENT '操作时间',\n" +
            "  `oper_args` varchar(11000) NOT NULL COMMENT '传入参数',\n" +
            "  `oper_result` varchar(4000) NOT NULL COMMENT '操作结果',\n" +
            "`system_id` varchar(60) NOT NULL COMMENT '系统编码,读取application.properties中niu.OperationLogger.System数值',\n"+
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台操作日志表';";


    public DriverManagerDataSource getDataSources(){
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(operationLoggerConfig.getDriverClassName());
            dataSource.setUrl(operationLoggerConfig.getUrl());
            dataSource.setUsername(operationLoggerConfig.getUserName());
            dataSource.setPassword(operationLoggerConfig.getPassword());
            return dataSource;
        }catch(Exception e){
            log.error("OperationLoggerDao.java->getDataSources()  数据据库配置读取错误！！！\n"+e.getMessage());
            return null;
        }
    }

    public JdbcTemplate getJdbcTemplate(){
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSources());
            return jdbcTemplate;
        }catch (Exception e){
            log.error("OperationLoggerDao->JdbcTemplate:\n"+e.getMessage());
            return null;
        }
    }

    public boolean init() throws Exception{
        Connection conn= getJdbcTemplate().getDataSource().getConnection();
        ResultSet resultset=null;
        String[] types= {"TABLE"};
        try {
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            resultset=databaseMetaData.getTables(OperationLoggerConfig.getDatabaseName(),null,tableName,types);
            if(resultset.next()){
                return true;
            }
        }catch(Exception e){
            log.error("perationLoggerDao->init:\n"+e.getMessage());
        }finally {
            resultset.close();
            conn.close();
        }
       return false;
    }
/**
 * 日志插入数据库
 * 如果operation_logger表不存在，首选执行创建表
 * */
    public Integer insertOperationLogger(OperLog operLog){
        int intResult=0;
        try {
            if (!init()) {
                getJdbcTemplate().execute(create);
            }
            String sql = "insert into operation_logger(oper_user_id,oper_api_name,oper_method,oper_ip,oper_time,oper_type,oper_url,oper_args,oper_result,system_id) values(?,?,?,?,?,?,?,?,?,?)";
            intResult = getJdbcTemplate().update(sql, operLog.getOperUserID(), operLog.getOperApiName(), operLog.getOperMethod(), operLog.getOperIP(), operLog.getOperTime(), operLog.getOperType(), operLog.getOperURL(), operLog.getOperArgs(), operLog.getOperReuslt(), operLog.getOperSystemID());
            return intResult;
        }catch(Exception e){
            log.error("OperationLoggerDao->insertOperationLogger数据库插入错误！！！\n"+e.getMessage());
            return intResult;
        }
    }

    /**
     * 日志查询
     * */
    public String selectInfo(){
        String sql="select * from operation_logger";
            List result_list=getJdbcTemplate().queryForList(sql);
            for(int i=0;i<result_list.size();i++){
                log.info(result_list.get(i).toString());
            }
            return null;

    }
}
