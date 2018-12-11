$(function(){

	/*学校下拉*/
	$(".btn-select-school").click(function(){
		$(".school-list").slideDown(200);
	});
	$(".left-nav").mouseleave(function(event) {
		$(".school-list").slideUp(200);
	});
	$(".head").animate({opacity:0.7},function(){
		$(this).addClass('bg-opacity');
		$(this).removeAttr('style');
	})
	
	$(".header").addClass('bg-opacity');
	$(".header").removeAttr('style');
	
	if($.browser.msie && ($.browser.version == "7.0") || $.browser.msie && ($.browser.version == "8.0") || $.browser.msie && ($.browser.version == "6.0")){
		$(".headder-bg-opacity,.head-bg-opacity,.headder-nav-bg-opacity").addClass('ie9next')
	}else{
		//$(".headder-bg-opacity,.head-bg-opacity,.headder-nav-bg-opacity").remove();
	}

	var arrayColor = ["#f23628","#4551a6","#058cc2","#89c73f"]

	$(".crae-data dd").hover(function(){
		var index = $(this).index();
		var _thisCh = $(this).children('.data-dd-text');
		var _H= _thisCh.outerHeight(true);
		_thisCh.show();
		$(this).css({borderColor:arrayColor[index]});
		_thisCh.stop().animate({top:-_H-30,opacity:1},300)
		$(this).children('.data-dd-text').show();

	},function(){
		var _thisCh = $(this).children('.data-dd-text');
		var _H= _thisCh.outerHeight(true);
		$(this).css({borderColor:'#ffffff'});
		_thisCh.stop().animate({top:0,opacity:0},300,function(){
			_thisCh.hide()
		});
		
	})
	
	var animLeft = ["210",'500','780'];
	$(".Ef-xueyuan-selcet ul li").hover(function() {
		var index = $(this).index();
		$(this).css({backgroundColor:"#f4752d"});
		$(".Ef-xueyuan-selcet dl").show();
		$(".Ef-xueyuan-selcet dl dd").hide();
		$(".Ef-xueyuan-selcet dl dd").eq(index).show(300);
		$(".Ef-xueyuan-selcet dl i").animate({left:animLeft[index]}, 300);

	}, function() {
		var index = $(this).index();
		$(this).css({backgroundColor:"#ffffff"})
		$(".Ef-xueyuan-selcet dl").hide();
		/* Stuff to do when the mouse leaves the element */
	});


	$(".voide-btn-pop").live('click',function() {
		$(".fixed-bg ,.voide-box").fadeIn();
		$(".voide-box").append("<div class='btn-off-box'></div>");
	});
	$(".fixed-bg,.btn-off-box").live('click',function() {
		$(".fixed-bg ,.voide-box ,.delong-box").fadeOut();
	});

	$(".moshi-right-dloaing,.btn-dlong").click(function() {
		$(".fixed-bg ,.delong-box").fadeIn();
		$(".delong-box").append("<div class='btn-off-box'></div>");
	});

	
})