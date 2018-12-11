<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
    <title>活动详情</title>
    <link rel="stylesheet" type="text/css" href="/static/css/common/bace.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/common/com.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/school-article.css" />
    
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
        <div class="main-con">
            <div class="w1200">
                <div class="article clearfix">
                    <div class="tit1" id="tit"></div>
                    <div class="foo"></div>
                    <div class="cont" id="cont">
                    </div>
                    <!-- JiaThis Button BEGIN -->
                    <div class="jiathis_style_32x32 clearfix">
                        <a class="jiathis_button_qzone"></a><a class="jiathis_button_tsina"></a><a class="jiathis_button_tqq">
                        </a><a class="jiathis_button_weixin"></a><a class="jiathis_button_renren"></a><a
                            href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis"
                            target="_blank"></a><a class="jiathis_counter_style"></a>
                    </div>
                    <script type="text/javascript" src="http://v3.jiathis.com/code/jia.js" charset="utf-8"></script>
                    <!-- JiaThis Button END -->
                </div>
            </div>
        </div>
    </div>
    <!-- 尾部 -->
    <script type="text/javascript" src="/static/head/js/foot.js"></script>
</body>


<script type="text/javascript" src="/static/js/school/hqzx.js"></script>
<script type="text/javascript" src="/static/js/school/school-article.js"></script>
<script type="text/javascript" src="/static/js/school/formatJsonDateTime.js"></script>

<!-- footer js start -->
<script src="/static/js/common/placeholder_IE.js"></script>
<script src="/static/js/common/com-fn.js"></script>
<!-- footer js end -->
</html>