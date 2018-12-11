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
                            <span class="ml-8px" title="${head.courseStatus}">${head.courseStatus}</span>
                        </li>
                        <#if head.courseDuration != ''>
                            <li>
                            	<span class="curriculum-list-icon curriculum-list-icon-1"></span>
                                <span class="ml-8px">${head.courseDuration}</span>
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
                
                <div class="calendar">
                	<div class="calendar-title">
                    	<div class="fl">
                        	<div class="text-control">
                                <span class="fs-16px"><span class="f-year"></span>年<span class="f-month"></span>月课程</span>
                                <#if businessId == 'zikao'>
                                	<span class="arrow bg-cover yellow-arrow-left ml-20px" onclick="calendar.prevMonth($(this))"></span>
                                	<span class="arrow bg-cover yellow-arrow-right ml-20px" onclick="calendar.nextMonth($(this))"></span>
								<#elseif businessId == 'kuaiji'>
									<span class="arrow bg-cover green-arrow-left ml-20px" onclick="calendar.prevMonth($(this))"></span>
                                	<span class="arrow bg-cover green-arrow-right ml-20px" onclick="calendar.nextMonth($(this))"></span>
								<#else>
									<span class="arrow bg-cover blue-arrow-left ml-20px" onclick="calendar.prevMonth($(this))"></span>
                                	<span class="arrow bg-cover blue-arrow-right ml-20px" onclick="calendar.nextMonth($(this))"></span>
								</#if>
                            </div>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        		<#if RequestParameters.isNoClass == "0">
                        			<a href="/learningCenter/web/live/schedule?userplanId=${RequestParameters.userplanId}&title=${encodeFun(RequestParameters.title)}&isNoClass=${RequestParameters.isNoClass}&courseStatus=${encodeFun(head.courseStatus)}&courseDuration=${encodeFun(head.courseDuration)}" class="btn radius-5px">课程表</a>
                            	<#else>
                            		<a href="javascript:;" class="disabled-btn radius-5px">课程表</a>
                            	</#if>
                            	
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
                    <div class="calendar-content">
                        <div class="f-rili-table">
                            <div class="f-rili-head">
                                <div class="f-rili-th ml-0px">周日</div>
                                <div class="f-rili-th">周一</div>
                                <div class="f-rili-th">周二</div>
                                <div class="f-rili-th">周三</div>
                                <div class="f-rili-th">周四</div>
                                <div class="f-rili-th">周五</div>
                                <div class="f-rili-th mr-0px">周六</div>
                            </div>
                            <div class="f-tbody">
	                            <#list 0..(calendar!?size-1) as i>
		                            <#if calendar[i].date[8] != "0">
		                            	<#if calendar[i].monType == -1>
		                            		<div class="f-td f-null f-lastMonth" onclick="calendar.prevMonth($(this))">
												<span class="f-day" data-id="${calendar[i].date}">${calendar[i].date[8]}${calendar[i].date[9]}</span>
												<#if calendar[i].status != 0>
													<span class="f-ke f-d-ke">课</span>
												</#if>
												<#if calendar[i].isAttend != 0>
													<div class="calendar-icon-p">
														<div class="calendar-icon calendar-icon-1 bg-cover"></div>
													</div>
												</#if>
											</div>
		                            	<#elseif calendar[i].monType == 0>
		                            		<#if i gte 7>
		                            			<div class="f-td f-number sign hide" onclick="calendar.TodaySelected($(this))">
		                            		<#else>
		                            			<div class="f-td f-number" onclick="calendar.TodaySelected($(this))">
		                            		</#if>
		                            			<span class="f-day" data-id="${calendar[i].date}">${calendar[i].date[8]}${calendar[i].date[9]}</span>
		                            			<#if calendar[i].status != 0>
		                            				<span class="f-ke">课</span>
		                            			</#if>
		                            			<#if calendar[i].isAttend != 0>
			                            			<div class="calendar-icon-p">
			                            				<div class="calendar-icon calendar-icon-2 bg-cover"></div>
			                            			</div>
		                            			</#if>
											</div>
		                            	<#else>
		                            		<#if i gte 7>
		                            			<div class="f-td f-null f-nextMounth hide" onclick="calendar.nextMonth($(this))">
		                            		<#else>
		                            			<div class="f-td f-null f-nextMounth" onclick="calendar.nextMonth($(this))">
		                            		</#if>
		                            			<span class="f-day" data-id="${calendar[i].date}">${calendar[i].date[8]}${calendar[i].date[9]}</span>
		                            			<#if calendar[i].status != 0>
		                            				<span class="f-ke f-d-ke">课</span>
		                            			</#if>
		                            			<#if calendar[i].isAttend != 0>
			                            			<div class="calendar-icon-p">
			                            				<div class="calendar-icon calendar-icon-1 bg-cover"></div>
			                            			</div>
		                            			</#if>
		                            		</div>
		                            	</#if>
		                            <#else>
		                            	<#if calendar[i].monType == -1>
		                            		<div class="f-td f-null f-lastMonth" onclick="calendar.prevMonth($(this))">
												<span class="f-day" data-id="${calendar[i].date}">${calendar[i].date[9]}</span>
												<#if calendar[i].status != 0>
													<span class="f-ke f-d-ke">课</span>
												</#if>
												<#if calendar[i].isAttend != 0>
													<div class="calendar-icon-p">
														<div class="calendar-icon calendar-icon-1 bg-cover"></div>
													</div>
												</#if>
											</div>
		                            	<#elseif calendar[i].monType == 0>
			                            	<#if i gte 7>
		                            			<div class="f-td f-number sign hide" onclick="calendar.TodaySelected($(this))">
		                            		<#else>
		                            			<div class="f-td f-number" onclick="calendar.TodaySelected($(this))">
		                            		</#if>
		                            			<span class="f-day" data-id="${calendar[i].date}">${calendar[i].date[9]}</span>
		                            			<#if calendar[i].status != 0>
		                            				<span class="f-ke">课</span>
		                            			</#if>
		                            			<#if calendar[i].isAttend != 0>
			                            			<div class="calendar-icon-p">
			                            				<div class="calendar-icon calendar-icon-2 bg-cover"></div>
			                            			</div>
		                            			</#if>
											</div>
		                            	<#else>
		                            		<#if i gte 7>
		                            			<div class="f-td f-null f-nextMounth hide" onclick="calendar.nextMonth($(this))">
		                            		<#else>
		                            			<div class="f-td f-null f-nextMounth" onclick="calendar.nextMonth($(this))">
		                            		</#if>
		                            			<span class="f-day" data-id="${calendar[i].date}">${calendar[i].date[9]}</span>
		                            			<#if calendar[i].status != 0>
		                            				<span class="f-ke f-d-ke">课</span>
		                            			</#if>
		                            			<#if calendar[i].isAttend != 0>
			                            			<div class="calendar-icon-p">
			                            				<div class="calendar-icon calendar-icon-1 bg-cover"></div>
			                            			</div>
		                            			</#if>
		                            		</div>
		                            	</#if>
		                            </#if>
	                            </#list>
                            </div>
                            <div class="show-more" id="showMore">
                            	<span class="black-arrow-icon down-arrow-icon bg-cover"></span>
                                <span class="text">查看本月课程</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="live-broadcast" id="liveBroadcast">
                <#--
                    <div class="live-broadcast-title"> 中级会计实务</div>
                    <div class="live-broadcast-list">
                    	<div class="fl">
                        	<div class="curriculum-name text-overflow">
                                <span class="chapter">第二节</span>
                                <span>长期长期长期长期长期长期长期长期长期长期长期长期长期长期</span>
                            </div>
                        </div>
                        <div class="right-content">
                            <span class="name-bar text-overflow">
                                <span class="persion-icon bg-cover"></span>
                                <span class="pl-5px">名字名字</span>
                            </span>
                            <span class="time-bar">
                                <span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
                                <span class="pl-5px">9:00-17:00</span>
                            </span>
                            <a href="javascript:;" class="disabled-btn">未开始</a>
                        </div>
                    </div>
                    <div class="live-broadcast-list">
                    	<div class="fl">
                        	<div class="curriculum-name text-overflow">
                                <span class="chapter">第二节</span>
                                <span>长期长期长期长期长期长期长期长期长期长期长期长期长期长期</span>
                            </div>
                        </div>
                        <div class="right-content">
                            <span class="name-bar text-overflow">
                                <span class="persion-icon bg-cover"></span>
                                <span class="pl-5px">名字名字</span>
                            </span>
                            <span class="time-bar">
                                <span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
                                <span class="pl-5px">9:00-17:00</span>
                            </span>
                            <a href="javascript:;" class="disabled-btn">已结束</a>
                        </div>
                    </div>
                    <div class="live-broadcast-list">
                    	<div class="fl">
                        	<div class="curriculum-name text-overflow">
                                <span class="chapter">第二节</span>
                                <span>长期长期长期长期长期长期长期长期长期长期长期长期长期长期</span>
                            </div>
                        </div>
                        <div class="right-content">
                            <span class="name-bar text-overflow">
                                <span class="persion-icon bg-cover"></span>
                                <span class="pl-5px">名字名字</span>
                            </span>
                            <span class="time-bar">
                                <span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
                                <span class="pl-5px">9:00-17:00</span>
                            </span>
                            <a href="javascript:;" class="default-btn radius-5px">观看回放</a>
                        </div>
                    </div>
                    <div class="live-broadcast-list bb-none">
                    	<div class="fl">
                        	<div class="curriculum-name text-overflow">
                                <span class="chapter">第二节</span>
                                <span>长期长期长期长期长期</span>
                            </div>
                        </div>
                        <div class="right-content">
                            <span class="name-bar text-overflow">
                                <span class="persion-icon bg-cover"></span>
                                <span class="pl-5px">名字</span>
                            </span>
                            <span class="time-bar">
                                <span class="curriculum-list-icon curriculum-list-icon-1 bg-cover"></span>
                                <span class="pl-5px">9:00-17:00</span>
                            </span>
                            <a href="javascript:;" class="btn radius-5px">正在直播</a>
                        </div>
                    </div>
                    -->
                </div>
                
                <div class="other-content" id="otherContent">
                	<#--
                    <div class="other-content-list">
                    	<div class="fl">
                            <div class="text-bar">
                                <span class="other-content-icon bg-cover other-content-icon-1"></span>
                                <span class="chapter">第二节</span>
                                <span>长期股权投资的后续计量（一） 章节预习</span>
                            </div>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        		<a href="javascript:;" class="btn radius-5px">观看课程</a>
                            </div>
                        </div>
                    </div>
                    <div class="other-content-list">
                    	<div class="fl">
                            <div class="text-bar">
                                <span class="other-content-icon bg-cover other-content-icon-2"></span>
                                <span class="chapter">第二节</span>
                                <span>长期股权投资的后续计量（一） 课后作业</span>
                            </div>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        		<a href="javascript:;" class="btn radius-5px">去做作业</a>
                            </div>
                        </div>
                    </div>
                    <div class="other-content-list">
                    	<div class="fl">
                            <div class="text-bar">
                                <span class="other-content-icon bg-cover other-content-icon-3"></span>
                                <span class="chapter">第二章</span>
                                <span>长期股权投资的后续计量（一） 章节预习</span>
                            </div>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        		<a href="javascript:;" class="btn radius-5px">去实训</a>
                            </div>
                        </div>
                    </div>
                    <div class="other-content-list">
                    	<div class="fl">
                            <div class="text-bar">
                                <span class="other-content-icon bg-cover other-content-icon-4"></span>
                                <span class="chapter">第二章</span>
                                <span>长期股权投资的后续计量（一） 章节预习</span>
                            </div>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        		<a href="javascript:;" class="btn radius-5px" target="_blank">下载</a>
                            </div>
                        </div>
                    </div>
                    <div class="other-content-list">
                    	<div class="fl">
                            <div class="text-bar">
                                <span class="other-content-icon bg-cover other-content-icon-5"></span>
                                <span class="text">中级会计职称   通关押题宝</span>
                            </div>
                        </div>
                        <div class="fr">
                        	<div class="btn-bar">
                        		<a href="javascript:;" class="btn radius-5px" id="preview">预览</a>
                            </div>
                        </div>
                    </div>
                    -->
                </div>
                
                <div class="my_curriculum_empty hide" id="myCurriculumEmpty">
                	<div class="curriculum_empty_icon bg-cover"></div>
                	<#if RequestParameters.isNoClass == '0'>
                    	<p>今天没有课，可以好好去放松一下哦~</p>
                	<#else>
                		<p>您的课程正在排课，如有疑问请点击“
	                		<#if wxCode?? && wxCode != ''>
	                    		<a href="javascript:;" class="color-333" data-url="${wxCode}" onclick="pageScript.contactTeacher($(this));">联系班主任</a>
	                    	<#else>
	                    		<a href="javascript:;" class="color-333">联系班主任</a>
	                    	</#if>
                		”~</p>
                	</#if>
                </div>
                
            </div>
            
		</div>
	</div>
    
    <div class="mask-layer">
        <div class="mask opacity-05"></div>
        <div class="content-bar">
        	<div class="content con-width">
                <div class="left-content" id="previewNav">
                	<#--
                    <div class="nav-list">
                        <div class="total-title text-overflow">
                            <span class="small-circle-icon bg-cover"></span>
                            <span class="text">中国近代史纲要</span>
                        </div>
                        <ul class="outer-ul">
                            <li class="outer-li">
                                <div class="pr">
                                    <span class="grey-arrow-icon bg-cover down-arrow-icon"></span>
                                    <span class="pl-17px">真题手册</span>
                                </div>
                                <ul class="inner-ul">
                                    <li>真题手册</li>
                                    <li>真题手册</li>
                                </ul>
                            </li>
                            <li class="outer-li">
                                <div class="pr">
                                    <span class="grey-arrow-icon bg-cover down-arrow-icon"></span>
                                    <span class="pl-17px">思维导图</span>
                                </div>
                                <ul class="inner-ul">
                                    <li>真题手册</li>
                                </ul>
                            </li>
                            <li class="outer-li">
                                <div class="pr">
                                    <span class="pl-17px">真题手册</span>
                                </div>
                            </li>
                            <li class="outer-li">
                                <div class="pr">
                                    <span class="pl-17px">通关报道</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="nav-list">
                        <div class="total-title">
                            <span class="small-circle-icon bg-cover"></span>
                            <span class="text">中国古代文学史</span>
                        </div>
                        <ul class="outer-ul">
                            <li class="outer-li">
                                <div class="pr">
                                    <span class="grey-arrow-icon bg-cover down-arrow-icon"></span>
                                    <span class="pl-17px">真题手册</span>
                                </div>
                                <ul class="inner-ul">
                                    <li>真题手册</li>
                                    <li>真题手册</li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                    <div class="nav-list">
                        <div class="total-title">
                            <span class="small-circle-icon bg-cover"></span>
                            <span class="text">中国古代文学史</span>
                        </div>
                    </div>
                    -->
                </div>
                <div class="right-content">
                    <div class="close" id="close">×</div>
                    <div class="article-content" id="previewContent"></div>
                    <div class="loading-bar hide">
                    	<#if businessId == 'zikao'>
							<div class="yellow-loading-icon bg-cover"></div>
						<#elseif businessId == 'kuaiji'>
							<div class="green-loading-icon bg-cover"></div>
						<#else>
							<div class="blue-loading-icon bg-cover"></div>
						</#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script type="text/javascript">
    	var pageParam = {
    		businessId : "${businessId}"
    	}
    </script>
    <script type="text/javascript" src="/resources/compress/lib/raphaeljs/raphael.js"></script>
    <script type="text/javascript" src="/resources/compress/lib/raphaeljs/cycle.js"></script>
    <script type="text/javascript" src="/resources/compress/js/studycenter/curriculum_detail.js"></script>
    <script type="text/javascript" src="/resources/compress/lib/calendar/calendar.js?date=20181107"></script>
</body>
</html>
</#compress>
