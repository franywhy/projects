
$(function () {
//    var nav_table = ("table");
//    var nav_div = ("[data-type='nav']");
//    $(nav_table).hide();
//    $(nav_div).hide();
//    $("[data-type='href']").on("hover", function () {
//        $(nav_table).hide(); $(nav_div).hide();
//        /****************切换TAB取消checkbox选择*************/
//        $("[data-val='checkbox']").removeClass("checked");
//        $("[data-val='checkbox']").addClass("unchecked");
//        $("[data-val='checkbox']").attr("data-checked", "false");
//        $("[data-val='checkbox']").attr("data-checked2", "false");
//        /****************切换TAB取消checkbox选择************/

//        $("[data-type='href']").parent().removeClass("action");
//        $(this).parent().addClass("action");
//        var tab = $(this).attr("data-val");
//        $("#" + tab).show();
//        $("[name='" + tab + "']").show();
//        getTotal($("#" + tab).attr("data-type2"));
//    });


//    $.each($("[data-type='href']"), function (i, val) {

//        if (i == 0) {
//            $(nav_table).hide();
//            $(nav_div).hide();
//            $("[data-type='href']").parent().removeClass("action");
//            $(val).parent().addClass("action");
//            var tab = $(val).attr("data-val");
//            $("#" + tab).show();
//            $("[name='" + tab + "']").show();
//        }


//    });



    $("[data-val='checkbox']").on("click", function () {
        if ($(this).hasClass("unchecked")) {
            $(this).removeClass("unchecked");
            $(this).addClass("checked");
            CheckChildItem(this);
            CheckFatherItem(this);
            UnCheckHasChildItem(this);

        } else {
            $(this).removeClass("checked");
            $(this).addClass("unchecked");
            UnCheckFatherItem(this);
            UnCheckChildItem(this);
        }

        getTotal($(this).attr("data-type"));

    });


    /*获取总数*/
    var getTotal = function (typeid) {

        var total = 0;
        productIds = '';
        productChildIds = '';
        $.each($("[data-val='checkbox']"), function (i, val) {
            //alert( $(val).attr("data-type") == typeid);
            if ($(val).attr("data-type") == typeid && $(val).attr("data-ispackage") == "false" && $(val).hasClass("checked")) {
                total += parseInt($(val).attr("data-price"));
                if (productIds == '') {
                    productIds = $.trim($(val).attr("data-productid").replace(',', ''));
                } else {
                    productIds += $.trim($(val).attr("data-productid").replace(',', ''));
                }
            }
        });

        $.each($("[data-val='checkbox']"), function (i, val) {
            //alert( $(val).attr("data-type") == typeid);
            if ($(val).attr("data-type") == typeid && $(val).attr("data-ispackage") == "true" && $(val).hasClass("checked")) {
                total += parseInt($(val).attr("data-price"));
                if (productChildIds == '') {
                    productChildIds = $.trim($(val).attr("data-productid").replace(',', ''));
                } else {
                    productChildIds += $.trim($(val).attr("data-productid").replace(',', ''));
                }
            }
        });


        $("#txtPrice").text(total);
        $("#txtPrice").css(
            "color", "red"
        );

        if (productIds != '') {
            $.ajax(
            {
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=7&productIds=" + productIds + "&productChildIds=" + productChildIds + "",
                beforeSend: function () {

                },
                success: function (data) {
                    var d = data.split("$$");
                    $("#txtPrice").html("" + d[0] + "");
                    $("#allPrice").val(d[0]);
                    $("#productChildIds").val(d[1]);
                }

            })
        }
        else {
            $("#productChildIds").val("");
        }
        //        alert("productIds" + productIds + "------------productChildIds" + productChildIds);
    }


    var CheckFatherItem = function (control) {
        var tmpDataType = $(control).attr("data-type");
        var tmpDataProductId = $(control).attr("data-productid");
        var currDataId;
        var currDataType;
        var tmpControl = null;
        $.each($("[data-val='checkbox']"), function (i, val) {
            currDataId = $(val).attr("data-id");
            currDataType = $(val).attr("data-type");
            if (currDataType == tmpDataType &&
             $(val).attr("data-productid") != tmpDataProductId
            && currDataId.indexOf(tmpDataProductId) != -1) {
                tmpControl = val;
                CheckFatherItem(this);
                return false;
            }
        });
        if (tmpControl != null) {

            var tmpDataId = $(tmpControl).attr("data-id");
            var tmpDataProductIds = tmpDataId.split(",");
            var res = 0;

            $.each($("[data-val='checkbox']"), function (i, val) {
                for (var i = 1; i < tmpDataProductIds.length - 1; i++) {

                    if ($.trim($(val).attr("data-productid")) == ("," + tmpDataProductIds[i] + ",") && $(val).hasClass("checked")) {
                        res += 1;
                    }
                }
            });

            if ((tmpDataProductIds.length - 2) == res) {

                $(tmpControl).removeClass("unchecked");
                $(tmpControl).addClass("checked");
                CheckFatherItem(tmpControl);
                UnCheckHasChildItem(tmpControl);

            }
        }

    }
    //取消含有子类的子类勾选
    var UnCheckHasChildItem = function (control) {

        var tmpDataType = $(control).attr("data-type");
        var tmpDataId = $(control).attr("data-id");
        var tmpDataProductIds = tmpDataId.split(",");
        if (tmpDataProductIds.length > 3) {
            $.each($("[data-val='checkbox']"), function (i, val) {
                if ($(val).attr("data-productid") != $(control).attr("data-productid")) {
                    var currDataId = $(val).attr("data-id");
                    var currDataProductIds = currDataId.split(",");
                    //  alert(currDataId);
                    for (var i = 1; i < tmpDataProductIds.length - 1; i++) {
                        if ($.trim($(val).attr("data-productid")) == ("," + tmpDataProductIds[i] + ",") && currDataProductIds.length >= 4) {
                            $(val).removeClass("checked");
                            $(val).addClass("unchecked");
                            UnCheckHasChildItem(this);

                        }
                    }

                }
            });
        }
    }

    var CheckChildItem = function (control) {
        var tmpDataType = $(control).attr("data-type");
        var tmpDataId = $(control).attr("data-id");
        var tmpDataProductIds = tmpDataId.split(",");
        if (tmpDataProductIds.length > 3) {
            $.each($("[data-val='checkbox']"), function (i, val) {
                if ($(val).attr("data-productid") != $(control).attr("data-productid")) {
                    for (var i = 1; i < tmpDataProductIds.length - 1; i++) {
                        if ($.trim($(val).attr("data-productid")) == ("," + tmpDataProductIds[i] + ",")) {
                            $(val).removeClass("unchecked");
                            $(val).addClass("checked");

                            CheckChildItem(this);
                            continue;
                        }
                    }
                }
            });
        }


    }
    //取消子类勾选
    var UnCheckChildItem = function (control) {
        var tmpDataType = $(control).attr("data-type");
        var tmpDataId = $(control).attr("data-id");
        var tmpDataProductIds = tmpDataId.split(",");
        if (tmpDataProductIds.length > 3) {
            $.each($("[data-val='checkbox']"), function (i, val) {
                if ($(val).attr("data-productid") != $(control).attr("data-productid")) {
                    for (var i = 1; i < tmpDataProductIds.length - 1; i++) {
                        if ($.trim($(val).attr("data-productid")) == ("," + tmpDataProductIds[i] + ",")) {
                            $(val).removeClass("checked");
                            $(val).addClass("unchecked"); $(val).parent().removeClass("delline");
                            UnCheckChildItem(this);
                            continue;
                        }
                    }
                }
            });
        }
    }
    //取消父类勾选
    var UnCheckFatherItem = function (control1) {
        var tmpDataType = $(control1).attr("data-type");
        var tmpDataProductId = $(control1).attr("data-productid");
        $.each($("[data-val='checkbox']"), function (i, val) {
            var currDataId = $(val).attr("data-id");
            var currDataType = $(val).attr("data-type");
            if (currDataType == tmpDataType &&
             $(val).attr("data-productid") != tmpDataProductId
            && currDataId.indexOf(tmpDataProductId) != -1) {
                $(val).removeClass("checked");
                $(val).addClass("unchecked"); $(val).parent().removeClass("delline");
                UnCheckFatherItem(this);
            }
        });

    }

});

$(function () {
    $("#btnPayOK").click(function () {//确认，去交费
        var allPrice = $("#allPrice").val();
        var productChildIds = $("#productChildIds").val();
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