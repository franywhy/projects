<#-- 左侧导航-->
<div class="container-left">
    <ul class="my-curriculum-nav">
    	<#if lcMenu??>
    		<#list lcMenu as item>
    			<#-- 选中样式-->
    			<#assign li_class="">
    			<#-- 点击跳转路径-->
    			<#assign a_url=item.url>
    			<#-- 当前地址栏-->
    			<#if servletPath == item.url >
    				<#assign li_class="active">
    				<#assign a_url="javascript:;">
    			</#if>
    			
    			<#-- 我的课程特殊处理-->
    			<#if item.url=='/learningCenter/web/home' && 
	    			(
	    				servletPath == '/learningCenter/web/live' || 
	    				servletPath == '/learningCenter/web/live/schedule' || 
	    				servletPath == '/learningCenter/web/record'
    				)
    			>
    				<#assign li_class="active">
    			</#if>
    			
    			<#-- 考勤申请特殊处理 -->
    			<#if item.url=='/learningCenter/web/courseabnormalorderList' && 
	    			(
	    				servletPath == '/learningCenter/web/courseAbnormallRegistrationList' || 
	    				servletPath == '/learningCenter/web/courseAbnormalAbandonExamList' || 
	    				servletPath == '/learningCenter/web/courseAbnormalFreeAssessmentList'
    				)
    			>
    				<#assign li_class="active">
    			</#if>
    			
    			<#-- 成绩登记特殊处理 -->
    			<#if item.url=='/learningCenter/web/examinationResult/addPage' && 
    				(
    					servletPath == '/learningCenter/web/examinationResult/addPage' || 
    					servletPath == '/learningCenter/web/examinationResult/listPage'
    				)
    			>
    				<#assign li_class="active">
    			</#if>
    			<li class="${li_class}"><a href="${a_url}">${item.name!''}</a></li>
    		</#list>
    	</#if>
    </ul>
</div>