$(function () {
	judgeUrl($(".header-kjyx"), $(".pageMain"), $(".header-v2").outerHeight(true));
    slideBanner2(4000, $(".slide-banner-v2"), $(".slide-banner-v2 .slide-ul-v2"), $(".slide-banner-v2 .slide-li-v2"), $(".slide-banner-v2 .slide-dd-v2"));
    GetDateStr(1,$(".month"),$(".days"));
    /*yBackTop($(".ybackTop"));*/
});

//切换banner
function slideBanner2(t, sb, ul, li, dd) {
    var ww = document.documentElement.clientWidth;
    //			var sb = $(".slide-banner");
    //			var ul = sb.find(".slide-ul");
    //			var li = sb.find(".slide-li");
    //			var dd = sb.find(".slide-dd");
    // console.log( document.documentElement.clientWidth)
    var index = 0;

    li.each(function () { $(this).css("width", ww); });
    ul.css({ "width": ww * li.length });

    var timer = setInterval(function () {
        index++;
        if (index >= li.length) { index = 0; }
        dd.eq(index).addClass("act").siblings("dd").removeClass("act");
        ul.stop(true, true).animate({ "marginLeft": -ww * index }, 300);
    }, t);

    dd.hover(function () {
        clearInterval(timer);
        var i = $(this).index();
        index = i;
        $(this).addClass("act").siblings("dd").removeClass("act");
        ul.stop(true, true).animate({ "marginLeft": -ww * index }, 300);
    }, function () {
        timer = setInterval(function () {
            index++;
            if (index >= li.length) { index = 0; }
            dd.eq(index).addClass("act").siblings("dd").removeClass("act");
            ul.stop(true, true).animate({ "marginLeft": -ww * index }, 300);
        }, t);
    });
}

//判断域名
function judgeUrl(obj,objBox,ttop){
	if(document.domain.indexOf("hqjy")>-1){
		scrollShow();
	}
	else{
		obj.show(0);
		objBox.css({ "padding-top": obj.outerHeight(true) });
	}
	
	//滚动 显示
	function scrollShow(){
		$(window).on("scroll",function(){
			if($(this).scrollTop()>ttop){
				obj.show(0);
			}
			else{
				obj.hide(0);
			}
		});
	}
}
//报名日期
function GetDateStr(AddDayCount,obj1,obj2){
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    
    obj1.text(m);
    obj2.text(d);
   
//  document.write("<br />昨天："+GetDateStr(-1));
//	document.write("<br />今天："+GetDateStr(0));
//	document.write("<br />明天："+GetDateStr(1));
//	document.write("<br />后天："+GetDateStr(2));
//	document.write("<br />大后天："+GetDateStr(3));
}
function yBackTop(obj){
	obj.on("click",function(){
		$("html,body").animate({"scrollTop":0},300);
	});
	$(window).on("scroll",function(){
		if($(this).scrollTop()>600){
			obj.fadeIn();
		}
		else{
			obj.fadeOut();
		}
	});
}
