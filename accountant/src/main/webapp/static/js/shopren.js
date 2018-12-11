$(function () {
    $("#tselectAll").click(function () { //上面全选
        allChk();
    });

    $(".oper_Af_L :checkbox").click(function () { //购物车选择  
        var chknum = $(".oper_Af_L :checkbox").size(); //选项总个数 
        var chk = 0;
        $(".oper_Af_L :checkbox").each(function () {
            if ($(this).attr("checked")) {
                chk++;
            }
        });
        if (chknum == chk) { //全选 
            $("#tselectAll").attr("checked", true);
            $("#bselectAll").attr("checked", true);
        } else if (chk == 0) { //全不选 
            $("#tselectAll").attr("checked", false);
            $("#bselectAll").attr("checked", false);
        }
        if (chk > 0) {
            $("#isselect").show();
            $("#unselect").hide();
        }
        else {
            $("#isselect").hide();
            $("#unselect").show();
        }
        getValue();
    });

    $("#bselectAll").click(function () { //获取选中选项的值
        allChk();
    });
});
function allChk() {
    var chknum = $(".oper_Af_L :checkbox").size(); //选项总个数 
    var chk = 0;
    $(".oper_Af_L :checkbox").each(function () {
        if ($(this).attr("checked")) {
            chk++;
        }
    });
    if (chknum == chk) { //全不选 
        $(".oper_Af_L :checkbox").attr("checked", false);
        $("#tselectAll").attr("checked", false);
        $("#bselectAll").attr("checked", false);
        $("#isselect").hide();
        $("#unselect").show();
    } else { //全选 
        $(".oper_Af_L :checkbox").attr("checked", true);
        $("#tselectAll").attr("checked", true);
        $("#bselectAll").attr("checked", true);
        $("#isselect").show();
        $("#unselect").hide();
    }
    getValue();
}
function getValue() {
    var count = 0;
    var money = 0;
    var point = 0;
    $(".oper_Af_L :checkbox").each(function () {
        if ($(this).attr("checked")) {
            var id = $(this).val();
            var pid = $(this).attr('title');
            $.ajax({
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=4&id=" + id + "&pid=" + pid + "",
                beforeSend: function () {

                },
                success: function (data) {
                    count++;
                    var d = data.split("$$");
                    money += new Number(d[0]);
                    point += new Number(d[1]);
                }

            })
        }
        else {
            var id = $(this).val();
            $.ajax({
                url: "/lib/product/ProductDetail.ashx",
                type: "POST",
                async: false,
                data: "type=5&id=" + id + "",
                beforeSend: function () {

                },
                success: function (data) { }

            })
        }
    });
    $("#CartCount").html(count);
    $("#AllSaleCount").html("￥" + money + "元");
    $("#AllSaleCount1").html("￥" + money + "元");
    $("#AllPoints").html("" + point + "分");
}
/*------------------------删除购物车---------------*/
$(".oper_Af_R a").live("click", function () {
    var id = $(this).attr('rel');
    $.ajax({
        url: "/lib/product/ProductDetail.ashx",
        type: "POST",
        data: "type=3&id=" + id + "",
        beforeSend: function () {

        },
        success: function (data) {
            window.location.href = "/shopping.html";
        }
    })

});

/*------------------------提交订单---------------*/
$(function () {
    $("#lbSubmit").click(function () {
        var check = "0";
        $(":checkbox[name='ckcashback'][checked]").each(function () {
            check += "," + $(this).val();
        });
        $.ajax({
            url: "/lib/product/ProductDetail.ashx",
            type: "POST",
            data: "type=6&check=" + check + "",
            beforeSend: function () {
                $("#loading").html("订单提交中,请稍后...");
            },
            success: function (data) {
                if (data != "000") {
                    if (data == "111") {
                        window.location.href = "personCenter.html";
                    }
                    else {
                        window.location.href = "pay.html?no=" + data;
                    }
                }
                else {
                    $("#loading").empty();
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";
                }
            }
        })

    });

});

/*------------------------购物车切换---------------*/
$(function () {
    var $tabTitleLi = $('.shop_top ul li');
    var $shop_bottom = $('.shop_bottom .box');

    $tabTitleLi.click(function () {
        var _index = $(this).index();
        $(this).addClass('active').siblings('li').removeClass('active');
        $shop_bottom.hide().eq(_index).show();
    });
});

/*------------------------------取消订单-------------------*/
$(function () {
    $(".cancelOrder a").click(function () {
        var orderno = $(this).attr('rel');

        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=9&orderno=" + orderno + "",
            beforeSend: function () {

            },
            success: function (data) {
                if (data == "订单取消成功") {
                    window.location.reload();
                }
                else {
                    alert(data);
                }

            }

        })

    });

});
var sub = false;
/*------------------------------提交支付-------------------*/
$(function () {
    $("#submitPay").click(function () {
        if (sub === true) {
            return;
        }
        sub = true;
        var cartno = $("#hiddeCartNo").val();
        var coupon = $("#hidCoupon").val();
        var saleid = $("#hiddensaleid").val();
        var check = document.getElementById("cb_Check").checked;
        var type = "0";
        var bankid = "0";
        if (check == true) {
            type = "1";
        }
        else {
            type = "2";
            bankid = $('input[name="radio"]:checked').val();
        }

        if (typeof bankid == 'undefined') {
            type = "-1";
        }
        $.ajax({
            url: "/lib/product/PayHandler.ashx",
            type: "POST",
//            async: false,
            data: "cartno=" + cartno + "&type=" + type + "&bankid=" + bankid + "&coupon=" + coupon + "&saleid=" + saleid + "",
            beforeSend: function () {
                $("#submitPay").hide();
                $("#submitPay2").show();
            },
            success: function (data) {
                if (data != "") {
                    if (data != "000") {
                        if (data == "订单支付成功") {
                            window.location.href = "/UserManage/personCenter.html";
                        }
                        else {
                            var tmpForm = $(data);
                            tmpForm.appendTo(document.body).submit();
                            //                        $(data).submit();
                        }
                    }
                    else {
                        var url = window.location.href;
                        window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                    }
                }
                else {
                    sub = false;
                    $("#submitPay2").hide();
                    $("#submitPay").show();
                }
            }

        })

    });

});

/*------------------------------添加地址-------------------*/
$(function () {
    $("#btnSubmit").on("click", function () {

        var a = new Dialog2("新增收货地址", "#details1", {
            "btnClose": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });
});


$(function () {
    $("#addAdress").click(function () {
        var name = $("#name").val();
        var mobile = $("#mobile").val();
        var addresses = $("#provincetext").val() + $("#citytext").val() + $("#addresses").val();
        var shippingregion = $("#provincevalue").val() + "," + $("#cityvalue").val() + "," + $("#provincetext").val() + "," + $("#citytext").val();
        $.ajax({
            url: "/lib/member/MemberAddress.ashx",
            type: "POST",
            data: "type=1&name=" + name + "&mobile=" + mobile + "&addresses=" + addresses + "&shippingregion=" + shippingregion + "",
            beforeSend: function () {
                $("#loading1").html("数据提交中,请稍候...");
            },
            success: function (data) {
                if (data != "000") {
                    window.location.reload();
                }
                else {
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                }

            }

        })

    });

});

$(function () {
    $(".li1 a").click(function () {
        var addressid = $(this).attr('rel');
        $.ajax({
            url: "/lib/member/MemberAddress.ashx",
            type: "POST",
            data: "type=2&addressid=" + addressid + "",
            beforeSend: function () {

            },
            success: function (data) {
                if (data != "000") {
                    window.location.reload();
                }
                else {
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                }

            }

        })

    });

});

/*----------面值卡充值支付--------*/
$(function () {
    $("#addcoupon").click(function () {
        var code = $("#code").val();
        var coupass = $("#coupass").val();
        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=5&coutype=1&code=" + code + "&coupass=" + coupass + "",
            beforeSend: function () {
                $("#msgView").html("数据提交中...");
            },
            success: function (data) {
                if (data != "登录状态已失效或超时") {
                    $("#msgView").html(data);
                    var orderNo = $("#hiddeCartNo").val();

                    $.ajax({
                        url: "/lib/member/member.ashx",
                        type: "POST",
                        async: false,
                        data: "type=6&orderNo=" + orderNo + "",
                        beforeSend: function () {

                        },
                        success: function (result) {
                            var d = result.split("$$");
                            $("#myaccountCash").html(d[0]);
                            $("#mypayCash").html(d[1]);
                            $("#hidPay").val(d[1]);
                        }

                    })

                }
                else {
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                }

            }

        })

    });

});

/*----------优惠码--------*/
$(function () {
    $("#addprefer").click(function () {
        var code = $("#prefercode").val();
        var orderid = $("#hiddeOrderId").val();
        var orderno = $("#hiddeCartNo").val();
        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=11&coutype=1&code=" + code + "&orderid=" + orderid + "&orderno=" + orderno + "",
            beforeSend: function () {

            },
            success: function (result) {
                if (result != "登录状态已失效或超时") {
                    if (result != "优惠码不正确") {
                        var d = result.split("$$");
                        $("#myaccountCash").html(d[0]);
                        $("#mypayCash").html(d[1]);
                        $("#hiddensaleid").val(d[2]);
                        $("#addprefermsg").html("");
                        $("#chAccountCash").attr("checked", false);
//                        $("#chAccountCash").trigger("click");
                        $("#chAccountCash").hide();
                        $(".preferening").hide();
                    }
                    else {
                        $("#addprefermsg").html(result);
                    }
                }
                else {
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                }

            }

        })

    });

});

/*----------积分充值支付--------*/
$(function () {
    $("#usePoint").click(function () {
        var point = $("#txtPoint").val();
        if (point == "") {
            $("#msgView1").html("");
            return;
        }
        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=7&point=" + point + "",
            beforeSend: function () {
                $("#msgView1").html("数据提交中...");
            },
            success: function (data) {
                if (data != "登录状态已失效或超时") {
                    var d = data.split("$$");
                    $("#msgView1").html(d[0]);
                    $("#myaccountPoint").html(d[1]);
                    var orderNo = $("#hiddeCartNo").val();

                    $.ajax({
                        url: "/lib/member/member.ashx",
                        type: "POST",
                        async: false,
                        data: "type=6&orderNo=" + orderNo + "",
                        beforeSend: function () {

                        },
                        success: function (result) {
                            var r = result.split("$$");
                            $("#myaccountCash").html(r[0]);
                            $("#mypayCash").html(r[1]);
                            $("#hidPay").val(r[1]);
                        }

                    })

                }
                else {
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                }

            }

        })

    });

});

/*----------订单--------*/

$(function () {
    $(".balance :checkbox").click(function () { //选择账户余额 
        var orderNo = $("#hiddeCartNo").val();
        if ($(this).attr("checked")) {
            $.ajax({
                url: "/lib/member/member.ashx",
                type: "POST",
                async: false,
                data: "type=6&orderNo=" + orderNo + "",
                beforeSend: function () {

                },
                success: function (result) {
                    var r = result.split("$$");
                    $("#myaccountCash").html(r[0]);
                    $("#mypayCash").html(r[1]);
                    $("#hidPay").val(r[1]);
                }

            })
        }
        else {

            $.ajax({
                url: "/lib/member/member.ashx",
                type: "POST",
                async: false,
                data: "type=8&orderNo=" + orderNo + "",
                beforeSend: function () {

                },
                success: function (result) {
                    $("#mypayCash").html(result);
                    $("#hidPay").val(result);
                }

            })
        }
    });
});

/*----------选择优惠劵--------*/

$(function () {
    var allBox = $(".use_card :checkbox");
    allBox.click(function () {
        if ($(this).attr("checked")) {
            allBox.removeAttr("checked");
            $(this).attr("checked", "checked");
            $("#hidCoupon").val($(":checkbox:checked").val());

        }
        else {
            allBox.removeAttr("checked");
            $("#hidCoupon").val("0");
        }
    });
});


/*------------------------------激活优惠劵-------------------*/
$(function () {
    $("#addmycoupon").on("click", function () {

        var a = new Dialog2("激活新的优惠劵", "#details1", {
            "btnClose": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });
});

$(function () {
    $("#addLearnCourse").click(function () {
        var code = $("#mycode").val();
        var coupass = $("#mycoupass").val();
        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=50&code=" + code + "&coupass=" + coupass + "&coutype=3",
            beforeSend: function () {
                $("#loading1").html("数据提交中,请稍候...");
            },
            success: function (data) {

                if (data == "优惠劵已激活") {
                    window.location.reload();
                }
                else {
                    $("#loading1").html(data);
                }

            }

        })

    });

});

/*----------订单--------*/

function pupopen() {
    document.getElementById("bg").style.display = "block";
    document.getElementById("details").style.display = "block";

}

function pupclose() {
    document.getElementById("bg").style.display = "none";
    document.getElementById("details").style.display = "none";

}
/*------------订单展开-------------*/
$(document).ready(function () {
    $(".ding").click(function () {
        $(".ordeils").slideToggle();
    });
});
try {
    $.setupJMPopups({
        screenLockerBackground: "#003366",
        screenLockerOpacity: "0.5"
    });
} catch (e) { }
function openStaticPopup() {
    $.openPopupLayer({
        name: "myStaticPopup",
        width: 1000,
        target: "myHiddenDiv1"
    });
}