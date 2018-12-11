<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
    <title>校区详情</title>
    <link rel="stylesheet" type="text/css" href="/static/css/common/bace.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/common/com.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/school-detail.css">
    <script type="text/javascript" src="/static/js/jquery.js"></script>
    <!-- header css start -->
	<link rel="stylesheet" type="text/css" href="/static/css/common/headerAfooter.css"/>
	<link rel="stylesheet" type="text/css" href="/static/css/loginNew.css"/>
	<script type="text/javascript" src="${STATIC_COMMON}/head/js/baidu_statistics.js"></script>
	<!-- header css end -->
</head>
<body>
<!-- 导航栏 -->
	<#include "../commons/header.ftl">

<script type="text/javascript">
    $('.nav li').eq(4).addClass('act'); //头部导航选中状态
    </script>
    <!--内容-->
    <div class="mainCon">
        <div class="main-bar-a">
            <div class="w1200">
                <a href="/index.html">首页</a> > <a href="/school">校区信息</a> > <span class="school_name"></span>
            </div>
        </div>
        <div class="Campus-B-main">
            <div class="w1200 marginAuto">
                <div class="Campus-B-con-1">
                    <div class="b-con-h1" id="school_name">
                        </div>
                    <dl class="b-con-1-dl">
                        <dd>
                            <div class="b-con-1-dd-h1">
                                学校地址</div>
                            <div class="b-con-1-dd-h2">
                                <span id="schoo_address"></span></div>
                        </dd>
                        <dd>
                            <div class="b-con-1-dd-h1">
                                咨询电话</div>
                            <div class="b-con-1-dd-h2">
                                <span id="school_tel"></span></div>
                        </dd>
                        <dd>
                            <div class="b-con-1-dd-h1">
                                工作时间</div>
                            <div class="b-con-1-dd-h2">
                                周一至周五：9：00-18：30
                            </div>
                            <div class="b-con-1-dd-h2">
                                周六至周日：8：00-18：30</div>
                        </dd>
                        <div class="clear">
                        </div>
                    </dl>
                    <a id="checkRoute" class="b-con-1-btn main-btn-w-g" target="_blank">查看校区路线</a>
                </div>
                <div class="Campus-B-con-3">
                    <div class="b-con-h1">
                        校区活动</div>
                    <dl class="b-con-3-dl" id="school_activity">
                    </dl>
                </div>
                <div class="Campus-B-con-4">
                    <div class="b-con-h1">
                        校区环境</div>
                    <dl class="b-con-4-dl" id="school_enviroment">
                    </dl>
                    <div class="black-bg img-max-pop-out">
                        <div class="img-max-pop">
                            <div class="img-show">
                            </div>
                            <div class="page-pver">
                            </div>
                            <div class="page-next">
                            </div>
                            <div class="i-off">
                                <img src="${STATIC_COMMON}/images/icon/off_btn_icon.png""></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 尾部 -->
    <script type="text/javascript" src="/static/head/js/foot.js"></script>
</body>

<script type="text/javascript" src="/static/js/school/hqzx.js"></script>
<script type="text/javascript" src="/static/js/school/school-detail.js"></script>
<script type="text/javascript" src="/static/js/school/formatJsonDateTime.js"></script>
<!-- footer js start -->
<script src="/static/js/common/placeholder_IE.js"></script>
<script src="/static/js/common/com-fn.js"></script>
<script src="/static/js/loginNew.js"></script>
<!-- footer js end -->
</html>