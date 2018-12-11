/// <reference path="jquery-1.8.3.min.js" />
//侧边栏的缩进


$(function () {

    var w_menu = 0;
    var w_widows = $(window).width();
    var w_menu = $("#panel_nr").width();
    var tmp_width = w_menu;

    $("#btnHidden").on("click", function () {
        var vid = "cc_" + $("#h_vid").val();
      
        var isShow = $("#panel_nr").is(":visible");
        if (isShow) {
            $("#panel_nr").hide();
            $("#panel_nr").css("width", 0);
            $(this).css("right", 0);
            tmp_width = 0;
        } else {
            $("#panel_nr").show();
            $(this).css("right", w_menu); $("#panel_nr").css("width", w_menu);
            tmp_width = w_menu;
        }

        getSWF(vid).width = w_widows - $("#btnHidden").width() - tmp_width - 30;
        getSWF(vid).height = 545;
   

    });


    $(window).on("resize", function () {
        var vid = "cc_" + $("#h_vid").val();
        w_widows = $(window).width();
        getSWF(vid).width = w_widows - $("#btnHidden").width() - tmp_width - 30;
        getSWF(vid).height = 545;
    });
});



//点击文章标题出现章节
$(function () {
    var $panel_content = $('#panel_content');
    var $dl = $panel_content.find('.panel_Cf');
    var $dt = $dl.find('dt');

    $dt.click(function () {
        var isShow = $(this).siblings('dd').is(':visible');
        if (isShow) {
            $(this).siblings('dd').hide();
        } else {
            $(this).siblings('dd').show();
        }
    });

});


//章节、笔记、问答的切换
$(function () {
    var $panel_Bf = $('.panel_Bf');
    var $panel_Bf_li = $panel_Bf.find('li');
    var $panel_content = $('.panel_content');

    $panel_Bf_li.click(function () {
        var _index = $(this).index();
        $(this).addClass('li1').siblings('li').removeClass('li1');
        $panel_content.eq(_index).show().siblings('.panel_content').hide();

    });

});



/*---------------------------------------追加提问-----------------------*/
$(document).ready(function () {

    $(".nr_Bf p").click(function () {
        $(".nr_Bf").hide();

    }, function () {
        $(".nr_Cf").show();
        $(".nr_Bf").hide();
    });

});
$(document).ready(function () {

    $(".nr_Bf ul,li.action").click(function () {
        $(".nr_Cf").hide();

    }, function () {
        $(".nr_Bf").show();
        $(".nr_Cf").hide();
    });
     
});



$(
    function () {
        //添加笔记    
        $("#btnNote").on("click", function () {

            var vid = "cc_" + $("#h_vid").val();
            var str = $("#txtNote").val();
            var position = getPosition(vid);

            if (position == undefined) {
                position = 0;
            }


            if (str != '' && str != null && $.trim(str) != "在此填写笔记") {

                $.ajax({
                    type: "POST",
                    url: "/lib/member/Note.ashx",
                    data: { dml: 1, content: $("#txtNote").val(), chapterid: $(this).attr("data-chapterid"), courseid: $(this).attr("data-courseid"), time: position },
                    success: function (data) {
                        if (data == 1) {
                            var strHtml = "";
                            strHtml += " <li>";
                            strHtml += "  <p><a href='javascript:void(0);' data-type='playerSeek' data-val='" + position + "'>" + ConvertTime(position) + "</a></p>";
                            strHtml += "<span>" + $("#txtNote").val() + "</span>";
                            strHtml += "</li>";
                            if ($("#ul_notes li").length > 0) {
                                $("#ul_notes li:eq(0)").before(strHtml);
                            } else {
                                $("#ul_notes").append(strHtml);
                            }
                            $("#txtNote").val("");
                            $("[data-type='playerSeek']").on("click", function () {
                                //  alert($("#h_vid").val());
                                customSeek("cc_" + $("#h_vid").val(), parseInt($(this).attr("data-val")));
                            });
                            initNoteData();
                        }
                    }
                });
            }
            else {
                $("#txtNoteMsg").text("笔记内容不能为空");
            }
        });
        //初始化笔记
        var initNoteData = function () {
            $("#ul_notes li").hide();
            $("#sub_note li").empty();
            $("#sub_note li").append("<img src='/images/sub_02.png' />");
            $("#sub_note li:eq(0)").empty();
            $("#sub_note li:eq(0)").append("<img src='/images/sub_01.png' />");
            var res = ($("#ul_notes li").length > 3 ? 3 : $("#ul_notes li").length);
            for (var i = 0; i < res; i++) {
                $("#ul_notes li:eq(" + i + ")").show();
            }

            $("#txtNoteCount").text("(" + $("#ul_notes li").length + ")");

        }
        initNoteData();
        $("#sub_note li").on("click", function () {
            $("#ul_notes li").hide();
            $("#sub_note li").empty();
            $("#sub_note li").append("<img src='/images/sub_02.png' />");
            $(this).empty();
            $(this).append("<img src='/images/sub_01.png' />");
            var res = parseInt($(this).attr("data-val"));
            var currpage = $("#ul_notes li").length < res * 3 ? $("#ul_notes li").length : res * 3;
            for (var i = (res - 1) * 3; i < currpage; i++) {
                $("#ul_notes li:eq(" + i + ")").show();
            }



        });
        $("[data-type='playerSeek']").on("click", function () {

            customSeek("cc_" + $("#h_vid").val(), parseInt($(this).attr("data-val")));
        });
        //添加问题
        $("#btnQues").on("click", function () {
            var str = $("#txtQues").val ();


            if (str != null && str != '' && str != "在此输入想提到的问题" && str.length > 9) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: "POST",
                    url: "../lib/member/Question.ashx",
                    data: { dml: 1, content: $("#txtQues").val(), chapterid: $(this).attr("data-chapterid"), courseid: $(this).attr("data-courseid") },
                    success: function (data) {
                        //    alert(data);
                        if (data != '' && data != -1) {
                            var dataObj = eval("(" + data + ")"); //转换为json对象 

                            var strHtml = "<a data-type='que_showdialog' href='javascript:void(0);' data-val='" + dataObj[0].QuesId + "'>";
                            strHtml += "<div class='tx'><img src='" + dataObj[0].ImageUri + "'><span>" + dataObj[0].MemberName + "</span></div>";
                            strHtml += "<p>" + dataObj[0].Content + "</p>";
                            strHtml += "</a>";


                            if ($("#ul_ques li").length > 0) {

                                $("#ul_ques li:eq(0)").before("<li></li>");
                                //  document.getElementById("ul_que").appendChild(e_li);
                            } else {
                                //document.getElementById("ul_ques").appendChild(e_li);
                                $("#ul_ques").append("<li></li>");
                            }

                            var li = $("#ul_ques li:eq(0)");
                            $(li).append(strHtml);
                            $("#ul_ques img").on("error", function () {
                                this.src = "/images/comments_03.png";
                            });
                            $("#txtQues").val("");

                            initQuesData();
                        }
                    }
                });
            } else {
                $("#txtQueMsg").text("问题内容不能少于10个字");
            }
        });
        //初始化问题
        var initQuesData = function () {
            $("#ul_ques li").hide();
            $("#sub_ques li").empty();
            $("#sub_ques li").append("<img src='/images/sub_02.png' />");
            $("#sub_ques li:eq(0)").empty();
            $("#sub_ques li:eq(0)").append("<img src='/images/sub_01.png' />");
            var res = ($("#ul_ques li").length > 3 ? 3 : $("#ul_ques li").length);
            for (var i = 0; i < res; i++) {
                $("#ul_ques li:eq(" + i + ")").show();
            }

            $("#txtQuesCount").text("(" + $("#ul_ques li").length + ")");

            /*添加弹窗事件*/
            $("[data-type='que_showdialog']").on("click", function () {
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
                        }
                        else {
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

                /*添加弹窗事件结束*/

            });

        }
        initQuesData();
        $("#sub_ques li").on("click", function () {
            $("#ul_ques li").hide();
            $("#sub_ques li").empty();
            $("#sub_ques li").append("<img src='/images/sub_02.png' />");
            $(this).empty();
            $(this).append("<img src='/images/sub_01.png' />");
            var res = parseInt($(this).attr("data-val"));
            var currpage = $("#ul_ques li").length < res * 3 ? $("#ul_ques li").length : res * 3;
            for (var i = (res - 1) * 3; i < currpage; i++) {
                $("#ul_ques li:eq(" + i + ")").show();
            }

        });
    });
 
        
       



    
