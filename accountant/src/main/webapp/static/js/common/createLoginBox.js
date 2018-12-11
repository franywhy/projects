 function createLoginPopup(){
	$("body").append('<!-- 登录框 -->\
	    <div class="login-bg"></div>\
	    <div id="login" class="loginBox">\
	        <div class="login-tip">用户名不存在或密码不正确</div>\
	        <div class="login-tip-phone">手机号码或密码不合法</div>\
	        <div class="top-title">\
	            <div class="back-btn"></div>\
	            <span class="top-left">\
	                <a class="loginText">登录</a>\
	                <a class="registerText">注册</a>\
	            </span>\
	            <span class="btn-right" onclick="loginController.hidePopupLogin();"></span>\
	        </div>\
	        <div class="ul">\
	            <!-- 登录 -->\
	            <div class="form-table userLogin">\
	                <div class="user-login-box">\
	                    <div class="baseInput userName"><input type="text" placeholder="手机号/邮箱"/> <div class="icon-pd"></div></div>\
	                    <div class="baseInput pwd"><input type="password" placeholder="密码"/>  <div class="icon-pd"></div></div>\
	                    <div class="select">\
	                        <span class="s-btn-forget"><a href="javascript:;">忘记密码?</a></span>\
	                        <span class="s-btn-reight go-register"><a href="javascript:;">注册</a></span>\
	                    </div>\
	                    <button class="bitme-btn  pop-login-btn" id="loginpop_login">立即登录</button>\
	                </div>\
	                <!-- 登录成功html -->\
	                <div class="login-scrccen">\
	                    <!--<p>为了更好的体验和账号安全，建议您绑定手机号，以便我们提供更多的免费优惠服务给您。</p>\
	                    <div class="other-btn">\
	                        <span class="btn-change-phone">去绑定手机</span>\
	                    </div>-->\
	                </div>\
	                <!-- 绑定手机号HTML -->\
	                <div class="bind-phone">\
	                    <div class="baseInput bind-phone-input"><input type="text" maxLength="11" placeholder="手机号"/>  <div class="icon-pd"></div> </div>\
	                    <div class="baseInput getCode-input">\
	                        <input type="text" maxLength="6" placeholder="验证码"/>\
	                        <span class="getCode-btn">获取验证码</span>\
	                    </div>\
	                    <div class="other-btn">\
	                        <span class="now-bind-phone">立即绑定手机</span>\
	                    </div>\
	                </div>\
	                <!-- 绑定手机号成功或失败提示 -->\
	                <div class="bind-phone-info relative">\
	                    <h1 style="line-height:55px"><i></i>恭喜您，手机号绑定成功</h1>\
	                    <div class="appKD-ewm absolute">\
	                        <img src="/images/ewm.png">\
	                            <p>扫描下载会答APP，会计学习无难题</p>\
	                            <p>用会答APP 老师免费答疑</p>\
	                        </div>\
	                    </div>\
	                </div>\
	                <!-- 注册 -->\
	                <div class="form-table userRegister">\
	                    <div class="re-ul" style="height:135px;">\
	                        <div class="re-ul-li">\
	                            <div class="baseInput userName"><input type="text" maxLength="11" placeholder="手机号"/>  <div class="icon-pd"></div> </div>\
	                            <div class="baseInput userCode">\
	                                <input type="text" maxLength="4" placeholder="验证码"/>\
	                                <span class="getCode">获取验证码</span>\
	                            </div>\
	                        </div>\
	                        <div class="re-ul-li">\
	                            <div class="baseInput upwd"><input type="password" maxLength="16" placeholder="密码"/>  <div class="icon-pd"></div> </div>\
	                            <div class="baseInput rpwd"><input type="password" maxLength="16" placeholder="确认密码"/>  <div class="icon-pd"></div> </div>\
	                        </div>\
	                        <div class="re-ul-li bind-phone-infos ">\
	                            <h1><i></i>注册成功，欢迎来到恒企自考</h1>\
	                        </div>\
	                        <div class="clear"></div>\
	                    </div>\
	                    <p class="tip-text"><!-- 密码或用户名错误 --></p>\
	                    <div class="select">\
	                        <span class="s-btn-forget"><a href="javascript:;"></a></span>\
	                        <span class="s-btn-reight back-login"><a href="javascript:;">返回登录</a></span>\
	                    </div>\
	                    <button class="bitme-btn  next-btn active">下一步</button>\
	                </div>\
	            </div>\
	        </div>\
	    </div>\
	');
}
createLoginPopup();