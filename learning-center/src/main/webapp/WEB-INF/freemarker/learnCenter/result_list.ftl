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
                            <li><a href="/learningCenter/web/examinationResult/addPage">成绩登记</a></li>
                            <li class="active"><a href="javascript:">成绩记录</a></li>
                        </ul>
                    </div>
            	</div>
            	
            	<div class="result-table">
                	<table>
                    	<tr>
                        	<th class="min-w-97px pl-30px">序号</th>
                            <th class="min-w-205px">报考课程</th>
                            <th class="min-w-121px">报考单号</th>
                            <th class="min-w-96px">报考省份</th>
                            <th class="min-w-95px">报考时间</th>
                            <th class="min-w-136px">报考班型</th>
                            <th class="min-w-101px">统考成绩</th>
                            <th class="min-w-87px">成绩截图</th>
                        </tr>
                        <#if data[0]??>
                        	<#list 0..(data!?size-1) as i>
								<tr>
		                    		<td class="pl-30px">${i+1}</td>
		                    		<td>
		                    			<div class="text-overflow" style="width:170px;">${data[i].courseName}</div>
		                    		</td>
		                    		<td>
		                    			<div class="text-overflow" style="width:100px;">${data[i].registerPk}</div>
		                    		</td>
		                    		<td>
		                    			<div class="text-overflow" style="width:65px;">${data[i].registerProinve}</div>
		                    		</td>
		                    		<td>
		                    			<div class="text-overflow" style="width:96px;">${data[i].scheduleDate}</div>
		                    		</td>
		                    		<td>
		                    			<div class="text-overflow" style="width:105px;">${data[i].orderName}</div>
		                    		</td>
		                    		<td>
		                    			<div class="text-overflow" style="width:45px;">${data[i].score}</div>
		                    		</td>
		                    		<td>
		                    			<#if data[i].img??>
		                    				<#if data[i].img != ''>
		                    					<div class="preview-img-box">
		                    						<a href="javascript:">查看图片</a>
		                    						<img src="${data[i].img}" class="preview-img" alt="img">
		                    					</div>
		                    				</#if>
		                    			</#if>
		                    		</td>
		                 		</tr>
		                 	</#list>
						<#else>
							<tr>
								<td colspan="7" class="bb-none">
									<div class="my_curriculum_empty" id="myCurriculumEmpty">
					                	<div class="curriculum_empty_icon bg-cover"></div>
					                    <p>暂无成绩记录</p>
					                </div>
				                </td>
			                </tr>
						</#if>
                    </table>
                </div>
                
            </div>
		</div>
	</div>
	
	<link rel="stylesheet" type="text/css" href="/resources/compress/lib/viewer/viewer.css">
	<script type="text/javascript" src="/resources/compress/lib/viewer/viewer.js"></script>
    <script type="text/javascript">
    	$(function(){
    		var viewer = $(".preview-img").viewer({
    			title: false,
    			navbar: false,
    			toolbar: false,
    			interval: 1500
    		});
    		
    		$(document).on("click", ".viewer-canvas", function(){
    			$(".viewer-button").trigger("click");
    		});
    		
    		//阻止冒泡事件
    		$(document).on("click", ".viewer-move", function(){
    			return false;
    		});
    	});
    </script>
</body>
</html>
</#compress>