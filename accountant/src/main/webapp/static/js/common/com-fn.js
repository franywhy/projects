var ie9 = false;
if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE8.0"||navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE9.0"){
	ie9 = true;
}
var timerr;
$(function(){
    window.onresize = function () {
        if(document.documentElement.clientWidth <= 1200) {
            $("body").css("width", 1200);
        }
    }
	headerNavHover();        //头部导航
	slideBanner();           //banner切换
	headerUserPhoto();        //头像下拉菜单
	backTop($(".backTop"),300); //返回顶部
	floatNavRightCode();     //右侧导航二维码
	scrollFix($(window),$(".fix-nav"));
	courseNavA();          //头部导航课程移入下拉
	courseNavAslide();     //头部 课程展示效果       
//	clickShowLeyu($(".float-nav-right .t-p-1,.ly-zixun"));  //点击打开乐语         
	feedbackPop($(".feedBackClass"));           //反馈按钮
});

//头部导航移入
function headerNavHover(){
	$(".header-v2 .nav li").not(".act,.course-nav-a").hover(function(){
		$(this).css({"color":"#18aa1f"});
		var w = $(this).width();
		$(this).find("i").stop().animate({"width":w},300);
		$(".course-nav-a").css({"color":"#333"});
	},function(){
		$(this).css({"color":"#333"});
		$(this).find("i").stop().animate({"width":"0"},300);
	});
	$(".header-v2").on("mouseleave",function(){
		$(".course-nav-a").css({"color":"#333"});
	});
	$(".header-2 .nav .act").on("mouseenter",function(){
		$(".course-nav-a").css({"color":"#333"});
	});
}
//切换banner
function slideBanner(){
	clearInterval(timerr);
	var ww = document.documentElement.clientWidth;
	if(ww<=1200&&ww>768){ww = 1200;}
	var sb = $(".slide-banner");
	var ul = sb.find(".slide-ul");
	var li = sb.find(".slide-li");
	var dd = sb.find(".slide-dd");
	var index = 0;
	
	li.each(function(){$(this).css("width",ww);});
	ul.css({"width":ww*li.length});
	
	timerr = setInterval(function(){
		index++;
		if(index>=li.length){index=0;}
		dd.eq(index).addClass("act").siblings("dd").removeClass("act");
		ul.stop(true,true).animate({"marginLeft":-ww*index},300);
	},4000);
	
	dd.off("mouseenter");
	dd.off("mouseleave");

	dd.hover(function(){
		clearInterval(timerr);
		var i = $(this).index();
		index = i;
		$(this).addClass("act").siblings("dd").removeClass("act");
		ul.stop(true,true).animate({"marginLeft":-ww*index},300);
	},function(){
		timerr = setInterval(function(){
			index++;
			if(index>=li.length){index=0;}
			dd.eq(index).addClass("act").siblings("dd").removeClass("act");
			ul.stop(true,true).animate({"marginLeft":-ww*index},300);
		},4000);
	});
	
}

//返回顶部
function backTop(obj,t){
	var t = t || 500;
	obj.on("click",function(){
		$("html,body").animate({"scrollTop":0},t);
	});
}

//头像下拉菜单
function headerUserPhoto(){
	var o = $(".register-yes-out .dl-out");
	var flag = true;
	$(".register-yes").on("mouseenter",function(){
		if(flag){
			flag = false;
			o.stop(true,true).slideDown(200,function(){
				flag = true;
			});
		}
	});
	$(".register-yes-out").on("mouseleave",function(){
		if(flag){
			flag = false;
			o.stop(true,true).slideUp(200,function(){
				flag = true;
			});
		}
	});
};

//二维码显示
function floatNavRightCode(){
	$(".phone-code").hover(function(){
		$(this).find("dl").stop().fadeIn(200);
	},function(){
		$(this).find("dl").stop().fadeOut(200);
	});
}

//课程切换
function courseSlide(){
	$(".main-big-box").each(function(){
		var slideNav = $(this).find(".main-bar-nav li");
		var slideBox = $(this).find(".main-course-box-out .main-course-box");
		slideNav.on("click",function(){
			var i = $(this).index();
			$(this).addClass("act").siblings("li").removeClass("act");
			slideBox.eq(i).stop(true,true).fadeIn().siblings().stop(true,true).fadeOut(0);
		});
	});
}

//头部课程移入下拉
function courseNavA(){
	var a = $(".course-nav-a").find("em");
	var xia = $(".course-xiala");
	var navA = $(".course-nav-a");
	$(".course-nav-a").on("mouseenter",function(){
		$(this).css({"color":"#18aa1f"});
		if(xia.css("display") == "block"){
			a.stop(true,true).fadeIn(0);
			xia.stop(true,true).fadeIn(0);
		}else{
			a.stop(true,true).fadeIn(200);
			xia.stop(true,true).fadeIn(200);
		}	
	});
//	$(".course-xiala,.course-nav-a").on("mouseleave",function(){
//		a.stop(true,true).fadeOut(100);
//		xia.stop(true,true).fadeOut(200);
//	});
	
//	xia.on("mouseenter",function(){
//		a.stop(true,true).fadeIn(0);
//		xia.stop(true,true).fadeIn(0);
//	});
	
	xia.on("mouseleave",function(){
		$(".course-nav-a").css({"color":"#333"});
		a.stop(true,true).fadeOut(200);
		xia.stop(true,true).fadeOut(200);
	});
	
	$(".header-v2 .nav li").not(".course-nav-a").on("mouseenter",function(){
		$(".course-nav-a").css({"color":"#333"});
		a.stop(true,true).fadeOut(200);
		xia.stop(true,true).fadeOut(200);
	});
	
	$(".course-xiala .div-left li").on("mouseenter",function(){
		var obj = $(".course-xiala .div-right");
		var i = $(this).index();
		$(this).addClass("act").siblings("li").removeClass("act");
		obj.eq(i).show(0).siblings("dl").hide(0);
	});
	
	
}
//头部 课程展示效果
function courseNavAslide(){
	if(ie9){
		$(".course-xiala .div-right dd .t-dd").css({"opacity":"1","filter":"alpha(opacity=100)","display":"block"});
	}
	$(".course-xiala .div-right").each(function(){
		var i = 0;
		var tddBox = $(this).find("dd .t-dd-box");
		var tdd = $(this).find("dd .t-dd");
		var boxWid = tdd.eq(0).outerWidth(true);  
		var num = tdd.length;                                     
		var boxMove = $(this).find("dd");
		var max = parseInt((num+1)/5);
		var btnBox = $(this).find(".btn-box");
		var leftBtn = $(this).find("p.l-btn");
		var rightBtn = $(this).find("p.r-btn");
		
		if(tddBox.length<=1){
			btnBox.hide(0);
		}else{
			btnBox.show(0);
		}
		
		boxMove.css({"width":1000});
		
		leftBtn.click(function(){
			clickBtn("left");
		});
		rightBtn.click(function(){
			clickBtn("right");
		});
		
		tdd.hover(function(){
			$(this).find("ul").fadeTo(0,1);
			$(this).parent().siblings().find("ul").fadeTo(0,0.7);
		},function(){
			$(this).find("ul").fadeTo(0,0.7);
		});
		
		if(!ie9){tddBox.eq(0).show(0).siblings().hide(0);}
		tddBox.each(function(){
			$(this).find(".t-dd").each(function(i){
				$(this).css({"-webkit-animation":"fadeInRight 0.8s "+i*0.1+"s forwards","animation":"fadeInRight 0.6s "+i*0.1+"s forwards"});
			});
		});
		
		boxMove.on("mouseleave",function(){
			tdd.find("ul").fadeTo(0,1);
		});
		
		var clickBtn = function(dir){
			if(dir=="right"){
				i++;
				if(i<=max){
					if(ie9){
						boxMove.animate({"marginLeft":-1000*i},200);
					}else{
						tddBox.eq(i).show(0).siblings().hide(0);
					}
					
				}else{
					i=max;
					return false;
				}
				hoverColor(i);
			}else{
				i--;
				if(i>=0){
					if(ie9){
						boxMove.animate({"marginLeft":-1000*i},200);
					}else{
						tddBox.eq(i).show(0).siblings().hide(0);
					}
				}else{
					i=0;
					return false;
				}
				hoverColor(i);
			}	
		}
		
		var hoverColor = function(i){
			if(i==0){
				leftBtn.addClass("no-click");
				rightBtn.removeClass("no-click");
			}else if(i==max){
				rightBtn.addClass("no-click");
				leftBtn.removeClass("no-click");
			}else{
				leftBtn.removeClass("no-click");
				rightBtn.removeClass("no-click");
			}
		}
	});
	
}

//滚动 使元素定位  
function scrollFix(win,obj){
	var flag = true;
	var cl = {};
	if(obj.length > 0){
		var ttop = obj.offset().top;
		var lleft = obj.offset().left;
		win.on("scroll",function(){
			if($(this).scrollTop()>=ttop){
				if(flag){
					flag = false;
					cl = obj.clone(true).css({"visibility":"hidden"});
					obj.after(cl);
					obj.css({"position":"fixed","width":"100%","top":0,"left":lleft,"zIndex":"6666"});
				}
			}else{
				if(cl.length > 0){cl.remove();}
				obj.css({"position":"","zIndex":"6665"});
				flag = true;
			}
		});
	}
}

//ie placeholder
function iePlaceholder(){
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
		}else{
			return false;
		}
	}
	var ba = pwIconPosi({
		top:"50%",
		left:"40px"
	});
}

//题库下向上移入
function sxHoverTop(obj,cl){
	obj.on("mouseenter",function(){
		$(this).find(cl).css({"display":"block"}).stop(true).animate({"top":"0px"},200);
	});
	obj.on("mouseleave",function(){
		$(this).find(cl).stop(true).animate({"top":"100%"},200,function(){
			$(this).css({"display":"none"});
		});
	});
}

//图片加载  先加载数据
function loadingImg(obj,imgAdd){
//	obj.each(function(){
//		obj.css({"background":"url(images/icon/img-loading.gif) no-repeat center center"});
//		obj.find("img").css({"display":"none"});
//	});
	
	var len = imgAdd.length;
	for(var i=0; i<len; i++){
		var img = new Image();
		img.src = imgAdd[i];
		img.alt = i;
		img.onload = function(){
			var f = $(this).attr("alt");
			obj.eq(f).css({"backgroundImage":"none"});
			obj.eq(f).find("img").fadeIn();
		}
	}
}

//数据加载前 加loading
function waitDataBefore(obj){
    return $("<div class='loading-data' style='height:200px;background:url(images/icon/img-loading.gif) no-repeat center center;'></div>").appendTo(obj);
}
//数据加载完成 删除loading
function waitDataAfter(obj){
	obj.find(".loading-data").remove();
}

//试听弹框
function showShiTingPop(clickObj){
	var bg = $(".black-bg");
	clickObj.on("click",function(){
		bg.stop(true,true).fadeIn();
		bg.find(".close-btn").on("click",function(){
			$(this).parents(".black-bg").fadeOut();
			return false;
		});
	});	
}

//鼠标提示框
function mouseTipsPopup(obj,str,arr){
	var tip;
	obj.on("mouseenter",function(e){
		var x = e.clientX+10;
		var y = e.clientY+24;
		$('<div class="mouseTips">'+str+'</div>').css({"top":y,"left":x}).appendTo("body").fadeIn(200);
		tip = $(".mouseTips");
	});
	obj.on("mousemove",function(e){
		var x = e.clientX+10;
		var y = e.clientY+24;
		tip.css({"top":y,"left":x});
	});
	obj.on("mouseleave",function(e){
		tip.remove();
	});
	if(arr.length>=1){
		for(var i=0; i<arr.length; i++){
			arr[i].hover(function(){
				tip.stop(true,true).fadeOut(0);
			},function(){
				tip.stop(true,true).fadeIn(200);
			});
		}
	}
}

////乐语入口
//function leyu_kf(){
////  document.write('doyoo.util.openChat();doyoo.util.accept();return false;');
//	doyoo.util.openChat('g=10066933');
//	doyoo.util.accept('g=10066933');
//	return false;
//}
////点击打开乐语
//function clickShowLeyu(obj){
//	obj.on("click",function(){  
//		leyu_kf();
//	});                 
//}
//反馈弹框
function feedbackPop(obj){
	obj.on("click",function(){
		$(".feedback-pop-bg").stop().fadeIn();
	});
	$(".fb-no-btn,.feedback-pop dt span").on("click",function(){
		$(this).parents(".feedback-pop-bg").stop().fadeOut();
	});
};