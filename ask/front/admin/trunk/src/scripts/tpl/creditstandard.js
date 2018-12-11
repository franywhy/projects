// 定义一个数组存储被修改的学分数据
var credits = new Array();
$(function() {
	//获取class为auto_edit的元素 
	$(".auto_edit").click(function() {
		// 将当前获取焦点的元素的text值取出
		var td = $(this);
		var tdText = td.text();
		// 创建一个文本框元素并设置值
		var input = $('<input class="input" type="number" style="width:50px;" value="'+tdText+'"/>')//$("<input style='width:50px;' type='text' value='" + tdText + "'/>");
		var idValue = td.attr("id");
		if(idValue == "auto_edit_info"){
			input = $("<input style='width:300px;' type='text' value='" + tdText + "'/>");
		}else if(idValue == "auto_edit_type"){
			var type = "<select style='width:120px' type='text' class='input' id='subject_type' name='subject_type'>";
			type += "<option value=''></option>";
			type += "<option value='会计基础'>会计基础</option>";
            type += "<option value='财经法规与职业道德'>财经法规与职业道德</option>";
            type += "<option value='初级会计电算化'>初级会计电算化</option>";
            type += "<option value='EXCEL财务运用'>EXCEL财务运用</option>";
            type += "<option value='手工账'>手工账</option>";
type += "<option value='电脑胀'>电脑胀</option>";
type += "<option value='税务课'>税务课</option>";
type += "<option value='工业会计实战'>工业会计实战</option>";
type += "<option value='初级会计实务'>初级会计实务</option>";
type += "<option value='经济法基础'>经济法基础</option>";
type += "<option value='中级会计实务'>中级会计实务</option>";
type += "<option value='中级经济法'>中级经济法</option>";
type += "<option value='中级财务管理'>中级财务管理</option>";
type += "<option value='出纳课'>出纳课</option>";
type += "<option value='财务管理'>财务管理</option>";

			type +="</select>";
			input = $(type);
		}else if (idValue=="auto_exam_type"){
				var type = "<select style='width:120px' type='text' class='input' id='exam_type' name='exam_type'>";
			type += "<option value=''></option>";
 

			type += "<option value='cy'>从业考试</option>";
            type += "<option value='cj'>初级考试</option>";
            type += "<option value='zj_shiwu'>中级会计实务</option>";
            type += "<option value='zj_jingjifa'>中级经济法</option>";
            type += "<option value='zj_guangli'>中级财务管理</option>";
 

			type +="</select>";
			input = $(type);
		}
		// 添加到当前获取焦点的元素中
		td.html(input);
		input.click(function() {
			return false;
		});
		// 文本框默认获取焦点 
		input.trigger("focus");
		//文本框失去焦点后提交内容，重新变为文本
		input.blur(function() {
			var attributeVal = $(this).val();
			//判断文本有没有修改 
			if(attributeVal != tdText) {
				td.html(attributeVal);
				// 获取td的name属性值该值存放要修改的对象属性名字
				var attribute = td.attr("name");
				// 获取tr的did属性值该值存放要修改的对象的id值
				var did = td.parent().attr("did");
				// 从数组根据id获取credit对象
				var credit = (function() {
					for(var i = 0; i < credits.length; i++) {
						var credit = credits[i];
						if(credit.id == did) {
							return credit;
						}
					}
					return null;
				})(credits, did);
				// 如果已经存在credit对象则更新或添加属性和值
				if(null != credit) {

					console.log("原对象：");
					console.log(credit);
					credit[attribute] = attributeVal;
					console.log("新对象：");
					console.log(credit);

				} else {
					// 无credit对象则将当前修改的添加进去
					credits[credits.length] = jQuery.parseJSON('{"id":"' + did + '","' + attribute + '":"' + attributeVal + '"}');
					console.log("新建标识对象,ID为：" + did);
				}
				console.log(JSON.stringify(credits));
				/* 
				*不需要使用数据库的这段可以不需要 
				var caid = $.trim(td.prev().text()); 
				//ajax异步更改数据库,加参数date是解决缓存问题 
				var url = "../common/Handler2.ashx?caname=" + newtxt + "&caid=" + caid + "&date=" + new Date(); 
				//使用get()方法打开一个一般处理程序，data接受返回的参数（在一般处理程序中返回参数的方法 context.Response.Write("要返回的参数");） 
				//数据库的修改就在一般处理程序中完成 
				$.get(url, function(data) { 
				if(data=="1") 
				{ 
				alert("该类别已存在！"); 
				td.html(txt); 
				return; 
				} 
				alert(data); 
				td.html(newtxt); 
				}); 
				*/
			} else {
				td.html(attributeVal);
			}
		});
	} 

 



	);
	
});