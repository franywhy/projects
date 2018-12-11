$(function(){
	courseSlide();			//课程切换
	navCenter();            //导航居中
//	hoverYuyue();
	allCourseShow();        //全部课程展开

	loadingImg($(".c-box .c-box-top"),[
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg"
	]);
	loadingImg($(".exp-box-right .exp-r-box .box-l"),[
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg",
		"images/temp/index-course-img-1.jpg"
	]);
	
});

//导航居中
function navCenter(){
	var num = 0;
	$(".main-bar-nav li").each(function(){
		num += $(this).outerWidth(true);
	});
	num = (1200-num)/2;
	$(".main-bar-nav").css({"paddingLeft":num +"px"});
}
//已预约
//function hoverYuyue(){
//	$(".exp-box-right .exp-r-box .box-r .t-p.yuyue-p").hover(function(){
//		$(this).text("立即观看");
//	},function(){
//		$(this).text("已预约");
//	});
//}

//全部课程展开
function allCourseShow(){
	var btn = $(".t-item .t-p-6");
	btn.on("click",function(){
		var obj = $(this).parents(".exp-r-box").find(".c-item-out");
		if($(this).hasClass("act")){
			$(this).removeClass("act");
			obj.slideUp();
		}else{
			$(this).addClass("act");
			obj.slideDown();
		}
	});
}
