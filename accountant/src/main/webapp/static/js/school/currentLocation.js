
var geolocation;
var map = new AMap.Map("container", {
    resizeEnable: true,
    zoom: 13
});
var totalData;
var totalPro;
var markers = [];
var dqlng = $.cookie('hqjy_dqlng');
var dqlat = $.cookie('hqjy_dqlat');
if (dqlng == "undefined" || typeof (dqlng) == "undefined") {
    //加载地图，调用浏览器定位服务
    map.plugin('AMap.Geolocation', function () {
        geolocation = new AMap.Geolocation({
            enableHighAccuracy: true, //是否使用高精度定位，默认:true
            timeout: 10000,          //超过10秒后停止定位，默认：无穷大
            buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
            zoomToAccuracy: false,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
            buttonPosition: 'RB'
        });
        map.addControl(geolocation);
        geolocation.getCurrentPosition();
        AMap.event.addListener(geolocation, 'complete', onComplete); //返回定位信息
//        AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
    });
}
else {
    getComplete(dqlng, dqlat)
}


//解析定位结果
function onComplete(geodata) {
    //根据当前定位（经纬度） 获得附近校区
    dqlng = geodata.position.getLng();
    dqlat = geodata.position.getLat();
    $.cookie('hqjy_dqlng', dqlng, { expires: 2, path: '/' });
    $.cookie('hqjy_dqlat', dqlat, { expires: 2, path: '/' });
    $.ajax({
        type: "GET",
        url: "" + getApiUrl() + "/school/get_nearby_city?longitude=" + dqlng + "&latitude=" + dqlat,
        dataType: "json",
        beforeSend: function () {
        },
        success: function (data) {
            var row = data.data.nearby_city_andschoollist;
            var schoollist = data.data.nearby_city_andschoollist.schoolist;
            var rows = data.data.citylist;
            totalData = rows;
            //所有城市信息
            if (rows.length) {
                //城市
                $("#around_city_list").empty();
                $.ajax({
                    type: "GET",
                    url: "" + getApiUrl() + "/school/get_province_and_city",
                    dataType: "json",
                    beforeSend: function () {
                    },
                    success: function (json) {
                        totalPro = json.data;
                        var html = "<div class=\"map-left-dl\"><div class=\"map-left-dt\">选择省份</div><div class=\"map-left-dd-box\">";
                        $.each(json.data, function (i, item) {
                            if (item.code == row.parent_nc_code) {
                                $(document).attr("title", "恒企" + item.name + "校区地址,恒企" + item.name + "校区电话,恒企" + item.name + "校区联系方式- 恒企会计培训 - 恒企教育官网 - 恒企教育");
                                html = "<div class=\"map-left-dl\"><div class=\"map-left-dt\">" + item.name + "</div><div class=\"map-left-dd-box\">";
                            }
                        });

                        $.each(json.data, function (i, item) {
                            //过滤掉特殊校区
                            if (item.code == "JH601" || item.code == "JH602" || item.code == "JH701" || item.code == "JHA01") {
                            }
                            else {
                                html += "<div rel=\"" + item.code + "\" class=\"map-left-dd\">" + item.name + "</div>";
                            }
                        });
                        html += "</div></div>";
                        $("#around_city_list").append(html);
                        mouseSelectsf();
                    },
                    complete: function () {
                        html = "<div class=\"map-right-dl clearfix\">";
                        for (var i = 0; i < rows.length; i++) {
                            if (rows[i]) {
                                if (row.code == rows[i].code) {
                                    html += "<div id=\"" + rows[i].code + "\" class=\"map-right-dd\">"+ rows[i].name +"</div>";
                                } else {
                                    html += "<div id=\"" + rows[i].code + "\" class=\"map-right-dd\">" + rows[i].name + "</div>";
                                }
                            }
                        };
                        html += "</div>";
                        $("#around_city_list").append(html);
                        clickEvent();
                    },
                    error: function (data) {

                    }
                });
            }
            if (schoollist.length) {
                var count = 1;
                //校区
                $("#city_school_list").empty();
                if (schoollist.length <= 50) {
                    for (var j = 0; j < schoollist.length; j++) {
                        if (schoollist[j]) {
                            j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        }
                    }
                } else {
                    for (var j = 0; j < 50; j++) {
                        if (schoollist[j]) {
                            j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        }
                    }
                    var total = Math.ceil(schoollist.length / 50)
                    for (var j = 0; j < total; j++) {
                        if (j == 0) {
//                            $(".C-A-right-con-pape").html("<a href=\"javascript:\" rel=\"1\" class=\"act\">1</a>");
                        }
                        else {
                            $(".C-A-right-con-pape").append("<a href=\"javascript:\" rel=\"" + parseInt(j + 1) + "\">" + parseInt(j + 1) + "</a>");
                        }
                    }
                    $(".C-A-right-con-pape a").click(function () {
                        var page = $(this).attr("rel");
                        $("#city_school_list").empty();
                        var begin = (parseInt(page) - 1) * 50;
                        var end = parseInt(page) * 50;
                        for (var j = begin; j < end; j++) {
                            showSchoolList(schoollist[j]);
                        }
                        $(this).addClass('act').siblings().removeClass('act');
                    });
                }
                $("#city_school_list").append('<div class="clear"></div>');
            }
        },
        complete: function () {

        },
        error: function (data) {

        }
    });
}
//已存在定位信息
function getComplete(dqlng, dqlat) {
	console.log("定位");
    $.ajax({
        type: "GET",
        url: "" + getApiUrl() + "/school/get_nearby_city?longitude=" + dqlng + "&latitude=" + dqlat,
        dataType: "json",
        beforeSend: function () {
        },
        success: function (data) {
            var row = data.data.nearby_city_andschoollist;
            var schoollist = data.data.nearby_city_andschoollist.schoolist;
            var rows = data.data.citylist;
            totalData = rows;
            //所有城市信息
            if (rows.length) {
                //城市
                $("#around_city_list").empty();
                $.ajax({
                    type: "GET",
                    url: "" + getApiUrl() + "/school/get_province_and_city",
                    dataType: "json",
                    beforeSend: function () {
                    },
                    success: function (json) {
                        totalPro = json.data;
                        var html = "<div class=\"map-left-dl\"><div class=\"map-left-dt\">选择省份</div><div class=\"map-left-dd-box\">";
                        $.each(json.data, function (i, item) {
                            if (item.code == row.parent_nc_code) {
                                $(document).attr("title", "恒企" + item.name + "校区地址,恒企" + item.name + "校区电话,恒企" + item.name + "校区联系方式- 恒企会计培训 - 恒企教育官网 - 恒企教育");
                                html = "<div class=\"map-left-dl\"><div class=\"map-left-dt\">" + item.name + "</div><div class=\"map-left-dd-box\">";
                            }
                        });

                        $.each(json.data, function (i, item) {
                            //过滤掉特殊校区
                            if (item.code == "JH601" || item.code == "JH602" || item.code == "JH701" || item.code == "JHA01") {
                            }
                            else {
                                html += "<div rel=\"" + item.code + "\" class=\"map-left-dd\">" + item.name + "</div>";
                            }
                        });
                        html += "</div></div>";
                        $("#around_city_list").append(html);
                        mouseSelectsf();
                    },
                    complete: function () {
                        html = "<div class=\"map-right-dl clearfix\">";
                        for (var i = 0; i < rows.length; i++) {
                            if (rows[i]) {
                                if (row.code == rows[i].code) {
                                    html += "<div id=\"" + rows[i].code + "\" class=\"map-right-dd\">" + rows[i].name + "</div>";
                                } else {
                                    html += "<div id=\"" + rows[i].code + "\" class=\"map-right-dd\">" + rows[i].name + "</div>";
                                }
                            }
                        };
                        html += "</div>";
                        $("#around_city_list").append(html);
                        clickEvent();
                    },
                    error: function (data) {

                    }
                });
            }
            if (schoollist.length) {
                var count = 1;
                //校区
                $("#city_school_list").empty();
                if (schoollist.length <= 50) {
                    for (var j = 0; j < schoollist.length; j++) {
                        if (schoollist[j]) {
                            j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        }
                    }
                } else {
                    for (var j = 0; j < 50; j++) {
                        if (schoollist[j]) {
                            j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        }
                    }
                    var total = Math.ceil(schoollist.length / 50)
                    for (var j = 0; j < total; j++) {
                        if (j == 0) {
                            //                            $(".C-A-right-con-pape").html("<a href=\"javascript:\" rel=\"1\" class=\"act\">1</a>");
                        }
                        else {
                            $(".C-A-right-con-pape").append("<a href=\"javascript:\" rel=\"" + parseInt(j + 1) + "\">" + parseInt(j + 1) + "</a>");
                        }
                    }
                    $(".C-A-right-con-pape a").click(function () {
                        var page = $(this).attr("rel");
                        $("#city_school_list").empty();
                        var begin = (parseInt(page) - 1) * 50;
                        var end = parseInt(page) * 50;
                        for (var j = begin; j < end; j++) {
                            showSchoolList(schoollist[j]);
                        }
                        $(this).addClass('act').siblings().removeClass('act');
                    });
                }
                $("#city_school_list").append('<div class="clear"></div>');
            }
        },
        complete: function () {

        },
        error: function (data) {

        }
    });
}
//显示校区信息
function showSchoolList(obj, first) {
    var name = obj.name == undefined ? "" : obj.name;
    var address = obj.address == undefined ? "" : obj.address;
    var telephone = obj.telephone == undefined ? "" : obj.telephone;
    var code = obj.code == undefined ? "" : obj.code;
    try {
        $.ajax({
            type: "GET",
            url: "http://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=baidu&key=fd7a313b9dd5d0cfdd4c97414fe89fbf&locations=" + obj.location.longitude + "," + obj.location.latitude,
            dataType: "json",
            beforeSend: function () {
            },
            success: function (data) {
                if (data.status == 1) {
                    //添加标记
                    addMarker(data.locations, address, name, telephone,code, first);
                }
//              显示默认第一个
//              if(!$(".Campus-A-navs dd").not(".select-sf").hasClass("act")){
//                  $(".Campus-A-navs dd").not(".select-sf").eq(0).addClass("act").siblings().not(".select-sf").removeClass("act");
//              }
            },
            complete: function () {

            },
            error: function (data) {
            }
        });
    }
    catch (err) {
    }
    var xqaddress = obj.address == undefined ? "" : obj.address;
    //console.log(xqaddress);
    var xqtelephone = obj.telephone == undefined ? "" : obj.telephone;
    $("#city_school_list").append('<a href="school/schoolinfo?code=' + obj.code + '" target="_blank"><li>'
        + '<div class="C-A-right-con-h1 left">' + name + '</div>'
        + '<div class="C-A-right-con-href right">查看校区</div>'
        + '<div class="clear"></div><div class="C-A-right-con-h2">' + xqtelephone + '</div>'
        + '<div class="C-A-right-con-h3">' + xqaddress + '</div>'
        + '</li></a>');

}

function addMarker(parm, address, name, telephone,code, first) {
    var title = name + "  " + telephone + "  " + address;
    if (marker) {
        return;
    }
    if (parm) {
        var array = parm.split(",");
        var position = [];
        for (var k = 0; k < array.length; k++) {
            position.push(array[k]);
        }

        var marker = new AMap.Marker({
            bubble: true,
            title: title,
            position: position,
            //animation:'AMAP_ANIMATION_BOUNCE',
            label: {//label默认蓝框白底左上角显示，样式className为：amap-marker-label
                offset: new AMap.Pixel(20, 20), //修改label相对于maker的位置
                content: name
            },
            map: map
        });
        
        markers.push(marker);
        
        AMap.event.addListener(marker, 'click', function () {
            openInfo(marker, address, name, telephone, code);
        });
        
        if (first) {
            openInfo(marker, address, name, telephone, code);
        }
    }
};

//点击定位点显示弹框
//var infoWindow;
function openInfo(obj, address, name, telephone, code) {
    //构建信息窗体中显示的内容
    //    address = "广州市白云区永泰";
    try {
        $.ajax({
            type: "GET",
            url: "" + getApiUrl() + "/school/get_school_notice_list?school_code=" + code,
            dataType: "json",
            beforeSend: function () {
            },
            success: function (data) {
                $("#slide-ul").empty();
                $("#slide-dl").empty();
                $.each(data.data, function (i, item) {
                    $("#slide-ul").append("<li class=\"slide-li\" style=\"background: url(" + item.activity_big_picurl + ") 50% 0px no-repeat;\"><a href=\"/school/article?id=" + item._id + "\" class=\"map-sl-a\" target=\"_blank\"></a></li>");
                    $("#slide-dl").append("<dd class=\"slide-dd act\"></dd>");
                });
            },
            complete: function () {
                $.getScript('/static/js/common/com-fn.js', function () { });
            },
            error: function (data) {
            }
        });
    }
    catch (err) {
    }
    var info = [];
    info.push("<div>" +
			    "<p style='font-size:16px;color:#27ae61;line-height:28px'>" + name + "</p>" +
			    "<p style='font-size:12px;line-height:22px;color:#666;'><span style='color:#000;'>校区地址：</span>" + address + "</p>" +
			    "<p style='font-size:12px;line-height:22px;color:#666;'><span style='color:#000;'>咨询电话：</span>" + telephone + "</p>" +
			  "</div>");
    var infoWindow = new AMap.InfoWindow({
        offset: new AMap.Pixel(0, -30),
        content: info.join("<br/>")  //使用默认信息窗体框样式，显示信息内容
    });
    infoWindow.open(map, obj.getPosition());
}

//切换城市-校区信息
function clickEvent() {
    $(".map-right-dd").click(
        function (e) {
            var cityid = $(this).attr("id");
            if (typeof (cityid) == "undefined") {
            }
            else {
                var Campus_A_navs_dd_index = 0;
                Campus_A_navs_dd_index = $(this).index();
                $(".map-right-dd").eq(Campus_A_navs_dd_index).addClass("act").siblings().removeClass("act");
                $(".Campus-A-con dl dd").eq(Campus_A_navs_dd_index).show().siblings().hide();
                getListById(cityid);
            }
        }
    );
//      显示默认第一个
//      if(!$(".Campus-A-navs dd").not(".select-sf").hasClass("act")){
//          $(".Campus-A-navs dd").not(".select-sf").eq(0).addClass("act").siblings().not(".select-sf").removeClass("act");
//      }

        $(".map-left-dd").click(
        function (e) {
            var cityid = $(this).attr("rel");
            if (typeof (cityid) == "undefined") {
            }
            else {
                $.each(totalPro, function (i, item) {
                    if (item.code == cityid) {
                        $.ajax({
                            type: "GET",
                            url: "" + getApiUrl() + "/school/get_nearby_city?longitude=" + item.schoollist[0].location.longitude + "&latitude=" + item.schoollist[0].location.latitude,
                            dataType: "json",
                            beforeSend: function () {
                            },
                            success: function (data) {
                                var row = data.data.nearby_city_andschoollist;
                                var schoollist = data.data.nearby_city_andschoollist.schoolist;
                                var rows = data.data.citylist;
                                totalData = rows;
                                //所有城市信息
                                if (rows.length) {
                                    //城市
                                    $("#around_city_list").empty();
                                    $.ajax({
                                        type: "GET",
                                        url: "" + getApiUrl() + "/school/get_province_and_city",
                                        dataType: "json",
                                        beforeSend: function () {
                                        },
                                        success: function (json) {
                                            totalPro = json.data;
                                            var html = "<div class=\"map-left-dl\"><div class=\"map-left-dt\">" + item.name + "</div><div class=\"map-left-dd-box\">";
                                            $.each(json.data, function (i, item) {
                                                //过滤掉特殊校区
                                                if (item.code == "JH601" || item.code == "JH602" || item.code == "JH701" || item.code == "JHA01") {
                                                }
                                                else {
                                                    html += "<div rel=\"" + item.code + "\" class=\"map-left-dd\">" + item.name + "</div>";
                                                }
                                            });
                                            html += "</div></div>";
                                            $("#around_city_list").append(html);
                                            mouseSelectsf();
                                        },
                                        complete: function () {
                                            html = "<div class=\"map-right-dl clearfix\">";
                                            for (var i = 0; i < rows.length; i++) {
                                                if (rows[i]) {
                                                    if (row.code == rows[i].code) {
                                                        html += "<div id=\"" + rows[i].code + "\" class=\"map-right-dd act\">" + rows[i].name + "</div>";
                                                    } else {
                                                        html += "<div id=\"" + rows[i].code + "\" class=\"map-right-dd\">" + rows[i].name + "</div>";
                                                    }
                                                }
                                            };
                                            html += "</div>";
                                            $("#around_city_list").append(html);
                                            clickEvent();
                                        },
                                        error: function (data) {

                                        }
                                    });
                                }
                                if (schoollist.length) {
                                    var count = 1;
                                    //校区
                                    $("#city_school_list").empty();
                                    if (schoollist.length <= 50) {
                                        for (var j = 0; j < schoollist.length; j++) {
                                            if (schoollist[j]) {
                                                j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                                            }
                                        }
                                    } else {
                                        for (var j = 0; j < 50; j++) {
                                            if (schoollist[j]) {
                                                j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                                            }
                                        }
                                        var total = Math.ceil(schoollist.length / 50)
                                        for (var j = 0; j < total; j++) {
                                            if (j == 0) {
//                                                $(".C-A-right-con-pape").html("<a href=\"javascript:\" rel=\"1\" class=\"act\">1</a>");
                                            }
                                            else {
                                                $(".C-A-right-con-pape").append("<a href=\"javascript:\" rel=\"" + parseInt(j + 1) + "\">" + parseInt(j + 1) + "</a>");
                                            }
                                        }
                                        $(".C-A-right-con-pape a").click(function () {
                                            var page = $(this).attr("rel");
                                            $("#city_school_list").empty();
                                            var begin = (parseInt(page) - 1) * 50;
                                            var end = parseInt(page) * 50;
                                            for (var j = begin; j < end; j++) {
                                                showSchoolList(schoollist[j]);
                                            }
                                            $(this).addClass('act').siblings().removeClass('act');
                                        });
                                    }
                                    $("#city_school_list").append('<div class="clear"></div>');
                                }
                            },
                            complete: function () {

                            },
                            error: function (data) {

                            }
                        });
                    }
                });
            }
        }
    );
}
//根据城市编号，改变校区区域信息及标志
function getListById(id) {
    if (id) {
        for (var a = 0; a < totalData.length; a++) {
            if (totalData[a].code == id) {
                var count = 1;
                var schoollist = totalData[a].schoolist;
                $("#city_school_list").empty();
                removeMarker();
                if (schoollist.length <= 50) {
                    for (var j = 0; j < schoollist.length; j++) {
                        if (schoollist[j]) {
                            j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        }
                    }
//                    $(".C-A-right-con-pape").html('<a href="javascript:" rel="1" class="act">1</a>');
                } else {
                    for (var j = 0; j < 50; j++) {
                        if (schoollist[j]) {
                            j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        }
                    }
                    var total = Math.ceil(schoollist.length / 50)
                    for (var j = 0; j < total; j++) {
                        if (j == 0) {
//                            $(".C-A-right-con-pape").html("<a href=\"javascript:\" rel=\"1\" class=\"act\">1</a>");
                        }
                        else {
                            $(".C-A-right-con-pape").append("<a href=\"javascript:\" rel=\"" + parseInt(j + 1) + "\">" + parseInt(j + 1) + "</a>");
                        }
                    }
                }
                $("#city_school_list").append('<div class="clear"></div>');
                map.setFitView();
                $(".C-A-right-con-pape a").click(function () {
                    try {
                        var page = $(this).attr("rel");
                        $("#city_school_list").empty();
                        var begin = (parseInt(page) - 1) * 50;
                        var end = parseInt(page) * 50;
                        for (var j = begin; j < end; j++) {
                            showSchoolList(schoollist[j]);
                        }
                    }
                    catch (err) {
                    }
                    
                    $(this).addClass('act').siblings().removeClass('act');
                });
            }
        }
    } else {
        removeMarker();
        $("#city_school_list").empty();
        for (var a = 0; a < totalData.length; a++) {
            var schoollist = totalData[a].schoolist;
            if (schoollist.length <= 50) {
                for (var j = 0; j < schoollist.length; j++) {
                    if (schoollist[j]) {
                        j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                    }
                }

            } else {
                for (var j = 0; j < 50; j++) {
                    if (schoollist[j]) {
                        j == 0 ? showSchoolList(schoollist[j], true) : showSchoolList(schoollist[j]);
                        //                      showSchoolList(schoollist[j]);
                    }
                }
            }
            $("#city_school_list").append('<div class="clear"></div>');
            map.setFitView();
        }
    }
}
//清空所有标志
function removeMarker() {
    map.remove(markers);
    markers = [];
}
function mouseSelectsf() {
    var btn = $(".select-sf");
    var pop = $(".shengfen-pop");
    btn.hover(function () {
        pop.fadeIn(200);
    }, function () {
        pop.fadeOut(200);
    });
}