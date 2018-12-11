var map, geolocation;
//getjson同步执行
$.ajaxSettings.async = false;
//获取浏览器 的经纬度参数
var latitude = getParameter("lati");
var longitude = getParameter("long");
var currentlng;
var currentlat;
//定位到当前城市
var map = new AMap.Map("container", {
    resizeEnable: true,
    zoom: 13 //地图显示的缩放级别
});
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
    AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
});

//解析定位结果
function onComplete(data) {
    //根据当前定位（经纬度） 获得附近校区
    currentlng = data.position.getLng();
    currentlat = data.position.getLat();
}
var transOptions = {
    map: map,
    panel: 'panel',
    //cityd:'乌鲁木齐',
    policy: AMap.TransferPolicy.LEAST_TIME
};
//构造公交换乘类
var transfer = new AMap.Transfer(transOptions);
var school_lng=0;
var school_lat=0;

$.ajax({
    type: "GET",
    url: "http://restapi.amap.com/v3/assistant/coordinate/convert?coordsys=baidu&key=fd7a313b9dd5d0cfdd4c97414fe89fbf&locations=" + longitude + "," + latitude,
    dataType: "json",
    beforeSend: function () {
    },
    success: function (data) {
        if (data.status == 1) {
            var lnglat = data.locations.split(",");
            school_lng = lnglat[0];
            school_lat = lnglat[1];
        }
    },
    complete: function () {

    },
    error: function (data) {
        alert(data.msg);
    }
});
//根据起、终点坐标查询公交换乘路线
//transfer.search(new AMap.LngLat(116.379028, 39.865042), new AMap.LngLat(116.427281, 39.903719));
transfer.search(new AMap.LngLat(currentlng, currentlat),new AMap.LngLat(school_lng,school_lat));
