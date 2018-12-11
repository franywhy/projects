/// <reference path="jquery-1.8.3.min.js" />

var SpiltPackageLi = function (pid, index) {
   // alert("aaa");
    var li = $("[data-type='li_package_" + pid + "']");
    var length = li.length;
    $.each($(li), function (i, val) {
        $(val).hide();
    });
    length = parseInt(index) * 3 > length ? length : parseInt(index) * 3;
    for (var i = (parseInt(index) - 1) * 3; i < parseInt(index) * 3; i++) {
        if (i == (parseInt(index) - 1) * 3) {
            $(li[i]).find(".de").hide();
        }
        $(li[i]).show();
    }
    $("[data-packageid='" + index + "']").removeClass("bright");

    $.each($("[data-type='spiltPage']"), function (i, val) {

        if ($(val).attr("data-packageid").toString() == pid.toString() && $(val).attr("data-val").toString() == index.toString()) {
            $(val).addClass("bright");
        }
    });
}
$(function () {
    $("[data-type='li_chanage']").on("click", function () {
        var that = $(this);
        $("[data-type='li_chanage']").removeClass("action");
        that.addClass("action");
        $("[data-type='package']").hide();
        var dataVal = that.attr("data-val");
        $.each($("[data-type='package']"), function (i, val) {
            if ($(val).attr("data-val") == dataVal) {
                $(val).show();
                SpiltPackageLi($(val).attr("data-val"), 1);
            }
        });
    });
    /********************************/
    $("[data-type='spiltPage']").on("click", function () {
        var that = $(this);
        $("[data-packageid='" + that.attr("data-packageid") + "']").removeClass("bright");

        that.addClass("bright");

        var pageIndex = that.attr("data-val");
        SpiltPackageLi(that.attr("data-packageid"), pageIndex);

    });

    
    /***********************************************/
    $("[data-val='li_package_li']").hide();
    var showDiv = $("[data-type='li_chanage']"); //.find(".action");
    var firstDiv;
    $.each(showDiv, function (i, val) {
        if ($(val).hasClass("action")) {
            firstDiv = $(val);
            return false;
        }
    });
    //  SpiltPackageLi($(showDiv).attr("data-val"), 1);
    var learnbzz = $(".learnbzz");
    $.each(learnbzz, function (j, val2) {

        if ($(firstDiv).attr("data-val") == $(val2).attr("data-val")) {
           
            $(val2).show();
            SpiltPackageLi($(showDiv).attr("data-val"), 1);
        }
    });



    $(".learnbzz").width(960);
});
$(function () {
    //获取url
    var pathname = window.location.pathname;
    if (pathname.indexOf("teacher_") != -1 || pathname.indexOf("personal") != -1) {
        $(".learnbzz").width(916);
        var nav = $("[data-type='package']");
        $.each($(nav), function (i, val) {
            var nav_li = $(val).find("li");
            $.each($(nav_li), function (j, val2) {
                if (j < 3) {
                    $(val2).show();
                }
            });
        });
        var gd_li = $(".gd");
        $(gd_li, function (i, val) {
            gd_li.find("li").removeClass();
            $(val).find("li").first().addClass("bright");
        });
    }

});
//列表页课程包分页
/*
$(function () {

    var itemCount = parseInt($(".selling_top").attr("data-itemCount"));
    var pageIndex = parseInt($(".selling_top").attr("data-pageIndex"));
    var pageSize = parseInt(itemCount / pageIndex + (itemCount % pageIndex > 0 ? 1 : 0));
    if (itemCount <= pageIndex) {
        $("#nav-spilt-page").hide();
    }
    function InitPackage(index) {
        if (index == pageSize) {
            $("#btnNextPackage").hide();
        } else {
            $("#btnNextPackage").show();

        }
        if (index == 1) {
            $("#btnPrePackage").hide();
        } else {
            $("#btnPrePackage").show();

        }
        var length = (index * 5 > itemCount ? itemCount : index * 5)
        $(".selling_top li").hide();
        for (var i = (index - 1) * 5; i < length; i++) {
            $($(".selling_top li")[i]).show();
        }
        var that = $($(".selling_top li")[(index - 1) * 5]);
        $("[data-type='li_chanage']").removeClass("action");
        that.addClass("action");
        $("[data-type='package']").hide();
        var dataVal = that.attr("data-val");
        $.each($("[data-type='package']"), function (i, val) {
            if ($(val).attr("data-val") == dataVal) {
                $(val).show();
                SpiltPackageLi($(val).attr("data-val"), 1);
            }
        });
        $(".selling_top").attr("data-pageIndex", index);
    }
    $("#btnPrePackage").on("click", function () {
        pageIndex = parseInt($(".selling_top").attr("data-pageIndex"));
        if (pageIndex > 1) {
            pageIndex -= 1;
        }
        InitPackage(pageIndex);
    });
    $("#btnNextPackage").on("click", function () {
        pageIndex = parseInt($(".selling_top").attr("data-pageIndex"));

        if (parseInt(pageIndex) < parseInt(pageSize)) {

            pageIndex += 1;
        }

        InitPackage(pageIndex);
        
    });

    InitPackage(1);
});
*/