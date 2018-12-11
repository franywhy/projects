$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "http://www.hqjy.com/head/lib/producttype.ashx",
        data: "type=1",
        dataType: "jsonp",
        jsonp: "callbackfun",
        beforeSend: function () {
        },
        success: function (json) {
            $.each(json.firstLevel, function (i, item) {
                if (i == 0) {
                    $(".div-left").append("<li class=\"act\">" + item.TYPENAME + "</li>");
                }
                else {
                    $(".div-left").append("<li>" + item.TYPENAME + "</li>");
                }
                var string = "<dl class=\"div-right right none\"><dt class=\"relative\">选择班型：";
                if (i == 0) {
                    string = "<dl class=\"div-right right\"><dt class=\"relative\">选择班型：";
                }

                var count = 0;
                $.each(json.secondLevel, function (k, model) {
                    if (model.FATHERID == item.TYPEID) {
                        count++;
                    }
                });
                if (count > 5) {
                    string += "<div class=\"btn-box absolute clearfix\"><p class=\"l-btn left no-click\"></p><p class=\"r-btn right\"></p></div></dt><dd class=\"clearfix\"><div class=\"t-dd-box\">";
                    var h = 0;
                    var package = json.secondLevel;
                    for (var j = 0; j < package.length; j++) {
                        if (package[j].FATHERID == item.TYPEID) {
                            if (h == 5) {
                                string += "</div><div class=\"t-dd-box\">";
                            }
                            var link = package[j].QQLINK;
                            if (package[j].QQLINK == "") {
                                link = "/course/" + package[j].TYPEID + ".html";
                            }
                            string += "<a href=\"http://www.hqjy.com" + link + "\"><div class=\"t-dd\"><ul><div class=\"t-h1\"><img src=\"http://img.kjcity.com/" + package[j].PIC + "\"/></div><div class=\"t-h2\">" + package[j].TYPENAME + "</div></ul></div></a>";
                            h++;
                        }
                    }
                    string += "</div></dd></dl>";
                }
                else {
                    string += "</dt><dd class=\"clearfix\"><div class=\"t-dd-box\">";

                    var package = json.secondLevel;
                    for (var j = 0; j < package.length; j++) {
                        if (package[j].FATHERID == item.TYPEID) {
                            var link = package[j].QQLINK;
                            if (package[j].QQLINK == "") {
                                link = "/course/" + package[j].TYPEID + ".html";
                            }
                            string += "<a href=\"http://www.hqjy.com" + link + "\"><div class=\"t-dd\"><ul><div class=\"t-h1\"><img src=\"http://img.kjcity.com/" + package[j].PIC + "\"/></div><div class=\"t-h2\">" + package[j].TYPENAME + "</div></ul></div></a>";
                        }
                    }
                    string += "</div></dd></dl>";
                }
                $("#course-xiala").append(string);
            });
        },
        complete: function () {
            $.getScript('http://www.hqjy.com/head/js/com-fn.js', function () { });
        }
    })

})