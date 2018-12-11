$(function () {
    $(".wt li p a").on("click", function () {
        var qaid = $(this).attr('rel');
        $.ajax({
            url: "/lib/member/UserFaq.ashx",
            type: "POST",
            async: false,
            data: "type=1&qaid=" + qaid + "",
            cache: false,
            beforeSend: function () {

            },
            success: function (result) {
                if (result != "") {
                    var dataObj = eval("(" + result + ")");
                    $("#hidqaid").val(dataObj.QAID);
                    $("#qacontent").html(dataObj.CONTENT);
                    $("#nickname").html(dataObj.NICKNAME);
                    $("#creationtime").html(formatDate(dataObj.CREATIONTIME));
                    if (dataObj.MYPHOTO != "") {
                        $("#myphoto").attr("src", "/" + dataObj.MYPHOTO);
                    }
                }
            }

        })



        var a = new Dialog2("回答问题", "#details1", {
            "btnCancel": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });
});


$(".btnRqid").live("click", function () {
    var qaid = $("#hidqaid").val();
    var optype = $("#hidtype").val();
    var content = $("#txtNote2").val(); 
    $.ajax({
        url: "/lib/member/UserFaq.ashx",
        type: "POST",
        async: false,
        data: "type=2&qaid=" + qaid + "&content=" + content + "&optype=" + optype + "",
        beforeSend: function () {
            $("#loading1").html("数据提交中,请稍候...");
        },
        success: function (result) {
            if (result != "000") {
                window.location.reload();
            }
            else {
                var url = window.location.href;
                window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";
            }
        }

    })

});


$("#update").live("click", function () {
    var qaid = $(this).attr('rel');

    $.ajax({
        url: "/lib/member/UserFaq.ashx",
        type: "POST",
        async: false,
        data: "type=1&qaid=" + qaid + "",
        cache: false,
        beforeSend: function () {

        },
        success: function (result) {
            if (result != "") {
                var dataObj = eval("(" + result + ")");
                $("#hidqaid").val(dataObj.QAID);
                $("#hidtype").val("1");
                $("#txtNote2").html(dataObj.CONTENT);

                $.ajax({
                    url: "/lib/member/UserFaq.ashx",
                    type: "POST",
                    async: false,
                    data: "type=1&qaid=" + dataObj.REQAID + "",
                    cache: false,
                    beforeSend: function () {

                    },
                    success: function (data) {
                        if (data != "") {
                            var dataObj = eval("(" + data + ")");
                            $("#qacontent").html(dataObj.CONTENT);
                            $("#nickname").html(dataObj.NICKNAME);
                            $("#creationtime").html(formatDate(dataObj.CREATIONTIME));
                            if (dataObj.MYPHOTO != "") {
                                $("#myphoto").attr("src", "/" + dataObj.MYPHOTO);
                            }
                        }
                    }

                })
            }
        }

    })
    var a = new Dialog2("修改回答", "#details1", {
        "btnCancel": function () { a.CloseDialog(); }
    }, false);
    a.ShowDialog();
});


$("#delete").live("click", function () {
    var qaid = $(this).attr('rel');

    $.ajax({
        url: "/lib/member/UserFaq.ashx",
        type: "POST",
        async: false,
        data: "type=3&qaid=" + qaid + "",
        cache: false,
        beforeSend: function () {

        },
        success: function (result) {
            if (result != "000") {
                window.location.reload();
            }
            else {
                var url = window.location.href;
                window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

            }
        }

    })
});


function formatDate(now) {

    var obj = now;
    var date = eval(obj.replace(/\/Date\((\d+)\)\//gi, "new Date($1)"));

    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
}


$(function () {
    $(".btnApply a").on("click", function () {
        var price = $(this).attr('rel');
        var name = $(this).attr('title'); 
        $("#productname").html(name);
        $("#productprice").html("￥" + price);
        $("#hidorderno").val($(this).attr('class'));
        $("#hidproductid").val($(this).attr('coords'));
        $("#hidpayamount").val(price);
        var a = new Dialog2("申请返值", "#details1", {
            "btnCancel": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });
});

$(function () {
    $("#addCashback").click(function () {
        var productid = $("#hidproductid").val();
        var orderno = $("#hidorderno").val();
        var examno = $("#examno").val();
        var score = $("#score").val();
        var content = $("#content").val();
        var payamount = $("#hidpayamount").val();
        if (examno.length < 5) {
            alert("请输入正确的准考证号");
            return;
        }
        if (score == "") {
            alert("请输入您的成绩");
            return;
        }
        $.ajax({
            url: "/lib/member/UserFaq.ashx",
            type: "POST",
            data: "type=5&productid=" + productid + "&payamount=" + payamount + "&orderno=" + orderno + "&examno=" + examno + "&score=" + score + "&content=" + content + "",
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