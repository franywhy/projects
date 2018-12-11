/// <reference path="jquery-1.8.3.min.js" />
;$(function () {
    //获取被选中的星星
    var star = null;
    var InitStart = function (arg) {
        $("[name='CommunionImage']").attr("src", "images/xing_02.png");
        $("[name='CommunionImage']").attr("data-checked", "false");
        var val = $("#CommunionImage" + arg).attr("data-val");
        for (var i = 0; i < parseInt(val); i++) { $("#CommunionImage" + (i + 1)).attr("src", "images/xing_01.png"); }
        $("#CommunionImage" + arg).attr("data-checked", "true");
        star = $("#CommunionImage" + arg);
    }
    //选择星星
    $("[name='CommunionImage']").on("mouseover", function () {

        InitStart($(this).attr("data-val"));
    });
    //初始化星星为5颗星
    InitStart(5);

    //提交事件
    $("#btnSubmit").on("click", function () {
        var score_1 = $(star).attr("data-val");
        var msg_1 = $("#txtCommunion").val().toString();
        var pid_1 = $(this).attr("value");
        $("txtMsg").css("color", "red");
        if (msg_1 == null || msg_1 == undefined || $.trim(msg_1) == "" || $.trim(msg_1).length < 15) {
            $("#txtMsg").text("内容不能少于15个字");
            return;
        }
        //  debugger;
        $.ajax({
            type: "POST",
            url: "lib/product/communion.aspx",
            data: { score: score_1, msg: msg_1, pid: pid_1 },
            dataType: "text",
            success: function (data) {
                if (data.indexOf("成功") != -1) {
                    location.href = "course_" + pid_1 + ".html";
                } else {
                    alert("提交失败"); return false;
                }
            }
        });
    });

    //换一组
    $("#btnChangeComment").on("click", function () {

        var that = $(this);
        var pid_1 = that.attr("data-pid");
        var pageIndex = that.attr("data-pageindex");
        var pageSize = that.attr("data-pagesize");
        var pageCount = that.attr("data-pagecount");
        var typeid = $("#hidtypeid").val();
        if (parseInt(pageIndex) + 1 > parseInt(pageCount)) {
            that.attr("data-pageindex", 1);
          //  alert("count<page");
        } else {
            that.attr("data-pageindex", (parseInt(pageIndex) + 1));
            //alert("count>page");
        }
        $.ajax({
            type: "GET",
            url: "/lib/product/communion.aspx",
            data: { ctype: 'select', pid: pid_1, pageindex: that.attr("data-pageindex"), size: pageSize, typeid: typeid },
            dataType: "text",
            success: function (data) {

                if (data != null && data != "" && data.toString() != "undefined") {

                    var tmp = eval("(" + data + ")");
                    $($("#lstComment").find("li")).remove();
                    var ul = $("#lstComment");
                    var li = "";
                    $.each(tmp, function (i, val) {

                        li += " <li data-val=" + val["C_Id"] + " data-type=\"commentType\">";
                        li += "<div class=\"portrait\">";
                        li += "<img src=\"" + val["M_ImageUri"] + "\" onerror=\"this.src='/images/default_avatar_28.png'\"></div>";
                        li += "<h4>";
                        li += ((val["M_Name"] == null || val["M_Name"].toString() == null) ? "" : val["M_Name"]) + "</h4>";
                        li += "<p>";
                        li += val["C_Time"] + "</p>";
                        li += "<div class=\"xing\">";

                        for (var i = 0; i < parseInt(val["C_Sorce"]); i++) {
                            li += "<img style='margin-right:2px;padding-top: 6px;' src='/images/xing_01.png'/>";
                        }

                        for (var i = 0; i < 5 - parseInt(val["C_Sorce"]); i++) {
                            li += "<img style='margin-right:2px;padding-top: 6px;' src='/images/xing_02.png'/>";
                        }
                        li += "</div>"
                        li += "<span>";
                        li += "  <a href='/" + val["Url"] + "' title='" + val["P_Name"] + "'  target='_blank'>";
                        li += val["C_Content"] + "</a></span></li>";
                    });

                    ul.html(li);
                }
            }
        });
    });

});