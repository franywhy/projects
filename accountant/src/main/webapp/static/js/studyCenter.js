$(function(){
	courseSlide();			//课程切换
	scListSlide();
	perBarWidth();          //学习进度百分比
	dianSlide();
});

function scListSlide(){
    $(".sc-list-box .sc-nav p").on("click", function () {
		var i = $(this).index();
		$(this).addClass("act").siblings("p").removeClass("act");
		$(".sc-list-box .sc-body .course-box").eq(i).stop(true,true).fadeIn().siblings(".course-box").stop(true,true).fadeOut(0);
	});
	
    $(".sc-list-box .sc-body .course-box .c-head").on("click", function () {
		if($(this).find("i").hasClass("act")){
			$(this).find("i").removeClass("act");
			$(this).siblings().stop(true,true).slideUp();
		}else{
			$(this).find("i").addClass("act");
			$(this).siblings().stop(true,true).slideDown();
		}
	});
}

//学习进度百分比
function perBarWidth(){
	$(".sc-fly-class .b-1 .b-1-r .sc-bar p").each(function(){
		var str = $(this).text();
		$(this).parent().css({"width":str});
	});
}

function dianSlide(){
    $(".select-bar li").on("click", function () {
		var i = $(this).index();
		$(this).addClass("act").siblings().removeClass("act");
		//$(".section-course").eq(i).stop(true,true).fadeIn().siblings(".section-course").stop(true,true).fadeOut(0);
	});
}
