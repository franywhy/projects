$(function(){
				$(".box_bg").outerWidth($(window).outerWidth()).outerHeight($(window).outerHeight())
				/**/
				$(".window_sc .window_sc_box").hide()
				$(".window_sc .box_bg").hide()
				$(".window_sc .window_sc_box  .close_sc,.window_sc .box_bg").click(function(){
							$(".window_sc .window_sc_box,.window_sc  .box_bg").fadeOut()
							})
				$(".cy-main2-ul a.sc-btn").click(function(){
					// 	var check = checkPost();
					// if(check){

					// 	return;

					// }
					
					var center_box_w1 = (($(window).width() - $(".window_sc .sc-modular").outerWidth()) / 2+ "px");
					var center_box_h1 =(($(window).height() - $(".window_sc .sc-modular").outerHeight())/6+ "px")
					$(".window_sc .window_sc_box ").css("left",center_box_w1);
					$(".window_sc .window_sc_box ").css("top",center_box_h1);
					$(".window_sc").fadeIn()
					$(".window_sc .window_sc_box").fadeIn();
					$(".window_sc .box_bg").fadeIn()
				})

				/**/
				$(".window_sc2 .window_sc_box").hide()
				$(".window_sc2 .box_bg").hide()
				$(".window_sc2 .window_sc_box  .close_sc,.window_sc2 .box_bg").click(function(){
							$(".window_sc2 .window_sc_box,.window_sc2  .box_bg").fadeOut()
							})
				var center_box_w2 = (($(window).width() - $(".window_sc2 .sc-modular").outerWidth()) / 2+ "px");
				var center_box_h2 =(($(window).height() - $(".window_sc2 .sc-modular").outerHeight() ) / 6+ "px")
				$(".mainCon-cy-new dl.test-1-dl dd").click(function(){
					// var check = checkPost();
					// if(check){

					// 	return;

					// }
					
					$(".window_sc2 .window_sc_box ").css("left",center_box_w2);
					$(".window_sc2 .window_sc_box ").css("top",center_box_h2);
					$(".window_sc2").fadeIn()
					$(".window_sc2 .window_sc_box").fadeIn();
					$(".window_sc2 .box_bg").fadeIn()
				})

				/**/
				$(".window_sc3 .window_sc_box").hide()
				$(".window_sc3 .box_bg").hide()
				$(".window_sc3 .window_sc_box  .close_sc,.window_sc3 .box_bg").click(function(){
							$(".window_sc3 .window_sc_box,.window_sc3  .box_bg").fadeOut()
					})
				var center_box_w3 = (($(window).width() - $(".window_sc3 .sc-modular").outerWidth()) / 2+ "px");
				var center_box_h3 =(($(window).height() - $(".window_sc3 .sc-modular").outerHeight() ) / 6+ "px")
				$(".cy-main4 .cy-slide-box .cy-slide-h3.cy-btn3").click(function(){
			
					
					$(".window_sc3 .window_sc_box ").css("left",center_box_w3);
					$(".window_sc3 .window_sc_box ").css("top",center_box_h3);
					$(".window_sc3").fadeIn()
					$(".window_sc3 .window_sc_box").fadeIn();
					$(".window_sc3 .box_bg").fadeIn()
				
				})

				/**/
				$(".window_sc4 .window_sc_box").hide()
				$(".window_sc4 .box_bg").hide()
				$(".window_sc4 .window_sc_box  .close_sc,.window_sc4 .box_bg").click(function(){
							$(".window_sc4 .window_sc_box,.window_sc4  .box_bg").fadeOut()
					})
				var center_box_w4= (($(window).width() - $(".window_sc4 .sc-modular").outerWidth()) / 2+ "px");
				var center_box_h4 =(($(window).height() - $(".window_sc4 .sc-modular").outerHeight() ) / 6+ "px")
				$(".cy-main4 .do-btn").click(function(){
					// var check = checkPost();
					// if(check){

					// 	return;

					// }
					$(".window_sc4 .window_sc_box ").css("left",center_box_w4);
					$(".window_sc4 .window_sc_box ").css("top",center_box_h4);
					$(".window_sc4").fadeIn()
					$(".window_sc4 .window_sc_box").fadeIn();
					$(".window_sc4 .box_bg").fadeIn()
				
				})





				function checkPost(){

					console.log(linkIndex)
					// var check = getJsCookie("haspost");
					if(check == "true"){

						if(linkIndex){


							window.location.href = linkIndex;

						}
						return true;
					}
					else
						return false;

				}




		})