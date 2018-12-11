
/*******测试环境********/
// var accountant_domain = "http://kjpcweb.ljtest.hqjy.com";
//  var learningCenter_domain = "http://10.0.98.15:18088";

/*******预生产环境********/
var accountant_domain = "http://www.hqjy.com";
var learningCenter_domain = "http://kuaiji.learning.hqjy.com";

var token = $.cookie('token');
var avatar = $.cookie('avatar');
var nickName = $.cookie('nickName');
document.writeln("<div class=\"header-top\"><div class=\"w1200 w-auto-Center clearfix\"><div class=\"header-top-left\"><div class=\"this-left-p\">上市公司的教育机构（开元股份：300338） 中国高新技术企业</div></div></div></div>");
document.writeln("<div class=\"header-v2\"><div class=\"w1200\">");
document.writeln("<a href=\"" + accountant_domain + "/\"><div class=\"logo\"></div></a>");
document.writeln("<ul class=\"nav clearfix-v\"><a href=\"" + accountant_domain + "/\"><li>首页<i></i></li></a> <a href=\"javascript:void(0);\"><li class=\"course-nav-a\">课程<em></em></li></a> <a href=\"http://tiku.hqjy.com/\"><li>题库<i></i></li></a> <a href=\"" + accountant_domain + "/shixun/index\"><li>实训<i></i></li></a> <a href=\"" + accountant_domain + "/school/\"><li>校区<i></i></li></a>");

if (token == "undefined" || typeof (token) == "undefined") {
    document.writeln("<a href=\"javascript:;\" onclick=\"$('.btn-login').trigger('click');\"><li>学习中心<i></i></li></a>");
} else {
    document.writeln("<a href=\"" + learningCenter_domain + "/learningCenter/web/home?SSOTOKEN=" + token + "\" target=\"_blank\"><li>学习中心<i></i></li></a>");
}

document.writeln("<a href=\"http://ces.hqjy.com/\" target=\"_blank\"><li>继续教育<i></i></li></a> <a href=\"http://www.hengqijy.com/\" target=\"_blank\"><li>关于我们<i></i></li></a>");

document.writeln("</ul>");

if (token == "undefined" || typeof (token) == "undefined") {
    document.writeln("<ul class=\"register clearfix-v\"><li class=\"btn-login\">登录</li><li class=\"btn-reget\">注册</li></ul>");
} else {
    document.writeln("<div class=\"register-yes-out right\"><div class=\"top-self-box\"><div class=\"self-box-t clearfix\"><div class=\"self-box-t-p\">" + nickName + "</div><div class=\"self-box-t-i\"><img style=\"border-radius:50%;\" src=\"" + avatar + "\" onerror=\"this.src='" + accountant_domain + "/static/images/icon/default-photo.png'\" /></div></div><div class=\"self-box-main\"><a href=\"" + accountant_domain + "/user/logout\" class=\"self-box-main-dd self-box-main-dd-last\">注销</a></div></div></div>");
}
document.writeln("</div>");

document.writeln("<div class=\"course-xiala\" style=\"display: none;\"><div id=\"course-xiala\" class=\"w1200 clearfix-v2 relative\">");
document.writeln("<ul class=\"div-left left\"></ul>");

document.writeln("</div></div>");
document.writeln("");
document.writeln("</div>");

$.ajax({
    type: "GET",
    url: "" + accountant_domain + "/head/lib/producttype",
    data: "",
    dataType: "jsonp",
    jsonp: "callback",
    beforeSend: function () {
    },
    success: function (json) {
        json = eval("("+json+")");
        $.each(json.firstLevel, function (i, item) {
            if (i == 0) {
                $(".div-left").append("<li class=\"act\">" + item.typename + "</li>");
            }
            else {
                $(".div-left").append("<li>" + item.typename + "</li>");
            }
            var string = "<dl class=\"div-right right none\"><dt class=\"relative\">选择班型：";
            if (i == 0) {
                string = "<dl class=\"div-right right\"><dt class=\"relative\">选择班型：";
            }

            var count = 0;
            $.each(json.secondLevel, function (k, model) {
                if (model.fatherid == item.typeid) {
                    count++;
                }
            });
            // if (count > 5) {
            //     string += "<div class=\"btn-box absolute clearfix\"><p class=\"l-btn left no-click\"></p><p class=\"r-btn right\"></p></div></dt><dd class=\"clearfix\"><div class=\"t-dd-box\">";
            //     var h = 0;
            //     var package = json.secondLevel;
            //     for (var j = 0; j < package.length; j++) {
            //         if (package[j].fatherid == item.typeid) {
            //             if (h == 5) {
            //                 string += "</div><div class=\"t-dd-box\">";
            //             }
            //             var link = package[j].qqlink;
            //             if (package[j].qqlink == "") {
            //                 link = "/course/" + package[j].typeid + ".html";
            //             }
            //             string += "<a href=\"" + accountant_domain + "" + link + "\"><div class=\"t-dd\"><ul><div class=\"t-h1\"><img src=\"http://img.kjcity.com/" + package[j].pic + "\"/></div><div class=\"t-h2\">" + package[j].typename + "</div></ul></div></a>";
            //             h++;
            //         }
            //     }
            //     string += "</div></dd></dl>";
            // }
            // else {
                string += "</dt><dd class=\"clearfix\"><div class=\"t-dd-box\">";

                var package = json.secondLevel;
                for (var j = 0; j < package.length; j++) {
                    if (package[j].fatherid == item.typeid) {
                        var link = package[j].qqlink;
                        if (package[j].qqlink == "") {
                            link = "/course/" + package[j].typeid + ".html";
                        }
                        string += "<a href=\"" + accountant_domain + "" + link + "\"><div class=\"t-dd\"><ul><div class=\"t-h1\"><img src=\"http://img.kjcity.com/" + package[j].pic + "\"/></div><div class=\"t-h2\">" + package[j].typename + "</div></ul></div></a>";
                    }
                }
                string += "</div></dd></dl>";
            // }
            $("#course-xiala").append(string);
        });
    },
    complete: function () {
        $.getScript('' + accountant_domain + '/static/head/js/com-fn.js', function () { });
    },
    error: function (data) {

    }
})