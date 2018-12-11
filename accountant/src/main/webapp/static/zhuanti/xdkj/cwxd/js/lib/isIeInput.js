/*
** @ 这个是个兼用 IE下 不能使用 placeholder 属性的插件
** @ 只需引用本插件，在你需要某个标签有获取焦点隐藏文字提示，离开显示文字提示 时使用 
** @ 本插件只适用IE9以下，其他浏览器无效果，因为其他浏览器支持CSS3 placeholder 属性
** @ author huangxuejie
*/
$(function(){
	//判断IE各个版本
	var isIE6 = /msie 6/i.test(navigator.userAgent);
	var isIE7 = /msie 7/i.test(navigator.userAgent);
	var isIE8 = /msie 8/i.test(navigator.userAgent);
	var isIE9 = /msie 9/i.test(navigator.userAgent);
	var isIE = /msie/i.test(navigator.userAgent);
	function pwIconPosi(setPostion){
		var _this = this
		var _top = setPostion.top || "50%";
		var _left = setPostion.left || "35px";
		// //找到所有Input的value值并清空
		// $('input').each(function(i){
		// 	var inputValue = $(this).val();
		// 	$(this).attr('placeholder',inputValue);
		// 	$(this).attr('value','');
		// 	//清除其标签的 onblur 与 onfocus 属性，以防止BUG出现
		// 	$(this).removeAttr('onblur');
		// 	$(this).removeAttr('onfocus');
		// })

		//判断是否在IE环境下
		if(isIE6 || isIE7 || isIE8 || isIE9){
			$('input[placeholder]').each(function(){
				/*-=-=-=-=-=-=-=-=*/
				if($(this).attr("type")=="text"){
					var thisVal = $(this).attr('placeholder');

					$(this).val(thisVal).css({color:"#999999"});
					$(this).focus(function(){
						if($(this).val()==thisVal){
							$(this).val("").css({color:"#555555"});
						}
					});
					$(this).blur(function(){
						if($(this).val()==""){
							$(this).val(thisVal).css({color:"#999999"});
						}
					})
				}
			
			})

			$("textarea").each(function(){
				var thisVal = $(this).attr('placeholder');
				$(this).text(thisVal).css({color:"#999999"});
				$(this).focus(function(){
					if($(this).text()==thisVal){
						$(this).text("").css({color:"#555555"});
					}
				});
				$(this).blur(function(){
					if($(this).text()==""){
						$(this).text(thisVal).css({color:"#999999"});
					}
				})
			});
		}else{
			return console.log("这不是IE9下的浏览器");
		}
	}
	var ba = pwIconPosi({
		top:"50%",
		left:"40px"
	});
})