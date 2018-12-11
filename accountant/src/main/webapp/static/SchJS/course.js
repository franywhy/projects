$(function () {
    var $tabTitleLi = $('.tabList ul li');
    var $tabCon = $('.tabCon .box');

    $tabTitleLi.click(function () {
        var _index = $(this).index()-1;
        $(this).addClass('active').siblings('li').removeClass('active');
        $tabCon.hide().eq(_index).show();
    });
});

//试听播放
$("#playBtn").live("click", function () {
//alert("sssss");
    $(this).parent().remove();
    var val = $(this).attr("data-val");
    if(val!=null&&val!="" && val!=undefined ){

        initMain(val, "productPlay", 460, 280);
     }else{
        alert("此商品没有试听内容！");
        return false;
     }
//    initMain();
});
function initMain1() {

    var fls = flashChecker();
    var s = "";
    if (!fls.f) {
        if (confirm("您的浏览器还没有安装Flash插件，现在安装？")) {
            window.location.href = "http://get.adobe.com/cn/flashplayer/";
        }
    }
    else {
        var tryUrl = "0B6A9FD7E2CB61929C33DC5901307461";

        var flashvars = {
            userid: "25CCD0665D668BCE",
            videoid: tryUrl,
            mode: "api",
            autostart: "true",
            jscontrol: "true"

        };
        var params = {
            allowFullscreen: "true",
            allowScriptAccess: "always",
            wmode: "transparent"
        };
        var attributes = {};
        swfobject.embedSWF("http://union.bokecc.com/flash/player.swf", "productPlay", "460", "260", "6.0.0", "expressInstall.swf", flashvars, params, attributes);
    }
}



function flashChecker() {
    var hasFlash = 0; //是否安装了flash  
    var flashVersion = 0; //flash版本  

    if (document.all) {
        var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        if (swf) {
            hasFlash = 1;
            VSwf = swf.GetVariable("$version");
            flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
        }
    } else {
        if (navigator.plugins && navigator.plugins.length > 0) {
            var swf = navigator.plugins["Shockwave Flash"];
            if (swf) {
                hasFlash = 1;
                var words = swf.description.split(" ");
                for (var i = 0; i < words.length; ++i) {
                    if (isNaN(parseInt(words[i]))) continue;
                    flashVersion = parseInt(words[i]);
                }
            }
        }
    }
    return { f: hasFlash, v: flashVersion };
}

$(document).ready(function () {
    var productid = $("#hiddepid").val();
    $.ajax(
    {
        url: "/lib/product/ProductConsult.ashx",
        type: "POST",
        data: "pid=" + productid + "&type=1&page=1",
        beforeSend: function () {
            //            $("#tabadcontent_10").html("数据加载中.....");

        },
        success: function (data) {
            $("#tabadcontent_10").empty();
            if (data != "") {
                var d = data.split("$$");
                $("#tabadcontent_10").html(d[0]);
                var i = new Number(d[1]) - 1;
                var j = new Number(d[1]) + 1;
                $("#divPase").html("<span><b>" + d[1] + "</b>/" + d[2] + " <a href=\"javascript:\" rel=\"" + i + "\"><img src=\"images/index_per.png\" /></a><a href=\"javascript:\" rel=\"" + j + "\"><img src=\"images/index_next.png\" /></a></span>");
            }
        }
    })
});

$(".consultant_top a").live("click", function () {

    var productid = $("#hiddepid").val();
    var page = $(this).attr('rel');

    $.ajax(
    {
        url: "/lib/product/ProductConsult.ashx",
        type: "POST",
        data: "pid=" + productid + "&type=1&page=" + page + "",
        beforeSend: function () {
            //            $("#tabadcontent_10").html("数据加载中.....");

        },
        success: function (data) {
            $("#tabadcontent_10").empty();
            if (data != "") {
                var d = data.split("$$");
                $("#tabadcontent_10").html(d[0]);
                var i = new Number(d[1]) - 1;
                var j = new Number(d[1]) + 1;
                $("#divPase").html("<span><b>" + d[1] + "</b>/" + d[2] + " <a href=\"javascript:\" rel=\"" + i + "\"><img src=\"images/index_per.png\" /></a><a href=\"javascript:\" rel=\"" + j + "\"><img src=\"images/index_next.png\" /></a></span>");
            }
        }
    })

});

$("#consultOK").live("click", function () {
    var productid = $("#hiddepid").val();
    var content = $("#content").val();
    if (content == "在此输入咨询内容") {
        return;
    }
    $.ajax({
        type: "POST",
        url: "/lib/product/ProductConsult.ashx",
        data: {
            type: "2",
            pid: productid,
            content: content
        },
        cache: false,
        beforeSend: function () {
            $("#loading").html("数据提交中,请稍后...");
        },
        success: function (result) {
            alert("咨询提交成功");
            $.ajax(
       {
           url: "/lib/product/ProductConsult.ashx",
           type: "POST",
           data: "pid=" + productid + "&type=1&page=1",
           beforeSend: function () {
               
           },
           success: function (data) {
               $("#tabadcontent_10").empty();
               $("#loading").empty();
               if (data != "") {
                   var d = data.split("$$");
                   $("#tabadcontent_10").html(d[0]);
                   var i = new Number(d[1]) - 1;
                   var j = new Number(d[1]) + 1;
                   $("#divPase").html("<span><b>" + d[1] + "</b>/" + d[2] + " <a href=\"javascript:\" rel=\"" + i + "\"><img src=\"images/index_per.png\" /></a><a href=\"javascript:\" rel=\"" + j + "\"><img src=\"images/index_next.png\" /></a></span>");
               }
           }
       })
        }

    });
});

/*------------------------提交购买---------------*/
$(function () {
    $("#getBuy").click(function () {
        var allPrice = $("#allPrice").val();
        var code = $("#hidSchool").val();
        var productChildIds = $("#hiddepid").val();
        if (productChildIds != '') {
            $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=8&productChildIds=" + productChildIds + "&allPrice=" + allPrice + "&code=" + code + "",
                beforeSend: function () {

                },
                success: function (data) {
                    if (data != "000") {
                        window.location.href = "/UserManage/order.html?ordertype=803";
                    }
                    else {
                        var url = window.location.href;
                        window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";
                    }
                }

            })
        }
    });
});

/********************获取和设置FLASH**********************************/
//获取FLASH ID
function getSWF(swfID) {

    if (window.document[swfID]) {
        return window.document[swfID];
    } else if (navigator.appName.indexOf("Microsoft") == -1) {
        if (document.embeds && document.embeds[swfID]) {
            return document.embeds[swfID];
        }
    } else {
        return document.getElementById(swfID);
    }
}

