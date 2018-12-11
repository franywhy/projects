/*
	author by tianjia
	time：20170420
*/
(function ($) { 
    $.fn.setSlide = function(option){ 
        var set = { 
            oSlide: $('.slide'), 
            slide_box_li: $('.slide-box .slide-li'), 
            slide_page_btn: $('.slide-page .slide-page-btn'), 
            slide_p: $('.slide-p'), 
            slide_n: $('.slide-n'),
            slide_time: 5000
        } 

        return this.each(
        	function(){
				window.oSlideIndex 	=	0;
				var oSlide 			=	set.oSlide;
				var slide_box_li 	= 	set.slide_box_li;
				var slide_page_btn 	= 	set.slide_page_btn;
				var slide_p 		= 	set.slide_p;
				var slide_n 		= 	set.slide_n;
				var slide_time 		= 	set.slide_time;
				var len 			=	slide_box_li.length;

				slide_page_btn.hover(
					function(){
						window.oSlideIndex = $(this).index();
						slide(oSlideIndex)
					}
				)

				slide_n.click(function(){
						window.oSlideIndex ++;
						if(oSlideIndex>len-1){
							oSlideIndex=0;
						}
						slide(oSlideIndex)
				})

				slide_p.click(function(){
						window.oSlideIndex --;
						if(oSlideIndex<0){
							oSlideIndex=len-1;
						}
						slide(oSlideIndex)
				})


				if(autoPlaySet){
					clearInterval(autoPlaySet);
					autoPlaySet =null;
				}

				var autoPlaySet = setInterval(autoPlay,slide_time);

				
				function autoPlay(){
					window.oSlideIndex ++;
					if(oSlideIndex>len-1){
						oSlideIndex=0;
					}
					slide(oSlideIndex)
				}

				oSlide.hover(
					function(){
						clearInterval(autoPlaySet)
					},
					function(){
						autoPlaySet = setInterval(autoPlay,slide_time)
					}
				)
				
				function slide(oSlideIndex){
					slide_box_li.eq(oSlideIndex).stop().animate({opacity:1}).siblings().stop().animate({opacity:0});
					slide_page_btn.eq(oSlideIndex).stop().animate({opacity:1}).siblings().stop().animate({opacity:0.2});
					console.log(oSlideIndex)
				}
			}
        )
        
    }; 
})(jQuery);
