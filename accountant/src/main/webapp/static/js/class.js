$(function () {
    $(".getBuy").click(function () {
        var productChildIds = $(this).attr("rel");
        var allPrice = $(this).attr("data-val");
        var courseID = $(this).attr("data-cid");
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
                        if (data == "111") {
                            window.location.href = "/UserManage/personCenter.html";
                        }
                        else {
                            window.location.href = "/UserManage/order.html?ordertype=803";
                        }
                    }
                    else {
                        $(".btn-login").trigger("click");
                    }
                }
            })
        }
    });

    $(".getLive").click(function () {
        var productChildIds = $(this).attr("rel");
        var allPrice = $(this).attr("data-val");
        var courseID = $(this).attr("data-cid");
        var id = $(this).attr("data-id");
        var isopen = $(this).attr("data-type");
        if (productChildIds != '') {
            $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=88&productChildIds=" + productChildIds + "&allPrice=" + allPrice + "&courseID=" + courseID + "&isopen=" + isopen + "&id=" + id + "",
                beforeSend: function () {
                },
                success: function (data) {
                    if (data != "000") {
                        if (data == "111") {
                            window.location.href = "/live/" + id + ".html";
                        }
                        else {
                            window.location.href = "/UserManage/order.html?ordertype=803";
                        }
                    }
                    else {
                        $(".btn-login").trigger("click");
                    }
                }
            })
        }
    });
    $(".tyePlayer").click(function () {
        $(".player-cc").empty();
        var vid = $(this).attr("rel");
        var url = "http://p.bokecc.com/player?vid=" + vid + "&siteid=FE7A65E6BE2EA539&autoStart=auto&width=848&height=600&playerid=CED4B0511C5D4992&playertype=1";
        var strScript = document.createElement("script");
        strScript.src = url;
        strScript.type = "text/javascript";
        

        document.getElementById("play_video").appendChild(strScript);

        var bg = $(".black-bg");
        bg.stop(true, true).fadeIn();
        bg.find(".close-btn").on("click", function () {
            $(this).parents(".black-bg").fadeOut();
            return false;
        });
    });
    $(".month-box").click(function () {
        var m = $(this).attr("title");
        var id = $(this).attr("data-val");
        $("div[id^='month_" + id + "_']").hide();
        $("div[id^='month_" + id + "_" + m + "']").show();

        $(this).addClass("act").siblings("div").removeClass("act");
    });
});