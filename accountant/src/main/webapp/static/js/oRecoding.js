/*--------------------------------------------------------------------
----------------------------------------------------------------------
 @Author's xiaotianjia
 @---Date 2016-10-10
 @---When you need to customize the new public style, 
  please write the name of the author, 
  and the use of the annotation style
----------------------------------------------------------------------
--------------------------------------------------------------------*/

//配合jquery.mCustomScrollbar.concat.min.js及jquery.mCustomScrollbar.css进行自定义滚动条操作
	(function($){
		$(window).load(function(){
			$("#content-1").mCustomScrollbar({
				// theme:"minimal"
				// setTop:$('.nav-box a.nav-p.active').offset().top - 70 + 'px',//让其进入时看到最底部发言
				// autoDraggerLength: false,//固定右边滚动条
				callbacks:{onTotalScroll:function(){
					// 触底执行
				}}
			});
		});
	})(jQuery);

	$(function () {
	    $(window).load(function () {
	        //经过显示二维码
	        $('.recoding-head .right-box a.right-box-p.right-box-p2').hover(function () {
	            $(this).find('.hide-img').slideDown();
	        }, function () {
	            $(this).find('.hide-img').slideUp();
	        });

	        //隐藏滚动条
	        $('.mCSB_draggerContainer').hide()

	        //下拉列表
	        $('.nav-box').css('display', 'none');
	        $('.recoding-head .left-logo').toggle(function () {
	            $('.nav-box').slideDown(300, function () {
	                $('.recoding-main').animate({ 'padding-left': '350px' }, 100);
	                //固定选中课程位置
	                var _top = $('.nav-box a.nav-p.active').length > 0 ? $('.nav-box a.nav-p.active').offset().top : $('.nav-box a.nav-h1:last').offset().top;
	                if (_top < ($(window).height() - 50)) {
	                    // console.log('不做定位')
	                }
	                else {
	                    $("#mCSB_1_container").animate({ 'top': '-' + (_top - 70) }, 200);
	                }
	            })

	        }, function () {
	            $('.nav-box').slideUp(function () {
	                $('.recoding-main').animate({ 'padding-left': '0px' });
	            });

	        });

	        $('.recoding-main .recoding-right .right-con').toggle(function () {
	            $('.recoding-main  .recoding-right').animate({ 'right': '0' }, 300, function () {
	                $('.recoding-main .recoding-right .right-con').addClass('slidedown')
	                $('.recoding-main').animate({ 'padding-right': '300px' }, 100);
	            })
	        }, function () {
	            $('.recoding-main  .recoding-right').animate({ 'right': '-300px' }, 300, function () {
	                $('.recoding-main .recoding-right .right-con').removeClass('slidedown')
	                $('.recoding-main').animate({ 'padding-right': '0' }, 100);
	            })
	        });


	    })

	})