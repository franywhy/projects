<#if businessId == 'zikao'>
	<div class="header" style="background:#ffda05;">
<#else>
	<div class="header">
</#if>
	<div class="con-width">
    	<div class="fl">
    		<#if businessId == 'zikao'>
    			<a href="${homeUrl}" class="logo">
	                <img src="/resources/images/common/zk_logo.png" alt="">
	            </a>
    		<#elseif businessId == 'kuaiji'>
    			<a href="${homeUrl}" class="logo">
	                <img src="/resources/images/common/logo.png" alt="">
	            </a>
    		<#else>
    			<a href="${homeUrl}" class="logo">
	                <img src="/resources/images/common/xlxw_logo.png" alt="">
	            </a>
    		</#if>
        </div>
        <div class="fr">
        	<#--
        	<div class="register" id="register">
                <a href="javascript:;">登录</a>
                <a href="javascript:;">注册</a>
            </div>
            -->
            <div class="sign-up">
            	<span>欢迎回到学习中心,${userInfo.nickName}</span>
            	<#if userInfo.avatar??>
                	<img src="${userInfo.avatar}" class="person-img circle" onerror="this.src='/resources/images/common/default_person_icon.png'">
                <#else>
                	<img src="/resources/images/common/default_person_icon.png" class="person-img circle">
                </#if>
            </div>
        </div>
        <#--
        <div class="fr">
        	<div class="welcome">同学你好，欢迎来到学习中心！请登录开始学习之旅~</div>
        </div>
        -->
    </div>
</div>

<div class="guide">
	<div class="con-width">
		<p><a href="${homeUrl}">首页</a> > 学习中心</p>
    </div>
</div>