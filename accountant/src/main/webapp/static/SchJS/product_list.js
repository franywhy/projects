/// <reference path="jquery-1.8.3.min.js" />
$(document).ready(function () {
    $("#allCourse").trigger("click");
;$(
    function () {
        $("[data-val='CourseProducts1']").hide();
        var lst_product = null;
        lst_product = $("[data-val='CourseProducts1']");
        $("#pager").data("pageIndex", 1);
        $("#pager").data("pageSize", 8);
        var length = 0;
        var SplitPage = function (index, size) {

            length = index * size;
            if (lst_product.length <= length) {
                $("#p_display").parent().hide();
            }
            length = lst_product.length < length ? lst_product.length : length;
          
            for (var i = (index - 1) * size; i < length; i++) {

                $(lst_product[i]).show();
            }
        }
        //按键分页
        $("#displayMore").on("click", function () {

            var pageIndex = parseInt($("#pager").data("pageIndex")) + 1;
            var pageSize = parseInt($("#pager").data("pageSize"));
            var count = parseInt((lst_product.length / pageSize)) + ((lst_product.length % pageSize)>0?1:0);
      
            if (pageIndex > count) {
                $("#pager").data("pageIndex", pageIndex - 1);
                
            } else {
                SplitPage(pageIndex, pageSize);
                $("#pager").data("pageIndex", pageIndex);
            }

        });
        //初始分页
        SplitPage(1, 10000);
        if (lst_product.length <= 10000) {
            $("div.more").hide();
        }
    });

});