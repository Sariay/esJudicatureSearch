package com.lcc.controller;

import com.lcc.entity.ElasticsearchConfig;
import com.lcc.entity.SearchTemplate;
import com.lcc.service.Search;
import com.lcc.service.SearchImpl;
import com.lcc.utils.HighlightHelper;
import org.elasticsearch.search.SearchHits;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName SearchServlet
 * @Description 处理前端网页传入的数据，整理数据后与业务逻辑层进行沟通（调用搜索接口），最后搜索结果返回给前端页面
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

        String pageNumberStr = req.getParameter("pageNumber");

        int pageNumber = 1;

        if (pageNumberStr != null && Integer.parseInt(pageNumberStr) > 1) {
            pageNumber = Integer.parseInt(pageNumberStr);
        }

        searchTemplate.setPageNumber(pageNumber);
        searchTemplate.setPageHits(20);

        //根据搜索词搜索信息
        searchJudiciaInformation(searchTemplate, req);
        //将搜索词，显示在搜索结果页的搜索输入框里
        req.setAttribute("searchWord", searchTemplate.getQuery());
        req.setAttribute("currentPageNumber", searchTemplate.getPageNumber() + "");

        //请求转发
        req.getRequestDispatcher("result.jsp").forward(req, resp);
    }

    private void searchJudiciaInformation(SearchTemplate searchTemplate, HttpServletRequest req) {
        long startTime = System.currentTimeMillis();

        //需要搜索的索引、类型、字段等， 不填写则默认搜索所有的索引及所有的合理字段
        String[] indexNames = {ElasticsearchConfig.INDEX_NAME.getValue()};
        String[] typeNames = {ElasticsearchConfig.TYPE_NAME.getValue()};
        String[] fieldNames = {"title", "content"};

        searchTemplate.setIndexNames(indexNames);
        searchTemplate.setTypeNames(typeNames);
        searchTemplate.setFieldsName(fieldNames);

        Search search = new SearchImpl();
        SearchHits searchCommonHits = search.searchCommon(searchTemplate);
        //SearchHits searchFuzzyHits = search.searchFuzzy(searchTemplate);
        //SearchHits searchRecommendHits = search.searchRecommend(searchTemplate);
        SearchHits hits = searchCommonHits;

        ArrayList<Map<String, Object>> searchResultList = new ArrayList<Map<String, Object>>();
        searchResultList = HighlightHelper.highlightHaveKeyWord(hits);

        long endTime = System.currentTimeMillis();

        req.setAttribute("searchResultList", searchResultList);
        req.setAttribute("searchResultLength", hits.getTotalHits() + "");
        req.setAttribute("pageHits", searchTemplate.getPageHits() + "");
        req.setAttribute("searchTook", (endTime - startTime) + "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}