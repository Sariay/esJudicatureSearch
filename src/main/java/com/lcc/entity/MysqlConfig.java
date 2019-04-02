package com.lcc.entity;

/**
 * @EnumName MysqlConfig
 * @Description 项目中用到的关于Mysql的常量
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 19:54
 * @Version 1.0
 **/
public enum MysqlConfig {
    DATABASE_NAME("essearch"),
    DATATABLE_NAME("essearch"),
    USER_NAME("root"),
    PASS_WORD("TM1261347403");

    private String value;

    MysqlConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}