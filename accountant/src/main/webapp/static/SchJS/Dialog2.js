/// <reference path="jquery-1.8.3.min.js" />
/*
*Js功能：弹窗　是用前必须调用Jquery
*参数说明：
*     title标题
*     element 元素
*     fn Json方法键值对{key:function()}
*调用方法: 
*     1、Dialog("测试",$("#id"));
*     2、Dialog("测试","<p>Hello World!</p>",{"button":function{alert("aaaa");},"button2":function(){alert("dddddd");}});
*/

var tmpElement;
var Dialog2 = function (title, element, fn, isshowclose) {

    var strHtml = "";
    strHtml = "<div id='bg'></div>";
    strHtml += "<div id='details'>";
    strHtml += "<div class='details_top'>";
    strHtml += "<p><a href='javascript:void(0);'>" + title + "</a></p>";
    strHtml += "<div class='guanbi'>";
    strHtml += "<a id='closeDialog' href='javascript:void(0)'><img src='/images/details.png'/></a>";
    strHtml += "</div>";
    strHtml += "</div>";
    strHtml += "<div class='details_bottom' id='divBottom'>";
    strHtml += "</div>";
    strHtml += "</div>";
    tmpElement = $(element).clone();
    $(strHtml).appendTo($("body"));
    $(element).appendTo("#divBottom");
    $(element).show();
    $("#bg").css({ "display": "none",
        "height": $(document).height(),
        "width": $(document).width,
        "z-index": 100001,
        'text-align': 'center'
    });

    if (isshowclose == true) {
        $(".details_top").css("display", "none");
    }
    var e_h = $(element).height();


    var e_w = $(element).width();

    var c_h = $("#details").height();
    var c_w = $("#details").width();
    if (c_h < e_h) {

        $("#details").height($(element).height());

    } else if (c_h >= e_h && e_h > 0) {

        $("#details").css("height", e_h);
    }
    if (c_w < e_w) {

        $("#details").css("width", e_w);

    } else if (c_w >= e_w && e_w > 0) {

        $("#details").css("width", e_w);
        $(".details_top").css("width", e_w);
        $(".details_bottom").css("width", e_w);
    }

    var InitWindow = function () {
        var windowHeight = $(window).height();
        //获得窗口的宽度 
        var windowWidth = $(window).width();
        //    //获得弹窗的高度 
        var popHeight = $("#details").height();
        //获得弹窗的宽度 
        var popWidht = $("#details").width();
        //获得滚动条的高度 
        var scrollTop = $(window).scrollTop();
        //获得滚动条的宽度 
        var scrollleft = $(window).scrollLeft();
        $("#details").css({
            "z-index": 100002,
            'border': 'none',
            'left': (windowWidth - popWidht) / 2 + scrollleft,
            'background': '#fff',
            'position': 'absolute',
            'top': (windowHeight - popHeight) / 2 + scrollTop

        });
    }
    InitWindow();
    $(window).on("scroll", function () {
        InitWindow();
        $("#details").css({ "display": "block" });
    });
    $("#closeDialog").on("click", function () {
        $("#details").remove();
        $("#bg").remove();
        $(tmpElement).hide();
        $(tmpElement).appendTo($("body"));
    });
    var that = {};
    that = fn;
    $.each(that, function (i, val) {
        $("#" + i).on("click", eval(val));
    });
}
Dialog2.prototype.ShowDialog = function () {
    $("#bg").css("display", "block");
    $("#details").css("display", "block");
}
Dialog2.prototype.CloseDialog = function () {
    try {
        $("#details").remove();
        $("#bg").remove();
        $(tmpElement).hide();
        $(tmpElement).appendTo($("body"));
    } catch (e) { }
}