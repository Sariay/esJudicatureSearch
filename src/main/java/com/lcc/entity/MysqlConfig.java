package com.lcc.entity;

/**
 * @EnumName MysqlConfig
 * @Description 项目中用到的Mysql的配置信息
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 19:54
 * @Version 1.0
 **/
public enum MysqlConfig {
    DATABASE_NAME("elasticsearch"),
    DATATABLE_NAME("test"),
    DATATABLE_LIVE_VIDOE_NAME("live_video"),
    DATATABLE_JUDICIAL_INTERPRETATION_NAME("judicial_interpretation"),
    DATATABLE_LITIGATION_GUIDE_NAME("litigation_guide"),
    DATATABLE_JUDICIAL_CASES_NAME("judicial_cases"),
    LIVE_VIDOE_URL("live_video_url"),
    JUDICIAL_INTERPRETATION_URL("judicial_interpretation_url"),
    LITIGATION_GUIDE_URL("litigation_guide_url"),
    JUDICIAL_CASES_URL("judicial_cases_url"),
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