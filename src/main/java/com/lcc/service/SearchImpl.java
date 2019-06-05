package com.lcc.service;

import com.lcc.entity.DocumentTemplate;
import com.lcc.entity.SearchTemplate;
import com.lcc.utils.ElasticsearchHelper;
import com.lcc.utils.HighlightHelper;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
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
        String[] indexNames = searchTemplate.getIndexNames();
        String[] typeNames = searchTemplate.getTypeNames();
        String[] fieldNames = searchTemplate.getFieldsName();
        int pageNumber = searchTemplate.getPageNumber();
        int pageHits = searchTemplate.getPageHits(); //默认10条

        //模糊搜索：返回包含”与关键词模糊相似的”词项的文档集合(可结合MulSearch API实现搜索)
        FuzzyQueryBuilder titleFuzzyQueryBuilder = QueryBuilders.fuzzyQuery("title", query);

        FuzzyQueryBuilder contentFuzzyQueryBuilder = QueryBuilders.fuzzyQuery("content", query);

        SearchRequestBuilder titleSrb = client.prepareSearch( indexNames )
                .setTypes( typeNames )
                .setQuery( titleFuzzyQueryBuilder )
                .setSize( pageHits );
        SearchRequestBuilder contentSrb = client.prepareSearch( indexNames )
                .setTypes( typeNames )
                .setQuery( contentFuzzyQueryBuilder )
                .setSize( pageHits );

        MultiSearchResponse multiSearchResponse = client.prepareMultiSearch()
                .add( titleSrb )
                .add( contentSrb )
                .execute()
                .actionGet();

        SearchResponse searchResponse = client.prepareSearch( indexNames )
                .setTypes( typeNames )
                .setQuery( contentFuzzyQueryBuilder )
                .setFrom( (pageNumber - 1)*pageHits )
                .setSize(pageHits)
                .execute()
                .actionGet();

        return searchResponse.getHits();
    }

    public SearchHits searchRecommend(SearchTemplate searchTemplate){
        String query = searchTemplate.getQuery();
        String[] indexNames = searchTemplate.getIndexNames();
        String[] typeNames = searchTemplate.getTypeNames();
        String[] fieldNames = searchTemplate.getFieldsName();
        int pageNumber = searchTemplate.getPageNumber();
        int pageHits = searchTemplate.getPageHits();

        //搜索推荐API1： 返回包含“与输入文本相似的“文本的文档集合
        String[] fieldsText = fieldNames;
        String[] textsText = { query };
        MoreLikeThisQueryBuilder.Item[] itemsText = null;
        MoreLikeThisQueryBuilder  moreLikeThisQueryBuilderText = QueryBuilders.moreLikeThisQuery(fieldsText, textsText, itemsText)
                .minTermFreq(0)
                .minDocFreq(0)
                .maxQueryTerms(20);

        SearchResponse searchResponse = client.prepareSearch( indexNames )
                .setTypes( typeNames )
                .setQuery( moreLikeThisQueryBuilderText )
                .highlighter( highlightBuilder )
                .setSize(pageHits)
                .execute()
                .actionGet();

        return searchResponse.getHits();
    }

    public SearchHits searchSimilarDocuments(SearchTemplate searchTemplate, DocumentTemplate documentTemplate){
        String query = searchTemplate.getQuery();
        String[] indexNames = searchTemplate.getIndexNames();
        String[] typeNames = searchTemplate.getTypeNames();
        String[] fieldNames = searchTemplate.getFieldsName();
        int pageNumber = searchTemplate.getPageNumber();
        int pageHits = searchTemplate.getPageHits();

        String currentDocIndex = documentTemplate.getDocumentIndexName();
        String currentDocType = documentTemplate.getDocumentTypeName();
        String currentDocId = documentTemplate.getDocumentId();

        //搜索推荐API2： 返回“与一条结果(一个文档)相似的”文档集合
        String[] filedsDoc = fieldNames;
        String[] textsDoc = null;
        MoreLikeThisQueryBuilder.Item[] itemsDoc = {new MoreLikeThisQueryBuilder.Item(currentDocIndex,currentDocType,currentDocId)};
        MoreLikeThisQueryBuilder moreLikeThisQueryBuilderDoc = QueryBuilders.moreLikeThisQuery(filedsDoc, textsDoc, itemsDoc)
                .minTermFreq(0)
                .minDocFreq(0)
                .maxQueryTerms(20);

        SearchResponse searchResponse = client.prepareSearch( indexNames )
                .setTypes( typeNames )
                .setQuery( moreLikeThisQueryBuilderDoc )
                .highlighter( highlightBuilder )
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
        int pageHits = searchTemplate.getPageHits();

        //全文检索API：多个字段中检索关键词，返回包含关键词的文档集合
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