document.body.addEventListener('touchstart',function(){});
$(function () {
    dateCd("April 18,2017", $(".djs-block em"));
    if (document.documentElement.clientWidth <= 768) {
        backTopH5();
    }
});
//报名日期倒计时
function dateCd(date,obj){
	var nowDate = new Date();
	var toDate = new Date(date);
	console.log(toDate.getTime()+"=="+nowDate);
	var s = toDate.getTime() - nowDate.getTime();
	s = Math.ceil(s/1000/60/60/24);

	if (s <= 1) {
	    obj.removeClass("san").text("01");
	} else if(s<10){
		obj.text("0"+s);
	}else if(s<100){
		obj.text(s);
	}else{
		obj.text(s);
	}
}
//返回顶部
function backTopH5(){
	var flag = true;
	var backTop;
	$(window).on("scroll",function(){
		if($(this).scrollTop()>800){
			if(flag){
				flag = false;
				$('<div class="back-top"><i>&and;</i></div>').appendTo("body");
				$(".back-top").on("touchend",function(){
					$("html,body").animate({"scrollTop":0},300);
				});
			}
		}else{
			flag = true;
			$(".back-top").remove();
		}
	});
}