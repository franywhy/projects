window.onload = function(){
		var isIE6 = /msie 6/i.test(navigator.userAgent);
		var isIE7 = /msie 7/i.test(navigator.userAgent);
		var isIE8 = /msie 8/i.test(navigator.userAgent);
		var isIE9 = /msie 9/i.test(navigator.userAgent);
		var isIE = /msie/i.test(navigator.userAgent);
		var body = document.getElementById('body');

		if(isIE6||isIE7||isIE8||isIE9){
			$(".fixed-body").text("不支持IE浏览器，请更换360，或者谷歌，或者火狐浏览器").css({color:'#ffffff'});
			alert("不支持IE浏览器，请更换360，或者谷歌，或者火狐浏览器");
			return;
		}


		$(".fixed-body").show();
		var browser = {
			versions:function(){
				var u = navigator.userAgent;
				var app = navigator.appVersion;
				return {
					trident: u.indexOf('Trident') > -1,//IE内核
					presto : u.indexOf('Presto') > -1,//opera 内核
					webkit : u.indexOf("AppleWebkit") >1,//苹果、谷歌内核  
					gecko  : u.indexOf('Gecko') > -1 && u.indexOf("KHTML") == -1,
					mobile : !!u.match(/AppleWebkit.'Mobile.'/),//是否为移动终端 
					ios    : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
					android: u.indexOf('Android') > -1 || u.indexOf("Linux") > -1, //android终端或者uc浏览器
					bBerry : u.indexOf("BlackBerry") >= 0,//黑莓手机内核
					iPhone : u.indexOf("iPhone") > -1,
					iPad   : u.indexOf('iPad') > -1,
					webApp : u.indexOf("Safari") == -1 //是否web应该程序，没有头部与底部  
				};
			}(),
			language:(navigator.browserLanguage || navigator.language).toLowerCase()
		}
		if(browser.versions.mobile || browser.versions.ios || browser.versions.android || browser.versions.iPhone || browser.versions.iPad || browser.versions.bBerry){
			body.style.position='fixed';
			$(".page1 .radius-logo").css({marginTop:'30%'});
			$(".page2 .user-tx").css({marginTop:'10%'});
			$(".page3 .user-tx").css({marginTop:'10%'});
			$(".page4 .user-tx").css({marginTop:'10%'});
		}else{
			body.style.width = "320px";
			body.style.height = "526px";
			body.style.margin = "0 auto";
			body.style.borderTop = "50px solid #cccccc";
			body.style.borderBottom = "30px solid #cccccc";
			body.style.borderLeft = "2px solid #cccccc";
			body.style.borderRight = "2px solid #cccccc";
			body.style.borderRadius ="30px";
			$(".content").css({height:'568px'});
		}

		var change = function(){
			var bw = $(".icon-big ").width()
			var bh = $(".icon-big ").width()
			$("#container").css({'width':bw-6,'height':bh-6});
		}
		change();
		$(window).resize(change);
	}