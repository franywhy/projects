var map, geolocation;
//获取浏览器 的经纬度参数
var address = getParameter("address");

//定位到当前城市
var map = new AMap.Map("container", {
    resizeEnable: true,
    zoom: 13 //地图显示的缩放级别
});
var transOptions = {
    map: map,
    panel: 'panel1'
    //cityd:'乌鲁木齐',
    //policy: AMap.TransferPolicy.LEAST_TIME
};
var drivingOptions = {
    map: map,
    panel: 'panel2'
    //cityd:'乌鲁木齐',
    //policy: AMap.TransferPolicy.LEAST_TIME
};
//构造公交换乘类
var transfer;
var driving;
AMap.service('AMap.Transfer', function () {//回调函数
    //实例化Transfer
    transfer = new AMap.Transfer(transOptions);
    //TODO: 使用transfer对象调用公交换乘相关的功能
});
AMap.service('AMap.Driving', function () {//回调函数
    //实例化Driving
    driving = new AMap.Driving(drivingOptions);
    //TODO: 使用driving对象调用驾车路径规划相关的功能
});
AMap.plugin('AMap.Autocomplete', function () {//回调函数
    //实例化Autocomplete
    var autoOptions = {
        city: "", //城市，默认全国
        input: "begin_pos"//使用联想输入的input的id
    };
    autocomplete = new AMap.Autocomplete(autoOptions);
    //TODO: 使用autocomplete对象调用相关功能
})

//根据起、终点坐标查询公交换乘路线
$("#end_pos").val(decodeURI(address));
$("#search_route").click(function () {
    var start = $("#begin_pos").val();
    var end = $("#end_pos").val();
    var index = $(".way_tabs .on").index();
    
    if (1 == index) {
        map.clearMap();
        driving.search([{ keyword: start }, { keyword: end}]);
        $("#panel1").hide();
        $("#panel2").show();
    } else {
        map.clearMap();
        transfer.search([{ keyword: start }, { keyword: end}]);
        $("#panel1").show();
        $("#panel2").hide();
    }
});
//transfer.search(new AMap.LngLat(currentlng, currentlat),new AMap.LngLat(school_lng,school_lat));
$(".way_tabs li").on("click", function () {
    var i = $(this).index();
    var start = $("#begin_pos").val();
    var end = $("#end_pos").val();
    $(this).addClass("on").siblings().removeClass("on");
    if (1 == i) {
        map.clearMap();
        driving.search([{ keyword: start }, { keyword: end}]);
        $("#panel1").hide();
        $("#panel2").show();
    } else {
        map.clearMap();
        transfer.search([{ keyword: start }, { keyword: end}]);
        $("#panel1").show();
        $("#panel2").hide();
    }
});
