$(function () {
    $(".fb-yes-btn").click(function () {
        var mobile = $("#mobile").val();
        var name = $("#name").val();
        var memo = $("#memo").val();
        if (mobile != "" && mobile != "") {
            $.ajax({
                url: "/lib/registHandler.ashx",
                type: "POST",
                data: "type=1&name=" + name + "&mobile=" + mobile + "&memo=" + memo + "",
                beforeSend: function () {

                },
                success: function (data) {
                    alert(data);
                    $(".fb-no-btn").trigger("click");
                }

            })
        }
    });

    $(".parti a").click(function () {
        var mobile = $("#mobile").val();
        var name = $("#name").val();
        var classname = $("#hidvalue").val();
        var memo = $(this).attr('rel');
        if (classname == "") {
            return;
        }
        if (mobile != "") {
		alert("亲，报名已经结束，敬请期待直播");//报名时间已经到，返回
		return;
            $.ajax({
                url: "/lib/registHandler.ashx",
                type: "POST",
                data: "type=1&name=" + name + "&mobile=" + mobile + "&classname=" + classname + "&memo=" + classname + "",
                beforeSend: function () {
                    $("#loading").html("数据提交中");
                },
                success: function (data) {
                    alert(data);
                    $("#loading").html("");
                }

            })
        }
    });

    $("#radio1").click(function () {
        $("#hidvalue").val("已报考初级职称");
    });
    $("#radio2").click(function () {
        $("#hidvalue").val("未报考初级");
    });

    $("#subTydc").click(function () {
        var mobile = $("#mobile").val();
        var name = $("#name").val();
        var classname = "";
        var memo = $("#memo").val();
        if (mobile != "" || name != "") {
            $.ajax({
                url: "/lib/registHandler.ashx",
                type: "POST",
                data: "type=1&name=" + name + "&mobile=" + mobile + "&classname=" + classname + "&memo=" + memo + "",
                beforeSend: function () {

                },
                success: function (data) {
                    alert(data);

                }

            })
        }
    });

});