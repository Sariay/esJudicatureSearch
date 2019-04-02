package com.lcc.service;

import com.lcc.entity.SearchTemplate;
import com.lcc.utils.ElasticsearchHelper;
import com.lcc.utils.HighlightHelper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

/**
 * @ClassName SearchImpl
 * @Description TODO
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:46
 * @Version 1.0
 **/
public class SearchImpl implements Search {

    private TransportClient client = ElasticsearchHelper.getSingleClient();

    private HighlightBuilder highlightBuilder = HighlightHelper.getHighlightBuilder();

    public SearchHits searchFuzzy(SearchTemplate searchTemplate){
        String query = searchTemplate.getQuery();
        int pageNumber = searchTemplate.getPageNumber();
        int pageHits = searchTemplate.getPageHits(); //默认10条

        //[模糊搜索]：返回包含”与关键词模糊相似的”词项的文档集合(可结合MulSearch API实现搜索)
        //FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("title", query);
        FuzzyQueryBuilder fuzzyQueryBuilder1 = QueryBuilders.fuzzyQuery("content", query);

        SearchResponse searchResponse = client.prepareSearch( )
                .setTypes( )
                .setQuery( fuzzyQueryBuilder1 )
                .highlighter( highlightBuilder )
                .setFrom( (pageNumber - 1)*pageHits )
                .setSize(pageHits)
                .execute()
                .actionGet();

        return searchResponse.getHits();
    }

    public SearchHits searchRecommend(SearchTemplate searchTemplate){
        String query = searchTemplate.getQuery();
        String[] fieldNames = searchTemplate.getFieldsName();
        int pageNumber = searchTemplate.getPageNumber();
        int pageHits = searchTemplate.getPageHits(); //默认10条

        //[搜索推荐]api-1： 返回包含“与输入文本相似的“文本的文档集合
        String[] fieldsText = fieldNames;
        String[] textsText = { query };
        MoreLikeThisQueryBuilder.Item[] itemsText = null;
        MoreLikeThisQueryBuilder  moreLikeThisQueryBuilderText = QueryBuilders.moreLikeThisQuery(fieldsText, textsText, itemsText)
                .minTermFreq(1)
                .maxQueryTerms(5);

        //[搜索推荐]api-2： 返回“与每一条结果(一个文档)相似的”文档集合（每条结果下使用collpase板显示）
        String[] filedsDoc = fieldNames;
        String[] textsDoc = null;
        MoreLikeThisQueryBuilder.Item[] itemsDoc = {new MoreLikeThisQueryBuilder.Item("1","2","3")};
        MoreLikeThisQueryBuilder moreLikeThisQueryBuilderDoc = QueryBuilders.moreLikeThisQuery(filedsDoc, filedsDoc, itemsDoc)
                .minTermFreq(1)
                .maxQueryTerms(30);

        SearchResponse searchResponse = client.prepareSearch( )
                .setTypes( )
                .setQuery( moreLikeThisQueryBuilderText )
                .highlighter( highlightBuilder )
                .setFrom( (pageNumber - 1)*pageHits )
                .setSize(pageHits)
                .execute()
                .actionGet();

        return searchResponse.getHits();
    }

    public SearchHits searchCommon(SearchTemplate searchTemplate){

        String query = searchTemplate.getQuery();
        String[] indexNames = searchTemplate.getIndexNames();
        String[] typeNames = searchTemplate.getTypeNames();
        String[] fieldNames = searchTemplate.getFieldsName();
        int pageNumber = searchTemplate.getPageNumber();
        int pageHits = searchTemplate.getPageHits(); //默认10条

        //[一般搜索]多字段查询api：多个字段中检索关键词，返回包含关键词的文档集合
        MultiMatchQueryBuilder multiMatchQuery =  QueryBuilders.multiMatchQuery(query, fieldNames);

        SearchResponse searchResponse = client.prepareSearch( indexNames )
                .setTypes( typeNames )
                .setQuery( multiMatchQuery )
                .highlighter( highlightBuilder )
                .setFrom( (pageNumber - 1)*pageHits )
                .setSize(pageHits)
                .execute()
                .actionGet();

        return searchResponse.getHits();

    }

}