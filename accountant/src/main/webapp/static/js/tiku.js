$(function(){
	tikuModehover();        //题库 做题模式 移入
	canvasCreate();
	titleNavSlide();
	liHeight();
	hoverTit();
});

//题库 做题模式 移入
function tikuModehover(){
	$(".tiku-mode li").hover(function(){
		$(this).find(".tiku-start").stop(true,true).fadeIn(200);
		$(this).find(".tiku-vip").stop(true,true).fadeOut(200);
	},function(){
		$(this).find(".tiku-start").stop(true,true).fadeOut(200);
		$(this).find(".tiku-vip").stop(true,true).fadeIn(200);
	});
}

function canvasCreate(){
	$('#indicator').radialIndicator({
		radius: 70,
        barColor: '#66bb6a',
        barWidth: 10,
        initValue: 0,
        fontWeight: 'normal',
        roundCorner : true,
        percentage: true,
        fontFamily:"微软雅黑",
        fontSize : "20",
    });
    var radialObj = $('#indicator').data('radialIndicator');
	//now you can use instance to call different method on the radial progress.
	//like
	radialObj.animate(60);
}

function titleNavSlide(){
	var o = $(".slideDown-select");
	$(".reg .main-bar-nav .tiku-nav").on("click",function(){
		o.css({"display":"block"}).stop(true,true).animate({"opacity":"1","top":"60px"}).css({"filter":"Alpha(opacity=100)"});
		return false;
	});
	$(".slideDown-select .close-btn").on("click",function(){
		o.stop(true,true).animate({"opacity":"0","top":"0px"},function(){
			o.css({"display":"none"});
		}).css({"filter":"Alpha(opacity=0)"});
		return false;
	});
}

function liHeight(){
	Array.prototype.max = function(){ 
		return Math.max.apply({},this) 
	} 
	var o = $(".tiku-main-a .tiku-fen li .tiku-fen-bot");
	var arr = [];
	var maxHei;
	o.each(function(){
		arr.push($(this).outerHeight());
	});
	maxHei = arr.max();
	o.css({"height":maxHei});
}

function hoverTit(){
	$(".tiku-main-a .tiku-fen li.t-li-1 .tiku-fen-top").on("click",function(){
		$(".no-reg").hide(0);
		$(".reg").show(0);
	});
}
