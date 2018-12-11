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
		$(window).load(function(){

			//点击送花
			$('.live-main .right-main .right-txt-box .txt-top .flower-box').click(
				function(event) {
					$('.live-main .right-main .cast-box .cast-p').eq(-1).after("<div class='cast-p'><div class='gift-msg'><span class='user-name'>" + "名字" + "</span><span>送给了" + $('.live-main .left-video .foot-nav .this-left .left-teacher-name').text() + "老师一朵鲜花</span><div class='gifi-img'></div>")
					//获取左侧的老师姓名
				rTh()
			});

			//点击发言
			 

			function uBtn(ubtn){
				if(ubtn == '' || ubtn ==ubtn){
					var  ubtn = $('.live-main .right-main .right-txt-box .up-box .up-btn');
				}
				function oAdd(){
					var oTarea = $('.live-main .right-main .right-txt-box textarea') ;
					if(oTarea.val() == '' || oTarea.val().match(/^\s+$/g)){
						alert('不输入任何东西是没法发的~');
					}
					else{
						$('.live-main .right-main .cast-box .cast-p').eq(-1).after("<div class='cast-p'><span class='this-name'>" + "名字" + "：</span><span class='this-p'>" + oTarea.val() + "</span></div>");
					}
					oTarea.val('');
					oTarea.blur()
				}
				$('.live-main .right-main .right-txt-box').keydown(
					function(event){
						if(event.keyCode==13){ 
							ubtn.click()//按下enter键出现bug，会出现一个多余换行
						} 
				});
				ubtn.click(
					function(event) {
						oAdd();
						rTh();
				});
			}

			uBtn()

			//增加后的固定最后一条以及滚动条在底部
			function rTh(){
				var oHm = $('#mCSB_1_container').height();
				var oLastH=$('.live-main .right-main .cast-box .cast-p').eq(-1).height();
				var thisHeight = $(document).height()-60-230-200;
				if(oHm < thisHeight){
					oHmOs = 0
				}
				else if(oHm >= thisHeight){
					oHmOs = thisHeight - oHm;
				}
				// console.log(oHmOs)
				$('#mCSB_1_container').stop().animate({top:oHmOs})
				if(oHmOs<0){
					$('#mCSB_1_dragger_vertical').stop().animate({top:405})
				}
			}

			//经过显示隐藏滚动条
			$('.mCSB_draggerContainer').hide()
			$('.mCSB_draggerContainer').css('right', '-10px');
			$('.live-main .right-main .cast-box').hover(function() {
				$('.mCSB_draggerContainer').fadeIn()
			}, function() {
				$('.mCSB_draggerContainer').fadeOut()
			});
			
		})
	})