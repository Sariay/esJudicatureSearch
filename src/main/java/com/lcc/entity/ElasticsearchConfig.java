package com.lcc.entity;

/**
 * @EnumName ElasticsearchConfig
 * @Description 项目中用到的elasticsearch的配置信息， 包括集群名、主机地址、客户端端口号、索引名、类型名等
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 20:29
 * @Version 1.0
 **/
public enum ElasticsearchConfig {
    CLUSTER_NAME("my-application"),
    HOST_IP("127.0.0.1"),
    TCP_PORT( "9300" ),//Need to convert to int!
    INDEX_NAME("test"),
    INDEX_LIVE_VIDOE_NAME("live_video"),
    INDEX_JUDICIAL_INTERPRETATION_NAME("judicial_interpretation"),
    INDEX_LITIGATION_GUIDE_NAME("litigation_guide"),
    INDEX_JUDICIAL_CASES_NAME("judicial_cases"),
    TYPE_NAME("webpage"),
    INDEX_SHARDS("4"),
    INDEX_REPLICAS("0");

    private String value;

    ElasticsearchConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
