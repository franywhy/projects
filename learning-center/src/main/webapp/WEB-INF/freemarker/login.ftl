<#-- 用户登录 -->
<#assign showNavbarTab="Y">
<#assign pageTitle="登录账号">


<div class="sign-wrap">
    <div class="contain">
        <div class="sign-form-wrap">
        <#if school.isRegEnabled?? && school.isRegEnabled == "Y">
            <div class="sign-bar">
                <a href="/login" class="active">登录</a>
                <a href="/register">注册</a>
            </div>
        </#if>    
            <form  id="loginForm" role="form" method="post" action="/ajax/login">
                <input type="hidden" name="login-weixinClose" id="login-weixinClose" value="${weixinClose!''}">
                <input type="hidden" name="redirectUrl" id="redirectUrl" value="${redirectUrl!''}">
                <div class="form-box">
                    <input type="text" class="form-input" name="username" id="username" placeholder="手机号码">
                </div>
                <div class="form-box">
                    <input type="password" class="form-input" name="password" id="password" placeholder="密码">
                </div>
                <div class="form-box form-core clearfix" id="kaptchaWrap">
				</div>                
                <div class="form-box form-rem">
                    <input type="checkbox" id="rememberBtn" name="rememberBtn"> 记住登录
                    <a href="/forget">忘记密码</a>
                </div>
                <p class="error-msg" id="errorMsg"></p>
                <div class="form-box">
                    <button type="submit" class="sign-btn" name="button">登录</button>
                </div>
                <#if school.isRegEnabled?? && school.isRegEnabled == "Y">
                <div class="form-desc">还没有帐号？ 去 <a href="/register">注册</a></div>
                </#if>
            </form>
            <#if isPcOauthLogin>
            <div class="quick-login">
                <div class="quick-login-title"><span>快捷登录</span></div>
                <div class="quick-login-type">
                    <div><a href="<#if wxqrcode?has_content>${wxqrcode!''}</#if>">微信登录</a></div>
                </div>
            </div>
            </#if>
        </div>
    </div>
</div>
<div class="mask"></div>
<#if oauth?has_content && oauth == "1">
<div class="login-tip-wrap" id="login-tip">
    <div class="tip-hd">
        <span class="tip-close">&times;</span>
        <h4>温馨提示</h4>
    </div>
    <div class="tip-bd clearfix">
        <div class="tip-text">
            <p>已有帐号,马上去</p>
            <a href="/login" class="link link-bind">登录绑定帐号</a>
        </div>
        <div class="tip-text">
            <p>还没有帐号?马上去</p>
            <a href="/register" target="_blank" class="link link-register">注册</a>
        </div>
    </div>
</div>
</#if>
<#-- 待加载的JS -->
<script src="/assets/js/libs/jquery/jquery-1.11.1.min.js"></script>
<!-- tooltip -->
<script src="/assets/js/libs/jquery-validation/jquery.validate.min.js"></script>
<script src="/assets/js/controller/front/login/login.js"></script>