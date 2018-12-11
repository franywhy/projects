<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>
	<div class="xxzx-container">
    	<div class="con-width">
    		<#include "../common/nav.ftl" encoding="UTF-8" parse=true>
    		
    		<div class="container-right pr">
            	<div class="my-curriculum-title">
            		<p>经验证书查询</p>
                </div>
                <#if data??>
                <div class="certificate">
                	<div class="certificate-bg">
                		<div class="pa" style="top:221px;">
                			<div class="pa text" style="left:79px; width:49px;">
                				<div class="text-right text-overflow">${data.name}</div>
                			</div>
                			<div class="pa text" style="left: 236px; width: 165px;">
                				<div class="text-center">${data.card}</div>
                			</div>
                			<div class="pa text" style="left: 438px;">
                				<div class="text-center">${data.readDate?string("yyyy")}</div>
                			</div>
                			<div class="pa text" style="left: 482px;">
                				<div class="text-center">${data.readDate?string("MM")}</div>
                			</div>
                			<div class="pa text" style="left: 530px;">
                				<div class="text-center">${data.endDate?string("yyyy")}</div>
                			</div>
                			<div class="pa text" style="left: 577px;">
                				<div class="text-center">${data.endDate?string("MM")}</div>
                			</div>
                		</div>
                		
                		<div class="pa" style="top:250px;">
                			<div class="pa text" style="left:79px; width:130px;">
                				<div class="text-center text-overflow">${data.course}</div>
                			</div>
                			<div class="pa text" style="left:619px;">
                				<div class="text-center no-br">${data.expStr}</div>
                			</div>
                		</div>
                		
                		<div class="pa" style="top:309px; width:100%;">
                			<div class="pa text" style="left:80px; width:586px">
                				<div style="text-indent:8.5em; height:60px; overflow:hidden;">${data.courseRemark}</div>
                			</div>
                		</div>
                		
                		<div class="pa" style="top:378px;">
                			<div class="pa text" style="left:145px;">
                				<div class="no-br">${data.certNo}</div>
                			</div>
                		</div>
                		
                		<div class="pa" style="top:407px;">
                			<div class="pa text" style="left:548px;">
                				<div class="no-br">${data.sendDate?string("yyyy")}</div>
                			</div>
                			<div class="pa text" style="left:590px;">
                				<div class="no-br">${data.sendDate?string("MM")}</div>
                			</div>
                			<div class="pa text" style="left:622px;">
                				<div class="no-br">${data.sendDate?string("dd")}</div>
                			</div>
                		</div>
                		
                	</div>
                	<#if data.sendStatus == 0>
                		<p class="fs-18px text-center mt-40px">证书未发放，请耐心等候</p>
                	<#elseif data.sendStatus == 1>
                		<p class="fs-18px text-center mt-40px">证书发放中，请耐心等候</p>
                	<#else>
                		<p class="fs-18px text-center mt-40px">证书已发放</p>
                	</#if>
                </div>
            </div>
            <#else>
            	<div class="my_curriculum_empty" id="myCurriculumEmpty">
					<div class="curriculum_empty_icon bg-cover"></div>
					<p>没有查询到相关证书信息</p>
				</div>
            </#if>
    	</div>
    </div>
</body>
</html>
</#compress>