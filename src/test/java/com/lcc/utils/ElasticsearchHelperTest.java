package com.lcc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ElasticsearchHelperTest {

    Logger logger = LogManager.getLogger(ElasticsearchHelperTest.class);

    @Test
    //为所有数据创建索引
    public void test() {
        //logger.info("数据库为空！");

        ElasticsearchHelper.daoMysqlToElasticsearch();

    }
}