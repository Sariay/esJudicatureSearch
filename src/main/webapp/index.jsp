<%--
  Created by IntelliJ IDEA.
  User: 水煮鱼肠面
  Date: 2019/3/23
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
                        <input type="text" name="query" data-provide="typeahead" class="search-input typeahead" autocomplete="off" placeholder="search for something..." />
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

<!-- 网站栏目链接 -->
<main class="main">
    <div class="container">
        <div class="row" id="post-list">
            <div class="col-xs-12 col-sm-6">
                <article class="post-item">
                    <h2 class="post-title">
                        <a href="#" target="_blank">司法解释</a>
                    </h2>
                    <div class="post-image">
                        <a class="thumbnail" href="#" target="_blank">
                            <img  width="531">
                        </a>
                    </div>
                    <div class="post-content">
                        <p>
                            <a href="#"></a>
                        </p>
                    </div>
                </article>
            </div>
            <div class="col-xs-12 col-sm-6">
                <article class="post-item">
                    <h2 class="post-title">
                        <a href="#" target="_blank">诉讼指南</a>
                    </h2>
                    <div class="post-image">
                        <a class="thumbnail" href="#" target="_blank">
                            <img  width="531">
                        </a>
                    </div>
                    <div class="post-content">
                        <p>
                            <a href="#"></a>
                        </p>
                    </div>
                </article>
            </div>
            <div class="col-xs-12 col-sm-6">
                <article class="post-item">
                    <h2 class="post-title">
                        <a href="#" target="_blank">司法案例</a>
                    </h2>
                    <div class="post-image">
                        <a class="thumbnail" href="#" target="_blank">
                            <img  width="531">
                        </a>
                    </div>
                    <div class="post-content">
                        <p>
                            <a href="#"></a>
                        </p>
                    </div>
                </article>
            </div>
            <div class="col-xs-12 col-sm-6">
                <article class="post-item">
                    <h2 class="post-title">
                        <a href="#" target="_blank">视频直播</a>
                    </h2>
                    <div class="post-image">
                        <a class="thumbnail" href="#" target="_blank">
                            <img  width="531">
                        </a>
                    </div>
                    <div class="post-content">
                        <p>
                            <a href="#"></a>
                        </p>
                    </div>
                </article>
            </div>
        </div>
    </div>
</main>

<%@ include file="footer.jsp" %>

</body>

</html>