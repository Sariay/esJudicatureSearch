jQuery(document).ready(function($) {

    "use strict";

    // nicescroll
    var Search_Nicescroll = function () {
        $("html").niceScroll({
             zindex: 8
            // autohidemode: "hidden"
        });
    };

    (function Malory_Init() {
        Search_Nicescroll();
    })();

});