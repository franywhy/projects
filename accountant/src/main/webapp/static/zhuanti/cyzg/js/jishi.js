function _fresh()  
	{  
		
		var endtime=new Date(time);  
		var nowtime = new Date(); //
		var leftsecond=parseInt((endtime.getTime()-nowtime.getTime())/1000);  
	 	__d=parseInt(leftsecond/3600/24);  
	 	__h=parseInt((leftsecond/3600)%24);  
	 	__m=parseInt((leftsecond/60)%60);  
	 	__s=parseInt(leftsecond%60);
		var c=new Date();
		var q=((c.getMilliseconds()));
	 	$("#ds").text(__d);  
	 	$("#hs").text(__h);  
	 	$("#ms").text(__m);  
	 	$("#ss").text(__s);  
		if($("#ds").text()<10){$("#ds").text("0"+__d)}
		if($("#hs").text()<10){$("#hs").text("0"+__h)}
 		if($("#ms").text()<10){$("#ms").text("0"+__m)}
 		if($("#ss").text()<10){$("#ss").text("0"+__s)}
		 if(leftsecond<=0){
		 	

		 	$(".cy-main1-2").hide();
		 	$(".jishi").html("时间到")
	 		clearInterval(sh);
	 		$("#us").text("00");
	 		return;
		 }
	}
_fresh();
var sh;  
sh=setInterval(_fresh,100);  



$(function(){
	var index=0;
	var len = $(".tabMeun span").length;
	var w = $(".img-li").width();
	$(".tabMeun span").hover(function(){
		index = $(this).index()
		showCon();
	})

	$(".btn-pageNext").click(function(){
		
			index ++;
			if(index>len-1){
				index=0;
			}
			showCon()
		
	})

	$(".btn-pagePver").click(function(){
		
			index --;
			if(index<0){
				index=len-1;
			}
			showCon()
		
	})
	function showCon(){
		$(".showCon .ul").stop().animate({marginLeft:-index*w});
		$(".tabMeun span").eq(index).addClass("active").siblings().removeClass("active")
	}
	
})