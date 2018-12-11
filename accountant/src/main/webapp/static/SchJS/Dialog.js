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
var Dialog = function (title, element, fn) {
    // alert(title);
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
    $("#bg").css("display", "block");
    $("#details").css("display", "block");
    $("#closeDialog").on("click", function () {
        CloseDiloag();
    });

    var that = {};
    that = fn;
    $.each(that, function (i, val) {
        $("#" + i).on("click", eval(val));
    });

}

var CloseDiloag = function () {
    try {
        $("#details").remove();
        $("#bg").remove();
        $(tmpElement).hide();
        $(tmpElement).appendTo($("body"));
    } catch (e) { }
}