/// <reference path="jquery-1.8.3.min.js" />
;$(function () {
    var oT;
    $("#jsTeamFun li").each(function (index) {
        $(this).hover(function () {
            var oI = $(this);
            oT = setTimeout(function () {
                oI.children(".jsBig").fadeIn(80);
            }, 300);
            oI.addClass("zIndex990");
        }, function () {
            var oI = $(this);
            clearTimeout(oT);
            oI.children(".jsBig").fadeOut(80);
            oI.removeClass("zIndex990");
        });
    });

    $("#btnChanageTeacher").on("click", function () {

        var that = $(this);
        var pageIndex = parseInt(that.attr("data-pageIndex"));
        var pageCount = parseInt(that.attr("data-pageCount"));
        var itemCount = parseInt(that.attr("data-itemCount"));
        var typeid = parseInt(that.attr("data-typeId"));
        var imageDomain = $("#IMAGE_DOMAIN").val();
        var ul = $("#ul_teacherList");
        if (pageIndex + 1 <= pageCount) {
            pageIndex += 1;
        } else {
            pageIndex = 1;
        }
        $.ajax({
            url: "/lib/Teacher.ashx",
            type: "GET",
            data: { page: pageIndex, typeid: typeid },
            cache: false,
            success: function (data) {

                if (data != '') {
                    $("#ul_teacherList").empty();
                    var dataObj = eval("(" + data + ")");
                    var li = "";
                    $("#jsTeamFun").empty();
                    $.each(dataObj, function (i, val) {

                        var a = document.createElement("a");
                        a.href = "/teacher_info_" + val.TEACHERID + ".html";
                        a.title = val.NAME;
                        a.target = "_blank";
                        var img = document.createElement("img");
                        img.src = imageDomain + val.PHOTO;
                        img.setAttribute("onerror", "this.src='/images/default_avatar_28.png'");
                        img.setAttribute("class","img");


                        var li = document.createElement("li");
                        a.appendChild(img)
                        li.appendChild(a);

                        var div = document.createElement("div");
                        div.setAttribute("class", "jsBig");
                        div.setAttribute("style", "display:none");
                        var div_a = document.createElement("a");
                        div_a.title = val.NAME;
                        div_a.href = "/teacher_info_" + val.TEACHERID + ".html";
                        div_a.target = "_blank";
                        var div_img = document.createElement("img");
                        div_img.src = imageDomain + val.PHOTO;
                        div_img.setAttribute("onerror", "this.src='/images/default_avatar_28.png'");

                        div_a.appendChild(div_img);
                        var div_p = document.createElement("p");
                        var div_strong = document.createElement("strong");
                        div_strong.class = "greenCol";
                        var div_strong_text = document.createTextNode(val.NAME);
                        div_strong.appendChild(div_strong_text);
                        div_p.appendChild(div_strong);
                        var div_p_text = document.createTextNode(removeHTMLTag(val.RESUME));
                        div_p.appendChild(div_p_text);
                        div.appendChild(div_a);
                        div.appendChild(div_p);
                        li.appendChild(div);
                        document.getElementById("jsTeamFun").appendChild(li);
                    });
                    function removeHTMLTag(str) {
                        str = str.replace(/<\/?[^>]*>/g, ''); //去除HTML tag
                        str = str.replace(/[ | ]*\n/g, '\n'); //去除行尾空白
                        //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
                        str = str.replace(/&nbsp;/ig, ''); //去掉&nbsp;
                        return str;
                    }
                    /************************重新调用函数begin**************************/

                    $("#jsTeamFun li").each(function (index) {
                        $(this).hover(function () {
                            var oI = $(this);
                            oT = setTimeout(function () {
                                oI.children(".jsBig").fadeIn(80);
                            }, 300);
                            oI.addClass("zIndex990");
                        }, function () {
                            var oI = $(this);
                            clearTimeout(oT);
                            oI.children(".jsBig").fadeOut(80);
                            oI.removeClass("zIndex990");
                        });
                    });


                    /************************重新调用函数end**************************/
                    that.attr("data-pageIndex", pageIndex);


                    function AutoResizeImage(maxWidth, maxHeight, objImg) {
                        var img = new Image();
                        img.src = objImg.src;
                        var hRatio;
                        var wRatio;
                        var Ratio = 1;
                        var w = img.width;
                        var h = img.height;
                        wRatio = maxWidth / w;
                        hRatio = maxHeight / h;
                        if (maxWidth == 0 && maxHeight == 0) {
                            Ratio = 1;
                        }
                        else if (maxWidth == 0) {
                            Ratio = hRatio;
                        } else if (maxHeight == 0) {
                            Ratio = wRatio;
                        } else if (wRatio < 1 || hRatio < 1) {
                            Ratio = (wRatio <= hRatio ? wRatio : hRatio);
                        }

                        w = w * Ratio;
                        h = h * Ratio;

                        objImg.height = h;
                        objImg.width = w;
                    }
                    $.each($(".jsBig a img"), function (i, val) {
                        AutoResizeImage(150, 0, this);
                    });

                }
            }

        });



    });


});


$(function () {
    //切换
    $("[name='stu_li']").on("click", function () {
        $("[name='stu_li']").removeClass("action");
        $(this).addClass("action");
        $("div.learnesing").hide();
        $("#" + $(this).attr("data-val")).show();
    });
    //分页
    var SpiltPage = function (packageId, index, pre) {

        var li = $("[data-type='" + pre + packageId + "']");
        var itemCount = li.length;

        var length = parseInt(index) * 3; // ( > itemCount ? itemCount : (parseint(index) * 3));
        length = length > itemCount ? itemCount : length;
        $(li).hide();

        for (var i = (parseInt(index) - 1) * 3; i < length; i++) {
            if (i == (parseInt(index) - 1) * 3) {
                $(li[i]).find(".de").hide();
            }
            $(li[i]).show();
        }
    }
    /*------------------------我的课程-----------------------------*/

    var packageLearn = $("[data-type='package-learn']")
    $.each(packageLearn, function (i, val) {
        var li = $(val).find(".collection li");
        $(li).hide();
        $.each($(li), function (j, val2) {
            if (j < 3) {
                $(val2).show();
            }
        });
    });

    var li_spilter = $("[data-type='package-learn-spilter']");
    $.each(li_spilter, function (i, val) {

        var li = $(val).find("li");
        $.each(li, function (j, val2) {
            if (j == 0) {
                $(val2).removeClass("bright");
                $(val2).addClass("bright");
            }

            $(val2).on("click", function () {
                var that = $(this);
                var packageId = that.attr("data-packageid");
                $(packageLearn).find($("[data-packageid='" + packageId + "']")).removeClass("bright");
                that.addClass("bright");
                SpiltPage(packageId, that.attr("data-val"), "li_package_learn_");
            });
            /*------------------------*/
        });
    });
    /****************我的收藏******************/
    var packageFavorites = $("[data-type='package-favorites']")
    $.each(packageFavorites, function (i, val) {
        var li = $(val).find(".collection li");
        $(li).hide();
        $.each($(li), function (j, val2) {
            if (j < 3) {
                $(val2).show();
            }
        });
    });

    var li_spilter = $("[data-type='package-favorites-spilter']");
    $.each(li_spilter, function (i, val) {

        var li = $(val).find("li");
        $.each(li, function (j, val2) {
            if (j == 0) {
                $(val2).removeClass("bright");
                $(val2).addClass("bright");
            }

            $(val2).on("click", function () {
                var that = $(this);
                var packageId = that.attr("data-packageid");
                $(packageFavorites).find($("[data-packageid='" + packageId + "']")).removeClass("bright");
                that.addClass("bright");
                SpiltPage(packageId, that.attr("data-val"), "li_package_favorites_");
            });
            /*------------------------*/
        });
    });
});