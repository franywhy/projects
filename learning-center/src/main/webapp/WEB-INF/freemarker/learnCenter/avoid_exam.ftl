<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>
	
	<div class="xxzx-container">
    	<div class="con-width">
    		<#include "../common/nav.ftl" encoding="UTF-8" parse=true>
    		
            <div class="container-right">
            	<div class="attendance-title">
                	<div class="nav" id="nav">
                        <ul>
                            <li><a href="/learningCenter/web/courseabnormalorderList">休学</a></li>
                            <li><a href="/learningCenter/web/courseAbnormallRegistrationList">报考</a></li>
                            <li><a href="/learningCenter/web/courseAbnormalAbandonExamList">弃考</a></li>
                            <li class="active"><a href="javascript:">免考核</a></li>
                        </ul>
                    </div>
                    <div class="fr">
                    	<a href="javascript:;" class="btn radius-5px" id="applyBtn">
                    		<#if businessId == 'zikao'>
								<span class="add-icon black-add-icon bg-cover"></span>
							<#elseif businessId == 'kuaiji'>
								<span class="add-icon white-add-icon bg-cover"></span>
							<#else>
								<span class="add-icon white-add-icon bg-cover"></span>
							</#if>
                            <span>免考核申请</span>
                        </a>
                    </div>
                </div>
                <div class="record-table">
                	<table>
                    	<tr>
                        	<th class="min-w-50px">序号</th>
                            <th class="min-w-150px">班型</th>
                            <th class="min-w-150px">课程</th>
                            <th class="min-w-110px">开始时间</th>
                            <th class="min-w-110px">结束时间</th>
                            <th class="min-w-150px">原因</th>
                            <th class="min-w-90px">状态</th>
                            <th class="min-w-50px">操作</th>
                        </tr>
						<#if data[0]??>
                        	<#list 0..(data!?size-1) as i>
								<tr>
		                        	<td>${i+1}</td>
		                        	<#if data[i].orderName??>
		                        		<td>${data[i].orderName}</td>
		                        	<#else>
		                        		<td>无</td>
		                        	</#if>
		                        	<td>${data[i].courseName}</td>
		                            <td>${data[i].startTime?string("yyyy-MM-dd")}</td>
		                            <#if data[i].endTime??>
		                        		<td>${data[i].endTime?string("yyyy-MM-dd")}</td>
		                        	<#else>
		                        		<td>-</td>
		                        	</#if>
		                            <#if data[i].abnormalReason??>
		                        		<td>${data[i].abnormalReason}</td>
		                        	<#else>
		                        		<td>无</td>
		                        	</#if>
		                            <#if data[i].auditStatus == 0>
		                            	<td class="color-333">待审核</td>
		                            <#elseif data[i].auditStatus == 1>
		                            	<td class="color-999">已取消</td>
		                            <#elseif data[i].auditStatus == 2>
		                            	<td class="color-18aa1f">通过</td>
		                            </#if>
		                            <#if data[i].auditStatus == 0>
		                            	<td>
		                            		<span class="cancel" onclick="pageScript.cancel('${data[i].id}')">取消</span>
		                            	</td>
		                            <#else>
		                            	<td></td>
		                            </#if>
		                        </tr>
							</#list>
						<#else>
							<tr>
								<td colspan="7" class="bb-none">
									<div class="my_curriculum_empty" id="myCurriculumEmpty">
					                	<div class="curriculum_empty_icon bg-cover"></div>
					                    <p>暂无免考核记录</p>
					                </div>
				                </td>
			                </tr>
						</#if>
                    </table>
                </div>
            </div>
		</div>
	</div>
    
    <div id="attFormLayer">
	    <div class="mask-layer">
	        <div class="mask opacity-05"></div>
	        <div class="att-content-bar">
	        	<div class="att-content" style="height:542px">
	            	<div class="grey-close-p">
	            		<span class="grey-close-icon grey-close-icon-1 bg-cover"></span>
	                </div>
	                <p class="title">免考核申请</p>
	                <form class="att-form" id="form">
	                	<div class="select-bar">
	                        <div class="att-label mb-0px" id="classType">
	                            <span class="input-title">班型</span>
	                            <input type="text" class="att-input radius-5px" readonly placeholder="" value="">
	                            <span class="solid-down-arrow-icon-p">
	                            	<span class="solid-down-arrow-icon bg-cover"></span>
	                            </span>
	                            <div class="sign">
		                        	<span class="color-ff5a00">*</span>
		                        </div>
	                        </div>
	                        <div class="select radius-5px" id="selectList">
		                        <#if classtypeData[0]??>
		                        	<#list 0..(classtypeData!?size-1) as i>
			                        	<div class="select-list" data-id="${classtypeData[i].orderId}">${classtypeData[i].orderName}</div>
			                        </#list>
			                    </#if>
	                        </div>
	                    </div>
	                    <div class="select-bar">
	                        <div class="att-label mb-0px" id="course">
	                            <span class="input-title">免考核课程</span>
	                            <input type="text" class="att-input radius-5px" readonly placeholder="" value="">
	                            <span class="solid-down-arrow-icon-p">
	                            	<span class="solid-down-arrow-icon bg-cover"></span>
	                            </span>
	                            <div class="sign">
		                        	<span class="color-ff5a00">*</span>
		                        </div>
	                        </div>
	                        <div class="select radius-5px" id="selectList2"></div>
	                    </div>
	                    <div class="att-label" id="startTime">
	                        <span class="input-title">开始时间</span>
	                        <input type="text" class="att-input radius-5px datetimepicker" placeholder="" value="">
	                        <span class="solid-down-arrow-icon-p">
	                            <span class="calendar_icon bg-cover"></span>
	                        </span>
	                        <div class="sign">
	                        	<span class="color-ff5a00">*</span>
	                        </div>
	                    </div>
	                    <div class="att-label" id="endTime">
	                        <span class="input-title">结束时间</span>
	                        <input type="text" class="att-input radius-5px datetimepicker" placeholder="" value="">
	                        <span class="solid-down-arrow-icon-p">
	                            <span class="calendar_icon bg-cover"></span>
	                        </span>
	                    	<div class="sign">
	                        	<span class="color-ff5a00">*</span>
	                        </div>
	                    </div>
	                    <div class="att-label">
	                        <span class="input-title">原因</span>
	                        <textarea rows="4" class="att-textarea radius-5px" id="cause" placeholder="" maxlength="200"></textarea>
	                    	<div class="sign" style="margin-top:-9px">
	                        	<span class="color-ff5a00">*</span>
	                        </div>
	                    </div>
	                    <div class="att-label" style="display:none;" id="promptText">
	                    	<div class="color-ff9000 text-left">
	                    		<span class="orange-prompt-icon bg-cover"></span>
	                    		<span class="text-se">请输入完整的免考核内容，不能有为空项。</span>
	                    	</div>
	                    </div>
	                    <div class="att-label">
	                        <input type="submit" class="sub-btn radius-5px" value="提交申请">
	                    </div>
	                </form>
	            </div>
	        </div>
		</div>
	</div>
	
	<div id="cancelConfirmLayer">
		<div class="mask-layer">
	        <div class="mask opacity-05"></div>
	        <div class="prompt-confirm">
	        	<div class="prompt-confirm-content">
	        		<div class="grey-close-p">
	            		<span class="grey-close-icon grey-close-icon-1 bg-cover"></span>
	                </div>
	                <div class="text">
	                	<#if businessId == 'kuaiji'>
	                		<span class="prompt-icon prompt-icon-1 bg-cover"></span>
	                	<#elseif businessId == 'zikao'>
	                		<span class="prompt-icon prompt-icon-2 bg-cover"></span>
	                	<#else>
	                		<span class="prompt-icon prompt-icon-3 bg-cover"></span>
	                	</#if>
	                	<span class="dblock fs-16px color-333 ml-28px">是否确认取消本次操作？</span>
	                </div>
	                <div class="btn-bar">
		        		<a href="javascript:;" id="cancelBtn">取消</a>
		        		<a href="javascript:;" class="sign" id="confirmBtn">确认</a>
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
    <link rel="stylesheet" type="text/css" href="/resources/compress/lib/datetimepicker/jquery.datetimepicker.css">
    <script type="text/javascript" src="/resources/compress/lib/datetimepicker/jquery.datetimepicker.full.min.js"></script>
    <script type="text/javascript" src="/resources/compress/js/studycenter/avoid_exam.js"></script>
</body>
</html>
</#compress>
