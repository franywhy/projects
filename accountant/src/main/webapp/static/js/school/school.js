var marker, map = new AMap.Map("container", {
    resizeEnable: true,
    zoom: 13
});
$.ajaxSettings.async = false;
var totalData;
var markers = [];

var longitude = $.cookie('hqonline.location.longitude');
var latitude = $.cookie('hqonline.location.latitude');

function addMarker(parm,address,name,telephone,first) {
    var title = name+"  "+telephone+"  "+address;
    if (marker) {
        return;
    }
    if(parm){
        var array = parm.split(",");
        var position = [ ];
        for (var k=0 ; k< array.length ;k++){
            position.push(array[k]);
        }

        var marker = new AMap.Marker({
            bubble:true,
            title:title,
            position:position,
            //animation:'AMAP_ANIMATION_BOUNCE',
            label:{//label默认蓝框白底左上角显示，样式className为：amap-marker-label
                offset: new AMap.Pixel(20, 20),//修改label相对于maker的位置
                content: name
            },
            map:map
        });
        markers.push(marker);
        
        AMap.event.addListener(marker, 'click', function() {
            openInfo(marker,address,name,telephone);
        });
        
        if(first){
        	openInfo(marker,address,name,telephone);
        }
    }
};

//点击定位点显示弹框
//var infoWindow;
function openInfo(obj,address,name,telephone) {
    //构建信息窗体中显示的内容
    var info = [];
    info.push("<div>"+
			    "<p style='font-size:16px;color:#27ae61;line-height:28px'>"+name+"</p>"+
			    "<p style='font-size:12px;line-height:22px;color:#666;'><span style='color:#000;'>校区地址：</span>"+address+"</p>"+
			    "<p style='font-size:12px;line-height:22px;color:#666;'><span style='color:#000;'>咨询电话：</span>"+telephone+"</p>"+
			  "</div>");
    var infoWindow = new AMap.InfoWindow({
    	offset: new AMap.Pixel(0,-30),
        content: info.join("<br/>")  //使用默认信息窗体框样式，显示信息内容
    });
    infoWindow.open(map, obj.getPosition());
}

core.ajax.getResult({
    url: hqonline.config.apiurl+"school/get_nearby_city?longitude="+longitude+"&latitude="+ latitude,
    requireToken: false,//是否需要access_token
    dataType : "json",
    type: "GET",
    success: function(data) {
        var row = data.data.nearby_city_andschoollist;
        var schoollist = data.data.nearby_city_andschoollist.schoolist;
        var rows= data.data.citylist;
        //所有城市信息
        totalData = rows;
        if(rows.length){
        	
        	console.log(rows);
            //城市
            $("#around_city_list").empty();
            $("#around_city_list").append('<dd id="">全部校区</dd>');
            for(var i=0;i<rows.length;i++){
                if(rows[i]){
                    if (row.code == rows[i].code) {
                        $(document).attr("title", "恒企" + rows[i].name + "校区地址,恒企" + rows[i].name + "校区电话,恒企" + rows[i].name + "校区联系方式- 恒企会计培训 - 恒企教育官网 - 恒企在线");
                        $("#around_city_list").append('<dd class="Campus-A-navs-active" id="'+rows[i].code+'">' +rows[i].name+'</dd>');
                    }else{
                        $("#around_city_list").append('<dd id="'+rows[i].code+'">' +rows[i].name+'</dd>');
                    }
                }
            };
            $("#around_city_list").append('<div class="clear"></div>');
            clickEvent();
        }
        if(schoollist.length){
            var count = 1;
            //校区
            $("#city_school_list").empty();
            if(schoollist.length <= 5){
                for(var j=0;j<schoollist.length;j++){
                    if(schoollist[j]){
                        j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                      showSchoolList(schoollist[j]);
                    }
                }
                $("#count").text(count);
                $(".tcdPageCode").hide();
            }else{
                for(var j=0;j<5;j++){
                    if(schoollist[j]){
                        j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                      showSchoolList(schoollist[j]);
                    }
                }
                $("#count").text(count);
            }
            $("#city_school_list").append('<div class="clear"></div>');
            map.setFitView();
            $("#next").click(function() {
                if(!schoollist[count*5]){
                    alert("后面没有了");
                }else{
                    count++;
                    $("#city_school_list").empty();
                    for(var j=(count-1)*5;j<(count*5);j++){
                        if(schoollist[j]){
                            j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                          showSchoolList(schoollist[j]);
                        }
                    }
                    $("#count").text(count);
                }
            });
            $("#prev").click(function() {
                if(count == 1){
                    alert("前面没有了");
                }else{
                    count--;
                    $("#city_school_list").empty();
                    for(var j=(count-1)*5;j<(count*5);j++){
                        if(schoollist[j]){
                            j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                          showSchoolList(schoollist[j]);
                        }
                    }
                    $("#count").text(count);
                }
            })
        }
    },
    resolveError : function (data) {
        alert(data.msg);
    }
});

//显示校区信息
function showSchoolList(obj,first){
    var name = obj.name==undefined?"":obj.name;
    var address = obj.address==undefined?"":obj.address;
    var telephone = obj.telephone==undefined?"":obj.telephone;


//    $.get("http://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=baidu&key=fd7a313b9dd5d0cfdd4c97414fe89fbf&locations="+obj.location.longitude+","+obj.location.latitude,function(res) {
//        if(res.status==1){
//            //添加标记
//            addMarker(res.locations,address,name,telephone,first);
//        }
//    },"json");
    core.ajax.getResult({
        url: "http://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=baidu&key=fd7a313b9dd5d0cfdd4c97414fe89fbf&locations="+obj.location.longitude+","+obj.location.latitude,
        requireToken: false,//是否需要access_token
        dataType : "json",
        type: "GET",
        success: function(data) {
            if(data.status==1){
                //添加标记
                addMarker(data.locations,address,name,telephone,first);
            }
        },
        resolveError : function (data) {
            alert(data.msg);
        }
    });
    var xqaddress =obj.address==undefined?"":obj.address;
    var xqtelephone = obj.telephone==undefined?"":obj.telephone;
    $("#city_school_list").append('<li>'
        +'<div class="C-A-right-con-h1">' + name + '</div>'
        +'<a href="schoolInfo.html?code=' + obj.code + '" target="_blank"><div class="C-A-right-con-href">查看校区课程</div></a>'
        +'<div class="C-A-right-con-h2">' + xqtelephone + '</div>'
        +'<div class="C-A-right-con-h3">' + xqaddress + '</div>'
        +'</li>');
        

}
//切换城市-校区信息
function clickEvent(){
    $(".Campus-A-navs dd").click(
        function (e) {
            var cityid = $(this).attr("id");
            var Campus_A_navs_dd_index = 0;
            Campus_A_navs_dd_index = $(this).index();
            $(".Campus-A-navs dd").eq(Campus_A_navs_dd_index).addClass("Campus-A-navs-active").siblings().removeClass("Campus-A-navs-active")
            $(".Campus-A-con dl dd").eq(Campus_A_navs_dd_index).show().siblings().hide();
            getListById(cityid);
        }
    );
}
//根据城市编号，改变校区区域信息及标志
function getListById(id){
    if(id){
        for(var a=0;a<totalData.length;a++){
            if(totalData[a].code==id){
                var count = 1;
                var schoollist = totalData[a].schoolist;
                $("#city_school_list").empty();
                removeMarker();
                if(schoollist.length <= 5){
                    for(var j=0;j<schoollist.length;j++){
                        if(schoollist[j]){   
                        	j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                          showSchoolList(schoollist[j]);
                        }
                    }
                    $("#count").text(count);
                    $(".tcdPageCode").hide();
                }else{
                    for(var j=0;j<5;j++){
                        if(schoollist[j]){
                        	j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                          showSchoolList(schoollist[j]);
                        }
                    }
                    $("#count").text(count);
                }
                $("#city_school_list").append('<div class="clear"></div>');
                map.setFitView();
            }
        }
    }else{
        removeMarker();
        $("#city_school_list").empty();
        for(var a=0;a<totalData.length;a++){
            var schoollist = totalData[a].schoolist;
            if(schoollist.length <= 5){
                for(var j=0;j<schoollist.length;j++){
                    if(schoollist[j]){
                    	j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                      showSchoolList(schoollist[j]);
                    }
                }
                $("#count").text(count);
                $(".tcdPageCode").hide();
            }else{
                for(var j=0;j<5;j++){
                    if(schoollist[j]){
                    	j == 0 ? showSchoolList(schoollist[j],true) : showSchoolList(schoollist[j]);
//                      showSchoolList(schoollist[j]);
                    }
                }
                $("#count").text(count);
            }
            $("#city_school_list").append('<div class="clear"></div>');
            map.setFitView();
        }
    }
}
//清空所有标志
function removeMarker(){
    map.remove(markers);
    markers = [];
}



$(function(){



})


