<!DOCTYPE html>
<html lang="zh">
<head>
	<title>恒企教育 _ 恒企会计培训 - 把经验传递给有梦想的人！</title>
    <meta name="Keywords" content="恒企教育,会计培训,恒企会计培训学校,恒企会计,恒企教育官网,会计证,会计培训机构,会计网校,会计直播公开课" />
    <meta name="Description" content="恒企教育是国内上市的会计培训机构，国家高新企业！15年来通过连锁经营方式专注会计职业教育，255家连锁校区遍布全国，助力百万学子成就会计梦想。提供会计职称、会计实战、学历教育等快速成才课程，为大量企业培养实战型会计人才。学会计就选上市公司，掌握上市公司财务经验，上岗升职加薪快！" />
    <link rel="stylesheet" type="text/css" href="${STATIC_COMMON}/css/course-ty-1.css"/>
    <link rel="stylesheet" type="text/css" href="${STATIC_COMMON}/css/index.css" />
    
    <!-- header css start -->
    <link rel="stylesheet" type="text/css" href="${STATIC_COMMON}/css/common/bace.css"/>
	<link rel="stylesheet" type="text/css" href="${STATIC_COMMON}/css/common/com.css"/>
	<link rel="stylesheet" type="text/css" href="${STATIC_COMMON}/css/common/headerAfooter.css"/>
	<link rel="stylesheet" type="text/css" href="${STATIC_COMMON}/css/loginNew.css"/>
	<style>
		#logs_nav,#logs_nav ul,#logs_nav ul li,#logs_nav ul li img{
			margin: 0;
			padding: 0;
			border: none;
		}
		#logs_nav{
			width: 1200px;
			height:360px;
			margin: 0 auto;
			text-align: center;
		}
		#logs_nav h2{
			font-size: 74px;
			margin:60px 0;
			font-weight: bold;
		}
		#logs_nav ul li{
			text-align: center;
			display: inline-block;
			list-style: none;   
		}
		#logs_nav ul li+li{
			margin-left: 75px;
		}
		#logs_nav ul li a{
			display: block;
			text-decoration: none;
			color: #6e6e6e;
		}
		#logs_nav ul li a img{

			vertical-align: top;
			border-radius:24%;
		}
		#logs_nav ul li a img:hover{
			box-shadow: 0 0 60px #979797;
		}
		#logs_nav ul li a p{
			margin-top:20px;	
		}

		#bg{
			background:#f8f8f8;
		}
	</style>
	<!-- header css end -->
	
	<script type="text/javascript" src="${STATIC_COMMON}/js/jquery.js"></script>
	<script type="text/javascript" src="${STATIC_COMMON}/head/js/baidu_statistics.js"></script>

</head>
<body>
	<!-- 导航栏 -->
	<#include "../commons/header.ftl">
	<!-- 主页 -->
	
    <script type="text/javascript">
        $('.nav li').eq(0).addClass('act');//头部导航选中状态
    </script>
    
    <!--内容-->
    <div class="mainCon index-mainCon relative">
        <!--新年背景-->
		<!--<div class="newyear-bg"></div>-->
		
        <div class="slide-banner">
        	<#if banners??>
        		<ul class="slide-ul clearfix">
        			<#-- 迭代banner -->
        			<#list banners as banner>
        				<#assign banner_url = (banner.imgurl!'')>
						<#if banner.imgurl==''>
							<#assign banner_url = "javascript:void(0);">
						</#if>
        				<a href="${banner_url}" target="_blank">
		                    <li class="slide-li" style="background: url(${imageDomain}${banner.imgaddress}) no-repeat center 0;background-size:cover; ">
		                    </li>
		                </a>
        			</#list>
	            </ul>
        	</#if>
            
            <dl class="slide-dl clearfix" style=" width:100px;">
				<#if banners??>
					<#-- 迭代banner-->
					<#list banners as banner>
						<#-- banner默认选中样式-->
						<#if banner_index == 0>
							<dd class="slide-dd act">
						<#else>	
							<dd class="slide-dd">
						</#if>
					</#list>
				</#if>
            </dl>
        </div>
        
        <!--<div class="main-a">
				<div class="w1200">
					<ul class="clearfix">
                    <% if (banners1 != null)
                               {
                                   foreach (KJCity.Core.Model.newsadvertModel model in banners1)
                                   {
                            %>
                            <a href="<%=model.imgurl %>" target="_blank"><li class="t-li-1">
							<img src="<%=UtCommon.IMAGE_DOMAIN %><%=model.imgaddress %>"/>
						</li></a>
                            <%
                                }
                           }
                            %>
					</ul>
				</div>
			</div>

        
        <div class="main-b main-pad">
            <div class="w1200">
                <div class="main-title">
                    <h1>
                        热门课程</h1>
                </div>
                <div class="main-big-box">
                    <div class="main-course-box-out">
                        <div class="main-course-box clearfix">
                        <% if (banners2 != null)
                               {
                                   foreach (KJCity.Core.Model.newsadvertModel model in banners2)
                                   {
                            %>
                            <a href="<%=model.imgurl %>" title="<%=model.title %>" target="_blank">
    <div class="c-box">
        <div class="c-box-top relative">
            <img src="<%=UtCommon.IMAGE_DOMAIN %><%=model.imgaddress %>">
        </div>
        <div class="c-box-bot">
            <div class="c-box-h1">
                <%=model.title %>
            </div>
            <div class="c-box-h2">
                <p class="left"><%=model.remark%>
                    </p>
                <div class="clear">
                </div>
            </div>
        </div>
    </div>
</a>
                            <%
                                }
                           }
                            %>
                        </div>
                        <div class="main-course-box clearfix none">
                        </div>
                    </div>
                </div>
            </div>
        </div>-->
        
        <!--
        <div class="main-c main-pad">
            <div class="w1200">
                <div class="main-title">
                    <h1>
                        我们的服务</h1>
                    <h2>
                        上岗就业晋升，一路贴心为您</h2>
                </div>
                <ul class="clearfix">
                <li>
                        <dl class="t-dl-2">
                            职业<br />
                            规划</dl>
                        <p>
                            个性化职业成长方案<br />
                            分阶段免费推送</p>
                    </li>
                    <li>
                        <dl class="t-dl-1">
                            020<br />
                            教学</dl>
                        <p>
                            构建知识网络体系<br />
                            自适应高效学习</p>
                    </li>
                    
                    
                    <li>
                        <dl class="t-dl-4">
                            名师<br />
                            答疑</dl>
                        <p>
                            名师在线一对一答疑<br />
                            扫清任何疑难杂症</p>
                    </li>
                    <li>
                        <dl class="t-dl-5">
                            考试<br />
                            题库</dl>
                        <p>
                            大数据题库，智能化服务<br />
                            刷新考试通过率</p>
                    </li>
                    <li>
                        <dl class="t-dl-6">
                            真账<br />
                            实操</dl>
                        <p>
                            真实还原工作场景<br />
                            工作经验轻松积累</p>
                    </li>
                    <li>
                        <dl class="t-dl-3">
                            终生<br />
                            成长</dl>
                        <p>
                            从上岗到高手<br />
                            相伴职业终生成长</p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="main-d main-pad">
            <div class="w1200">
                <div class="main-title">
                    <h1>
                        恒企教育，把经验传递给有梦想的人</h1>
                    <h2>
                        专业、专注、全面、权威，赢得学员信任</h2>
                </div>
                <ul class="clearfix">
                    <li>
                        <dl class="t-dl-1">
                            120万</dl>
                        <h1>
                            1200000学员</h1>
                        <h2>
                            累计帮助上百万学员走上会计岗位</h2>
                    </li>
                    <li>
                        <dl class="t-dl-2">
                            200+</dl>
                        <h1>
                            超200家校区</h1>
                        <h2>
                            覆盖全国23个省（包含直辖市、自治区），连锁经营，统一优质服务</h2>
                    </li>
                    <li>
                        <dl class="t-dl-3">
                            15</dl>
                        <h1>
                            15年专注会计领域</h1>
                        <h2>
                            中国会计培训行业者，专业、快捷、高效</h2>
                    </li>
                    <li class="clear">
                        <dl class="t-dl-4">
                            100</dl>
                        <h1>
                            100位优秀学员档案</h1>
                        <h2>
                            选择恒企，在这里学到的即是工作需要的</h2>
                    </li>
                    <li>
                        <dl class="t-dl-5">
                            10</dl>
                        <h1>
                            10项无忧学习服务</h1>
                        <h2>
                            保就业，分期付款，免费重学……</h2>
                    </li>
                    <li>
                        <dl class="t-dl-6">
                            200</dl>
                        <h1>
                            200位专家名师团</h1>
                        <h2>
                            聆听教授、财务总监的实战课</h2>
                    </li>
                </ul>
            </div>
        </div>


        <div class="main-e main-pad">
				<div class="w1200">
					<div class="main-title">
						<h1>恒企媒体报道</h1>
					</div>
					<ul class="clearfix">
						<a href="http://www.hengqijy.com/content-5-1567-1.html" target="_blank"><li class="t-li-1 hover-shadow">
							<dl>新法制报</dl>
							<p>恒企教育：振兴中国职业教育，打造一流教育品牌</p>
						</li></a>
						<a href="http://edu.qq.com/a/20131205/016725.htm" target="_blank"><li class="t-li-2 hover-shadow">
							<dl>腾讯教育</dl>
							<p>专访恒企教育集团：把经验传给有梦想的人</p>
						</li></a>
						<a href="http://hb.sina.com.cn/edu/news/2014-12-30/103338732.html?from=wap" target="_blank"><li class="t-li-3 hover-shadow">
							<dl>新浪湖北</dl>
							<p>"恒企杯"湖北省大学生会计技能大赛落幕</p>
						</li></a>
						<a href="http://learning.sohu.com/20160817/n464658575.shtml" target="_blank"><li class="t-li-4 hover-shadow no-mar">
							<dl>搜狐教育</dl>
							<p>恒企教育助力“2016我是好讲师”</p>
						</li></a>
						<a href="http://news.163.com/16/1129/16/C72698GL000187VG.html" target="_blank"><li class="t-li-5 hover-shadow">
							<dl>网易新闻</dl>
							<p>恒企教育首谈老师培养：幸为师、诚为师、勤为师、慎为师</p>
						</li></a>
						<a href="http://edu.qq.com/a/20160901/024556.htm" target="_blank"><li class="t-li-6 hover-shadow">
							<dl>腾讯教育</dl>
							<p>恒企教育与海致BDP合作 深耕教育培训O2O模式</p>
						</li></a>
						<a href="http://video.sina.com.cn/view/250860564.html?from=singlemessage" target="_blank"><li class="t-li-7 hover-shadow">
							<dl>新浪视频</dl>
							<p>新浪2016中国教育盛典：恒企教育总裁助理何小跃</p>
						</li></a>
						<a href="http://xw.qq.com/edu/20161201019002/EDU2016120101900200?from=singlemessage" target="_blank"><li class="t-li-8 hover-shadow no-mar">
							<dl>腾讯教育</dl>
							<p>恒企教育总裁助理何小跃：塑造品牌注重产品</p>
						</li></a>
					</ul>
				</div>
			</div>

        <div class="course-j main-pad">
				<div class="w1200">
					<div class="course-title">
						<div class="t-h1">学会计必选上市公司，百万学子共同见证</div>
						<div class="t-h3">我们始终坚持用专业的力量，成就每一个平凡的梦想</div>
					</div>
					<div class="cont">
						<div class="text">以下仅为部分荣誉，成长从未止步 </div>
						<ul class="clearfix">
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-1.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中国企业战略联盟</div>
								<div class="t-h3">竞争力行业品牌</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-2.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中国企业战略联盟</div>
								<div class="t-h3">中国著名品牌</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-3.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中国企业质量信用备案公示网</div>
								<div class="t-h3">AAA级信用企业</div>
							</li>
							<div class="clear"></div>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-4.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中国企业质量信用备案公示网</div>
								<div class="t-h3">全国315质量服务客户满意诚信企业</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-5.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">十年价值职业教育品牌</div>
								<div class="t-h3"></div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-6.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中华人民共和国国家版权局著作权登记证书</div>
								<div class="t-h3">2012年-2013年，恒企研发的7大课程教材</div>
							</li>
							<div class="clear"></div>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-7.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">百度金融</div>
								<div class="t-h3">“教育信贷合作伙伴”</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-8.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">上海市合同信用促进会</div>
								<div class="t-h3">2014-2015年度合同信用等级为AA级</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-8.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">上海市合同信用促进会</div>
								<div class="t-h3">2014-2015年度上海市守合同重信用企业</div>
							</li>
							<div class="clear"></div>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-9.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中国培训发展研究中心</div>
								<div class="t-h3">重视人才发展企业奖</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-6.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">中华人民共和国国家版权局著作权登记证书</div>
								<div class="t-h3">2016年，恒企研发2大课程教材</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-10.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">2016年新浪教育盛典</div>
								<div class="t-h3">2016中国品牌知名度职业培训机构</div>
							</li>
							<div class="clear"></div>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-11.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">2016年腾讯教育年度盛典</div>
								<div class="t-h3">“2016年度特色职业教育品牌”</div>
							</li>
							<li class="left">
								<div class="t-h1"><img class="v-mid" src="${STATIC_COMMON}/images/temp/index-ssn-img-12.png"/><i class="v-mid-a"></i></div>
								<div class="t-h2">2016年网易教育年度大选</div>
								<div class="t-h3">“2016年度最受信赖职业教育品牌”</div>
							</li>
							<li class="left">
								<div class="last-text">以上仅为部分荣誉，成长从未止步</div>
							</li>
						</ul>
						
					</div>
				</div>
			</div>-->
			
        <div class="tuijian-main w1200-out">
				<div class="w1200 clearfix">
					<div class="t-h t-h1 relative left" onclick="NTKF_kf()">
						<div class="t-p t-p1">恒企秘方</div>
						<div class="t-p t-p2">经验十一关，变身企业财务精英</div>
						<div class="hidden-box absolute">
							<div class="t-d t-d1">恒企秘方</div>
							<div class="t-d t-d2">历时8年，创建“经验十一关”，融合恒企自身企业发展各阶段的会计经验、会计方法和会计工具，帮助零基础学员步步闯关，修炼成企业财务精英</div>
						</div>
					</div>
					<div class="t-h t-h2 relative left" onclick="NTKF_kf();">
						<div class="t-p t-p1">考霸魔方</div>
						<div class="t-p t-p2">把教材变薄，您就是考试大赢家</div>
						<div class="hidden-box absolute">
							<div class="t-d t-d1">考霸魔方</div>
							<div class="t-d t-d2">由恒企考霸教练经20年实战经验总结而出，贯穿会计生涯考证课程，把教材变薄，把考点集中，十六级进阶，层层提分，考试势如破竹！</div>
						</div>
					</div>
                    <div class="t-h t-h3 relative left no-mar-right" onclick="NTKF_kf();">
						<div class="t-p t-p1">名校直通车</div>
						<div class="t-p t-p2">轻松提升学历，工作住房不再受限制</div>
						<div class="hidden-box absolute">
							<div class="t-d t-d1">名校直通车</div>
							<div class="t-d t-d2">恒企与几十家知名财经院校都签订了教研合作和联合办学的协议，建立成人教育、网络教育、自学考试，快速实现您高升专、专升本、本升研的名校梦！</div>
						</div>
					</div>
				</div>
			</div>
			<#--
			<div class="kscc-main main-pad w1200-out">
				<div class="w1200 clearfix">
					<div class="course-title">
						<div class="t-h1">恒企教育十万年薪闯关系统</div>
						<div class="t-h3">学经验，考证书，升学历，<br>一站实现你10万年薪的会计梦</div>
					</div>
					<div class="cont relative">
						<div class="btn absolute"></div>
					</div>
				</div>
			</div>
			-->
			
			<div class="xkj-main main-pad w1200-out">
				<div class="w1200 clearfix">
					<div class="course-title">
						<div class="t-h1">学会计<br>选上市公司的教育机构</div>
						<#--<div class="t-h3">上市公司的教育机构更靠谱</div>-->
					</div>
					<div class="cont">
						<img src="${STATIC_COMMON}/images/index/xkj-main-img1.png"/>
					</div>
				</div>
			</div>
			
			<div class="showPower-main main-pad w1200-out">
				<div class="w1200 clearfix">
					<div class="cont">
						<div class="v-mid t-h">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/showPower-main-icon1.png"/></div>
							<div class="t-p2">16<span>年</span></div>
							<div class="t-p3">财务实战经验</div>
						</div>
						<div class="v-mid t-h">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/showPower-main-icon2.png"/ style="padding-top: 2px;"></div>
							<div class="t-p2">400<span>家</span></div>
							<div class="t-p3">全国连锁校区</div>
						</div>
						<div class="v-mid t-h">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/showPower-main-icon3.png"/ style="padding-top: 4px;"></div>
							<div class="t-p2">150<span>座</span></div>
							<div class="t-p3">全国覆盖城市</div>
						</div>
						<div class="v-mid t-h">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/showPower-main-icon4.png"/ style="padding-top: 4px;"></div>
							<div class="t-p2">9<span>大</span></div>
							<div class="t-p3">著作版权证书</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="ttl-main main-pad w1200-out">
				<div class="w1200">
					<div class="course-title">
						<div class="t-h1">好老师<br>让您学到真经验，学习更轻松！</div>
						<#--<div class="t-h3">让您学到真经验，学习更轻松！</div>-->
					</div>
					<div class="cont">
						<div class="t-h t-h1">
							<i class="v-mid t-i1"></i>
							<i class="v-mid t-i2">
								<div class="t-p1">好老师=严格考核，让您学到真经验</div>
								<div class="t-p2">我们制定了“星级评定体系”和“持证上岗体系”，每一位老师都需要经过总部的层层考核，让您真正享受优质老师的教学体验！</div>
							</i>
						</div>
						<div class="t-h t-h2">
							<i class="v-mid t-i1"></i>
							<i class="v-mid t-i2">
								<div class="t-p1">好老师=善用方法，让您爱上学习</div>
								<div class="t-p2">我们的老师善于灵活使用八大教学方法，包括游戏课程、场景教学、情景教学、沙盘演练、翻转课堂、项目贯穿、企业游学以及班组长责任制，不同的课程采用不同的教学方式，让您摆脱死记硬背，爱上恒企课堂！</div>
							</i>
						</div>
						<#--
						<div class="t-h t-h3">
							<i class="v-mid t-i1"></i>
							<i class="v-mid t-i2">
								<div class="t-p1">锦旗千余面+好评度95.5%</div>
								<div class="t-p2">每年恒企全国各地校区都会收到学员锦旗千余面，每一面锦旗都是对恒企老师教学质量的真实肯定。在每年举行的老师测评报告中，学员对恒企老师的好评度更是高达95.5%！</div>
							</i>
						</div>
						-->
					</div>
				</div>
			</div>
			
			<div class="qwmt-main main-pad w1200-out">
				<div class="w1200">
					<div class="course-title">
						<div class="t-h1">权威媒体发声</div>
					</div>
					<div class="cont">
						
					</div>
				</div>
			</div>

			<div id="logs_nav">
							<h2>恒企教育旗下品牌</h2>
							<ul>
								<!-- 恒企会计 -->
								<li>
									<a href="http://www.hqjy.com/"  target="_blank">
										<img src="${STATIC_COMMON}/images/hq_logos_03.png" alt="">
										<p>恒企会计</p>
									</a>
								</li>
								<!-- 恒企自考 -->
								<li>
										<a href="http://zikao.hqjy.com/"  target="_blank">
											<img src="${STATIC_COMMON}/images/zh_logos_05.png" alt="">
										<p>恒企自考</p>
										</a>
								</li>
								<!-- 牵引力 -->
								<li>
										<a href="http://gz.qyl.hengqiedu.cn/"  target="_blank">
											<img src="${STATIC_COMMON}/images/duodi_logos_07.png" alt="">
										<p>牵引力</p>
										</a>
								</li>
								<!-- 天琥设计 -->
								<li>
										<a href="http://www.tianhujy.com/"  target="_blank">
											<img src="${STATIC_COMMON}/images/tianhu_logos.png" alt="">
										<p>天琥设计</p>
										</a>
								</li>
							
									<!--  学来学往 -->
									<li>
											<a href="http://www.xuelxuew.com/"  target="_blank">
												<img src="${STATIC_COMMON}/images/xlxw_logos.png" alt="">
											<p> 学来学往</p>
											</a>
									</li>
							</ul>   
   
   
   
   
   
    </div>










			<div class="hyjx-main main-pad w1200-out" id="bg">
				<div class="w1200">
					<div class="course-title">
						<div class="t-h1">行业奖项一览</div>
					</div>
					<div class="cont clearfix">
						<div class="t-h t-h1 left">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/hyjx-main-img1.png"></div>
							<div class="t-p2">2017年度影响力<br>科技创新教育品牌</div>
						</div>
						<div class="t-h t-h2 left">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/hyjx-main-img2.png"></div>
							<div class="t-p2">2017年度品牌<br>影响力教育机构</div>
						</div>
						<div class="t-h t-h3 left no-mar-right">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/hyjx-main-img3.png"></div>
							<div class="t-p2">2017中国品牌<br>价值教育连锁机构</div>
						</div>
						<div class="t-h t-h4 left no-mar-bot">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/hyjx-main-img4.png"></div>
							<div class="t-p2">世界创新500强</div>
						</div>
						<div class="t-h t-h4 left no-mar-bot">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/hyjx-main-img5.png"></div>
							<div class="t-p2">工业和信息化领域急需紧缺人才培养工程<br>岗位胜任力提升计划示范培训基地</div>
						</div>
						<div class="t-h t-h4 left no-mar-right no-mar-bot">
							<div class="t-p1"><img src="${STATIC_COMMON}/images/index/hyjx-main-img6.png"></div>
							<div class="t-p2">中央财经大学<br>创新教研合作基地</div>
						</div>
					</div>
				</div>
			</div>
			
    </div>
	<#--
    <div class="black-bg"></div>
	<div class="video-box">
		<video width="1000" src="${STATIC_COMMON}/images/index/kscc-mp4.mp4" controls="controls" preload="auto"></video>
		<div class="close-btn">&times;</div>
	</div>
	-->
    <script type="text/javascript" src="${STATIC_COMMON}/js/index.js"></script>
    <script type="text/javascript" src="${STATIC_COMMON}/head/js/foot.js"></script>
</body>
</html>
