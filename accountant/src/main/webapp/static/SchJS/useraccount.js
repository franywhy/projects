/*----------面值卡充值支付--------*/
$(function () {
    $("#btnSubmit").on("click", function () {

        var a = new Dialog2("面值卡充值", "#details1", {
            "btnClose": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });
});

$(function () {
    $("#addcoupon").click(function () {
        var code = $("#codeByMianzhi").val();
        var coupass = $("#coupassByMianzhi").val();
        $.ajax({
            url: "/lib/member/member.ashx",
            type: "POST",
            data: "type=5&coutype=1&code=" + code + "&coupass=" + coupass + "",
            beforeSend: function () {
                $("#loading_mianzhika").html("数据提交中...");
            },
            success: function (data) {
                if (data != "登录状态已失效或超时") {
                    $("#loading_mianzhika").html(data);
                }
                else {
                    var url = window.location.href;
                    window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

                }

            }

        })

    });

});