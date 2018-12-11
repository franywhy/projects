/*
* Izb.login
*/
$.module("Izb.login",function(){
	var _actionSession = "",
		_actionLogin = "",
		_redirectUrl = "",
		$uid = $('#uid'),
		$pwd = $('#pwd'),
		$tips = $('#tips'),
		$auth_code = $('#auth_code'),
		$authcodeImg = $('#authcodeImg'),
		$uid = $('#uid');
	
	//刷新验证码
	function _refreshCode() {
	    $auth_code.val('');		
	    $authcodeImg.attr('src', Izb.core.resolveAction({
	    	path: '/authcode.json?v=' + new Date().getTime(),
	    	domain: Izb.config.domain.data
	    }));
	}
	
	function _isLogin(){
		Izb.common.getResult({
			action:_actionSession,
			success:function(result){
				top.location.href = _redirectUrl;
			},
			error: function () {

			}
		});	
	}
	
	function _login(){
		if($uid.val() == '') {			
			$uid.focus();
			return false; 
		}
		
		if($pwd.val() == '') {
			$pwd.focus();
			return false; 
		}
		
		if($auth_code.val() == '' ) {
			$auth_code.focus();
			return false; 
		}
		var data = { name : $uid.val(), password : $pwd.val(), auth_code:$auth_code.val()};
		
		Izb.common.getResult({
			action: _actionLogin,
			data : data, 
			success : function(result) {				
				top.location.href = _redirectUrl;
			},
			error: function (xhr, status, result) {
				$tips.html(result.msg);
				_refreshCode();
			}
		});	
	}
	
	function _bindEvent(){
		$uid.bind("keyup",function(k){				
			if(k.keyCode==13){
				_login();
			}
		});
		
		$pwd.bind("keyup",function(k){				
			if(k.keyCode==13){
				_login();
			}
		});
		
		$auth_code.bind("keyup",function(k){				
			if(k.keyCode==13){
				_login();
			}
		});
		
		$('#btnLogin').click(function(){
			_login();
		});
		
		$authcodeImg.click(function(){
			_refreshCode();
		});
	}
	
	//登录模块
	return {
		init: function (opt) {
			Izb.core.setOptions({ dataFormat: false });

			_actionSession = opt.actionSession;
			_actionLogin = opt.actionLogin;
			_redirectUrl = opt.redirectUrl;
			_isLogin();
			_refreshCode();
			_bindEvent();
			$uid.focus();
		}
	};
});