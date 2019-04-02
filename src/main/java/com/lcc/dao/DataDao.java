package com.lcc.dao;

import com.lcc.entity.IndexTemplate;
import com.lcc.entity.InformationTemplate;

/**
 * @InterfaceName DataDao
 * @Description 司法信息 Mysql数据输入输出接口 interface
 * @Methods:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 19:18
 * @Version 1.0
 **/
public interface DataDao {

    /**
     * @MethodName insertDataToMysql
     * @Description 将爬虫爬取到的数据持久化到Mysql
     * @Param [informationTemplate]
     * @Return void
     **/
    void insertDataToMysql(InformationTemplate informationTemplate);

    /**
     * @MethodName getDataFromMysql
     * @Description 取出Mysql的每一条数据，并为每一条数据创建相应的索引
     * @Param [indexTemplate]
     * @Return void
     **/
    void getDataFromMysql(IndexTemplate indexTemplate);
}
