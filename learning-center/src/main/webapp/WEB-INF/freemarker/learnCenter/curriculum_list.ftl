<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<link rel="stylesheet" type="text/css" href="/resources/lib/boostrap/css/bootstrap-responsive.min.css">
<link rel="stylesheet" type="text/css" href="/resources/lib/boostrap/css/bootstrap.min.css">
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>
    
	<div class="xxzx-container">
    	<div class="con-width">
            <#include "../common/nav.ftl" encoding="UTF-8" parse=true>
            
            <div class="container-right pr">
            	<#if data??>
	            	<#if data.topMsg??>
			            <#if data.topMsg.status != 0>
			            	<div class="container-right-prompt-p">
					            <div class="container-right-prompt" id="prompt">
					                <div class="prompt-text">
					                    <p><a href="${data.topMsg.url}">${data.topMsg.msgContent}</a></p>
					                </div>
					                <span class="close" id="promptCloseBtn">×</span>
					            </div>
				            </div>
			            </#if>
		            </#if>
	            </#if>
            	<div class="my-curriculum-title">
            		<p>我的课程</p>
                </div>
                <#if data??>
					<#if data.live??>
		                <#list data.live as item>
							<#if item.isEffective == 1>
								<a href="/learningCenter/web/live?userplanId=${item.userPlanId}&title=${encodeFun(item.courseTitle)}&isNoClass=${item.isNoClass}" class="curriculum-list">
			                        <p class="curriculum-list-title text-overflow" title="${item.courseTitle}">${item.courseTitle}</p>
			                        <ul class="curriculum-list-prompt">
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-1"></span>
			                            	<#if item.effectiveDuration??>
			                                	<span class="ml-8px">${item.effectiveDuration}</span>
			                                <#else>
			                                	<span class="ml-8px">无效</span>
			                                </#if>
			                            </li>
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-2"></span>
			                                <span class="ml-8px">直播</span>
			                            </li>
			                        </ul>
			                    	<ul class="processingbar-list">
			                            <li>
			                                <div class="processingbar">
			                                    <span>${item.progressRate}%</span>
			                                </div>
			                                <p class="text-center color-999">课程进度</p>
			                            </li>
			                            <#--
			                            <li>
			                                <div class="processingbar">
			                                    <span>${item.participationRate}%</span>
			                                </div>
			                                <p class="text-center color-999">出勤率</p>
			                            </li>
			                            -->
			                            <#if item.completedRate??>
				                            <li>
				                                <div class="processingbar">
				                                    <span>${item.completedRate}%</span>
				                                </div>
				                                <p class="text-center color-999">作业完成率</p>
				                            </li>
			                            </#if>
			                    	</ul>
								</a>
			                <#else>
			                	<div class="d-curriculum-list">
			                        <p class="curriculum-list-title text-overflow" title="${item.courseTitle}">${item.courseTitle}</p>
			                        <ul class="curriculum-list-prompt">
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-1"></span>
			                                <span class="ml-8px">${item.effectiveDuration}</span>
			                            </li>
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-2"></span>
		                                	<span class="ml-8px">直播</span>
			                            </li>
			                        </ul>
			                    	<ul class="processingbar-list">
			                            <li>
			                                <div class="dprocessingbar">
			                                    <span>${item.progressRate}%</span>
			                                </div>
			                                <p class="text-center color-999">课程进度</p>
			                            </li>
			                            <#--
			                            <li>
			                                <div class="dprocessingbar">
			                                    <span>${item.participationRate}%</span>
			                                </div>
			                                <p class="text-center color-999">出勤率</p>
			                            </li>
			                            -->
			                            <#if item.completedRate??>
				                            <li>
				                                <div class="dprocessingbar">
				                                    <span>${item.completedRate}%</span>
				                                </div>
				                                <p class="text-center color-999">作业完成率</p>
				                            </li>
			                            </#if>
			                        </ul>
				                </div>
			                </#if>
		                </#list>
					</#if>
	                <#if data.record??>
		                <#list data.record as item>
							<#if item.isEffective == 1>
								<a href="/learningCenter/web/record?orderId=${item.orderId}&title=${encodeFun(item.courseTitle)}" class="curriculum-list">
			                        <p class="curriculum-list-title text-overflow" title="${item.courseTitle}">${item.courseTitle}</p>
			                        <ul class="curriculum-list-prompt">
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-1"></span>
			                                <#if item.effectiveDuration??>
			                                	<span class="ml-8px">${item.effectiveDuration}</span>
			                                <#else>
			                                	<span class="ml-8px">无效</span>
			                                </#if>
			                            </li>
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-4"></span>
			                                <span class="ml-8px">录播</span>
			                            </li>
			                        </ul>
			                    	<ul class="processingbar-list">
			                            <li>
			                                <div class="processingbar">
				                                <span>${item.progressRate}%</span>
			                                </div>
			                                <p class="text-center color-999">课程进度</p>
			                            </li>
			                            <#if item.participationRate??>
				                            <li>
				                                <div class="processingbar">
				                                    <span>${item.participationRate}%</span>
				                                </div>
				                                <p class="text-center color-999">出勤率</p>
				                            </li>
			                            </#if>
			                            <#if item.completedRate??>
				                            <li>
				                                <div class="processingbar">
				                                    <span>${item.completedRate}%</span>
				                                </div>
				                                <p class="text-center color-999">作业完成率</p>
				                            </li>
			                            </#if>
			                        </ul>
				                </a>
							<#else>
								<div class="d-curriculum-list">
			                        <p class="curriculum-list-title text-overflow" title="${item.courseTitle}">${item.courseTitle}</p>
			                        <ul class="curriculum-list-prompt">
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-1"></span>
			                                <span class="ml-8px">${item.effectiveDuration}</span>
			                            </li>
			                            <li>
			                            	<span class="curriculum-list-icon bg-cover curriculum-list-icon-4"></span>
		                                	<span class="ml-8px">录播</span>
			                            </li>
			                        </ul>
			                    	<ul class="processingbar-list">
			                            <li>
			                                <div class="dprocessingbar">
				                                <span>${item.progressRate}%</span>
			                                </div>
			                                <p class="text-center color-999">课程进度</p>
			                            </li>
			                            <#if item.participationRate??>
				                            <li>
				                                <div class="dprocessingbar">
				                                    <span>${item.participationRate}%</span>
				                                </div>
				                                <p class="text-center color-999">出勤率</p>
				                            </li>
			                            </#if>
			                            <#if item.completedRate??>
				                            <li>
				                                <div class="dprocessingbar">
				                                    <span>${item.completedRate}%</span>
				                                </div>
				                                <p class="text-center color-999">作业完成率</p>
				                            </li>
			                            </#if>
			                        </ul>
				                </div>
							</#if>
						</#list>
					</#if>
				</#if>
				<#if data??>
					<#if data.live?? || data.record??>
					<#else>
						<div class="my_curriculum_empty">
		                	<div class="curriculum_empty_icon bg-cover"></div>
		                    <p>快去报名吧~</p>
		                </div>
					</#if>
				</#if>
            </div>
        </div>
    </div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="protocal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-container">
			<p class="title">嗨，同学！</p>
			<div class="desc">
                检测到你这边还有在线协议没签约，要登录APP签约后才能学习哦！</div>
			<div id="img-wrap">
				<img src="/resources/images/studycenter/protocal.jpg"/>
			</div>
			<div class="button-wrap"> <a href="http://zikao.hqjy.com/download/pc">前往下载APP</a></div>
		</div>
	</div>
    
    <script type="text/javascript">
    	var pageParam = {
    		businessId : "${businessId}"
    	}
        function getCookie(name)
        {
            var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
            if(arr=document.cookie.match(reg)) return unescape(arr[2]);
            else return null;
        }
        var protocal = $('#protocal')
        $.ajax({
            url: '/learningCenter/web/contractRecord/getContractUnSignNum',
            type: 'get',
            dataType: 'json',
            timeout: 1000,
            data: {
                token: getCookie('SSOTOKEN')
            },
            success: function (res, status) {
                if (res.data.count > 0) {
                    protocal.modal('show')
                }
            },
            fail: function (err, status) {
                console.log(err)
            }
        })
    </script>
	<style>
		#protocal {
            height: 680px;
            width: 1000px;
            left: 50%;
            margin-left: -500px;
		}
        .modal.fade {
			top: -680px;
		}
        #protocal .modal-container {
			text-align: center;
		}
        .modal-container .close-icon {
            width: 25px;
            height: 25px;
            cursor: pointer;
            position: absolute;
            top: 20px;
            right: 20px;
        }
        .modal-container .title {
            margin-top: 48px;
			text-align: center;
			font-size: 22px;
		}
        .modal-container .desc {
            text-align: center;
            font-size: 18px;
        }
        .modal-container .img-wrap img {
			height: 490px;
			width: 100%;
		}
        .modal-container .button-wrap {
			width: 200px;
            height: 48px;
			line-height: 48px;
			background: #ffda05;
			text-align: center;
			display: inline-block;
			font-size: 18px;
		}
        .modal-container .button-wrap a {
            display: block;
            width: 100%;
            height: 48px;
            line-height: 48px;
            text-decoration: none;
            color: #454545;
        }
		.con-width:after {
			clear: both;
		}
	</style>
    <script type="text/javascript" src="/resources/compress/lib/raphaeljs/raphael.js"></script>
    <script type="text/javascript" src="/resources/compress/lib/raphaeljs/cycle.js"></script>
    <script type="text/javascript" src="/resources/compress/js/studycenter/curriculum_list.js"></script>
</body>
</html>
﻿</#compress>