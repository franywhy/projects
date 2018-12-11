<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>
	
	<div class="xxzx-container">
    	<div class="con-width">
    		<#include "../common/nav.ftl" encoding="UTF-8" parse=true>
    		
            <div class="container-right">
            	<div class="result-title">
            		<div class="nav" id="nav">
                        <ul>
                            <li class="active"><a href="javascript:">成绩登记</a></li>
                            <li><a href="/learningCenter/web/examinationResult/listPage">成绩记录</a></li>
                        </ul>
                    </div>
            	</div>
            	
            	<div class="result-form">
            		<div class="list">
            			<span class="list-title">
            				<i>*</i>
            				<span>报考课程</span>
            			</span>
            			<span class="ipt-box" id="chooseCourse">
            				<span class="ipt-placeholder">请选择报考课程</span>
            				<input type="text" class="ipt cp" disabled value="">
            				<span class="solid-down-arrow-icon-p">
            					<span class="solid-down-arrow-icon bg-cover"></span>
            				</span>
            			</span>
            			<div class="select" id="chooseList">
            				<#if data[0]??>
		                        <#list 0..(data!?size-1) as i>
		                        	<div class="select-list" data-id="${data[i].id}" data-orderName="${data[i].orderName}" data-registerPk="${data[i].registerPk}" data-registerProinve="${data[i].registerProinve}" data-scheduleDate="${data[i].scheduleDate}">${data[i].courseName}</div>
			                    </#list>
			                </#if>
            			</div>
            			<span class="ipt-prompt">
            				<span class="orange-prompt-icon bg-cover"></span>
            				<span class="text">该成绩已提交，请勿再提交</span>
            			</span>
            		</div>
            		
            		<div class="list">
            			<span class="list-title">
            				<i>*</i>
            				<span>报考单号</span>
            			</span>
            			<span class="ipt-box">
            				<input type="text" class="ipt default" id="num" disabled value="">
            			</span>
            		</div>
            		
            		<div class="list">
            			<span class="list-title">
            				<i>*</i>
            				<span>报考省份</span>
            			</span>
            			<span class="ipt-box">
            				<input type="text" class="ipt default" id="province" disabled value="">
            			</span>
            		</div>
            		
            		<div class="list">
            			<span class="list-title">
            				<i>*</i>
            				<span>报考时间</span>
            			</span>
            			<span class="ipt-box">
            				<input type="text" class="ipt default" id="time" disabled value="">
            			</span>
            		</div>
            		
            		<div class="list">
            			<span class="list-title">
            				<i>*</i>
            				<span>报考班型</span>
            			</span>
            			<span class="ipt-box">
            				<input type="text" class="ipt default" id="classType" disabled value="">
            			</span>
            		</div>
            		
            		<div class="list">
            			<span class="list-title">
            				<i>*</i>
            				<span>统考成绩</span>
            			</span>
            			<span class="ipt-box">
            				<input type="text" class="ipt" maxlength="3" id="score" value="">
            			</span>
            		</div>
            		
            		<div class="list">
            			<span class="list-title">
            				<i class="hidden">*</i>
            				<span>成绩截图</span>
            			</span>
            			<span class="ipt-box">
            				<input type="file" class="file-ipt" id="uploadImg" value="">
            				<img src="/resources/images/common/big_add_icon.png" class="upload-img-icon" data-type="0" id="uploadImgIcon">
            				<div class="upload-img-mask-box" id="uploadImgMaskBox">
            					<div class="upload-img-mask"></div>
            					<p>修改图片</p>
            				</div>
            				<p class="upload-img-prompt">支持jpg、jpeg、png、bmp格式，不要超过5M</p>
            			</span>
            		</div>
            		
            		<div class="btn-box">
            			<button type="button" id="sub">提 交</button>
            		</div>
            	</div>
            	
            </div>
		</div>
	</div>
	
	<div id="resultFormLayer">
	    <div class="mask-layer">
	        <div class="mask opacity-05"></div>
	        <div class="att-content-bar" style="margin-top: -353px;">
	        	<div class="att-content" style="height: 706px;">
	            	<div class="grey-close-p">
	            		<span class="grey-close-icon grey-close-icon-1 bg-cover"></span>
	                </div>
	                <p class="title" style="margin-top: 30px;">成绩登记</p>
	                <div class="preview-info">
	                	<div class="list">
	                		<span class="list-title">报考课程</span>
	                		<input type="text" class="ipt default" id="previewCourse" disabled value="">
	                	</div>
	                	<div class="list">
	                		<span class="list-title">报考单号</span>
	                		<input type="text" class="ipt default" id="previewNumber" disabled value="">
	                	</div>
	                	<div class="list">
	                		<span class="list-title">报考省份</span>
	                		<input type="text" class="ipt default" id="previewProvince" disabled value="">
	                	</div>
	                	<div class="list">
	                		<span class="list-title">报考时间</span>
	                		<input type="text" class="ipt default" id="previewTime" disabled value="">
	                	</div>
	                	<div class="list">
	                		<span class="list-title">报考班型</span>
	                		<input type="text" class="ipt default" id="previewClassType" disabled value="">
	                	</div>
	                	<div class="list">
	                		<span class="list-title">统考成绩</span>
	                		<input type="text" class="ipt default" id="previewScore" disabled value="">
	                	</div>
	                	<div class="list">
	                		<span class="list-title">统考截图</span>
	                		<img src="" class="preview-img" id="previewImg">
	                	</div>
	                </div>
	                <div class="btn-box">
	                	<a href="javascript:;" class="sub-btn radius-5px" id="confirmSubmit">确认并提交</a>
	                	<a href="javascript:;" class="cancel-btn radius-5px" id="cancel">取消</a>
	                <div>
	                <div class="prompt-box">
	                	<span class="orange-prompt-icon bg-cover"></span>
	                	<span class="text">注意：请再次确认您的信息，提交后无法修改！</span>
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
    <script type="text/javascript" src="/resources/compress/js/studycenter/result.js"></script>
</body>
</html>
</#compress>