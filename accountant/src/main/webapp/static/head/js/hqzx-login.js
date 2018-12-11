
/*===测试环境会计官网地址===*/
// var HOSTNAME = "http://kjpcweb.ljtest.hqjy.com";
/*===测试环境学习中心地址(会计)===*/
// var learningCenterUrl = "http://10.0.98.15:18088/learningCenter/web/home?SSOTOKEN=";

/*===预生产环境会计官网地址===*/
var HOSTNAME = "http://www.hqjy.com";
/*===生产环境学习中心地址(会计)===*/
var learningCenterUrl = "http://kuaiji.learning.hqjy.com/learningCenter/web/home?SSOTOKEN=";

/*===验证码===*/
$(document).ready(function () {
	
	//获取页面头部的用户昵称和头像
	var _ticket = $.cookie("token");
	if(!_ticket){
		return ;
	}
	$.ajax({
        type: "GET",
        dataType: "jsonp",
        jsonp: "callback",
        url: HOSTNAME+"/user/userInfo?token="+_ticket,
        cache: false,
        success: function (result) {
        	result = $.parseJSON(result);
        	if (result != null && result != 'null' && result != 'undefined' && result != '' && (result.nickname != '' || result.mobileNo != '')) {
                var username = result.nickname == ''? result.mobileNo : result.nickname;
                var photoUrl = result.photo;
                if(photoUrl != undefined && photoUrl != 'undefined' && photoUrl != null && photoUrl != 'null' && photoUrl != ''){
                	$("#headPhoto").attr("src",photoUrl);
                }
                $("#headUserName").html(username);
                
                $("#registerYesDiv").attr("style","display: block");
                $("#registerNoDiv").attr("style","display:none");
            }
        }
    });
});

$(function () {

    /*
    登陆按钮 $('.hqjy-sign-btn')
    注册按钮 $('.hqjy-register-btn')
    请把该项写在对应的class里面
    */

    /*===点击切换tab选项卡===*/
    $('.login-title-tab .login-tab-title').click(

	function () {
	    var index = $(this).index();
	    $(this).addClass('act').siblings().removeClass('act');
	    var oThis_tabMain = $(this).parents('.login-title-tab').siblings('.login-title-main').find('.login-tab-main');
	    oThis_tabMain.eq(index).siblings().slideUp();
	    oThis_tabMain.eq(index).slideDown();
	})

    //关闭事件
    $('.login-box .close').click(

	function () {
	    $(this).parents('.login-box').fadeOut();
	    if ($(this).parents('.login-txt').length > 0) {

	    } else {
	        $('.mask-login').fadeOut()
	    }
	})

    /*===点击按钮打开对应弹窗===*/

    function openWindow(windowbox, e) { //打开的弹窗，打开的选项卡
        windowbox.fadeIn();
        $('.mask-login').fadeIn();
        $('.login-box.tologin .login-title-tab .login-tab-title').eq(e).click();
    }

    //登录
    $('.btn-login').click(

	function () {
	    openWindow($('.login-box.tologin'), 0);

	})

    //注册
    $('.btn-reget').click(

	function () {
	    openWindow($('.login-box.tologin'), 1);

	})

    //注册里面的服务条款
    $('.login-tab-main .cast-p b').click(

	function () {
	    openWindow($('.login-box.login-txt'));

	})

    //注册里面的同意按钮点击触发关闭
    $('.login-box.login-txt .iagree-btn').click(

	function () {
	    $("#checkbox-cast").attr("checked", 'true');
	    $('.login-box.login-txt .close').click()
	})

    /*===忘记密码===*/
    $('.disrememberpw').click(

	function () {
	    $('.login-box.tologin').fadeOut();
	    $('.login-box.toreact').fadeIn()
	})
    /*===重置密码里面的返回===*/
    $('.return-icon').click(
        function () {
            $('.login-box.toreact').fadeOut();
            $('.login-box.tologin').fadeIn()
        }
    )

    
    function login(sMobile,sPwd,timeout,oThisAleat){
    	$.ajax({
	        type: "GET",
	        url: HOSTNAME+"/user/login",
	        data: {
	        	loginName: sMobile,
	        	password: sPwd,
	            timeout: timeout
	        },
	        dataType: "jsonp",
	        jsonp: "callback",
	        beforeSend: function () {
	            $('.login-box .validate').html("正在登录...");
	        },
	        success: function (result) {
	        	result = $.parseJSON(result);
	        	if (result.code == '200') {
                    $('.login-box .validate').html("登录成功");
                    if (getUrlParam("ReturnUrl") == null) {
                        window.location.reload();
                    } else {
                        window.location.href = getUrlParam("ReturnUrl");
                    }
                } else if (result.code == '402' || result.code == '403') {
                    oThisAleat.html('用户名或密码错误');
                    animateAleat(oThisAleat)
                    $('.login-box .validate').html("立即登录");
                } else {
                    oThisAleat.html(result.msg);
                    animateAleat(oThisAleat)
                    $('.login-box .validate').html("立即登录");
                }
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown) {
	        	oThisAleat.html("登录出错");
                animateAleat(oThisAleat)
                $('.login-box .validate').html("立即登录");
            }
	    });
    }
    /*===表单验证提示信息===*/
    $('.login-box .validate').click(

	function () {
	    var oThisAleat = $(this).parents('.login-box').find('.red-alert');
	    var sMobile = $("#sMobile").val();
	    var sPwd = $("#sPwd").val();
	    var timeout = 120;
	    
	    login(sMobile,sPwd,timeout,oThisAleat);
	})
    $(function () {
        document.onkeydown = function (e) {
            var ev = document.all ? window.event : e;
            if (ev.keyCode == 13) {
                var isFocus = $("#sPwd").is(":focus");
                if (isFocus == true) {
                    var oThisAleat = $(".red-alert");
                    var sMobile = $("#sMobile").val();
                    var sPwd = $("#sPwd").val();
                    var timeout = 120;
                    $.ajax({
                        type: "GET",
                        url: HOSTNAME+"/user/login",
                        data: {
                            type: "1",
                            loginName: sMobile,
                            password: sPwd,
                            timeout: timeout
                        },
                        dataType: "jsonp",
                        jsonp: "callback",
                        beforeSend: function () {
                            $('.login-box .validate').html("正在登录...");
                        },
                        success: function (result) {
                        	result = $.parseJSON(result);
            	        	if (result.code == '200') {
                                $('.login-box .validate').html("登录成功");
                                if (getUrlParam("ReturnUrl") == null) {
                                    window.location.reload();
                                } else {
                                    window.location.href = getUrlParam("ReturnUrl");
                                }
                            } else if (result.code == '402' || result.code == '403') {
                                oThisAleat.html('用户名或密码错误');
                                animateAleat(oThisAleat)
                                $('.login-box .validate').html("立即登录");
                            } else {
                                oThisAleat.html(result.msg);
                                animateAleat(oThisAleat)
                                $('.login-box .validate').html("立即登录");
                            }
                        },
            	        error: function(XMLHttpRequest, textStatus, errorThrown) {
            	        	oThisAleat.html("登录出错");
                            animateAleat(oThisAleat)
                            $('.login-box .validate').html("立即登录");
                        }
                    });
                }
            }
        }
    });
    /*===获取短信验证码===*/
    // 获取验证码计时器 
    var canIn = true;
    var canGetCode = true;
    var k = 60;
    var timera;

    $("#getCode").click(function () {
        var oThisAleat = $(this).parents('.login-box').find('.red-alert');
        var sMobile = $("#rMobile").val();
        var imgCode = $.trim($("#userCode").val());
        var telReg = !!sMobile.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/); //前端手机的验证
        if (telReg == false) {
            oThisAleat.html('手机号码格式不正确');
            animateAleat(oThisAleat);
            return;
        }else if(imgCode == ''){
        	oThisAleat.html('请输入图形验证码');
            animateAleat(oThisAleat);
            return;
        }else if(imgCode.length != 4){
        	oThisAleat.html('图形验证码不正确');
            animateAleat(oThisAleat);
            return;
        }

        if (canGetCode) {
            $.ajax({
                type: "GET",
                url: HOSTNAME+"/user/registerSendSMS",
                data: {
                    type: "0",
                    mobileNo: sMobile,
                    verifyCode: imgCode
                },
                dataType: "jsonp",
                jsonp: "callback",
                success: function (result) {
                	/*result = $.parseJSON(result);
                	oThisAleat.html(result.code);
                    animateAleat(oThisAleat);*/
                	result = $.parseJSON(result);
                	if (result.code != "200") {
                        oThisAleat.html(result.msg);
	                    animateAleat(oThisAleat);
	                } else {
	                    canGetCode = false;
	                    timera = setInterval(function () {
	                        k--;
	                        $("#getCode").text(k + "秒后重新获取");
	                        if (k < 0) {
	                            clearTimeout(timera);
	                            $("#getCode").removeClass("active");
	                            $("#getCode").text("获取验证码");
	                            canGetCode = true;
	                            k = 60;
	                        }
	                    }, 1000);
	                }
                }
            });
        }
    });
    
    /*===重置密码发送手机验证码==*/
    $("#getCode1").click(function () {
        var oThisAleat = $(this).parents('.login-box').find('.red-alert');
        var sMobile = $("#rMobile1").val();
        var imgCode = $.trim($("#userCode1").val());
        var telReg = !!sMobile.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/); //前端手机的验证
        if (telReg == false) {
            oThisAleat.html('手机号码格式不正确');
            animateAleat(oThisAleat);
            return;
        }else if(imgCode == ''){
        	oThisAleat.html('请输入图形验证码');
            animateAleat(oThisAleat);
            return;
        }else if(imgCode.length != 4){
        	oThisAleat.html('图形验证码不正确');
            animateAleat(oThisAleat);
            return;
        }
        if (canGetCode) {
        	$.ajax({
                type: "GET",
                url: HOSTNAME+"/user/resetPWSendSMS",
                data: {
                    type: "01",
                    mobileNo: sMobile,
                    verifyCode: imgCode
                },
                dataType: "jsonp",
                jsonp: "callback",
                success: function (result) {
                	result = $.parseJSON(result);
                	if (result.code != "200") {
                        oThisAleat.html(result.msg);
                        animateAleat(oThisAleat);
                    } else {
                        canGetCode = false;
                        timera = setInterval(function () {
                            k--;
                            $("#getCode1").text(k + "秒后重新获取");
                            if (k < 0) {
                                clearTimeout(timera);
                                $("#getCode1").removeClass("active");
                                $("#getCode1").text("获取验证码");
                                canGetCode = true;
                                k = 60;
                            }

                        }, 1000);
                   }
                }
            });
        }
    });
    /*===提交注册===*/
    $('.login-box .register').click(
	function () {
	    var oThisAleat = $(this).parents('.login-box').find('.red-alert');
	    var sMobile = $("#rMobile").val();
	    var sCode = $("#sCode").val();
	    var r_sPwd = $("#r_sPwd").val();
	    var u_sPwd = $("#u_sPwd").val();
	    if (r_sPwd.length > 5) {
	        if (r_sPwd === u_sPwd) {
	            if ($('#checkbox-cast').attr('checked')) {
	                $.ajax({
	                    type: "GET",
	                    url: HOSTNAME+"/user/register",
	                    data: {
	                    	mobileNo: sMobile,
	                    	password: r_sPwd,
	                    	SMSCode: sCode
	                    },
	                    dataType: "jsonp",
	                    jsonp: "callback",
	                    beforeSend: function () {
	                        $('.login-box .register').html("正在注册...");
	                    },
	                    success: function (result) {
	                    	result = $.parseJSON(result);
	                    	if (result.code == '200') {
	                            $('.login-box .register').html("注册成功");
	                            login(sMobile,r_sPwd,120,oThisAleat);//自动登录
	                            //window.location.reload();
	                        }
	                        else {
	                            $('.login-box .register').html("立即注册");
	                            oThisAleat.html(result.msg);
	                            animateAleat(oThisAleat);
	                        }
	                    }
	                });
	            }
	            else {
	                oThisAleat.html("请确认服务条款");
	                animateAleat(oThisAleat);
	            }
	        }
	        else {
	            oThisAleat.html("两次输入的密码不一致");
	            animateAleat(oThisAleat);
	        }
	    }
	    else {
	        oThisAleat.html("密码长度必须6位或以上");
	        animateAleat(oThisAleat);
	    }
	})

    /*===重置密码===*/
    $('.login-box .repass').click(
	function () {
	    var oThisAleat = $(this).parents('.login-box').find('.red-alert');
	    var sMobile = $("#rMobile1").val();
	    var sCode = $("#sCode1").val();
	    var r_sPwd = $("#r_sPwd1").val();
	    var u_sPwd = $("#u_sPwd1").val();
	    if(sMobile == ''){
	    	 oThisAleat.html("请填写手机号码");
	    	 animateAleat(oThisAleat);
	    	 return false;
	    }
	    if(sCode == ''){
	    	 oThisAleat.html("请填写短信验证码");
	    	 animateAleat(oThisAleat);
	    	 return false;
	    }
	    if (r_sPwd.length > 5) {
	        if (r_sPwd === u_sPwd) {
	            $.ajax({
	                type: "GET",
	                url: HOSTNAME+"/user/resetPassWord",
	                data: {
	                	mobileNo: sMobile,
	                	password: r_sPwd,
	                	SMSCode: sCode
	                },
	                dataType: "jsonp",
	                jsonp: "callback",
	                beforeSend: function () {
	                    $('.login-box .repass').html("重置中...");
	                },
	                success: function (result) {
	                	result = $.parseJSON(result);
	                	if (result.code == '200') {
	                    	oThisAleat.html(result.msg);
	                        animateAleat(oThisAleat);
	                    	//window.location.reload();
	                        $('.return-icon').click();
	                    }else {
	                        $('.login-box .repass').html("提交");
	                        oThisAleat.html(result.msg);
	                        animateAleat(oThisAleat);
	                    }
	                }
	            });
	        }
	        else {
	            oThisAleat.html("两次输入的密码不一致");
	            animateAleat(oThisAleat);
	        }
	    }
	    else {
	        oThisAleat.html("密码长度必须6位或以上");
	        animateAleat(oThisAleat);
	    }
	})

	
	//跳转到学习中心
	$('#learning').click(
		function gotoLearning(){
			var _ticket = $.cookie("token");
			if(!_ticket){
	    		$('.btn-login').trigger('click');
	    		return false;
	    	}
	    	window.location.href=learningCenterUrl+_ticket;
	  })
	
    //定时动画
    function animateAleat(oSelect) {
        oSelect.animate({
            'margin-top': '0'
        });
        oSelect.animate({
            'display': 'block'
        }, 2000, //类型定时器的效果
		function () {
		    oSelect.animate({
		        'margin-top': '-60px'
		    })
		})
    }
})

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}