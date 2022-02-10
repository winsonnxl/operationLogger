package niu.winson.dao;

import niu.winson.entity.JDBCConfig;
import niu.winson.entity.OperLog;
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
    JDBCConfig jdbcConfig;

    String tableName = "operation_logger";//表名
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
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';";


    public DriverManagerDataSource getDataSources()  {
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(jdbcConfig.getDriverClassName());
            dataSource.setUrl(jdbcConfig.getUrl());
            dataSource.setUsername(jdbcConfig.getUserName());
            dataSource.setPassword(jdbcConfig.getPassword());
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
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean init() throws SQLException{
        Connection conn= getJdbcTemplate().getDataSource().getConnection();
        ResultSet resultset=null;
        String[] types= {"TABLE"};
        try {
            DatabaseMetaData databaseMetaData=conn.getMetaData();
            resultset=databaseMetaData.getTables(null,null,tableName,types);
            if(resultset.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            resultset.close();
            conn.close();
        }
       return false;
    }

    public Integer insertOperationLogger(OperLog operLog) throws SQLException {
        int intResult=0;
        try {
            if (!init()) {
                getJdbcTemplate().execute(create);
            }
            String sql = "insert into operation_logger(oper_user_id,oper_api_name,oper_method,oper_ip,oper_time,oper_type,oper_url,oper_args,oper_result) values(?,?,?,?,?,?,?,?,?)";
            intResult=getJdbcTemplate().update(sql, operLog.getOperUserID(), operLog.getOperApiName(), operLog.getOperMethod(), operLog.getOperIP(), operLog.getOperTime(), operLog.getOperType(), operLog.getOperURL(), operLog.getOperArgs(), operLog.getOperReuslt());
            return intResult;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return intResult;
        }
    }
}
