$(function () {
    $(".main-btn-g").click(function () {
        var productChildIds = $(this).attr("rel");
        var allPrice = $(this).attr("data-val");
        var isBack = $(this).attr("lang");
        if (isBack == "True") {
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
                        $(".btn-login").trigger("click");
                    }

                }
            })
        }
        else {
            $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=8&productChildIds=" + productChildIds + "&allPrice=" + allPrice + "",
                beforeSend: function () {
                    $(".main-btn-g").html("提交中...");
                },
                success: function (data) {
                    if (data != "000") {
                        var check = "0";
                        $.ajax({
                            url: "/lib/product/ProductDetail.ashx",
                            type: "POST",
                            data: "type=6&check=" + check + "",
                            beforeSend: function () {
                            },
                            success: function (data) {
                                if (data != "000") {
                                    if (data == "111") {
                                        window.location.href = "/UserManage/personCenter.html";
                                    }
                                    else {
                                        window.location.href = "/UserManage/pay.html?no=" + data;
                                    }
                                }
                                else {
                                    var url = window.location.href;
                                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";
                                }
                            }
                        })
                    }
                    else {
                        $(".btn-login").trigger("click");
                    }
                    $(".main-btn-g").html("提交订单");
                }
            })
        }
    });

    $("#pay1").click(function () {
        $(this).addClass('act');
        $("#pay2").removeClass('act');
        $("#pay3").removeClass('act');
        $("#price1").show();
        $("#price2").hide();
        $("#price3").hide();
    });

    $("#pay2").click(function () {
        $(this).addClass('act');
        $("#pay1").removeClass('act');
        $("#pay3").removeClass('act');
        $("#price1").hide();
        $("#price3").hide();
        $("#price2").show();
    });
    $("#pay3").click(function () {
        $(this).addClass('act');
        $("#pay1").removeClass('act');
        $("#pay2").removeClass('act');
        $("#price1").hide();
        $("#price2").hide();
        $("#price3").show();
    });
});