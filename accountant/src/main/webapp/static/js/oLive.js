/*--------------------------------------------------------------------
----------------------------------------------------------------------
 @Author's xiaotianjia
 @---Date 2016-10-10
 @---When you need to customize the new public style, 
     please write the name of the author, 
     and the use of the annotation style
----------------------------------------------------------------------
--------------------------------------------------------------------*/
$(function(){
	//配合jquery.mCustomScrollbar.concat.min.js及jquery.mCustomScrollbar.css进行自定义滚动条操作
	(function($){
		$(window).load(function(){
			$("#content-1").mCustomScrollbar({
				// theme:"minimal"
				setTop: '100%',//让其进入时看到最底部发言
				autoDraggerLength: false,//固定右边滚动条
				callbacks:{onTotalScroll:function(){
					// 触底执行
				}}
			});
		});
	})(jQuery);
	
	//经过显示二维码
	$('.live-head .right-box a.right-box-p.right-box-p2').hover(function() {
		$(this).find('.hide-img').slideDown();
	}, function() {
		$(this).find('.hide-img').slideUp();
	});
	
	//点击宽屏
	$('.live-main .left-video .video-small').css('display', 'none');
	$('.live-main .left-video .foot-nav .this-right .right-p.right-p3 i').toggle(function() {
		$('.live-main').animate({'padding-right': '0'},300)
	}, function() {
		$('.live-main').animate({'padding-right': '300px'},300)
	});
	

	//音量控制
	//赋值
	var oNoDie=$('.live-main .left-video .right-p.this-no-die.right-p2');
	var oSound=$('.live-main .left-video .sound-bg');
	var oSpace=$('.live-main .left-video .space');
	var oAll  =$('.live-main .left-video .all');

	oNoDie.find('i').click(
		function(event) {
			$(this).parent().toggleClass('this-no-die')
			oSpace.css('top',70);
			oAll.css('height',0);
		rPer(70)
	});

	//鼠标点击触发事件
	oSound.click(
		function(event) {
			var oSoundSct 	=  oSound.offset().top;
			var oCtop		=  event.pageY;
			var oCut  		=  oCtop - oSoundSct;
			oNoDie.addClass('this-no-die')
			if( oCut < 0 ){
				oSpace.css('top',0);
				oAll.css('height', 80);
			}
			else if(oCut > 70 ){
				oSpace.css('top',70);
				oAll.css('height', 0);
			}
			else{
				oSpace.css('top',oCut);
				oAll.css('height',80-oCut);
			}
			rPer(oCut)
	});

	//鼠标按住触发事件
	oSpace.mousedown(
		function(e) {
	  		// var oSpaceSct 	=  oSpace.offset().top;
			// var oSpacePy	=  e.pageY;
			// var oY			=  oSpacePy - oSpaceSct;
			var oSoundSct 	=  oSound.offset().top;
			oNoDie.addClass('this-no-die')
		$('.live-main').mousemove(
			
			function(ed) {
				var _y = ed.pageY - oSoundSct;
		  		// console.log(oSpaceSct,oSpacePy,oY,_y)
				if( _y > 70 ){
					oSpace.css('top', 70);
				}
				else if(_y < 0 ){
					oSpace.css('top', 0);
				}
				else{
					oSpace.stop().animate({
						top: _y + "px"
					}, 10);
					oAll.stop().animate({
						height: 80 - _y 
					}, 8);
				}
			 rPer(_y)
		});
	});	

	//鼠标松开移除事件
	$('.live-main').mouseup(function() {
		$(this).unbind("mousemove");
	})

	//需要返回的值
	function rPer(e){
		var retPes = Math.round(100 - e/70*100)
		if(retPes < 0 || retPes > 100){
			return false
		}
		console.log(retPes + '%') 
	}
})