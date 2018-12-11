/*


 */
$(function(){

	// box move
	var offselectText = document.getElementById("login");
	/*	if(document.all){
		    offselectText.onselectstart= function(){return false;}; //for ie
		}else{
		    offselectText.onmousedown= function(){return false;};
		    offselectText.onmouseup= function(){return true;};
		}
		document.onselectstart = new Function('event.returnValue=false;');
	*/


	var canMove = false;
	var boxW = $("#login .top").outerWidth(true);
	var boxH = $("#login .top").outerHeight(true);
	var bigW = $("#login").outerWidth(true);
	var bigH = $("#login").outerHeight(true);
	var tw = boxW/2;
	var th = boxH/2;
	var offset;
	var ox,oy;
	$(".top-title").mousedown(function(event){
		offset = $(this).offset();
		ox = event.pageX-offset.left;
		oy = event.pageY-offset.top;
		canMove=true;
		$(this).css({"cursor":'move'})
		move();
	})

	$(document).mouseup(function(){
		$(".top-title").css({"cursor":'default'})
		canMove = false;
		move()
		//return;
	})
	function move(){
		var w = $(window).width();
		var h = $(window).height();
		$(window).mousemove(function(e){
			if(canMove != false){
				var x = e.clientX-ox+250;
				var y = e.clientY-oy+200;
				//console.log(e.clientX,e.clientY,x,y)
				$("#login").css({left:x,top:y})
				if(x+boxW>=w-10){
					$("#login").css({left:w-boxW})
					// console.log(0)
				}
				if(x<=0){
					$("#login").css({left:0})
					// console.log(1)
				}
				if(y+bigH>=h){
					$("#login").css({top:h-bigH});
				}
				if(y<=0){
					$("#login").css({top:0})
					// console.log(3)
				}
			}else{
				return false;
			}
		})
	}

	// 切换 
	function regTab(){
		var ulWidth = $(".loginBox").width();
		$(".loginBox .ul").width(ulWidth*2)
		$(".go-register").click(function(){
			$(".top-left .loginText").animate({marginLeft:-100,opacity:0});
			$(".top-left .registerText").animate({left:'44%',opacity:1});
			$(".loginBox .ul").animate({marginLeft:-ulWidth},500);
			$(".back-btn").fadeIn();
			$(".userRegister .select").show();
			$(".userRegister .next-btn").css({marginTop:0})
		});
		$(".back-login,.back-btn").click(function(){
			$(".top-left .loginText").animate({marginLeft:0,opacity:1});
			$(".top-left .registerText").animate({left:'56%',opacity:0});
			$(".re-ul").animate({marginLeft:0},100);
			$(".loginBox .ul").animate({marginLeft:0},500);
			$(".back-btn").fadeOut()
		})
	}
 	regTab()


 	function showPop(clickShowPopBtn,showConClass,clickoffPopBtn){
		$.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
		// 高丽撕模糊 
		$(clickShowPopBtn).live('click',function(){
			var i=0.5;
			if($.browser.msie){
				$(".login-bg").animate({backgroundColor:"#000000",opacity:0.7})
			/*	$("body").css({'overflow-x':'hidden'})
				var timer = setInterval(function(){
					i+=1;
					if(i>=15){clearInterval(timer)}
					$(".body").css({
						'filter': 'progid:DXImageTransform.Microsoft.Blur(PixelRadius='+i+', MakeShadow=false)' 
					});
				},17);*/
			}else{			
				var timer = setInterval(function(){
					i+=1;
					if(i>=10){clearInterval(timer)}
					$(".body").css({
						'-webkit-filter': 'blur('+i+'px)',
						'-moz-filter': 'blur('+i+'px)',
						'-ms-filter': 'blur('+i+'px)',
						'filter': 'blur('+i+'px)',
						'filter': 'blur('+i+'px)' 
					});
				},17);
			}
			$(".login-bg").show();
			$(showConClass).fadeIn(0);
			$(showConClass).animate({marginTop:-200,opacity:0.98},500)
		});

		$(clickoffPopBtn).click(function(){
			var i=10;
			if($.browser.msie){		
		/*		var timer = setInterval(function(){
					i-=1;
					if(i<=0){clearInterval(timer);i=0;}
					$(".body").css({
						'filter': 'progid:DXImageTransform.Microsoft.Blur(PixelRadius='+i+', MakeShadow=false)' 
					});
				},17);*/

				$(".login-bg").animate({backgroundColor:"#000000",opacity:0},function(){
					$(this).hide()
				})

			}else{			
				var timer = setInterval(function(){
					i-=1
					if(i<=0){clearInterval(timer)}
					$(".body").css({
						'-webkit-filter': 'blur('+i+'px)',
						'-moz-filter': 'blur('+i+'px)',
						'-ms-filter': 'blur('+i+'px)',
						'filter': 'blur('+i+'px)',
						'filter': 'blur('+i+'px)' 
					});
				},17);

			}
			$(showConClass).animate({marginTop:-130,opacity:0},500)
			$(showConClass).fadeOut(400);
			$(".login-bg").fadeOut(400);
		});

	}
	showPop(".get-VIP a",".buy-VIP-popBox",".off-pop-btn");
	showPop(".btn-login",".loginBox",".btn-right");




	
	var userOK  = false;
	var pwdOK   = false;
	var codeOK ;
	var phoneOK = false;
	var rPwdOK  = false;
	var rUPwdOK = false;
	var phone = $(".userLogin .userName input");
	var pword = $(".userLogin .pwd input");
	var phoneR = $(".userRegister .userName input");
	var pCode = $(".userRegister .userCode input");
	var upwd = $(".userRegister .upwd input");
	var rpwd = $(".userRegister .rpwd input");


	/*登录验证*/
	function phoneCode(){
		var sMobile = $(phone).val();
		if($(phone).val().length<11 || !(/^1[3|4|5|8][0-9]\d{4,8}$/.test(sMobile))){
			$(phone).css({borderColor:"#ff0000"});
			$(phone).siblings(".icon-pd").show().addClass("active");
			userOK = false;
		}else{
			$(phone).css({borderColor:"#d8d8d8"});
			$(phone).siblings(".icon-pd").removeClass("active")
			userOK = true;
		}
		showOK();
	}

	function pwdCode(){
		var sPwd = $(pword).val();
		var zzpd = /^[a-z0-9A-Z_]{6,16}$/;
		if(zzpd.test(sPwd)){
			$(pword).css({borderColor:"#d8d8d8"});
			$(pword).siblings(".icon-pd").removeClass("active")
			pwdOK = true;
		}else{
			$(pword).css({borderColor:"#ff0000"});
			$(pword).siblings(".icon-pd").show().addClass("active");
			pwdOK = false;
		}
		showOK();
	}


	// 注册验证
	function phoneCodeR(){
		var sMobile = $(phoneR).val();
		if($(phoneR).val().length<11 || !(/^1[3|4|5|8][0-9]\d{4,8}$/.test(sMobile))){
			$(phoneR).css({borderColor:"#ff0000"});
			$(phoneR).siblings(".icon-pd").show().addClass("active");
			phoneOK = false;
		}else{
			$(phoneR).css({borderColor:"#d8d8d8"});
			$(phoneR).siblings(".icon-pd").removeClass("active")
			phoneOK = true;
		}
		regisOK();

	}


	var codeLodin = false;
	var lodinCode = true;
	// 
	function coCode(){
		var sCode = $(pCode).val();
		var zzNun = /^[0-9]*$/;
		if(zzNun.test(sCode) && sCode.length==6){
			// 验证码进行ajax验证  ，成功则为 true ，失败则 return；
			
			$(".userRegister .userCode").css({borderColor:"#d8d8d8"});


			// function canRun(){
			// 	if(codeLodin){
					codeOK = true;
			// 	}else{
			// 		codeOK = false;
			// 	}

			// 	regisOK();
			// }
			// console.log(codeOK)

		}else{
			$(".userRegister .userCode").css({borderColor:"#ff0000"});
			codeOK = false;
		}
		regisOK();

	}
	var r_sPwd,u_sPwd;
	// 密码验证
	function pwdRcode(){
		u_sPwd = $(upwd).val();
		var u_zzpd = /^[a-z0-9A-Z_]{6,16}$/;
		if(u_zzpd.test(u_sPwd)){
			$(upwd).css({borderColor:"#d8d8d8"});
			$(upwd).siblings(".icon-pd").removeClass("active")
			rPwdOK = true;
		}else{
			$(upwd).css({borderColor:"#ff0000"});
			$(upwd).siblings(".icon-pd").show().addClass("active");
			rPwdOK = false;
		}
		pwdShow();
	}	
	function pwdURcode(){
		pwdRcode();

		r_sPwd = $(rpwd).val();
		var r_zzpd = /^[a-z0-9A-Z_]{6,16}$/;
		if(r_zzpd.test(r_sPwd) && r_sPwd===u_sPwd){
			$(rpwd).css({borderColor:"#d8d8d8"});
			$(rpwd).siblings(".icon-pd").removeClass("active")
			rUPwdOK = true;
		}else{
			$(rpwd).css({borderColor:"#ff0000"});
			$(rpwd).siblings(".icon-pd").show().addClass("active");
			rUPwdOK = false;
		}
		pwdShow();
	}

	// ====================================================================

	// 用户名登陆验证
	$(".userLogin .userName input").focus(function(){
		$(this).keyup(function(event){
			phoneCode();
		})
	});


	$(".pwd input").focus(function(){
		$(this).keyup(function(event){
			phoneCode();
			pwdCode();
		})
	});

	// 手机验证
	$(".userRegister .userName input").focus(function(){
		$(this).keyup(function(event){
			phoneCodeR();
		})
	});

	$(".userCode input").focus(function(){
		$(this).keyup(function(event){
			phoneCodeR();
			coCode();
		})
	})

	// 密码验证
	$(".userRegister .upwd input").focus(function(){
		$(this).keyup(function(event){
			pwdRcode();
		})
	});

	$(".userRegister .rpwd input").focus(function(){
		$(this).keyup(function(event){
			pwdURcode();
		})
	})



	function pwdShow(){
		if(rPwdOK && rUPwdOK){
			$(".next-btn").addClass("active");
			$(".baseInput input").blur()
			/*setTimeout(function(){
				alert('注册成功')
			},2000)*/
		}else{
			$(".next-btn").removeClass("active");
		}
	}


	// ====================================================================
	function showOK(){
		if(userOK && pwdOK){
			$(".login-btn").addClass("active");
		}else{
			$(".login-btn").removeClass("active");
		}
		
	}	

	// ====================================================================
	function regisOK(){
		setTimeout(function(){
			if(phoneOK && codeOK){
					$(".next-btn").addClass("active");
					setTimeout(function(){
						$(".next-btn").text("提交中...")
					},3500);

					setTimeout(function(){
						$(".re-ul").animate({marginLeft:-500},function(){
							$(".next-btn").removeClass('active').text("立即注册");
							$(".userRegister .select").hide();
							$(".next-btn").animate({marginTop:28},800);
						})
					},5500);
			

			}else{
				$(".next-btn").removeClass("active");
			}
			if(codeOK){
				$(".getCode").stop().animate({width:30},function(){
					$(".getCode").addClass("loginBg");

					// 验证成功 
					
					setTimeout(function(){ //模拟网速缓冲
						$(".getCode").removeClass("loginBg").addClass("addBg");
						codeLodin =true;
					},2000);


				});
			}else{
				$(".getCode").removeClass("addBg").stop().animate({width:115})

			}	
		},200)
	}






/*	function blurInput(){
		console.log(canClick)
	}	if(phone.val().length>6 && pword.val().length>6){
			canClick = true;
		}else{
			canClick = false;
		}
		blurInput();
	*/

	$(".login-btn").click(function(){
		if($(this).has(".active")){


			/*进行ajax判断*/

		}else{
			return;
		}


		





	})




	// 获取验证码计时器 
	var canIn = true;
	var canGetCode = true;
	var k = 30;
	var timera;

	$(".getCode").click(function(){
		regisOK();
		if(!phoneOK){
			$(".userRegister .userName").addClass("animated"+" shake");
			$(phoneR).css({borderColor:"#ff0000"});
			$(phoneR).siblings(".icon-pd").show().addClass("active");
			setTimeout(function(){
				$(".userRegister .userName").removeClass("animated"+" shake");
			},1000);
			return;
		}
		$(".getCode").addClass("active");
		if(canGetCode){
			timera = setInterval(function(){
				k--;
				$(".getCode").text(k+"秒后重新获取");
				if(k<0){
					clearTimeout(timera);
					$(".getCode").removeClass("active");
					$(".getCode").text("获取验证码");
					canGetCode = true;
					k=30;
					codeOK = false;
				}
			},1000);
		}
		canGetCode = false;

	});
	

})