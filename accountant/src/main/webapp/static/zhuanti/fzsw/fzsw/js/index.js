$(function(){
	$(".hover-show").hover(function(){
		var _this = $(this);
		_this.addClass('hover');
		_this.find(".hidden-text").css({backgroundColor:"rgba(0,0,0,0.2)"});
	},function(){
		var _this = $(this);

		_this.find(".hidden-text").removeAttr("style");
		_this.removeClass('hover');

	});
	/*学校下拉*/
	$(".btn-select-school").click(function(){
		$(".school-list").slideDown(200);
	});
	$(".left-nav").mouseleave(function(event) {
		$(".school-list").slideUp(200);
	});

	/**/

	var liW = $(".df-flash-lb ul li").width();
	var liLen = $(".df-flash-lb ul li").length;
	$(".df-flash-lb ul").width(liW*liLen);
	var i;
	var timer;
	var index=0;

	$(".btn-pverPage ").click(function(){
		index--;
		if(index<0){
			index=liLen-1;
		}
		lbShow();
	})
	$(".btn-nextPage").click(function(){
		index++;
		if(index>=liLen){
			index=0;
		}
		lbShow();
	})
	$(".df-flash-lb").hover(function(){
		clearInterval(timer)
	},function(){
		timer = setInterval(function(){
			index++;
			if(index>=liLen){
				index=0;
			}
			lbShow();
		},4000)
	}).trigger('mouseleave');

	function lbShow(){
		$(".df-flash-lb ul").animate({marginLeft:-liW*index}, 800)
	}
})