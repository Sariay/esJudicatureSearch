package com.lcc.utils;

import com.lcc.entity.MysqlConfig;

import java.sql.*;
import java.util.List;

/**
 * @ClassName MysqlHelper
 * @Description 连接、关闭、插入、删除等数据库操作方法
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 19:38
 * @Version 1.0
 **/
public class MysqlHelper {
    //JDBC驱动名及数据库URL，数据库名test
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/" + MysqlConfig.DATABASE_NAME.getValue() + "?useSSL=false&serverTimezone=UTC";

    //数据库的用户名与密码
    private static final String USER_NAME = MysqlConfig.USER_NAME.getValue();
    private static final String PASS_WORD = MysqlConfig.PASS_WORD.getValue();

    //连接对象、操作对象
    private static Connection connection = null;
    private static Statement statement = null;

    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    /**
     * @MethodName MysqlHelper
     * @Description Connection Mysql
     * @Param []
     * @Return
     **/
    public MysqlHelper() {
        try {
            connection = MysqlHelper.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName getConnection
     * @Description 单例模式，得到一个Connection实例对象
     * @Param []
     * @Return java.sql.Connection
     **/
    private static synchronized Connection getConnection() {
        if(connection == null){
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URL, USER_NAME, PASS_WORD);
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            } catch (SQLException e){
                e.printStackTrace();
            }
            System.out.println("连接数据库成功");
        }
        return connection;
    }

    /**
     * @MethodName closeConnection
     * @Description Close Mysql
     * @Param []
     * @Return void
     **/
    public void closeConnection() {
        try {
            if (connection != null) {
                MysqlHelper.connection.close();
            }
            if (preparedStatement != null) {
                this.preparedStatement.close();
            }
            if (resultSet != null) {
                this.resultSet.close();
            }
            System.out.println("关闭数据库成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @MethodName executeQuery
     * @Description Query data
     * @Param [sql, sqlValues]
     * @Return java.sql.ResultSet
     **/
    public ResultSet executeQuery(String sql, List<String> sqlValues) {
        try {
            preparedStatement = connection.prepareStatement(sql);
            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(preparedStatement, sqlValues);
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * @MethodName executeUpdate
     * @Description Update data or insert data
     * @Param [sql, sqlValues]
     * @Return int
     **/
    public int executeUpdate(String sql, List<String> sqlValues) {
        int status = -1;
        try {
            preparedStatement = connection.prepareStatement( sql );

            if (sqlValues != null && sqlValues.size() > 0) {
                setSqlValues(preparedStatement, sqlValues);
            }
            status = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * @MethodName setSqlValues
     * @Description Set value by sql command
     * @Param [preparedStatement, sqlValues]
     * @Return void
     **/
    private void setSqlValues(PreparedStatement preparedStatement, List<String> sqlValues) {
        for (int i = 0; i < sqlValues.size(); i++) {
            try {
                preparedStatement.setObject(i + 1, sqlValues.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
