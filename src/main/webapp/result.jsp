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
    String currentPage = (String) request.getAttribute("currentPageNumber");
    ArrayList<Map<String, Object>> searchResultList = (ArrayList<Map<String, Object>>) request.getAttribute("searchResultList");
    String searchResultLength = (String) request.getAttribute("searchResultLength");
    String pageHits = (String) request.getAttribute("pageHits");
    String searchTook = (String) request.getAttribute("searchTook");

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
    <meta name="description" content="用于搜索司法信息的搜索系统"/>

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
                        <input type="text" name="query" class="search-input" autocomplete="off" value="<%= searchWord %>"/>
                        <button type="submit" value="search" class="submit-button"><i
                                class="glyphicon glyphicon-search"></i></button>
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
        <div class="row" id="result-list">

            <h6 class="result-info">共搜索到<em><%= searchResultLength %>
            </em> 条结果（用时<%= Double.parseDouble(searchTook) / 1000.0 %>s）</h6>


            <%
                if (searchResultList.size() > 0) {
                    Iterator<Map<String, Object>> iterator = searchResultList.iterator();

                    while (iterator.hasNext()) {
                        Map<String, Object> searchResultItem = iterator.next();

                        String content = searchResultItem.get("content").toString();

            %>

            <div class="col-xs-12">
                <article class="result-item">
                    <div class="result-title">
                        <a href="<%= searchResultItem.get( "url" ) %>" target="_blank">
                            <%= searchResultItem.get("title") %>
                        </a>
                    </div>
                    <div class="result-content">
                        <p>
                            <%= searchResultItem.get("content") %>
                        </p>
                    </div>
                    <div class="result-tags">
                        <%= searchResultItem.get("tags")%>
                    </div>
                </article>
            </div>

            <%
                    }
                }
            %>
        </div>

        <nav class="pagination">
            <ul>
                <% if (pages > 1) { %>

                <% if (currentPageNumber == 1) { %>

                <%} else {%>
                <li><a href="/SearchInfo?query=<%= searchWord %>&pageNumber=<%= currentPageNumber-1 %>">&laquo;</a></li>
                <% } %>

                <%

                    for (int i = 1; i <= pages; i++) {
                        if (i == currentPageNumber) {
                %>
                <li class="active"><a href="/SearchInfo?query=<%= searchWord %>&pageNumber=<%= i %>"><%= i %>
                </a></li>
                <% } else { %>
                <li><a href="/SearchInfo?query=<%= searchWord %>&pageNumber=<%= i %>"><%= i %>
                </a></li>
                <%
                        }
                    }
                %>

                <% if (currentPageNumber == pages) { %>

                <% } else { %>
                <li><a href="/SearchInfo?query=<%= searchWord %>&pageNumber=<%= currentPageNumber+1 %>">&raquo;</a></li>
                <% } %>

                <% } %>
            </ul>
        </nav>
    </div>
</main>

<%@ include file="footer.jsp" %>

</body>

</html>