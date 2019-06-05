package com.lcc.service;

import com.lcc.entity.DocumentTemplate;
import com.lcc.entity.SearchTemplate;
import org.elasticsearch.search.SearchHits;

/**
 * @InterfaceName Search
 * @Description 司法信息搜索接口 interface
 * @Methods:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:45
 * @Version 1.0
 **/
public interface Search {
    /**
     * @MethodName searchFuzzy
     * @Description 其他搜索模块：模糊搜索（Term level queries术语查询，模糊查询）
     * @Param [searchTemplate]
     * @Return org.elasticsearch.search.SearchHits
     **/
    SearchHits searchFuzzy(SearchTemplate searchTemplate);

    /**
     * @MethodName searchRecommend
     * @Description 相关搜索模块：搜索推荐（Specialized queries专业查询，相似度查询）
     * @Param [searchTemplate]
     * @Return org.elasticsearch.search.SearchHits
     **/
    SearchHits searchRecommend(SearchTemplate searchTemplate);

    /**
     * @MethodName searchSimilarDocuments
     * @Description 相似搜索模块：用于查询与一篇文档相似的所有文档
     * @Param [searchTemplate, documentTemplate]
     * @Return org.elasticsearch.search.SearchHits
     **/
    SearchHits searchSimilarDocuments(SearchTemplate searchTemplate, DocumentTemplate documentTemplate);

    /**
     * @MethodName searchCommon
     * @Description 全文搜索模块：通用搜索（Full text queries全文搜索，多字段查询）
     * @Param [searchTemplate]
     * @Return org.elasticsearch.search.SearchHits
     **/
    SearchHits searchCommon(SearchTemplate searchTemplate);
}