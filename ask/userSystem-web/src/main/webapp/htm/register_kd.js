$(function() {
	var canGetCode = true;
	var MAX_S = 60;
	var i = MAX_S;
	var timer;
	if (canGetCode) {
		$(".btn-getCode").click(function() {
			if (i != MAX_S) {
				return;
			}
			var phone = $(".user-phone input").val();
			if (phone == "") {
				alert("手机号不能为空");
				return;
			} else if (!isMobil(phone)) {
				alert("请填写正确的手机号码");
				return;
			}

			//发送验证码
			$.ajax({
				url : '/security',// 跳转到 发送验证码
				data : {
					"phone" : phone
				},
				type : 'post',
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.code == "1") {
						$(".btn-getCode").addClass("active");
						setTimeout(function() {
							$(".code-tip").fadeOut();
						}, 500);
						timer = setInterval(function() {
							$(".btn-getCode").text(i + "秒后重新获取");
							i--;
							if (i < 0) {
								clearTimeout(timer);
								$(".btn-getCode").text("重新获取验证码");
								$(".btn-getCode").removeClass("active");
								i = MAX_S;
							}
						}, 1000);
					} else {
						alert(data.msg);
					}
				},
				error : function() {
					// view("异常！");    
					alert("网络异常！");
				}

			});
		});

		$(".btn-tijiao").click(function() {
			var phone = $(".user-phone input").val();
			var psw = $(".user-password input").val();
			var name = $(".user-name input").val();
			var security = $(".j_security").val();

			if (phone == "") {
				alert("手机号不能为空");
				return;
			} else if (!isMobil(phone)) {
				alert("手机号不正确");
				return;
			} else if (psw == "") {
				alert("密码不能为空");
				return;
			} else if (!isPasswd(psw)) {
				alert("密码格式不正确");
				return;
			} else if (name == "") {
				alert("名字不能为空");
				return;
			} else if (_length(name) > 8) {
				alert("用户名为1-8位字符,请重新输入！");
				return;
			}else if(security.length != 4 ){
				alert("请输入正确的验证码");
				return;
			} else {

				//发送注册
				$.ajax({
					url : '/register',// 发送注册
					data : {
						"user_name" : phone,
						"nick_name" : name,
						"security" : security,
						"password" : psw,
						"invite_user_id" : getUrlParam("invite_user_id"),
						"invite_user_type" : getUrlParam("invite_user_type"),
						"invite_time" : getUrlParam("invite_time"),
						"share_type" : getUrlParam("share_type")
					},
					type : 'post',
					cache : false,
					dataType : 'json',
					success : function(data) {
						if (data.code == "1") {
							location.href = "down_app.html";
						} else {
							alert(data.msg);
						}
					},
					error : function() {
						// view("异常！");    
						alert("网络异常！");
					}

				//location.href = "index2.html";
				});
			}

		});

		//校验手机号码：必须以数字开头，除数字外，可含有“-”
		function isMobil(s) {
			var patrn = /^1\d{10}$/;
			if (!patrn.exec(s))
				return false;
			return true;
		}

		//校验密码：只能输入6-10个字母、数字、下划线
		function isPasswd(s) {
			var patrn = /^\w{6,20}/;
			if (!patrn.exec(s))
				return false;
			return true;
		}
		
		//字符长度 中文两个字符 英文一个字符
		function _length(str) {
			var len = 0;
			for (var i = 0; i < str.length; i++) {
				if (str.charAt(i) > '~') {
					len += 2;
				} else {
					len++;
				}
			}
			return len;
		}
		
		/* 
		返回密码的强度级别 
		 */
		/*
		function checkStrong(sPW) {  
		    if (sPW.length <= 4)  
		        return 0; //密码太短    
		    Modes = 0;  
		    for (i = 0; i < sPW.length; i++) {  
		        //测试每一个字符的类别并统计一共有多少种模式.    
		        Modes |= CharMode(sPW.charCodeAt(i));  
		    }  
		    return bitTotal(Modes);  
		}  
		 */

	}
});

function getUrlParam(name){  
    //构造一个含有目标参数的正则表达式对象  
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
    //匹配目标参数  
    var r = window.location.search.substr(1).match(reg);  
    //返回参数值  
    if (r!=null) return unescape(r[2]);  
    return null;  
}  