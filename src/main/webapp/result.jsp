<%--
  Created by IntelliJ IDEA.
  User: 水煮鱼肠面
  Date: 2019/3/24
  Time: 15:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String searchWord = (String) request.getAttribute("searchWord");
    String currentIndex = (String) request.getAttribute("currentIndex");
    String currentPage = (String) request.getAttribute("currentPageNumber");
    ArrayList<Map<String, Object>> searchResultList = (ArrayList<Map<String, Object>>) request.getAttribute("searchResultList");
    String searchResultLength = (String) request.getAttribute("searchResultLength");
    String pageHits = (String) request.getAttribute("pageHits");
    String searchTook = (String) request.getAttribute("searchTook");
    ArrayList<Map<String, Object>> relateSearchResultList = (ArrayList<Map<String, Object>>) request.getAttribute("relateSearchResultList");
    Map<Map<String, Object >, ArrayList<Map<String, Object>>> similarSearchResultMap = (Map<Map<String, Object>, ArrayList<Map<String, Object>>>) request.getAttribute("similarSearchResultMap");
    String keywordsSuggestion = (String) request.getAttribute("keywordsSuggestion");

    int pages = Integer.parseInt(searchResultLength) / Integer.parseInt(pageHits) + 1;
    pages = pages > 15 ? 15 : pages;
    int currentPageNumber = Integer.parseInt(currentPage);
%>

<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>司法信息搜索系统</title>
    <meta name="keywords" content="搜索系统、司法信息"/>
    <meta name="description" content="用于司法信息领域的搜索系统"/>

    <!-- Favicons -->
    <link rel="shortcut icon" href="img/favicon.ico"/>

    <!-- css -->
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="css/main.css"/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://staff.bootcss.com/npm/html5shiv@3.7.3/dist/html5shiv.min.js"></script>
    <script src="https://staff.bootcss.com/npm/respond.js@1.4.2/dest/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<%@ include file="nav.jsp" %>

<header class="header">
    <div class="container">
        <div class="row">
            <div class="col-xs-0 col-sm-1 col-md-2">
            </div>
            <div class="col-xs-12 col-sm-10 col-md-8">
                <div class="search-form">
                    <form action="/SearchInfo" method="get">
                        <input type="text" name="query" data-provide="typeahead" class="search-input typeahead" autocomplete="off" value="<%= searchWord %>" />
                        <button type="submit" value="search" class="submit-button"><i class="glyphicon glyphicon-search"></i></button>
                    </form>
                </div>
                <div class="site-logo">
                    <h1>
                        <span>司法信息搜索系统</span>
                    </h1>
                </div>
            </div>
            <div class="col-xs-0 col-sm-1 col-md-2">
            </div>
        </div>
    </div>
</header>

<main class="main">
    <div class="container">
        <div class="result-info">
            <h6 class="info-title">
                共搜索到 <em><%= searchResultLength %></em> 条结果，用时<%= Double.parseDouble(searchTook) / 1000.0 %>s
                <% if (searchResultList.size()>0){%>
                    （第<%= (currentPageNumber-1)*Integer.parseInt(pageHits)+1 %>-<%= currentPageNumber*Integer.parseInt(pageHits) %>条）
                <% } else {%>
                    （推荐关键词如下）
                <%}%>
            </h6>

            <!-- 信息源选择模块 -->
            <div class="search-function">
                <label class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="current-category">全部</span>
                    <span class="caret"></span>
                </label>
                <ul class="dropdown-menu" data-active="<%= currentIndex %>">
                    <li class="zero">
                        <a href="/SearchInfo?query=<%= searchWord %>&index=zero">全部</a>
                    </li>
                    <li class="one">
                        <a href="/SearchInfo?query=<%= searchWord %>&index=one">视频直播</a>
                    </li>
                    <li class="two">
                        <a href="/SearchInfo?query=<%= searchWord %>&index=two">司法解釋</a>
                    </li>
                    <li class="three">
                        <a href="/SearchInfo?query=<%= searchWord %>&index=three">诉讼指南</a>
                    </li>
                    <li class="four">
                        <a href="/SearchInfo?query=<%= searchWord %>&index=four">司法案例</a>
                    </li>
                </ul>
            </div>
        </div>

        <!-- 全文搜索模块 -->
        <div class="result-list row">
            <%
                if (searchResultList.size() > 0) {
                    Iterator<Map<String, Object>> iterator = searchResultList.iterator();
                    int hrefId = 0;

                    while (iterator.hasNext()) {
                        Map<String, Object> searchResultItem = iterator.next();
            %>
                <div class="col-xs-12">
                    <article class="result-item">
                        <div class="result-title">
                            <a href="<%= searchResultItem.get( "url" ) %>" class="new-trigger" target="_blank">
                                <%= searchResultItem.get("title") %>
                            </a>
                        </div>
                        <div class="result-content">
                            <p>
                                <%= searchResultItem.get("content") %>
                            </p>
                        </div>
                        <div class="other-metas">
                            <% if (searchResultItem.get("tags")!=null){ %>
                                <div class="result-tags">
                                    <i class="glyphicon glyphicon-tags"></i>
                                    <%= searchResultItem.get("tags") %>
                                </div>
                            <%}%>

                            <% if (searchResultItem.get("category")!=null){ %>
                                <div class="result-category">
                                    <i class="glyphicon glyphicon-folder-open"></i>
                                    <%= searchResultItem.get("category") %>
                                </div>
                            <%}%>
                        </div>


                        <!-- 相似搜索模块 -->
                        <%
                            ArrayList<Map<String, Object>> similarSearchResultList= similarSearchResultMap.get( searchResultItem );
                            if (similarSearchResultList.size()>0){
                        %>
                        <div class="result-similar panel panel-default">
                            <div class="panel-heading" role="tab" id="headingOne">
                                <h4 class="panel-title">
                                    <a role="button" data-toggle="collapse" data-parent="#accordion" href="#<%= hrefId %>" aria-expanded="true" aria-controls="<%= hrefId %>">
                                        相似推荐(点击查看)
                                    </a>
                                </h4>
                            </div>
                            <div id="<%= hrefId %>" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                                <div class="panel-body">
                                    <ul>
                                        <%
                                            Iterator<Map<String, Object>> iterator1 = similarSearchResultList.iterator();

                                            while (iterator1.hasNext()){
                                                Map<String, Object> similarResultItem = iterator1.next();
                                        %>
                                        <li>
                                            <a href="<%= similarResultItem.get("url") %>" class="new-trigger" target="_blank"><%= similarResultItem.get("title") %></a>
                                        </li>
                                        <%
                                            }
                                        %>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </article>
                </div>
            <%
                        hrefId++;
                    }
                } else {
            %>
                <div class="col-xs-12 search-suggest">
                    <%= keywordsSuggestion %>
                </div>
            <%
                }
            %>
        </div>

        <!-- 相关搜索模块 -->
        <div class="result-relate row">
            <div class="col-xs-12">
                <% if(relateSearchResultList.size() > 0){ %>
                    <h4><span class="highlight"><%= searchWord %></span>的相关搜索</h4>
                    <ul>
                        <%
                            Iterator<Map<String, Object>> iterator = relateSearchResultList.iterator();
                            while(iterator.hasNext()){
                                Map<String, Object> relateSearchResultItem = iterator.next();
                        %>

                            <li>
                                <a href="<%= relateSearchResultItem.get("url")%>" class="new-trigger" target="_blank">
                                    <%= relateSearchResultItem.get("title")%>
                                </a>
                            </li>

                        <% } %>
                    </ul>
                <% } %>
            </div>
        </div>

        <!-- 分页模块 -->
        <nav class="result-pagination pagination">
            <ul>
                <% if (pages > 1) { %>

                    <% if (currentPageNumber == 1) { %>

                    <%} else {%>
                        <li><a href="/SearchInfo?query=<%= searchWord %>&index=<%= currentIndex %>&pageNumber=<%= currentPageNumber-1 %>">&laquo;</a></li>
                    <% } %>

                    <%
                        for (int i = 1; i <= pages; i++) {
                            if (i == currentPageNumber) {
                    %>
                                <li class="active"><a href="/SearchInfo?query=<%= searchWord %>&index=<%= currentIndex %>&pageNumber=<%= i %>"><%= i %></a></li>
                            <% } else { %>
                                <li><a href="/SearchInfo?query=<%= searchWord %>&index=<%= currentIndex %>&pageNumber=<%= i %>"><%= i %></a></li>
                    <%
                            }
                        }
                    %>

                    <% if (currentPageNumber == pages) { %>

                    <% } else { %>
                        <li><a href="/SearchInfo?query=<%= searchWord %>&index=<%= currentIndex %>&pageNumber=<%= currentPageNumber+1 %>">&raquo;</a></li>
                    <% } %>
                <% } %>
            </ul>
        </nav>
    </div>
</main>

<!-- Ajax加载新页面 -->
<nav class="new-html-container">
    <div class="new-header">
        <span class="new-logo">Navigation</span>
        <span class="new-close-container"> <a href="javascript:;" class="new-close">关闭</a></span>
    </div>
    <div class="new-body">
        <div class="new-iframe">
        </div>
    </div>
</nav>

<%@ include file="footer.jsp" %>

</body>

</html>