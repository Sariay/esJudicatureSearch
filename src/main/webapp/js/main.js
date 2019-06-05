jQuery(document).ready(function($) {

    "use strict";

    // 滚动条美化
    var Search_Nicescroll = function () {
        var niceScrollId = 'body',
            niceScrollSetting = $(niceScrollId).niceScroll({
                index: 2,
                cursorborder: "none",
                autohidemode: true
            });
    };

    // 信息源设置
    var Search_IndexActive = function () {
        var currentIndex = " ",
            dropdownMenuId = ".search-function .dropdown-menu",
            currentCategoryId = ".search-function .current-category";
        if ($(dropdownMenuId).length) {
            currentIndex = "." + $(dropdownMenuId).attr('data-active').trim();
        }
        $(currentIndex).addClass('active');

        var currentCategory = $(dropdownMenuId + " " + "li.active").text();

        if (currentCategory){
            $(currentCategoryId).html(currentCategory);
        } else {
            $(currentCategoryId).html("全部");
        }
    };

    // Ajax打开新链接
    var Search_NewHtml = function() {
        //open navigation
        $('.new-trigger').on('click', function(event) {
            event.preventDefault();
            toggleHtml(true);

            var href = $(this).attr("href"),
                iframeContainer = $(".new-iframe");

            iframeContainer.empty().append($("<div id='myFrame' style='text-align: center'>页面加载中...</div>"));

            $.ajax({
                type: "get",
                url: href,
                async: true,
                timeout: 10000, //10s
                error: function(request) {
                    //TODO: error
                    iframeContainer.empty().append($("<div id='myFrame' style='text-align: center'>网络错误！</div>"));
                },
                success: function(data) {
                    var height = $('.new-html-container ').outerHeight() - 30;

                    iframeContainer.empty().append( $("<iframe id='myFrame'></iframe>"));

                    $("#myFrame").attr({
                        "src" : href,
                        "width" : "100%",
                        "height" :　height,
                        "frameborder" : 0
                        // "scrolling" : "no"
                    });
                },
                complete: function() {
                    // TODO
                }
            });
        });

        //close navigation
        $('.new-close').on('click', function(event) {
            event.preventDefault();
            toggleHtml(false);
        });

        function toggleHtml(bool) {
            $('.new-html-container ').toggleClass('is-visible', bool);
        }
    };

    // 关键词提示
    var Search_WordSuggest = function(){
        function enterToSearch(){
            var e = jQuery.Event("keydown");
            e.keyCode = 13;

            $("input.search-input").trigger(e);
        }

        $.get("js/searchsuggetion.json", function(data){
            $(".typeahead").typeahead({
                source: data,
                items: 15,
                autoSelect: false,
                fitToElement: true,
                afterSelect: enterToSearch()
            });
        },'json');
    };

    (function Malory_Init() {
        Search_Nicescroll();
        Search_IndexActive();
        //Search_NewHtml();
        //Search_WordSuggest();
    })();

});