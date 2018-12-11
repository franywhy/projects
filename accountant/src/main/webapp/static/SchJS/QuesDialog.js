/// <reference path="jquery-1.8.3.min.js" />
/// <reference path="Dialog.js" />
$(function () {

    $("[data-type='que_showdialog']").on("click", function () {
        if ($(this).attr("data-courseid") != undefined) { $("#btnAppendQue").attr("data-courseid", $(this).attr("data-courseid")); }
        if ($(this).attr("data-chapterid") != undefined) { $("#btnAppendQue").attr("data-chapterid", $(this).attr("data-chapterid")); }
        var QueId = $(this).attr("data-val").toString();
        $("#lstQues").empty();
        $.ajax({
            url: "/lib/member/Question.ashx",
            type: "POST",
            cache: false,
            data: { dml: "3", qaid: QueId },
            success: function (data) {
                if (data != '') {
                    InsertData($("#lstQues"), data);
                }
            }
        });

        var InsertData = function (div, data) {
            $(div).empty();
            if (data != '') {
                var ObjData = eval("(" + data + ")");
            }
            var strHtml = "";

            var i = 1;
            $.each(ObjData, function (index, val) {
                if (index % 2 == 0) {
                    strHtml += "<div class='nr_Af'>";
                    strHtml += "<div class='head'>";
                    strHtml += "<a href='#'>";
                    strHtml += "<img src='" + val.ImageUri + "'></a>";
                    strHtml += "</div>";
                    strHtml += "<div class='SJ'>";
                    strHtml += "<div class='name'>";
                    strHtml += "<a href='#'>" + val.MemberName + "</a></div>";
                    strHtml += "<span>" + val.CreationTime + "</span><p>";
                    strHtml += "58.254.92.*</p>";
                    strHtml += "<font>" + i + "#</font></div>";
                    strHtml += "<span class='pl'>" + val.Content + "</span>";
                    strHtml += "</div>";
                    i += 1;
                } else {
                    strHtml += "<dl class='ting'>";
                    strHtml += "<dt>" + val.MemberName + "老师回复：<span>" + val.CreationTime + "</span></dt>";
                    strHtml += "<dd>" + val.Content + "</div>";
                    strHtml += "</dl>";

                }
            });
            $(div).append(strHtml);
        }

        Dialog("课程提问", $("#innerDiv"), {
            "btnAppendQue": function () {
                if ($(".nr_Af").length == $(".ting").length) {
                    $.ajax({
                        url: "/lib/member/Question.ashx",
                        type: "POST",
                        cache: false,
                        data: { dml: "5", qaid: QueId, content: $("#txtNote2").val(), chapterid: $(this).attr("data-chapterid"), courseid: $(this).attr("data-courseid") },
                        success: function (data) {
                            if (data != '') {
                                InsertData($("#lstQues"), data);
                            }
                        }
                    });
                } else {
                    $("#msgInfo").text("您的问题还没有老师回答，不能继续追问！");
                }
            },
            "btnFavoritesQue": function () {

            }

        });

    });

});
