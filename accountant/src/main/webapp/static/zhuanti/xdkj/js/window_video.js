$(function(){
				$(".box_bg").width($(window).width()).height($(window).height())
				$(".video_box").hide()
				$(".window_video .box_bg").hide()
				$(".video_box  .close_video,.window_video .box_bg").click(function(){
							$(".video_box").fadeOut(100)
							$(".window_video .box_bg").fadeOut(500)
							$(".video_box").children('video').attr("src",'null');
							})
				var center_box = (($(window).width() - $(".video_box").width() ) / 2+ "px");
				var middle_box =(($(window).height() - $(".video_box").height() ) / 2+ "px")
				$(".video_box ").css("left",center_box);
				$(".video_box ").css("top",middle_box);
				$(".video_btn").click(function(){
					var vsrc = $(this).attr('vsrc');
					var vtitle=$(this).attr('title')	
					if(vsrc !=''){
						$(".video_box").children('video').attr("src",vsrc);
					}
					if(vtitle !=''){
						$(".video_box .close_video h1").html(vtitle)
					}
					$(".video_box").fadeIn();
					$(".window_video .box_bg").fadeIn()
				})
			})	
	/*通过标题，vsrc 来决定显示的内容*/
	