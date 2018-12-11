/// <reference path="jquery-1.8.3.min.js" />

$(function () {
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

    /***********************************************************************/
    $("#teacherCommentSubmit").on("click", function () {
        var that = $(that);
        var teacherId1 = $("#teacherCommentSubmit").attr("data-val");
        var content1 = $("#txt_content").val();
        var leavel = $(star).attr("data-val");

        if (content1.length < 15) {
            $("#txtMsg").text("内容不能少于15个字");
            return;
        }
        $.ajax({
            url: "/lib/member/TeacherComment.ashx",
            type: "POST",
            data: { dml: 1, teacherid: teacherId1, content: content1, score: leavel },
            cache: false,
            success: function (data) {
                if (data == '') {
                    $("#txtMsg").text("添加失败！");
                } else {
                    $("#txt_content").val("");

                    var dataObj = eval("(" + data + ")");

                    $("#ul_teachercomment").empty();
                    $.each(dataObj, function (i, val) {
                        var li = document.createElement("li");
                        var div_portrait = document.createElement("div");
                        div_portrait.setAttribute("class", "portrait");
                        var img = document.createElement("img");
                        var imagePath = (val.MemberPhoto == null ? "/images/comments_03.png" : val.MemberPhoto)
                        img.src = "/" + imagePath;
                        img.setAttribute("onerror", "javascript:this.src='/images/comments_03.png';");
                        div_portrait.appendChild(img);
                        li.appendChild(div_portrait);
                        var h4 = document.createElement("h4");
                        h4.innerText = val.MemberName;
                        li.appendChild(h4);
                        var p = document.createElement("p");
                        p.innerText = val.CreateTime;
                        li.appendChild(p);
                        var div_xing = document.createElement("div");
                        div_xing.setAttribute("class", "xing");

                        var tag = val.Sorce;
                        for (var i = 0; i < parseInt(tag); i++) {

                            var img_xing = document.createElement("img");
                            img_xing.src = "/images/xing_01.png";
                            div_xing.appendChild(img_xing);

                        }

                        for (var i = 0; i < 5 - parseInt(tag); i++) {
                            img_xing.src = "/images/xing_01.png";
                            div_xing.appendChild(img_xing);
                        }

                        li.appendChild(div_xing);
                        var span = document.createElement("span");
                        span.innerText = val.Content;
                        li.appendChild(span);
                        document.getElementById("ul_teachercomment").appendChild(li);
                    });


                }
            }
        });

    });

    /*********************************************************/
    $('[data-val="btnChanageTeacherComment"]').on("click", function () {
        var that = $(this);
        var pageIndex1 = parseInt(that.attr("data-pageIndex"));
        var pageCount = parseInt(that.attr("data-pageCount"));
        var itemCount = parseInt(that.attr("data-itemCount"));
        var teacherId1 = parseInt(that.attr("data-teacherId"));
        if (pageIndex1 + 1 > pageCount) {
            pageIndex1 = 1;

        } else {
            pageIndex1 += 1;

        }
        $(this).attr("data-pageIndex", pageIndex1);
        $.ajax({
            url: "/lib/member/TeacherComment.ashx",
            type: "POST",
            data: { dml: 2, teacherid: teacherId1, pageindex: pageIndex1 },
            cache: false,
            success: function (data) {
                if (data != '') {

                    var dataObj = eval("(" + data + ")");

                    $("#ul_teachercomment").empty();
                    $.each(dataObj, function (i, val) {
                        var li = document.createElement("li");
                        var div_portrait = document.createElement("div");
                        div_portrait.setAttribute("class", "portrait");
                        var img = document.createElement("img");
                        var imagePath = (val.MemberPhoto == null ? "/images/comments_03.png" : val.MemberPhoto)
                        img.src = "/" + imagePath;
                        img.setAttribute("onerror", "javascript:this.src='/images/comments_03.png';");

                        div_portrait.appendChild(img);

                        li.appendChild(div_portrait);
                        var h4 = document.createElement("h4");
                        li.appendChild(h4);
                        var p = document.createElement("p");
                        p.innerText = val.CreateTime;
                        li.appendChild(p);
                        var div_xing = document.createElement("div");
                        div_xing.setAttribute("class", "xing");

                        var tag = val.Sorce;
                        for (var i = 0; i < parseInt(tag); i++) {
                            // strHtml += "<img src=\"/images/xing_01.png\">";

                            var img_xing = document.createElement("img");
                            img_xing.src = "/images/xing_01.png";
                            div_xing.appendChild(img_xing);

                        }

                        for (var i = 0; i < 5 - parseInt(tag); i++) {
                            img_xing.src = "/images/xing_01.png";
                            div_xing.appendChild(img_xing);
                        }
                        li.appendChild(div_xing);
                        var span = document.createElement("span");
                        span.innerText = val.Content;
                        li.appendChild(span);
                        document.getElementById("ul_teachercomment").appendChild(li);
                    });



                }
            }
        });
    });
    /*********************************************************/


});