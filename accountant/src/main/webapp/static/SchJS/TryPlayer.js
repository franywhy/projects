$(function () {

    $("[data-type='TryVideo']").on("click", function () {

        var vid = $(this).attr("data-val");
        var that = $(this);
        if (vid != '') {
            var url = "http://p.bokecc.com/player?vid=" + vid + "&siteid=FE7A65E6BE2EA539&autoStart=auto&width=600&height=450&playerid=CED4B0511C5D4992&playertype=1";
            var strScript = document.createElement("script");
            strScript.src = url;
            strScript.type = "text/javascript";
            $(".playback_img").empty();
            var a = new Dialog2("Test", "#dvTest", {
                "btnClose": function () { a.CloseDialog(); }
            }, true);

            document.getElementById("playback_img").appendChild(strScript);
            a.ShowDialog();

          //  alert("ddddd");

            $(".play_tm_L p").text(that.attr("data-productname"));
            $(".play_tm_R p").text(that.attr("data-price") + "元");
            $(".play_tm_R del").text("原价" + that.attr("data-originalprice") + "元");

            $(".play_tm_R span a").on("click", function () {
                var allPrice = that.attr("data-price");
                var productChildIds = that.attr("data-productid");
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

    });

});
