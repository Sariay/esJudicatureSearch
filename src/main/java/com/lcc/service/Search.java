package com.lcc.service;

import com.lcc.entity.SearchTemplate;
import org.elasticsearch.search.SearchHits;

/**
 * @InterfaceName Search
 * @Description 司法信息 信息搜索接口 interface
 * @Methods:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:45
 * @Version 1.0
 **/
public interface Search {
    //模糊搜索
    SearchHits searchFuzzy(SearchTemplate searchTemplate);

    //搜索推荐
    SearchHits searchRecommend(SearchTemplate searchTemplate);

    //通用搜索
    SearchHits searchCommon(SearchTemplate searchTemplate);
}