package com.lcc.utils;

import com.lcc.entity.SearchTemplate;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName HighlightHelper
 * @Description TODO
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 17:50
 * @Version 1.0
 **/
public class HighlightHelper {

    /**
     * @MethodName getHighlightBuilder
     * @Description TODO
     * @Param []
     * @Return org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder
     **/
    public static HighlightBuilder getHighlightBuilder() {

        SearchTemplate searchTemplate = new SearchTemplate();
        searchTemplate.setHighlightTitle("title");
        searchTemplate.setHighlightContent("content");

        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .numOfFragments(5)
                .preTags("<span class=\"highlight\">")
                .postTags("</span>")
                .field(searchTemplate.getHighlightTitle())
                .field(searchTemplate.getHighlightContent());

        return highlightBuilder;
    }

    /**
     * @MethodName highlightHaveKeyWord
     * @Description 存在关键词，则高亮关键词
     * @Param [hits]
     * @Return java.util.ArrayList<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public static ArrayList<Map<String, Object>> highlightHaveKeyWord(SearchHits hits) {

        ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        for (SearchHit hit : hits) {

            Map<String, Object> item = hit.getSourceAsMap();

            //高亮关键词：使用'<i class="highlight">关键词</i>'替换 '关键词'
            HighlightField highlightTitle = hit.getHighlightFields().get("title");
            if (highlightTitle != null) {
                Text[] fragments = highlightTitle.fragments();
                String highlightTitleStr = "";
                for (Text text : fragments) {
                    highlightTitleStr += text;
                }
                item.put("title", highlightTitleStr);
            }

            HighlightField highlightContent = hit.getHighlightFields().get("content");
            if (highlightContent != null) {
                Text[] fragments = highlightContent.fragments();
                String highlightContentStr = "";
                for (Text text : fragments) {
                    highlightContentStr += text;
                }
                item.put("content", highlightContentStr);
            }

            resultList.add(item);
        }

        return resultList;
    }

    /**
     * @MethodName highlightNoKeyWord
     * @Description 不存在关键词， 则将content字段的内容截断（截取100-300）
     * @Param [hits]
     * @Return java.util.ArrayList<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public static ArrayList<Map<String, Object>> highlightNoKeyWord(SearchHits hits) {
        ArrayList<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        for (SearchHit hit : hits) {

            Map<String, Object> item = hit.getSourceAsMap();

            resultList.add(item);
        }

        return resultList;
    }
}