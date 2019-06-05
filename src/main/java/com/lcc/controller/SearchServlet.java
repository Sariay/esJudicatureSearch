package com.lcc.controller;

import com.lcc.entity.DocumentTemplate;
import com.lcc.entity.ElasticsearchConfig;
import com.lcc.entity.SearchTemplate;
import com.lcc.service.Search;
import com.lcc.service.SearchImpl;
import com.lcc.utils.HighlightHelper;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SearchServlet
 * @Description 处理前端网页传入的数据，整理数据后与业务逻辑层进行沟通（调用搜索接口），最后将搜索结果返回给前端网页
 * @Properties:
 * @Author 水煮鱼肠面
 * @Date 2019/3/30 21:53
 * @Version 1.0
 **/
@WebServlet(name = "/SearchInfo", urlPatterns = "/SearchInfo")
public class SearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        SearchTemplate searchTemplate = new SearchTemplate();
        searchTemplate.setQuery(req.getParameter("query"));

        String indexNumber = req.getParameter("index");

        if (indexNumber == null  ){
            indexNumber = "zero";
        }

        // 设置需要搜索的索引
        switch (indexNumber){
            case "zero": {
                searchTemplate.setIndexNames(new String[]{
                        ElasticsearchConfig.INDEX_JUDICIAL_INTERPRETATION_NAME.getValue(),
                        ElasticsearchConfig.INDEX_JUDICIAL_CASES_NAME.getValue(),
                        ElasticsearchConfig.INDEX_LITIGATION_GUIDE_NAME.getValue(),
                        ElasticsearchConfig.INDEX_LIVE_VIDOE_NAME.getValue()
                });
            }
            break;
            case "one": {
                searchTemplate.setIndexNames(new String[]{ElasticsearchConfig.INDEX_LIVE_VIDOE_NAME.getValue()});
            }
            break;
            case "two": {
                searchTemplate.setIndexNames(new String[]{ElasticsearchConfig.INDEX_JUDICIAL_INTERPRETATION_NAME.getValue()});
            }
            break;
            case "three": {
                searchTemplate.setIndexNames(new String[]{ElasticsearchConfig.INDEX_LITIGATION_GUIDE_NAME.getValue()});
            }
            break;
            case "four": {
                searchTemplate.setIndexNames(new String[]{ElasticsearchConfig.INDEX_JUDICIAL_CASES_NAME.getValue()});
            }
            break;
            default: {
                searchTemplate.setIndexNames(new String[]{
                        ElasticsearchConfig.INDEX_JUDICIAL_INTERPRETATION_NAME.getValue(),
                        ElasticsearchConfig.INDEX_JUDICIAL_CASES_NAME.getValue(),
                        ElasticsearchConfig.INDEX_LITIGATION_GUIDE_NAME.getValue(),
                        ElasticsearchConfig.INDEX_LIVE_VIDOE_NAME.getValue()
                });
            }
        }

        String pageNumberStr = req.getParameter("pageNumber");

        int pageNumber = 1;

        if (pageNumberStr != null && Integer.parseInt(pageNumberStr) > 1) {
            pageNumber = Integer.parseInt(pageNumberStr);
        }

        searchTemplate.setPageNumber(pageNumber);
        searchTemplate.setPageHits(20);

        //根据输入的关键词搜索司法公开信息
        searchJudiciaInformation(searchTemplate, req);
        //将输入的关键词，显示在结果页的搜索输入框里（result.jsp）
        req.setAttribute("searchWord", searchTemplate.getQuery());
        req.setAttribute("currentIndex", indexNumber + "");
        req.setAttribute("currentPageNumber", searchTemplate.getPageNumber() + "");

        //请求转发
        req.getRequestDispatcher("result.jsp").forward(req, resp);
    }

    private void searchJudiciaInformation(SearchTemplate searchTemplate, HttpServletRequest req) {
        //设置需要搜索的索引、类型、字段等
        String[] typeNames = {ElasticsearchConfig.TYPE_NAME.getValue()};
        String[] fieldNames = {"title", "content"};
        String keywordsSuggestion = null;

        searchTemplate.setTypeNames(typeNames);
        searchTemplate.setFieldsName(fieldNames);

        Search search = new SearchImpl();

        // 全文搜索模块
        long startTime = System.currentTimeMillis();
        SearchHits searchCommonHits = search.searchCommon(searchTemplate);
        if (searchCommonHits.getTotalHits()<=0){
            // 关键词推荐模块
            // 可以考虑从txt文件读取关键词
            keywordsSuggestion = "<span>有限公司</span><span>纠纷</span><span>案例</span><span>最高人民法院</span><span>纠纷案</span><span>合同</span><span>指导</span><span>案件</span><span>问题</span><span>被告</span><span>规定</span><span>借贷</span><span>执行</span><span>民间</span><span>人民法院</span><span>原告</span><span>股份有限公司</span><span>适用</span><span>法院</span><span>审理</span><span>公司</span><span>科技</span><span>行政</span><span>责任</span><span>解释</span><span>诉讼</span><span>法律</span><span>财产</span>";
        }
        ArrayList<Map<String, Object>> searchResultList = new ArrayList<Map<String, Object>>();
        searchResultList = HighlightHelper.highlightHaveKeyWord(searchCommonHits);
        long endTime = System.currentTimeMillis();

        // 相关搜索模块
        SearchHits searchRelateHits = search.searchRecommend(searchTemplate);
        ArrayList<Map<String, Object>> relateSearchResultList = new ArrayList<Map<String, Object>>();
        relateSearchResultList = HighlightHelper.highlightNoKeyWord(searchRelateHits);

        // 相似搜索模块
        ArrayList<Map<String, Object>> singleDocSimilarResultList = new ArrayList<Map<String, Object>>();
        Map<Map<String, Object >, ArrayList<Map<String, Object>>> similarSearchResultMap = new HashMap<Map<String, Object >, ArrayList<Map<String, Object>>>();
        for (SearchHit hit:searchCommonHits) {
            DocumentTemplate documentTemplate = new DocumentTemplate();
            documentTemplate.setDocumentIndexName( hit.getIndex() );
            documentTemplate.setDocumentTypeName( hit.getType() );
            documentTemplate.setDocumentId( hit.getId() );
            SearchHits singleDocSimilarHits = search.searchSimilarDocuments(searchTemplate, documentTemplate);

            singleDocSimilarResultList = HighlightHelper.highlightNoKeyWord(singleDocSimilarHits);

            similarSearchResultMap.put(hit.getSourceAsMap(), singleDocSimilarResultList);
        }

        req.setAttribute("searchResultList", searchResultList);
        req.setAttribute("searchResultLength", searchCommonHits.getTotalHits() + "");
        req.setAttribute("pageHits", searchTemplate.getPageHits() + "");
        req.setAttribute("searchTook", (endTime - startTime) + "");
        req.setAttribute("relateSearchResultList", relateSearchResultList);
        req.setAttribute("similarSearchResultMap", similarSearchResultMap);
        req.setAttribute("keywordsSuggestion", keywordsSuggestion);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}