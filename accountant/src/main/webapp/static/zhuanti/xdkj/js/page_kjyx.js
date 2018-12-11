$(function () {
    judgeUrl($(".header-kjyx"), $(".main-page"), $(".header-v2").outerHeight(true));
    backTop($("#back-to-top"));
    slideBanner2(4000, $(".slide-banner-2"), $(".slide-banner-2 .slide-ul-2"), $(".slide-banner-2 .slide-li-2"), $(".slide-banner-2 .slide-dd-2"));
});

//切换banner
function slideBanner2(t, sb, ul, li, dd) {
    var ww = document.documentElement.clientWidth;
    //			var sb = $(".slide-banner");
    //			var ul = sb.find(".slide-ul");
    //			var li = sb.find(".slide-li");
    //			var dd = sb.find(".slide-dd");
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
	    $(".hqjy-nav-new").show(0);
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
function backTop(obj) {
    obj.click(function () {
	    $('body,html').animate({ scrollTop: 0 }, 500);
	});
}