<#compress>
<#include "/common/top.ftl" encoding="UTF-8" parse=true>
<body class="bg-f2">
	<#include "/common/header.ftl" encoding="UTF-8" parse=true>
    
    <div class="xxzx-container">
    	<div class="con-width">
    		<#include "../common/nav.ftl" encoding="UTF-8" parse=true>
            
            <div class="container-right">
            	<div class="sc-main-box-r right">
                    <div class="main-big-box">
                    <#--
                    <div class="xuefen-table"><table style="BORDER-COLLAPSE: collapse" bordercolor="#666" cellspacing="0" width="950px" align="center" bgcolor="#ffffff" border="1"><tbody><tr style="background-color: #F3F3F3"><th rowspan="2"><div align="center">班级</div></th><th rowspan="2"><div align="center">科目</div></th><th colspan="3"><div align="center">获得的学分</div></th><th rowspan="2"><div align="center">综合学分<br>满分50分</div></th><th rowspan="2"><div align="center">毕业学分标准<br>≥每科总分*80%</div></th><th rowspan="2"><div align="center">学习建议</div></th></tr><tr style="background-color: #F3F3F3"><td><div align="center">考勤</div></td><td><div align="center">作业</div></td><td><div align="center">结课考核</div></td></tr><tr><td rowspan="7"><div align="center">全能级（有证）</div></td></tr><tr><td><div align="center">电脑账</div></td><td><div align="center"><div class="t-h1">实修<span>2分</span></div><div class="t-h2">应修<span>2分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>2分</span></div><div class="t-h2">应修<span>2分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>6分</span></div><div class="t-h2">应修<span>6分</span></div></div></td><td rowspan="7"><div align="center">当前学分<br>37分 !</div></td><td rowspan="7"><div align="center">你的学分还没<br>达到毕业标准<br>要加油哦!<br></div></td><td rowspan="7"><div align="center"></div></td></tr><tr><td><div align="center">Excel财务运用实战</div></td><td><div align="center"><div class="t-h1">实修<span>5分</span></div><div class="t-h2">应修<span>5分</span></div></div></td><td><div align="center">———</div></td><td><div align="center">———</div></td></tr><tr><td><div align="center">手工账</div></td><td><div align="center"><div class="t-h1">实修<span>2分</span></div><div class="t-h2">应修<span>2分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>2分</span></div><div class="t-h2">应修<span>2分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>6分</span></div><div class="t-h2">应修<span>6分</span></div></div></td></tr><tr><td><div align="center">仿真税务软件实训</div></td><td><div align="center"><div class="t-h1">实修<span>5分</span></div><div class="t-h2">应修<span>5分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>2分</span></div><div class="t-h2">应修<span>5分</span></div></div></td><td><div align="center">———</div></td></tr><tr><td><div align="center">出纳岗位实操</div></td><td><div align="center"><div class="t-h1">实修<span>5分</span></div><div class="t-h2">应修<span>5分</span></div></div></td><td><div align="center">———</div></td><td><div align="center">———</div></td></tr><tr><td><div align="center">工业会计实战</div></td><td><div align="center"><div class="t-h1">实修<span>0分</span></div><div class="t-h2">应修<span>2分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>0分</span></div><div class="t-h2">应修<span>2分</span></div></div></td><td><div align="center"><div class="t-h1">实修<span>0分</span></div><div class="t-h2">应修<span>6分</span></div></div></td></tr></tbody></table></div></div>
                    -->
                    <#if result??>
                    	${result}
                    <#else>
						<div class="my_curriculum_empty">
		                	<div class="curriculum_empty_icon bg-cover"></div>
		                    <p>暂无学分</p>
		                </div>
                    </#if>
                </div>
            </div>
		</div>
	</div>
</body>
</html>
﻿</#compress>
