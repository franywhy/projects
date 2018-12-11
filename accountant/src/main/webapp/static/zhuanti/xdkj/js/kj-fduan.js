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
		$(".headder-bg-opacity,.head-bg-opacity,.headder-nav-bg-opacity").css({opacity:0.1});
		$(".headder-bg-opacity,.headder-nav-bg-opacity").animate({opacity:0.2}, 1000);
		$(".head-bg-opacity").animate({opacity:0.7}, 1000);

	}else{
		//$(".headder-bg-opacity,.head-bg-opacity,.headder-nav-bg-opacity").remove();
	}

	/*var arrayColor = ["#f23628","#4551a6","#058cc2","#89c73f"]

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
		
	})*/
	
	

	$(".voide-btn-pop").click(function() {
		$(".fixed-bg ,.voide-box").fadeIn();
	});
	$(".fixed-bg").click(function() {
		$(".fixed-bg ,.voide-box ,.delong-box").fadeOut();
	});

	$(".but-delong").click(function(){
		$(".fixed-bg ,.delong-box").fadeIn();
		
	})

	$(".nav-bg").css({opacity:1});
	$(".nav-bg").animate({opacity:0.5}, 1000);



	// 导航条定位
	

	var ulW = $(".h-nav-a .ul").width();
	var allW = 0;
	var nextW = 0;
	var pverW=0;
	var alen = $(".h-nav-a .ul a");
	var btnLeft = "<div class='btn-left absolute'></div>";
	var btnRight = "<div class='btn-right absolute'></div>";
	var boxbtn = $(".header-nav .w-auto-Center");
	var xy;
	var i;
	for(i=0;i<alen.length;i++){
		allW += alen.eq(i).outerWidth(true);
		nextW +=alen.eq(i+1).outerWidth(true);
		pverW +=alen.eq(i-1).outerWidth(true);
		if(i==alen.length-1){
			if(allW>1000){
				$(".h-nav-a .ul").css({width:allW});

				boxbtn.addClass('relative');
				boxbtn.append(btnLeft);
				boxbtn.append(btnRight);
				$(".btn-left").css({
					width:"24px",
					height:"38px",
					left:"-25px",
					top:"2px",
					background:"url(images/icon/heard-jt-Icon_0.png) no-repeat left top",
					display:"none",
					cursor:"pointer"
					
				});
				$(".btn-right").css({
					width:"24px",
					height:"38px",
					right:"-25px",
					top:"2px",
					background:"url(images/icon/heard-jt-Icon_0.png) no-repeat right top",
					cursor:"pointer"
				});
				clickSlide(allW);
		
			}else{
				xy = alen.length;
				clickSlide();
			}
		}
	}
	function clickSlide(){
		var index = 0;
		var wIndex =  parseInt(allW/1000);
		$(".btn-right").click(function() {
			index++;
			if(index>wIndex-1){
				index = wIndex;
				$(".btn-right").fadeOut();
				$(".btn-left").fadeIn();
			}
			$(".h-nav-a .ul").animate({marginLeft:-1000*index}, 500);
		
		});
		$(".btn-left").click(function() {
			index--;
			if(index<=0){
				index=0;
				$(".btn-right").fadeIn();
				$(".btn-left").fadeOut();
			}
			$(".h-nav-a .ul").animate({marginLeft:-1000*index}, 500);
		});
		
	}

	$(".af-con-teb span").hover(function(){
		var index = $(this).index();
		$(this).addClass("active").siblings().removeClass('active');
		$(".af-con-teb .tab-con .show-tab-con").eq(index).show().siblings().hide();
	})

	$(".df-con-teb span").hover(function(){
		var index = $(this).index();
		$(this).addClass("active").siblings().removeClass('active');
		$(".df-con-teb .tab-con .show-tab-con").eq(index).show().siblings().hide();
	})

	$(".ff-dd-list dl .dd").hover(function(){
		var index = $(this).index();
		$(this).css({borderColor:"#f4752d"}).siblings().removeAttr('style');
		var src = $(this).find('img').attr('src');
		$(".left-show-tab-con img").attr('src',src);
	});


	/*===============================================*/

	$(".cf-right-con .btn-test-a a,.yf-con-right .btn-a a").click(function(){
		$(".main-slidef-con").slideDown()
	})

	/*===============================================*/

	$(".ef-con-bg .ef-con-right-tabMeun span").hover(function(){
		var src = $(this).attr("data-src");
		var index= $(this).index();
		$(this).addClass('active').siblings('span').removeClass('active');
		$(".ef-con-left-tabconImg img").attr("src",src);

		$(".ef-con-bg .tab-text .show-text-con").eq(index).show().siblings().hide()
	});


	$(".tab-con-btn .icon1").live("click",function(){
		$(this).addClass('icon2');
		$(this).removeClass('icon1');
		$(".tab-con-list-find").hide();
		$(".tab-con-map-find").show();
		$(this).text("地图查询");
	})
	$(".tab-con-btn .icon2").live("click",function(){
		$(this).addClass('icon1');
		$(this).removeClass('icon2');
		$(".tab-con-map-find").hide();
		$(".tab-con-list-find").show();
		$(this).text("列表查询");
	});




	// 表格颜色 
	$(".fxmap-table .con-table tr").find("td:first").css({backgroundColor:"#ededed",textAlign:"center"})
	$(".hover-pop1").hover(function(){
		$(".pop-tip-con,.pop-tip-con .con-1").fadeIn();
	});
	$(".pop-tip-con .con-1").mouseleave(function(event) {
		$(".pop-tip-con,.pop-tip-con .con-1").fadeOut(200);
	});
	$(".hover-pop2").hover(function(){
		$(".pop-tip-con,.pop-tip-con .con-2").fadeIn(200);
	});
	$(".pop-tip-con .con-2").mouseleave(function(event) {
		$(".pop-tip-con,.pop-tip-con .con-2").fadeOut(200);
	});
	$(".hover-pop3").hover(function(){
		$(".pop-tip-con,.pop-tip-con .con-3").fadeIn(200);
	});
	$(".pop-tip-con .con-3").mouseleave(function(event) {
		$(".pop-tip-con,.pop-tip-con .con-3").fadeOut(200);
	});
});

$(function(){
	function tabClass(showId){
		var index=0;
		var len = $(showId+" "+".tabMeun span").length;
		var w = $(showId+" "+".img-li").width();
		$(showId+" "+".showCon .ul").width(w*len)
		$(showId+" "+".tabMeun span").hover(function(){
			index = $(this).index()
			showCon();
		})

		$(showId+" "+" "+".btn-pageNext").click(function(){
			index ++;
			if(index>len-1){
				index=0;
			}
			showCon()
		})

		$(showId+" "+".btn-pagePver").click(function(){
			index --;
			if(index<0){
				index=len-1;
			}
			showCon()
		})
		function showCon(){
			$(showId+" "+".showCon .ul").stop().animate({marginLeft:-index*w});
			$(showId+" "+".tabMeun span").eq(index).addClass("active").siblings().removeClass("active")
		}
	}
	tabClass("#tabshow0");
	tabClass("#tabshow1");


	//弹出窗口 
	$(".crae-data .btn-pop").click(function(){
		var index = $(".crae-data .btn-pop").index(this);
		$(".crae-data .date-popBox").eq(index).fadeIn(300);
		$(".crae-data dd").eq(index).addClass('active');
	});
	$(".crae-data .pop-offBtn").click(function(event) {
		$(this).parent().parent().find('.date-popBox').fadeOut(300);
		$(this).parent().parent().removeClass('active')
	});

	/* $(".ef-a-dllist .conText a").click(function(){
		$(".fixed-bg,.delong-box").fadeIn();
	}) */
})
/*20160229增加  by tianjia*/
$(function(){
	var xd_dd_all_w=$(".xd-add-main-dl dd").width()*5
	var xd_f_dd_w=$(".xd-add-main-dl").css("width",xd_dd_all_w)
	$(".xd-add-main-dl dd").css("width",$(window).width())
	var xdindex=0
	var xdlen = $(".xd-add-main-dl dd").length;
	$(".xd-add-slide-con .xd-add-right").click(function(){
		
			xdindex ++;
			if(xdindex>xdlen-1){
				xdindex=0;
			}
			xdshowCon()
		
	})

	$(".xd-add-slide-con .xd-add-left").click(function(){
		
			xdindex --;
			if(xdindex<0){
				xdindex=xdlen-1;
			}
			xdshowCon()
		
	})
	function xdshowCon(){
			$("dl.xd-add-main-dl").stop().animate({marginLeft:-xdindex*$(window).width()});
		}
})