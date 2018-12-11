/// <reference path="jquery-1.8.3.min.js" />
$(
    function () {
        $("[data-type='dt_main_menu']").on("click", function () {

            var that = $(this);
            var second = $("[data-type='dt_second_menu']");
            var tag = that.find("font").text();
            if ($.trim(tag) == "收起") { that.find("font").text("展开"); that.find("font").css({ "background": "url(/images/couname_down.png) no-repeat left center" }); }
            else if ($.trim(tag) == "展开") { that.find("font").text("收起"); that.find("font").css({ "background": "url(/images/couname.png) no-repeat left center" }); }
            $.each(second, function (i, val) {
                if ($(val).attr("data-val").toString() == that.attr("data-val")) {
                    $(val).toggle();
                }
            });
        });
    }
);