//ajax加载数据
var ajaxLoadFunc = {
    //加载面值卡记录
    mianzhika: function (pn) {
        var $loading = waitDataBefore($('#mianzhikahtml'));
        $.ajax({
            url: '/lib/member/PersonCenterHandler.ashx?action=LoadMianzhika',
            data: { page: pn },
            dataType: 'json',
            success: function (data) {
                var html = '';
                if (data.PageCount > 0) {
                    $.each(data.List, function (idx, row) {
                        html += '<dd class="clearfix">';
                        html += '    <p class="t-p-1">' + row.CODE + '</p>';
                        html += '    <p class="t-p-2">' + row.AMOUNT + '元</p>';
                        html += '    <p class="t-p-3">' + (row.DR == 1 ? "成功" : "未使用") + '</p>';
                        html += '    <p class="t-p-4">' + row.USETIME + '</p>';
                        html += '</dd>';
                    });
                    $('#mianzhikahtml').html(html);
                    $('#mianzhikapager').html(getPagerHtml2(data.PageIndex, data.PageSize, data.PageCount, 'javascript:ajaxLoadFunc.mianzhika({0})'));
                }
                $loading.remove();
            },
            error: function () {
                $loading.remove();
            }
        });
    },
    //加载课程卡记录
    kechengka: function (pn) {
        var $loading = waitDataBefore($('#kechengkahtml'));
        $.ajax({
            url: '/lib/member/PersonCenterHandler.ashx?action=LoadKechengka',
            data: { page: pn },
            dataType: 'json',
            success: function (data) {
                var html = '';
                if (data.PageCount > 0) {
                    $.each(data.List, function (idx, row) {
                        html += '<dd class="clearfix">';
                        html += '    <p class="t-p-1">' + row.CODE + '</p>';
                        html += '    <p class="t-p-2">' + row.AMOUNT + '元</p>';
                        html += '    <p class="t-p-3" title="' + row.NAME + '">' + row.NAME + '</p>';
                        html += '    <p class="t-p-4">' + row.USETIME + '</p>';
                        html += '</dd>';
                    });
                    $('#kechengkahtml').html(html);
                    $('#kechengkapager').html(getPagerHtml2(data.PageIndex, data.PageSize, data.PageCount, 'javascript:ajaxLoadFunc.kechengka({0})'));
                }
                $loading.remove();
            },
            error: function () {
                $loading.remove();
            }
        });
    },
    //加载金额明细
    jine: function (pn) {
        var $loading = waitDataBefore($('#jinehtml'));
        $.ajax({
            url: '/lib/member/PersonCenterHandler.ashx?action=LoadJinE',
            data: { page: pn },
            dataType: 'json',
            success: function (data) {
                var html = '';
                if (data.PageCount > 0) {
                    $.each(data.List, function (idx, row) {
                        html += '<dd class="clearfix">';
                        html += '    <p class="t-p-1">' + row.CREATIONTIME + '</p>';
                        html += '    <p class="t-p-2">' + row.INCOME + '元</p>';
                        html += '    <p class="t-p-3">' + row.OUTLAY + '元</p>';
                        html += '    <p class="t-p-4">' + row.BALANCE.toFixed(2) + '元</p>';
                        html += '    <p class="t-p-5">' + row.MEMO + '</p>';
                        html += '</dd>';
                    });
                    $('#jinehtml').html(html);
                    $('#jinepager').html(getPagerHtml2(data.PageIndex, data.PageSize, data.PageCount, 'javascript:ajaxLoadFunc.jine({0})'));
                }
                $loading.remove();
            },
            error: function () {
                $loading.remove();
            }
        });
    },
    //加载返现明细
    fanxian: function (pn, callback) {
        var $loading = waitDataBefore($('#fanxianhtml'));
        $.ajax({
            url: '/lib/member/PersonCenterHandler.ashx?action=LoadFanXian',
            data: { page: pn },
            dataType: 'json',
            success: function (data) {
                var html = '';
                if (data.PageCount > 0) {
                    $.each(data.List, function (idx, row) {
                        html += '<dd class="clearfix">';
                        html += '    <p class="t-p-1" title="' + row.productname + '">' + row.productname + '</p>';
                        html += '    <p class="t-p-2">' + row.orderno + '</p>';
                        html += '    <p class="t-p-3">' + row.product_price + '元</p>';
                        html += '    <p class="t-p-4">' + row.creationtime.Value + '</p>';
                        html += '    <p class="t-p-5">';
                        html += '       <font class="btnApply" data-productid="' + row.productid + '" data-orderno="' + row.orderno + '" data-payamount="' + row.product_price + '" data-productname="' + row.productname + '">';
                        html += '           <a class="main-btn-g" href="javascript:;" title="' + row.productname + '">申请返值</a>';
                        html += '       </font>';
                        html += '       <font class="viewxieyi" data-orderno="' + row.orderno + '">';
                        html += '           <a style="color:#5FB2F3" href="javascript:;">查看协议</a>';
                        html += '       </font>';
                        html += '    </p>';
                        html += '</dd>';
                    });
                    $('#fanxianhtml').html(html);
                    $('#fanxianpager').html(getPagerHtml2(data.PageIndex, data.PageSize, data.PageCount, 'javascript:ajaxLoadFunc.fanxian({0})'));
                }
                if (callback) {
                    callback();
                }
                $loading.remove();
            },
            error: function () {
                $loading.remove();
            }
        });
    },
    //加载积分明细
    jifen: function (pn) {
        var $loading = waitDataBefore($('#jifenhtml'));
        $.ajax({
            url: '/lib/member/PersonCenterHandler.ashx?action=LoadJifen',
            data: { page: pn },
            dataType: 'json',
            success: function (data) {
                var html = '';
                if (data.PageCount > 0) {
                    $.each(data.List, function (idx, row) {
                        html += '<dd class="clearfix">';
                        html += '    <p class="t-p-1">' + row.CREATIONTIME + '</p>';
                        html += '    <p class="t-p-2">' + row.POINTS + '</p>';
                        html += '    <p class="t-p-3">' + row.BALANCE + '</p>';
                        html += '    <p class="t-p-4">' + row.NOTE + '</p>';
                        html += '</dd>';
                    });
                    $('#jifenhtml').html(html);
                    $('#jifenpager').html(getPagerHtml2(data.PageIndex, data.PageSize, data.PageCount, 'javascript:ajaxLoadFunc.jifen({0})'));
                }
                $loading.remove();
            },
            error: function () {
                $loading.remove();
            }
        });
    },
    //加载优惠券明细
    youhui: function (pn) {
        var $loading = waitDataBefore($('#youhuihtml'));
        $.ajax({
            url: '/lib/member/PersonCenterHandler.ashx?action=LoadYouHui',
            data: { page: pn },
            dataType: 'json',
            success: function (data) {
                var html = '';
                if (data.PageCount > 0) {
                    $.each(data.List, function (idx, row) {
                        html += '<dd class="clearfix">';
                        html += '    <p class="t-p-1">' + row.CODE + '</p>';
                        html += '    <p class="t-p-2">' + row.NAME + '</p>';
                        html += '    <p class="t-p-3">' + row.VALIDITY + '</p>';
                        html += '    <p class="t-p-4">' + (row.ISUSE == true ? "已使用" : "未使用") + '</p>';
                        html += '    <p class="t-p-5">' + row.NOTE + '</p>';
                        html += '</dd>';
                    });
                    $('#youhuihtml').html(html);
                    $('#youhuipager').html(getPagerHtml2(data.PageIndex, data.PageSize, data.PageCount, 'javascript:ajaxLoadFunc.youhui({0})'));
                }
                $loading.remove();
            },
            error: function () {
                $loading.remove();
            }
        });
    }
};

$(function () {
    //绑定面值卡的加载
    $('.sc-main-box-l .t-li-2, .user-data-r a[data-flag="mianzhika"], .main-bar-nav li[data-flag="mianzhika"]').click(function () {
        if (!$('#mianzhikahtml').attr('isLoaded')) {
            ajaxLoadFunc.mianzhika(1);
            $('#mianzhikahtml').attr('isLoaded', true);
        }
    });
    //绑定课程卡的加载
    $('.user-data-r a[data-flag="kechengka"], .main-bar-nav li[data-flag="kechengka"]').click(function () {
        if (!$('#kechengkahtml').attr('isLoaded')) {
            ajaxLoadFunc.kechengka(1);
            $('#kechengkahtml').attr('isLoaded', true);
        }
    });
    //绑定金额明细的加载
    $('.user-data-r a[data-flag="jine"], .main-bar-nav li[data-flag="jine"]').click(function () {
        if (!$('#jinehtml').attr('isLoaded')) {
            ajaxLoadFunc.jine(1);
            $('#jinehtml').attr('isLoaded', true);
        }
    });
    //绑定返现明细的加载
    $('.user-data-r a[data-flag="fanxian"], .main-bar-nav li[data-flag="fanxian"]').click(function () {
        if (!$('#fanxianhtml').attr('isLoaded')) {
            ajaxLoadFunc.fanxian(1, function () {
                bindApplyFanzhi();
                bindViewXieyi();
            });
            $('#fanxianhtml').attr('isLoaded', true);
        }
    });
    //绑定积分明细的加载
    $('.user-data-r a[data-flag="jifen"], .main-bar-nav li[data-flag="jifen"]').click(function () {
        if (!$('#jifenhtml').attr('isLoaded')) {
            ajaxLoadFunc.jifen(1);
            $('#jifenhtml').attr('isLoaded', true);
        }
    });
    //绑定优惠券明细的加载
    $('.user-data-r a[data-flag="youhui"], .main-bar-nav li[data-flag="youhui"]').click(function () {
        if (!$('#youhuihtml').attr('isLoaded')) {
            ajaxLoadFunc.youhui(1);
            $('#youhuihtml').attr('isLoaded', true);
        }
    });
    //绑定申请返值的弹出层
    function bindApplyFanzhi() {
        var data = {};
        //申请返值 - 弹出层
        $('.btnApply').click(function () {
            data = {
                productid: $(this).attr('data-productid'),
                orderno: $(this).attr('data-orderno'),
                payamount: $(this).attr('data-payamount'),
                productname: $(this).attr('data-productname')
            };
            $(".pop-3").find("#productname").text(data.productname);
            $(".pop-3").find("#productprice").text('￥' + data.payamount);
            $(".pop-3").stop(true, true).fadeIn();
        });
        //申请返值 - 提交
        $("#addCashback").click(function () {
            var $pop3 = $(".pop-3");
            data.examno = $.trim($pop3.find("#examno").val());
            data.score = $.trim($pop3.find("#score").val());
            data.content = $.trim($pop3.find("#content").val());
            data.content = $.trim($pop3.find("#content").val());

            if (data.examno.length < 5) {
                alert("请输入正确的准考证号");
                return;
            }
            if (data.score == "") {
                alert("请输入您的成绩");
                return;
            }

            $.ajax({
                url: "/lib/member/UserFaq.ashx",
                type: "POST",
                data: "type=5&productid=" + data.productid + "&payamount=" + data.payamount + "&orderno=" + data.orderno + "&examno=" + data.examno + "&score=" + data.score + "&content=" + data.content + "",
                beforeSend: function () {
                    $pop3.find("#loading1").html("数据提交中,请稍候...");
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
    };
    //绑定查看协议的弹出层
    function bindViewXieyi() {
        var data = {};
        //查看协议 - 弹出层
        $('.viewxieyi').click(function () {
            var $pop4 = $(".pop-4");
            var orderno = $(this).attr('data-orderno');

            $.ajax({
                url: "/lib/member/UserFaq.ashx",
                type: "POST",
                async: false,
                data: "type=6&orderno=" + orderno + "",
                beforeSend: function () {

                },
                success: function (data) {
                    if (data != "") {
                        var dataObj = eval("(" + data + ")");
                        $pop4.find("#h2").html(dataObj.title);
                        $pop4.find("#agreement_cont").html(dataObj.content);
                        $pop4.find("#myname").html(dataObj.myname);
                        $pop4.find("#myidcard").html(dataObj.myidcard);
                        $pop4.find("#myaddress").html(dataObj.myaddress);
                        $pop4.find("#mypost").html(dataObj.mypost);
                        $pop4.find("#mymobile").html(dataObj.mymobile);
                    }
                    $(".pop-4").stop(true, true).fadeIn();
                }
            })
        });
    };

});