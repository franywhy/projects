$(function () {
    imgLeftRightSlide();
    imgShowPopup();
    var schoolCode = getParameter("code");
    //	校区展示图片
    function imgLeftRightSlide() {
        var one = $(".Campus-B-con-4 .t-dd");
        var out = $(".Campus-B-con-4 .img-bar-box");
        var pBtn = $(".b-con-4-dl .page-pver");
        var nBtn = $(".b-con-4-dl .page-next");
        var oneWid = one.outerWidth(true);
        var oneLen = one.length;
        var index = 0;

        out.css({ "width": oneLen * oneWid });

        pBtn.on("click", function () {
            index--;
            if (index < 0) {
                index = oneLen - 4;
            }
            clickBtn(index);
        });
        nBtn.on("click", function () {
            index++;
            if (index > oneLen - 4) {
                index = 0;
            }
            clickBtn(index);
        });

        function clickBtn(i) {
            out.stop(true, true).animate({ "margin-left": -oneWid * i }, 300);
        }
    }
    //	大框查看图片
    function imgShowPopup() {
        var one = $(".Campus-B-con-4 .t-dd");
        var out = $(".img-max-pop-out");
        var box = $(".img-max-pop-out .img-show");
        var pBtn = $(".img-max-pop-out .page-pver");
        var nBtn = $(".img-max-pop-out .page-next");
        var index = 0;

        one.on("click", function () {
            out.fadeIn();
            var i = $(this).index();
            index = i;
            judgeImg(one.eq(index).find("img"));
        });

        $(".img-max-pop-out .i-off").on("click", function () {
            out.fadeOut();
        });

        pBtn.on("click", function () {
            console.log(index);
            index--;
            if (index < 0) {
                index = one.length - 1;
            } else {
                judgeImg(one.eq(index).find("img"));
            }
        });

        nBtn.on("click", function () {
            index++;
            if (index > one.length - 1) {
                index = 0;
            } else {
                judgeImg(one.eq(index).find("img"));
            }
        });

        function judgeImg(img) {
            box.empty();
            var w = img.width();
            var h = img.height();
            var imgUrl = img.attr("src");
            var oimg = img.clone().css({ "display": "none" });
            if ((w / h) > (800 / 500)) {
                oimg.css({ "width": "100%" });

            } else {
                oimg.css({ "height": "100%" });
            }
            box.append(oimg);
            oimg.fadeIn();
        }
    }

    $.ajax({
        type: "GET",
        url: "" + getApiUrl() + "/school/get_school_detail?school_code=" + schoolCode,
        dataType: "json",
        beforeSend: function () {
        },
        success: function (data) {
            var res = data.data;
            if (res) {
                var school = res.school;
                var address = encodeURI(school.address);
                $("#checkRoute").attr("href", "http://ditu.amap.com/search?query=" + address);
                $(".school_name").text(school.name);
                $("#school_name").text(school.name);
                $("#schoo_address").text(school.address);
                $("#school_tel").text(school.telephone);
                //校区环境
                var enviroment = res.piclist;
                for (var j = 0; j < enviroment.length; j++) {
                    $("#school_enviroment").append('<dd class="t-dd">'
                        + '<img src="' + enviroment[j].pic_url + '"/>'
                        + '</dd>');
                }
                $("#school_enviroment").append('<div class="clear"></div>');
                //校区活动
                var activity = res.aclist;
                for (var k = 0; k < activity.length; k++) {
                    $("#school_activity").append('<dd class="hover-shadow">'
                        + '<a href="article.html?id=' + activity[k]._id + '" target="_blank">'
                        + '<div class="b-con-3-dd-img-box">'
                        + '<img src="' + activity[k].activity_picurl + '"/>'
                        + '</div>'
                        + '<div class="b-con-3-dd-p">' + activity[k].activity_title + '</div>'
                        + '<div class="b-con-3-dd-data">' + FormatDateTime(activity[k].create_time) + '</div>'
                        + '</a>'
                        + '</dd>');
                }
                $("#school_activity").append('<div class="clear"></div>');
                //校区课程

                var courselist = res.courselist;
                for (var l = 0; l < courselist.length; l++) {
                    $("#school_course_list").append('<li>'
                            + '<a href="commodity_package.xhtml?commodity_id=' + courselist[l]._id + '">'
                            + '<div class="li-top-con">'
                            + '<div class="img"><img src="' + courselist[l].photo + '"></div>'
                            + '<div class="pop-bg"></div>'
                            + '<div class="con-btn">'
                            + '<div class="btn-play">'
                            + '<span>试听</span>'
                            + '</div>'
                            + '<div class="btn-consult">'
                            + '<span>咨询</span>'
                            + '</div>'
                            + '<div class="btn-infoVoide">'
                            + '<span>详细</span>'
                            + '</div>'
                            + '</div>'
                            + '</div>'
                            + '<div class="li-ckName">'
                            + '<h2>' + courselist[l].name + '</h2>'
                            + '<div class="li-ckName-h3">价格：' + courselist[l].titles + '元</div>'
                            + '<div class="li-ckName-p">适用对象：' + courselist[l].reader + '</div>'
                            + '<div class="li-ckName-p">课程内容：' + courselist[l].commodity_type_list + '</div>'
                            + '</div>'
                            + '</a>'
                            + '</li>'
                    );
                }
            }
        },
        complete: function () {

        },
        error: function (data) {
            alert(data.msg);
        }
    });
});

function FormatDateTime(timestamp) {
//    timestamp = 1484534605517;
    var newDate = new Date();
    try {
        newDate.setTime(timestamp);
    }
    catch (ex) {
    }
    return newDate.format('yyyy-MM-dd');
}

Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                            ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}