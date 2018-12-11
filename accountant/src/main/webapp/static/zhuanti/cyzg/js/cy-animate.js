$(function(){
		$(".cy-new-banner-box .animate-box >div,.cy-banner-btn").hide();
		$('body,html').animate({
			scrollTop: $('.head-bg').height()+'px'
		},500,function(){		
		}); 
		$(".cy-banner-h1").css('top', '0').fadeIn().animate({
					'top': 110 + 'px'},
					'fast', function() {
					$(".cy-banner-h2").css('width','0').fadeIn().animate({
						'width': 100+'%'
						},
						'fast', function() {
						$(".cy-line-bg").fadeIn(1000)
						$(".cy-mo").css({
							'opacity': '0',
							'display': 'block'
						});
						$(".cy-mo-1").css('top', '430px').animate({
							'opacity':1,
							'top': 360 +'px'},
							'slow', function() {
							$(".cy-mo-2").css('top', '430px').animate({
								'opacity':1,
								'top': 360 +'px'},
								'slow', function() {
								$(".cy-mo-3").css('top', '430px').animate({
									'opacity':1,
									'top': 360 +'px'},
									'slow', function() {
									$(".cy-banner-h3").css({
										'opacity': '0',
										'display': 'block'
									}).animate({
										'opacity': '1'},
										'', function() {
										$(".cy-banner-btn").slideDown()
									});
								});;
							});;
						});;
					});
			});	
		
	})