<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>

    <div class="xxzx-container">
    	<div class="con-width">
    		<#include "../common/nav.ftl" encoding="UTF-8" parse=true>
            
            <div class="container-right">
            	<div class="training-list">
                	<div class="img-bar">
                		<img src="/resources/images/studycenter/t1.jpg" alt="">
                        <div class="desc">
                        	<div class="bg opacity-05"></div>
                        	<p>账务处理+税务实训</p>
                        </div>
                    </div>
                    <p class="text-left fs-18px pt-20px pb-40px">小规模企业账税实训</p>
                    <p class="color-999">
                    	了解小企业岗位职责及企业相关信息，熟悉小企业注册登记流程，了解小企业财务制度建立，熟悉小企业账务处理程序，熟悉小企业税务处理事项，能够掌握小企业帐税一体化办理流程。
                    </p>
                    <#if userInfo.userId??>
                    	<#assign getAccFunA003 = getAccFun("A003","${userInfo.userId}")/>
	                    ${getAccFunA003}
	                <#else>
	                    <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
	                </#if>
                </div>
                <div class="training-list">
                	<div class="img-bar">
                		<img src="/resources/images/studycenter/t2.jpg" alt="">
                        <div class="desc">
                        	<div class="bg opacity-05"></div>
                        	<p>现金收付业务+非现金收付业务+期末业务</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-40px">出纳实训</p>
                    <p class="color-999 pb-20px">
                    	了解大中型企业岗位职责，熟悉现金和非现金业务的处理，熟悉报销流程和网银业务处理，熟悉资金期末业务处理，掌握出纳岗位相关工作的内容，顺利进入岗位角色。
                    </p>
                    <#if userInfo.userId??>
	                    <#assign getAccFunA002 = getAccFun("A002","${userInfo.userId}")/>
	                    ${getAccFunA002}
	                <#else>
	                    <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
	                </#if>
                </div>
                <div class="training-list">
                	<div class="img-bar">
                		<img src="/resources/images/studycenter/t2.jpg" alt="">
                        <div class="desc">
                        	<div class="bg opacity-05"></div>
                        	<p>手工全盘账+电算化全盘账+工出纳</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-40px">一般纳税人报税系统</p>
                    <p class="color-999">
                    	全面掌握会计上岗所必备的知识与技能，完成不同难度和不同业态环境下的账务处理，掌握电算化系统的操作，能够进行日常的开票、发票管理、国地税及个税的申报工作。
                    </p>
                    <#if userInfo.userId??>
	                    <#assign getAccFunForAllA001 = getAccFunForAll("A001","${userInfo.userId}")/>
	                    ${getAccFunForAllA001}
	                <#else>
	                    <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
	                </#if>
                </div>
                <div class="training-list mr-0px">
                	<div class="img-bar">
                		<img src="/resources/images/studycenter/t2.jpg" alt="">
                        <div class="desc">
                        	<div class="bg opacity-05"></div>
                        	<p>中级资格无纸化考试模拟系统</p>
                        </div>
                    </div>
                    <p class="text-left fs-18px pt-20px pb-20px">中级资格无纸化考试模拟系统</p>
                    <p class="color-999 pb-40px">
                    	四大优势：1、实力强劲，全面覆盖；2、全方位能力测评为考试蓄势而发；3、全真模拟，不再陌生；4、方便便捷，无需安装，随处做题 
                    </p>
					<#if userInfo.userId??>
						<#assign getAccFunA004 = getAccFun("A004","${userInfo.userId}")/>
						${getAccFunA004}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
                <div class="training-list">
                	<div class="img-bar">
                		<img src="/resources/images/studycenter/t3.jpg" alt="">
                        <div class="desc">
                        	<div class="bg opacity-05"></div>
                        	<p>财务与进销存一体化管理的中小企业适用云管理系统</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">“精斗云”云财贸管理软件</p>
                    <p class="color-999 pb-20px">
                    	1.云会计+云进销存;2.免安装、免维护、自动升级；支持多人同时记账;3.数据一键导入，账簿、报表自动生成;4.手机拍照，智能记账;5.进销存单据直接生成会计凭证。
                    </p>
                    <#if userInfo.userId??>
	                    <#assign getYouShangB001 = getYouShang("B001","${userInfo.userId}")/>
	                    ${getYouShangB001}
	                <#else>
	                    <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
	                </#if>
                </div>
                <div class="training-list">
                	<div class="img-bar">
                		<img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                        	<div class="bg opacity-05"></div>
                        	<p>建账—做账—过账—结账—报账</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">十大行业真账实训</p>
                    <p class="color-999">
                    	工业、商业、旅游业、建筑施工、外贸、物流业、房地产、酒店餐饮、广告业、装饰装修十大热门高薪行业经验，快速轻松掌握。有了它，会计跳槽高薪行业再也不用怕！
                    </p>
                    <a href="http://www.hqjy.com/shixun/index" target="_blank" class="btn radius-5px">立即体验</a>
                </div>

                <div class="training-list">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t2.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>初级职称仿真考试系统</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">初级职称仿真考试系统</p>
                    <p class="color-999" style="padding-bottom:80px;">
                        会计专业技术初级资格无纸化考试模拟系统
					</p>
					<#if userInfo.userId??>
						<#assign getAccFunA005 = getAccFun("A005","${userInfo.userId}")/>
						${getAccFunA005}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>

                <div class="training-list mr-0px">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>注册会计师仿真考试系统<br>CPA-审计</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">注册会计师仿真考试系统<br>CPA-审计</p>
                    <p class="color-999" style="padding-bottom:60px;">
                        注册会计师仿真考试系统<br>CPA-审计
					</p>
					<#if userInfo.userId??>
						<#assign getAccFunA006 = getAccFun("A006","${userInfo.userId}",14)/>
						${getAccFunA006}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
                <div class="training-list">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>注册会计师仿真考试系统<br>CPA-财务管理成本</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">注册会计师仿真考试系统<br>CPA-财务管理成本</p>
                    <p class="color-999">
                        注册会计师仿真考试系统<br>CPA-财务管理成本
                    </p>
					<#if userInfo.userId??>
						<#assign getAccFunA006 = getAccFun("A006","${userInfo.userId}",15)/>
					${getAccFunA006}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
                <div class="training-list">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>注册会计师仿真考试系统<br>CPA-经济法</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">注册会计师仿真考试系统<br>CPA-经济法</p>
                    <p class="color-999">
                        注册会计师仿真考试系统<br>CPA-经济法
                    </p>
					<#if userInfo.userId??>
						<#assign getAccFunA006 = getAccFun("A006","${userInfo.userId}",16)/>
					${getAccFunA006}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
                <div class="training-list">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>注册会计师仿真考试系统<br>CPA-会计</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">注册会计师仿真考试系统<br>CPA-会计</p>
                    <p class="color-999">
                        注册会计师仿真考试系统<br>CPA-会计
                    </p>
					<#if userInfo.userId??>
						<#assign getAccFunA006 = getAccFun("A006","${userInfo.userId}",17)/>
					${getAccFunA006}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
                <div class="training-list mr-0px">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>注册会计师仿真考试系统<br>CPA-公司战略与风险管理</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">注册会计师仿真考试系统<br>CPA-公司战略与风险管理</p>
                    <p class="color-999">
                        注册会计师仿真考试系统<br>CPA-公司战略与风险管理
                    </p>
					<#if userInfo.userId??>
						<#assign getAccFunA006 = getAccFun("A006","${userInfo.userId}",18)/>
					${getAccFunA006}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
                <div class="training-list">
                    <div class="img-bar">
                        <img src="/resources/images/studycenter/t4.jpg" alt="">
                        <div class="desc">
                            <div class="bg opacity-05"></div>
                            <p>注册会计师仿真考试系统<br>CPA-税法</p>
                        </div>
                    </div>
                    <p class="text-center fs-18px pt-20px pb-20px">注册会计师仿真考试系统<br>CPA-税法</p>
                    <p class="color-999">
                        注册会计师仿真考试系统<br>CPA-税法
                    </p>
					<#if userInfo.userId??>
						<#assign getAccFunA006 = getAccFun("A006","${userInfo.userId}",19)/>
					${getAccFunA006}
					<#else>
                        <a href="javascript:;" class="btn radius-5px" onclick="pageScript.showLayer();">立即体验</a>
					</#if>
                </div>
            </div>
		</div>
	</div>
	
	<div class="mask-layer">
		<div class="mask opacity-05"></div>
		<div class="center-popup">
	        <p class="close">
	        	<a href="javascript:" onclick="pageScript.close()">╳</a>
	        </p>
	        <div class="popup-tips">温馨提示</div>
	        <div class="popup-use">
	        	<img src="/resources/images/common/pop.png" alt="">
	        	<span>Sorry，您暂无权限使用</span>
	        </div>
	        <div class="popup-explain">该功能仅开放给报名相关课程的恒企学员使用
	        	<#if businessId == 'zikao'>
	        		<br>您可以<span class="color-ffda05" onclick="NTKF_kf()">咨询客服</span>了解相关信息。
	        	<#elseif businessId == 'kuaiji'>
	            	<br>您可以<span class="color-18aa1f" onclick="NTKF_kf()">咨询客服</span>了解相关信息。
	            <#else>
	            	<br>您可以<span class="color-16a6e5" onclick="NTKF_kf()">咨询客服</span>了解相关信息。
	            </#if>
	        </div>
	        <a class="popup-talk" href="javascript:;" onclick="NTKF_kf()">咨询客服</a>
	    </div>
	</div>
	
	<script type="text/javascript" src="/resources/compress/js/studycenter/practical_training.js"></script>
	
	<script>
    function NTKF_kf() {
        NTKF.im_openInPageChat('kf_9005_1497664034455');
    }
	</script>
	<script language="javascript" type="text/javascript">
    NTKF_PARAM = {
        siteid: "kf_9005", 	            //企业ID，为固定值，必填
        settingid: "kf_9005_1497664034455"	//接待组ID，为固定值，必填
    }
	</script>
	<!--基础脚本加载 -->
	<script type="text/javascript" src="https://dl.ntalker.com/js/xn6/ntkfstat.js?siteid=kf_9005" charset="utf-8"></script>
	
</body>
</html>
</#compress>
