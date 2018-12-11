
/*------------------------提交购买---------------*/
$(function () {
    $(".orange a").click(function () {
        var allPrice = $(this).attr("title");
        var productChildIds = $(this).attr("rel");
        if (productChildIds != '') {
            $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=8&productChildIds=" + productChildIds + "&allPrice=" + allPrice + "",
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

//播放试听方法
function goToListenCourse() {
    //    var ccUrl = "0B6A9FD7E2CB61929C33DC5901307461";

    //    var swfobj = new SWFObject('http://union.bokecc.com/flash/player.swf', 'playerswf', "679", "460", '8');
    //    swfobj.addVariable("userid", "25CCD0665D668BCE"); // partnerID,用户id
    //    swfobj.addVariable("videoid", ccUrl); // spark_videoid,视频所拥有的 api id
    //    swfobj.addVariable("mode", "api"); // mode, 注意：必须填写，否则无法播放
    //    swfobj.addVariable("autostart", "true"); // 开始自动播放，true/false
    //    swfobj.addVariable("jscontrol", "true"); // 开启js控制播放器，true/false
    //    swfobj.addParam('allowFullscreen', 'true');
    //    swfobj.addParam('allowScriptAccess', 'always');
    //    swfobj.addParam('wmode', 'transparent');
    //    swfobj.write('ccplayer');



}
;$(function () {
    $("[data-type='tyePlayer']").on("click", function () {
        var productId = $(this).attr("data-productid");

        $.ajax({
            url: "/lib/product/CourseProducts.ashx",
            type: "GET",
            data: { pid: productId, type: "8" },
            cache: false,
            success: function (data) {

                if (data != '') {
                    var objData = eval("(" + data + ")");
                    var url = "http://p.bokecc.com/player?vid=" + objData.TEACHERSID + "&siteid=FE7A65E6BE2EA539&autoStart=auto&width=600&height=450&playerid=CED4B0511C5D4992&playertype=1";
                    var strScript = document.createElement("script");
                    strScript.src = url;
                    strScript.type = "text/javascript";
                    $(".playback_img").empty();

                    var a = new Dialog2("Test", "#dvTest", {
                        "btnClose": function () { a.CloseDialog(); }
                    }, true);
                    //$(".playback_img").html(str);
                    document.getElementById("playback_img").appendChild(strScript);
                    a.ShowDialog();
                    $(".play_tm_L p").text(objData.PRODUCTNAME);
                    $(".play_tm_R p").text(objData.PRICE + "元");
                    $(".play_tm_R del").text("原价" + objData.ORIGINAL_PRICE + "元");

                    $(".play_tm_R span a").on("click", function () {
                        var allPrice = objData.PRICE;
                        var productChildIds = objData.PRODUCTID;
                        if (productChildIds != '') {
                            $.ajax(
                                       {
                                           url: "/lib/product/ProductDetail.ashx",
                                           type: "POST",
                                           async: false,
                                           data: "type=8&productChildIds=" + productChildIds + "&allPrice=" + allPrice + "",
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

                                       });
                        }
                    });

                } else {
                    alert("没有试听内容！");
                }
            }
        });

    });

});
