<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>
    
    <div class="xxzx-container">
    	<div class="con-width">
            <#include "../common/nav.ftl" encoding="UTF-8" parse=true>
            
            <div class="container-right">
            	<div class="curriculum-list">
                    <p class="curriculum-list-title text-overflow" title="${RequestParameters.title}">${RequestParameters.title}</p>
                    <ul class="curriculum-list-prompt">
                        <li class="text-overflow pr-250px">
                        	<span class="curriculum-list-icon curriculum-list-icon-2"></span>
                            <span class="ml-8px" title="${RequestParameters.courseStatus}">${RequestParameters.courseStatus}</span>
                        </li>
                        <#if RequestParameters.courseDuration != ''>
	                        <li>
	                        	<span class="curriculum-list-icon curriculum-list-icon-1"></span>
	                            <span class="ml-8px">${RequestParameters.courseDuration}</span>
	                        </li>
                        </#if>
                    </ul>
                    <ul class="processingbar-list">
                        <li>
                            <div class="processingbar">
                                <span>${rate.progressRate}%</span>
                            </div>
                            <p class="text-center color-999">课程进度</p>
                        </li>
                        <#--
                        <#if rate.participationRate??>
	                        <li>
	                            <div class="processingbar">
	                                <span>${rate.participationRate}%</span>
	                            </div>
	                            <p class="text-center color-999">出勤率</p>
	                        </li>
                        </#if>
                        -->
                        <#if rate.completedRate??>
                            <li>
                                <div class="processingbar">
                                    <span>${rate.completedRate}%</span>
                                </div>
                                <p class="text-center color-999">作业完成率</p>
                            </li>
                        </#if>
                    </ul>
                </div>
                
                <div class="curriculum">
                	<div class="curriculum-title">
                    	<div class="fl">
                        	<a href="/learningCenter/web/live?userplanId=${RequestParameters.userplanId}&title=${encodeFun(RequestParameters.title)}&isNoClass=${RequestParameters.isNoClass}" class="text-control">
                            	<span class="arrow black-arrow-left bg-cover"></span>
                                <span>返回当天课程</span>
                            </a>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        	
                        		<#if udesk.agent_id??>
                            		<#assign agent_id=udesk.agent_id>
                            	<#else>
                            		<#assign agent_id=''>
                            	</#if>
                            	
                            	<#if udesk.c_email??>
                            		<#assign email=udesk.c_email>
                            	<#else>
                            		<#assign email=''>
                            	</#if>

                            	<a href="javascript:;" class="btn radius-5px" onclick="pageScript.contactTeacher($(this),'${agent_id}','${udesk.session_key}','${udesk.c_phone}','${email}','${udesk.web_token}','${udesk.nonce}','${udesk.timestamp}','${udesk.sign}');" id="im_s">联系学习顾问</a>
                            </div>
                        </div>
                    </div>
                    <#list 0..(data!?size-1) as i>
	                    <div class="level-curriculum-bar">
	                    	<div class="level-curriculum-title">
	                    		<div class="fl">
	                    			${data[i].classplanName}
	                    		</div>
	                    		<div class="fr">
	                    			<span class="black-arrow-icon-2 bg-cover down-arrow-icon"></span>
	                    		</div>
	                    	</div>
	                    	<#list 0..(data[i].list!?size-1) as j>
		                        <div class="level-curriculum-list hide" data-time="${data[i].list[j].time}" onclick="pageScript.goBackCurriculumDetail($(this))">
		                        	<div class="fl">
		                                <div class="curriculum-name text-overflow" style="width:220px;">
		                                    <span title="${data[i].list[j].classplanLiveName}">${data[i].list[j].classplanLiveName}</span>
		                                </div>
		                            </div>
		                            <div class="right-content">
		                                <span class="name-bar">
		                                	<span class="icon-p">
		                                    	<span class="persion-icon bg-cover"></span>
		                                	</span>
		                                	<span class="teacher text-overflow">${data[i].list[j].teacher}</span>
		                                </span>
		                                <span class="time-bar" style="width:185px;">
		                                    <span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
		                                    <span class="pl-5px">${data[i].list[j].time}</span>
		                                </span>
			                            <span class="attendance">
				                            <span class="attendance-text">出勤</span>
				                            <span class="progress-bar" style="width:78px;">
				                            	<span class="progress-bg radius-8px" style="width:78px;">
					                            	<span class="progress radius-8px" style="width: ${data[i].list[j].attendPer}%;">
					                            		<span class="progress-text">${data[i].list[j].attendPer}%</span>
					                            	</span>
					                            </span>
				                            </span>
			                            </span>
			                            <#if data[i].list[j].classStatus == 0>
			                            	<a href="javascript:;" class="disabled-btn">未开始</a>
			                            <#elseif data[i].list[j].classStatus == 1>
			                            	<a href="javascript:;" class="btn radius-5px" data-id="${data[i].list[j].classplanLiveId}" onclick="pageScript.goVideo($(this))">即将开始</a>
			                            <#elseif data[i].list[j].classStatus == 2>
			                            	<a href="javascript:;" class="btn radius-5px" data-id="${data[i].list[j].classplanLiveId}" onclick="pageScript.goVideo($(this))">正在直播</a>
			                            <#elseif data[i].list[j].classStatus == 3>
			                            	<a href="javascript:;" class="disabled-btn">已结束</a>
			                            <#elseif data[i].list[j].classStatus == 4>
		                                	<a href="javascript:;" class="default-btn radius-5px" data-id="${data[i].list[j].classplanLiveId}" onclick="pageScript.goReplay($(this))">观看回放</a>
		                                </#if>
		                            </div>
		                    	</div>
	                        </#list>
						</div>
					</#list>
				</div>
				
			</div>
            
		</div>
	</div>
    
    <script type="text/javascript">
    	var pageParam = {
    		businessId : "${businessId}",
    		userplanId : "${RequestParameters.userplanId}",
    		title : "${encodeFun(RequestParameters.title)}",
    		isNoClass : "${RequestParameters.isNoClass}"
    	}
    </script>
    <script type="text/javascript" src="/resources/compress/lib/raphaeljs/raphael.js"></script>
    <script type="text/javascript" src="/resources/compress/lib/raphaeljs/cycle.js"></script>
    <script type="text/javascript" src="/resources/js/studycenter/curriculum.js"></script>
</body>
</html>
</#compress>