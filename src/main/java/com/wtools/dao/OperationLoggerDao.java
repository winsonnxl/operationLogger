package com.wtools.dao;

import com.wtools.entity.OperLog;
import com.wtools.entity.OperationLoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OperationLoggerDao {

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
            "  `oper_args` varchar(15000) NOT NULL COMMENT '传入参数',\n" +
            "  `oper_result` varchar(5000) NOT NULL COMMENT '操作结果',\n" +
            "`system_id` varchar(60) NOT NULL COMMENT '系统编码,读取application.properties中niu.OperationLogger.System数值',\n"+
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台操作日志表';";


    public DriverManagerDataSource getDataSources()  {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(operationLoggerConfig.getDriverClassName());
            dataSource.setUrl(operationLoggerConfig.getUrl());
            dataSource.setUsername(operationLoggerConfig.getUserName());
            dataSource.setPassword(operationLoggerConfig.getPassword());
            return dataSource;
        }catch(Exception e){
            System.out.println("OperationLoggerDao.java->getDataSources()  数据据库配置读取错误！！！\n"+e.getMessage());
            return null;
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSources());
            return jdbcTemplate;
        }catch (Exception e){
            System.out.println("OperationLoggerDao->JdbcTemplate:\n"+e.getMessage());
            return null;
        }
    }

    public boolean init() throws SQLException{
        //System.out.println("进入 init()");
        Connection conn= getJdbcTemplate().getDataSource().getConnection();
        ResultSet resultset=null;
        String[] types= {"TABLE"};
        try {
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            resultset=databaseMetaData.getTables(operationLoggerConfig.getDatabaseName(),null,tableName,types);
            //System.out.println("连接数据库，检索表是否存在");
            if(resultset.next()){
                //System.out.println("连接数据库，检索表存在");
                return true;
            }
        }catch(Exception e){
            System.out.println("perationLoggerDao->init:\n"+e.getMessage());
        }finally {
            resultset.close();
            conn.close();
        }
       return false;
    }

    public Integer insertOperationLogger(OperLog operLog) throws SQLException {
        //System.out.println("进入 insertOperationLogger()");
        int intResult=0;
        try {
            //System.out.println("即将判断表是否存在");
            if (!init()) {
                getJdbcTemplate().execute(create);
                //System.out.println("完成创建表");
            }
            String sql = "insert into operation_logger(oper_user_id,oper_api_name,oper_method,oper_ip,oper_time,oper_type,oper_url,oper_args,oper_result,system_id) values(?,?,?,?,?,?,?,?,?,?)";
            //System.out.println("即将插入数据");
            intResult=getJdbcTemplate().update(sql, operLog.getOperUserID(), operLog.getOperApiName(), operLog.getOperMethod(), operLog.getOperIP(), operLog.getOperTime(), operLog.getOperType(), operLog.getOperURL(), operLog.getOperArgs(), operLog.getOperReuslt(),operLog.getOperSystemID());
            //System.out.println("完成插入数据");
            return intResult;
        }catch (Exception e){
            System.out.println("perationLoggerDao->insertOperationLogger:\n"+e.getMessage());
            return intResult;
        }
    }
}
