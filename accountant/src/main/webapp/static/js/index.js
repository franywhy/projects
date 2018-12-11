$(function(){
//	courseSlide();			//课程切换
////	课程图片加载完成再显示   需先加载数据
//	loadingImg($(".c-box .c-box-top"),[
//		"http://attachments.gfan.com/forum/201601/26/074503cc9c90psss4bsncg.jpg",
//		"http://attach.bbs.miui.com/forum/201503/06/162417trpz6vobv63b6v44.jpg",
//		"http://attach.bbs.miui.com/forum/201502/13/183109ac85u6wvqwd688sv.jpg",
//		"http://img.elife.com/forum/201411/19/170208aaw788ajfl9ezaud.jpg",
//		"images/temp/index-course-img-1.jpg"
//	]);
	ksccBtn();
});

function ksccBtn(){
	var btn = $(".kscc-main .btn");
	var pop = $(".video-box");
	var bg = $(".black-bg");
	var close = pop.find(".close-btn");
	var mp4 = pop.find("video");
	
	btn.off("click").on("click",function(){
		bg.fadeIn(0);
		pop.fadeIn();
		mp4.get(0).play();
	});
	
	close.off("click").on("click",function(){
		bg.fadeOut();
		pop.fadeOut();
		mp4.get(0).pause();
	});
}
