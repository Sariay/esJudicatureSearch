package com.lcc.utils;

import com.lcc.entity.ElasticsearchConfig;
import com.lcc.entity.MysqlConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class ElasticsearchHelperTest {
    @Test
    //为所有数据创建索引
    public void test() {
        Logger logger = LogManager.getLogger(ElasticsearchHelperTest.class);
        ElasticsearchHelper.daoMysqlToElasticsearch(MysqlConfig.DATATABLE_LIVE_VIDOE_NAME.getValue(), ElasticsearchConfig.INDEX_LIVE_VIDOE_NAME.getValue());
        ElasticsearchHelper.daoMysqlToElasticsearch(MysqlConfig.DATATABLE_LITIGATION_GUIDE_NAME.getValue(), ElasticsearchConfig.INDEX_LITIGATION_GUIDE_NAME.getValue());
        ElasticsearchHelper.daoMysqlToElasticsearch(MysqlConfig.DATATABLE_JUDICIAL_INTERPRETATION_NAME.getValue(), ElasticsearchConfig.INDEX_JUDICIAL_INTERPRETATION_NAME.getValue());
        ElasticsearchHelper.daoMysqlToElasticsearch(MysqlConfig.DATATABLE_JUDICIAL_CASES_NAME.getValue(), ElasticsearchConfig.INDEX_JUDICIAL_CASES_NAME.getValue());
        logger.info( "Test end!");
    }
}