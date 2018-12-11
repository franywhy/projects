$(function(){
	$('.tip-pop').animate({'display':'none'},5000,function(){
		$('.tip-pop').fadeIn()
	})
	$('.tip-close').click(
		function(event) {
			$('.tip-pop').fadeOut(function(){
				$('.tip-pop').animate({'display':'none'},1000*90,function(){
					$('.tip-pop').fadeIn()
				})
			})
	});
})	