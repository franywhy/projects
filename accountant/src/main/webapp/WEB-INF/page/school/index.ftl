<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/static/css/common/bace.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/common/com.css" />
	<link rel="stylesheet" type="text/css" href="/static/css/school/map.css" /> 
	<link rel="stylesheet" type="text/css" href="/static/css/school/school-map.css" /> 
  
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=fd7a313b9dd5d0cfdd4c97414fe89fbf"></script>
	
	<!-- header css start -->
	<link rel="stylesheet" type="text/css" href="/static/css/common/headerAfooter.css"/>
	<link rel="stylesheet" type="text/css" href="/static/css/loginNew.css"/>
	<script type="text/javascript" src="${STATIC_COMMON}/head/js/baidu_statistics.js"></script>
	<!-- header css end -->
<head>
	<title>查看校区</title>
</head>
<body>
<!-- 导航栏 -->
	<#include "../commons/header.ftl">

	<script type="text/javascript">
        $('.nav li').eq(4).addClass('act'); //头部导航选中状态
    </script>
    
    <!--内容-->
    <div class="main-bar-a">
        <div class="w1200">
            <a href="/index.html">首页</a> > 校区信息
        </div>
    </div>
    <div class="slide-banner">
        <ul class="slide-ul clearfix" id="slide-ul">
        </ul>
        <dl class="slide-dl clearfix" id="slide-dl">
        </dl>
    </div>
    
    <div class="w1200 marginAuto">
        <div class="map-h1">恒企校区</div>
        <div class="map-top clearfix" id="around_city_list">
        </div>
    </div>
    
    <div class="Campus-A-main">
        <div class="w1200 marginAuto">
            <div class="Campus-A-navs">
                <dl id="around_city_list1">
                </dl>
            </div>
            <div class="Campus-A-con">
                <dl>
                    <dd class="Campus-A-con-dd-active">
                        <div class="C-A-left-con-box">
                            <div  class="C-A-left-con-map" style="height: 100%;">
                                <div id="container">
                                </div>
                            </div>
                        </div>
                        <div class="C-A-right-con-box">
                            <ul class="C-A-right-con-ol" id="city_school_list">
                            </ul>
                            <div class="C-A-right-con-pape">
                            </div>
                        </div>
                        <div class="clear">
                        </div>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
    <!-- 尾部 -->
    <script type="text/javascript" src="/static/head/js/foot.js"></script>
    
</body>
	<script type="text/javascript" src="/static/js/school/hqzx.js"></script>
    <script type="text/javascript" src="/static/js/school/currentLocation.js"></script>
    <script type="text/javascript" src="/static/js/school/school-map.js"></script>
</html>