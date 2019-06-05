package com.lcc.dao;

import com.lcc.entity.IndexTemplate;
import com.lcc.entity.InformationTemplate;

import java.util.List;

/**
 * @InterfaceName DataDao
 * @Description Mysql数据输入输出接口 interface
 * @Methods:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 19:18
 * @Version 1.0
 **/
public interface DataDao {

    /**
     * @MethodName insertDataToMysql
     * @Description 将爬虫采集到的数据持久化到Mysql
     * @Param [informationTemplate]
     * @Return void
     **/
    void insertDataToMysql(InformationTemplate informationTemplate, String dataTable);

    /**
     * @MethodName getDataFromMysql
     * @Description 取出Mysql的相应数据表的每一条记录，并为每一条记录创建相应的索引
     * @Param [indexTemplate]
     * @Return void
     **/
    void getDataFromMysql(IndexTemplate indexTemplate, String dataTable);

    /**
     * @MethodName getUrlsFromMysql
     * @Description 取出url种子
     * @Param [dataTable]
     * @Return java.util.List<java.lang.String>
     **/
    List<String> getUrlsFromMysql(String dataTable);

    /**
     * @MethodName getAllTextFromMysql
     * @Description 获取所有的title的内容, 关键词提取的前提
     * @Param [dataTable]
     * @Return java.lang.String
     **/
    String getAllTextFromMysql(String dataTable);

    /**
     * @MethodName getAlltitleFromMysql
     * @Description 获取所有的title的内容, 创建json格式的字符串，webapp/js/searchsuggetion.json
     * @Param [dataTable]
     * @Return java.lang.String
     **/
    String getAlltitleFromMysql(String dataTable);
}
