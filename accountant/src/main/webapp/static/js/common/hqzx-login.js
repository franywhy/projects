$(function(){
	loginController.init();
});

var loginController = (function(){
	//$.module('zk.user.loginController', function () {

//  var loginurl = zk.config.loginurl;
    var returl="";
    var _anyPopup = false; //标示是否有任何一个框弹出
    var _mainController;//用户信息模块
    var $topLoginButton = $(".login-btn");
    var $topLoginBgClass = $(".loginBox");
    var $topLoginCloseButton = $(".btn-right");
    var $top_reg_area = $(".register-btn");
    var _loginCallback = null;
    var _loginfailCallback = null;
    var _regCallback = null;
    var offselectText = document.getElementById("login");

    var canMove = false;
    var boxW = $("#login .top").outerWidth(true);
    var boxH = $("#login .top").outerHeight(true);
    var bigW = $("#login").outerWidth(true);
    var bigH = $("#login").outerHeight(true);
    var tw = boxW / 2;
    var th = boxH / 2;
    var offset;
    var ox, oy;
    $(".loginBox .userRegister").hide();
    $(".top-title").mousedown(function (event) {
        offset = $(this).offset();
        ox = event.pageX - offset.left;
        oy = event.pageY - offset.top;
        canMove = true;
        $(this).css({ "cursor": 'move' })
        move();
    })

    $(document).mouseup(function () {
        $(".top-title").css({ "cursor": 'default' })
        canMove = false;
        move()
        //return;
    })



    function _init(){
//      _mainController = zk.user.mainController;

        _loginCallback = typeof success == 'function' ? success : function() {

            $(".loginText").text('登录成功');
            $(".userLogin .user-login-box").hide();
            $(".userLogin").append(saveScueeHtml);
            $(".userLogin .login-scrccen").show();
            if (returl == "") {
                window.location.reload();
            }
            else {
                window.location.href = returl;
            }

        };

        _loginfailCallback = function(){
            loginBtn.text('立即登录');
            loginBtn.removeClass("active");
            $(pword).focus().val("");
            $("#login .login-tip").show().stop().animate({ top: 0 }, 200, function () {
                $(pword).focus().val("");
                setTimeout(function () {// 设置间隔，防止连续点击登录按钮,多次发送ajax请求。
                    loginBtn.text('立即登录');
                    canCLickLoginBtn = true;
                    $("#login .login-tip").stop().animate({ top: -56 }, 120).hide();
                }, 1500)
            })
        };

        _regCallback = function(){

            $(".re-ul").animate({ marginLeft: -1000 }, 300, function () {
                setTimeout(function () {
                    $(".btn-right").click();
                    location.reload();
                }, 2000)
            })
            $(".next-btn ").removeClass("active").fadeOut();

        }

        _registerEvents();
//      _actions = _mainController.getActions();
    }



    function move() {
        var w = $(window).width();
        var h = $(window).height();
        $(window).mousemove(function (e) {
            if (canMove != false) {
                var x = e.clientX - ox + 250;
                var y = e.clientY - oy + 200;
                console.log(e.clientX, e.clientY, x, y)
                $("#login").css({ left: x, top: y })
                if (x + boxW >= w - 10) {
                    $("#login").css({ left: w - boxW })
                    // console.log(0)
                }
                if (x <= 0) {
                    $("#login").css({ left: 0 })
                    // console.log(1)
                }
                if (y + bigH >= h) {
                    $("#login").css({ top: h - bigH });
                }
                if (y <= 0) {
                    $("#login").css({ top: 0 })
                    // console.log(3)
                }
            } else {
                return false;
            }
        })
    }

    // 切换
    $(".register-btn").click(function () {
        $(".go-register").click();
    });
     $(".login-btn").click(function () {
       	$(".back-login,.back-btn").click();
    });
    function regTab() {
        var ulWidth = $(".loginBox").width();
        $(".loginBox .ul").width(ulWidth * 2)
        $(".go-register").click(function () {
            $(".top-left .loginText").animate({ marginLeft: -100, opacity: 0 })
            $(".top-left .registerText").animate({ left: '44%', opacity: 1 });
            $(".loginBox .userLogin").animate({ left: '-100%' }, function () { $(this).fadeOut(0) })
            $(".loginBox .userRegister").fadeIn(0).animate({ left: 0 })

            $(".back-btn").fadeIn();
            $(".userRegister .select").show();
            $(".userRegister .next-btn").css({ marginTop: 0 });
            $(".registerText").text("注册");
        });
        $(".s-btn-forget").click(function () {
            $(".top-left .loginText").animate({ marginLeft: -100, opacity: 0 })
            $(".top-left .registerText").animate({ left: '44%', opacity: 1 });
            $(".loginBox .userLogin").animate({ left: '-100%' }, function () { $(this).fadeOut(0) })
            $(".loginBox .userRegister").fadeIn(0).animate({ left: 0 })

            $(".back-btn").fadeIn();
            $(".userRegister .select").show();
            $(".userRegister .next-btn").css({ marginTop: 0 });
            $(".registerText").text("找回密码");
        });

        $(".back-login,.back-btn").click(function () {

            $(".top-left .loginText").animate({ marginLeft: 0, opacity: 1 });
            $(".top-left .registerText").animate({ left: '56%', opacity: 0 });
            $(".loginBox .userRegister").fadeIn(0).animate({ left: '100%' }, function () { $(this).fadeOut(0) })
            $(".loginBox .userLogin").fadeIn(0).animate({ left: 0 });


            $(".back-btn").fadeOut();
            $(".userRegister input").val("");
            $(".userRegister .baseInput .icon-pd ").hide();
            $(".userRegister .baseInput input").css({ borderColor: "#d8d8d8" });
        })
    }
    regTab()

    function showPop(clickShowPopBtn, showConClass, clickoffPopBtn) {
        $.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
        // 高丽撕模糊
        $(clickShowPopBtn).live('click', function () {

            //可能会触发两次登录
            if ($(this).text() == "登录") {
                $(".back-btn").click();
            }
            var i = 0.5;

            $(".login-bg").animate({ backgroundColor: "#000000", opacity: 0.7 })
            $(".body").css({ overflow: "hidden" })
            var timer = setInterval(function () {
                i += 1;
                if (i >= 10) { clearInterval(timer) }
                $(".body").css({
                    '-webkit-filter': 'blur(' + i + 'px)',
                    '-moz-filter': 'blur(' + i + 'px)',
                    '-ms-filter': 'blur(' + i + 'px)',
                    'filter': 'blur(' + i + 'px)',
                    'filter': 'blur(' + i + 'px)'
                });
            }, 17);
            /*}*/
            $(".login-bg").show();
            $(showConClass).fadeIn(0);
            $(showConClass).animate({ marginTop: -200, opacity: 0.98 }, 500);
            $("#login input").val("");
        });

//        $(clickoffPopBtn).click(function () {
//            var i = 10;
//            if ($.browser.msie) {
//                $(".login-bg").animate({ backgroundColor: "#000000", opacity: 0 }, function () {
//                    $(this).hide();
//                    $(".login-scrccen,.bind-phone,.bind-phone-info").hide().remove()
//                    $(".user-login-box").show();
//                    $(".loginText").text("登录");
//                    $(".pop-login-btn").text("登录")
//                })
//
//            } else {
//                var timer = setInterval(function () {
//                    i -= 1
//                    $(".body").css({
//                        '-webkit-filter': 'blur(' + i + 'px)',
//                        '-moz-filter': 'blur(' + i + 'px)',
//                        '-ms-filter': 'blur(' + i + 'px)',
//                        'filter': 'blur(' + i + 'px)',
//                        'filter': 'blur(' + i + 'px)'
//                    });
//                    if (i <= 0) { clearInterval(timer) }
//                }, 17);
//
//            }
//            $(showConClass).animate({ marginTop: -130, opacity: 0 }, 500)
//            $(showConClass).fadeOut(400, function () {
//                $(".login-scrccen,.bind-phone,.bind-phone-info").hide().remove()
//                $(".user-login-box").show();
//                $(".loginText").text("登录");
//                $(".pop-login-btn").text("登录");
//                $("#login input").val("");
//            });
//            $(".login-bg").fadeOut(400);
//        });

    }
    //有3个参数 ，showPop([0 = 点击弹出登录层的按钮,1 = 弹出层,2=关闭弹出层的按钮])
    // showPop(".get-VIP a",".buy-VIP-popBox",".off-pop-btn");
    /*showPop(".btn-login",".loginBox",".btn-right");*/
//    showPop("#top_login_area .login", ".loginBox", ".btn-right");
//    showPop(".btn-reget", ".loginBox", ".btn-right");


    var userOK = false;
    var pwdOK = false;
    var codeOK;
    var loginBtnCan = true;
    var phoneOK = false;
    var rPwdOK = false;
    var rUPwdOK = false;
    var canCLickLoginBtn = true;
    var phone = $("#login .userLogin .userName input"); //登录手机或邮箱输入框
    var pword = $("#login .userLogin .pwd input"); //登录密码输入框
    var zMaliYZ = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; //邮箱正则（已不用 恒企教育只能使用手机号登录）
    var phoneYZ = /^1[3|4|5|8][0-9]\d{4,8}$/; //手机正则   旧
    var zzpd = /^[a-z0-9A-Z_]{6,16}$/; //密码正则
    var loginBtn = $("#login .pop-login-btn");
    var canBindPhone; //绑定手机的提交按钮 ture 为能点
    var bingPhoneOK1 = bingPhoneOK2 = false;
    // 注册
    var phoneR = $("#login .userRegister .userName input");
    var pCode = $("#login .userRegister .userCode input");
    var upwd = $("#login .userRegister .upwd input");
    var rpwd = $("#login .userRegister .rpwd input");


    /*登录验证*/

    function phoneCode() {//邮箱或密码
        var sMobile = $(phone).val();
        if (sMobile.length >= 1) {
            $(phone).css({ borderColor: "#d8d8d8" });
            userOK = true; //条件成立;
        } else {
            $(phone).css({ borderColor: "#ff0000" });
            userOK = false; //条件不成立
        }
        loginShowOK(); //显示条件是否成立
    }


    function pwdCode() {//登录密码验证
        var sPwd = $(pword).val();
        if (sPwd.length >= 1) {
            $(pword).css({ borderColor: "#d8d8d8" });
            // $(pword).siblings(".icon-pd").removeClass("active")
            pwdOK = true;
        } else {
            $(pword).css({ borderColor: "#ff0000" });
            // $(pword).siblings(".icon-pd").show().addClass("active");
            pwdOK = false;
        }
        loginShowOK();
    }

    // 文字提示样式
    $("#login .login-tip").css({
        color: '#ffffff',
        backgroundColor: "#ff0000",
        height: '65px',
        left: 0,
        lineHeight: '65px',
        display: 'none',
        position: 'absolute',
        textAlign: 'center',
        top: -65,
        fontSize: '20px',
        width: '100%',
        zIndex: 99
    })
    // 文字提示样式
    $("#login .login-tip-phone").css({
        color: '#ffffff',
        backgroundColor: "#ff0000",
        height: '65px',
        left: 0,
        lineHeight: '65px',
        display: 'none',
        position: 'absolute',
        textAlign: 'center',
        top: -65,
        fontSize: '20px',
        width: '100%',
        zIndex: 99
    })
    // ====================================================================

    /*====================================
    ======================================
    用户名登陆验证
    ======================================
    ====================================*/

    $(".userLogin .userName input").focus(function () {
        $(this).keyup(function (event) {
            phoneCode();
        })
    });

    $(".pwd input").focus(function () {
        $(this).keyup(function (event) {
            phoneCode();
            pwdCode();
        })
    });

    function loginShowOK() {
        if (userOK && pwdOK) {
            $(loginBtn).addClass("active");
        } else {
            $(loginBtn).removeClass("active");
        }
    }


    var saveScueeHtml = $(".userLogin .login-scrccen").clone(); //保存登录成功的HTML
    var saveGetCodeHtml = $(".userLogin .bind-phone").clone(); //保存绑定手机的HTML
    var saveBindScreen = $(".userLogin .bind-phone-info").clone(); //
    $(".login-scrccen,.bind-phone,.bind-phone-info").hide().remove(); //移除登录成功的HTML

    function clickLogin() {
        if (userOK && pwdOK) { //当条件全部成立
            var sMobile = $(phone).val();
            var sPwd = $(pword).val();
            loginBtn.text('正在登录...');
            loginBtn.removeClass("active");
            //if( (/*zMaliYZ.test(sMobile) && zzpd.test(sPwd)) || */(phoneYZ.test(sMobile) && zzpd.test(sPwd)) ) {//殴判断验证格式是否正确
            if (sMobile.length > 0 && zzpd.test(sPwd)) {//殴判断验证格式是否正确

                var logindata = {
                    user_name:sMobile,
                    password:sPwd,
                    priv:3,
                    qd:1
                }
                _mainController.login(logindata, _loginCallback, _loginfailCallback,_loginfailCallback);
            } else {

                _loginfailCallback();
            }
        }
    }
    $("#loginpop_login").live('click', function () {
        clickLogin();
    });
    $("body").on("keypress", function (eve) {
        e = eve || event;
        if (e.keyCode == 13) {
            clickLogin();
        }
    });


    function bingPhoneShow() {
        if (bingPhoneOK2 && bingPhoneOK1) {
            $(".now-bind-phone").addClass("active");

            // AJAX判断验证是否正确
            if ($(".bind-phone .getCode-input input").val().length == 6) {
                return true;
            } else {
                return false;
            }
        } else {
            $(".now-bind-phone").removeClass("active");
            return false;
        }
    }




    /*====================================
    ======================================
    注册验证
    ======================================
    ====================================*/
    /*function phoneCodeR(){
    var sMobile = $(phoneR).val();
    if($(phoneR).val().length>0){
    //$(phoneR).css({borderColor:"#d8d8d8"});
    //$(phoneR).siblings(".icon-pd").removeClass("active").hide();
    phoneOK = true;
    }else{
    $(phoneR).css({borderColor:"#ff0000"});
    phoneOK = false;
    }
    regisOK();

    }*/

    var codeLodin = false;

    var r_sPwd, u_sPwd;
    // 密码验证
    function pwdRcode() {
        u_sPwd = $(upwd).val();
        if (u_sPwd.length > 0) {
            $(upwd).css({ borderColor: "#d8d8d8" });
            $(upwd).siblings(".icon-pd").removeClass("active");
        } else {
            $(upwd).css({ borderColor: "#ff0000" });
        }
    }
    function pwdURcode() {
        r_sPwd = $(rpwd).val();
        if (r_sPwd.length > 0 && r_sPwd === u_sPwd) {
            $(rpwd).css({ borderColor: "#d8d8d8" });
            $(rpwd).siblings(".icon-pd").removeClass("active")
            $(upwd).siblings(".icon-pd").removeClass("active");
            rUPwdOK = true;
        } else {
            $(rpwd).css({ borderColor: "#ff0000" });
            $(rpwd).siblings(".icon-pd").show().addClass("active");
            $(upwd).siblings(".icon-pd").show().addClass("active");
            rUPwdOK = false;
        }
        pwdShow();
    }


    var timer;
    var canDo = true;
    function pwdShow() {
        if (rUPwdOK) {
            $(".next-btn").addClass("active");
            $(".baseInput input").blur();

            if ($(".next-btn").text() == "重置密码") {
                zk.core.getResult({
                    url: loginurl + "resetpsw_v200?user_name=" + $(phoneR).val() + "&password=" + r_sPwd + "&security=" + $(pCode).val(),
                    requireToken: false,//是否需要access_token
                    dataType : "json",
                    type: "POST",
                    success: function(data) {

                        var logindata = {
                            user_name:sMobile,
                            password:sPwd,
                            priv:3,
                            qd:1
                        }
                        _mainController.login(logindata, _loginCallback, _loginfailCallback,_loginfailCallback);

                        $(".re-ul").animate({ marginLeft: -1000 }, 300, function () {
                            setTimeout(function () {
                                $(".btn-right").click();
                            }, 2000)
                        })
                        $(".next-btn ").removeClass("active").fadeOut();
                    },
                    resolveError : function (data) {
                        if (data.code == "30306") {
                            alert(data.msg);
                            $(".re-ul").animate({ marginLeft: -1000 }, 300, function () {
                                setTimeout(function () {
                                    $(".btn-right").click();
                                }, 2000)
                            })
                            $(".next-btn ").removeClass("active").fadeOut();
                        } else if (data.code == "30319") {
                            alert(data.msg);
                        }
                    }
                });
            } else {


                var regdata = {
                    nick_name:$(phoneR).val(),
                    user_name:$(phoneR).val(),
                    password:r_sPwd,
                    security:$(pCode).val(),
                    priv:3
                }

                _mainController.register(regdata, _regCallback, function(res) {

                    var index = 1;
                    if (res.code == "30306") {
                        alert(data.msg);
                        $(".re-ul").animate({ marginLeft: -1000 }, 300, function () {
                            setTimeout(function () {
                                $(".btn-right").click();
                            }, 2000)
                        })
                        $(".next-btn ").removeClass("active").fadeOut();
                    }

                });

            }

        } else {
            $(".next-btn").removeClass("active");
        }
    }



    // 获取验证码计时器
    var canIn = true;
    var canGetCode = true;
    var k = 60;
    var timera;

    //若不增加下行，则会出现 点击一次请求2次的情况
    window.onload = function () {
        $(".getCode").click(function () {
            var sMobile = $(phoneR).val();
            var telReg = !!sMobile.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/); //前端手机的验证
            /*regisOK();*/
            if (telReg == false) {
                $(".userRegister .userName").addClass("animated" + " shake");
                $(phoneR).css({ borderColor: "#ff0000" });
                $(phoneR).siblings(".icon-pd").show().addClass("active");
                setTimeout(function () {
                    $(".userRegister .userName").removeClass("animated" + " shake");
                    $(".icon-pd").css("display", "none");
                }, 1000);
                return;
            }
            $(".getCode").addClass("active");
            if (canGetCode) {
                if ($(".registerText").text() == "注册") {
                    zk.core.getResult({
                        url: loginurl + "security_v200",
                        data:{
                            type: "0",
                            phone: $(phoneR).val()
                        },
                        requireToken: false,//是否需要access_token
                        dataType : "json",
                        type: "POST",
                        success: function(result) {
                            alert("手机验证码已经发送，请注意查收！");
                            timera = setInterval(function () {
                                k--;
                                $(".getCode").text(k + "秒后重新获取");
                                if (k < 0) {
                                    clearTimeout(timera);
                                    $(".getCode").removeClass("active");
                                    $(".getCode").text("获取验证码");
                                    canGetCode = true;
                                    k = 60;
                                    codeOK = false;
                                }
                            }, 1000);
                        },
                        resolveError : function (result) {
                            $(".getCode").removeClass("active");
                            alert(result.data);
                        }
                    });
                }else if($(".registerText").text() == "找回密码"){
                    zk.core.getResult({
                        url: loginurl + "security",
                        data:{
                            type: "0",
                            phone: $(phoneR).val()
                        },
                        requireToken: false,//是否需要access_token
                        dataType : "json",
                        type: "POST",
                        success: function(res) {
                            alert("手机验证码已经发送，请注意查收！");
                            timera = setInterval(function () {
                                k--;
                                $(".getCode").text(k + "秒后重新获取");
                                if (k < 0) {
                                    clearTimeout(timera);
                                    $(".getCode").removeClass("active");
                                    $(".getCode").text("获取验证码");
                                    canGetCode = true;
                                    k = 60;
                                    codeOK = false;
                                }
                            }, 1000);
                        },
                        resolveError : function (result) {
                            alert(result.data);
                        }
                    });
                }
            }
            canGetCode = false;
        });

        $(".next-btn").live('click', function () {
            var sCode = $(pCode).val();
            var sMobile = $(phoneR).val();

            if (!canDo) return;
            canDo = false;
            zk.core.getResult({
                url: loginurl + "isregkey?phone=" + sMobile + "&security=" + sCode,
                requireToken: false,//是否需要access_token
                dataType : "json",
                type: "GET",
                success: function(data) {
                    codeOK = true;
                    $(".next-btn").text("提交中...");
                    $(".next-btn").removeClass("active");

                    if ($(".registerText").text() == "找回密码") {
                        timer = setTimeout(function () {
                            $(".re-ul").animate({ marginLeft: -500 }, function () {
                                $(".next-btn").removeClass('active').text("重置密码");
                                $(".userRegister .select").hide();
                                $(".next-btn").animate({ marginTop: 28 }, 800);
                                $(".back-btn").hide();
                                $(".registerText").text("最后一步");
                                canDo = true
                            })
                        }, 4500);
                    } else {
                        timer = setTimeout(function () {
                            $(".re-ul").animate({ marginLeft: -500 }, function () {
                                $(".next-btn").removeClass('active').text("立即注册");
                                $(".userRegister .select").hide();
                                $(".next-btn").animate({ marginTop: 28 }, 800);
                                $(".back-btn").hide();
                                $(".registerText").text("最后一步");
                                canDo = true
                            })
                        }, 4500);
                    }
                },
                resolveError : function (res) {
                    canDo = true;
                    alert("验证码不正确或已失效");
                }
            });

            if (codeOK) {
                $(".getCode").stop().animate({ width: 30 }, function () {
                    $(".getCode").addClass("loginBg");
                    // 验证成功
                    timer = setTimeout(function () { //模拟网速缓冲
                        $(".getCode").removeClass("loginBg").addClass("addBg");
                        codeLodin = true;
                    }, 2000);
                });
            } else {
                $(".getCode").removeClass("addBg").stop().animate({ width: 115 })

            }

        });

        // 绑定手机
        $(".userLogin .btn-change-phone").live('click', function () {
            $(".loginText").text('绑定手机');
            $(".userLogin .login-scrccen").hide().remove()
            $(".userLogin").append(saveGetCodeHtml);
            $(".userLogin .bind-phone").show();
        })

        $(".bind-phone .bind-phone-input input").live('focus', function () {
            $(this).keyup(function () {
                //if( phoneYZ.test($(this).val()) ){
                if ($(this).val().length > 0) {
                    bingPhoneOK1 = true;
                } else {
                    bingPhoneOK1 = false;
                }
                bingPhoneShow()
            })
        });

        $(".bind-phone .getCode-input input").live('focus', function () {
            $(this).keyup(function (event) {
                if ($(this).val().length >= 1) {
                    bingPhoneOK2 = true;
                } else {
                    bingPhoneOK2 = false;
                }
                bingPhoneShow()
            })
        })

        $(".now-bind-phone").live('click', function () {
            $(this).removeClass("active");
            $(this).text("正在绑定...")
            if (bingPhoneShow()) {
                setTimeout(function () { //模拟缓存，可去掉定时器，
                    // 初始化
                    $(".userLogin .bind-phone .now-bind-phone").text("立即绑定手机").removeClass("active");
                    $(".userLogin .bind-phone input").val("")
                    $(".userLogin .bind-phone").hide().remove()

                    $(".userLogin").append(saveBindScreen);
                    $(".bind-phone-info").show();
                    $(".loginText").text('绑定成功');
                }, 2000)

            } else {
                $(this).text("立即绑定手机");
            }
            return;
        })
        // ====================================================================

        // 手机验证
        /*$("#login .userRegister .userName input").focus(function(){
        $(this).keyup(function(event){
        phoneCodeR();
        })
        });*/
        /*
        $("#login .userCode input").focus(function(){
        $(this).keyup(function(event){
        if($(this).focus()){
        phoneCodeR();
        coCode();
        }
        })
        })*/

        // 密码验证
        $("#login .userRegister .upwd input").focus(function () {
            $(this).keyup(function (event) {
                pwdRcode();
            })
        });

        $("#login .userRegister .rpwd input").focus(function () {
            $(this).keyup(function (event) {
                pwdURcode();
            })
        })

    }

    //  浏览器保存账号密码，显示登陆按钮
    function saveNumJudge() {
        var b = 0;
        var t = setInterval(function () {
            b++;
            var a = $(".userLogin .userName input").val();
            if (a || b > 30) {
                clearInterval(t);
                if (a) {
                    phoneCode();
                    pwdCode();
                }
            }
        }, 100);
    }
    saveNumJudge();


    function _registerEvents() {

        //登录按钮事件
        showPop($topLoginButton, $topLoginBgClass, $topLoginCloseButton);
        //注册按钮事件
        showPop($top_reg_area, $topLoginBgClass, $topLoginCloseButton);

        $("#userinfo_logout").live("click",function(){

            zk.user.logout();
        });
        //单点登录事件注册
        $(".sso_url").live("click", function () {
//            var url = $(this).attr("item_url");
//            var username = $.cookie('hqonline.user.username');
//            if (username == null || username == "null") {
//                returl = url;
//                $(".login").trigger("click");
//                return;
//            }
//            window.location.href = "http://passport.kjcity.com/redirect_to_other?access_token=" + username + "&rd_url=" + url;
       });
    }

    //弹出注册和登录框
    function _popupLogin (success) {
        //弹出注册登录框逻辑
        if(!_anyPopup)
        {
            _showLoginBox();
            _anyPopup = true;
        }

    }
    function _popupLoginHide (success) {

        //弹出注册登录框逻辑
        _closeLoginBox();
        _anyPopup = false;
    }

    function _closeLoginBox()
    {
        var i = 10;
        if ($.browser.msie) {
            $(".login-bg").animate({ backgroundColor: "#000000", opacity: 0 }, function () {
                $(this).hide();
                $(".login-scrccen,.bind-phone,.bind-phone-info").hide().remove()
                $(".user-login-box").show();
                $(".loginText").text("登录");
                $(".pop-login-btn").text("登录")
            })

        } else {
            var timer = setInterval(function () {
                i -= 1
                $(".body").css({
                    '-webkit-filter': 'blur(' + i + 'px)',
                    '-moz-filter': 'blur(' + i + 'px)',
                    '-ms-filter': 'blur(' + i + 'px)',
                    'filter': 'blur(' + i + 'px)',
                    'filter': 'blur(' + i + 'px)'
                });
                if (i <= 0) { clearInterval(timer) }
            }, 17);

        }
        $($topLoginBgClass).animate({ marginTop: -130, opacity: 0 }, 500)
        $($topLoginBgClass).fadeOut(400, function () {
            $(".login-scrccen,.bind-phone,.bind-phone-info").hide().remove()
            $(".user-login-box").show();
            $(".loginText").text("登录");
            $(".pop-login-btn").text("登录");
            $("#login input").val("");
        });
        $(".login-bg").fadeOut(400);
    }

    function _showLoginBox()
    {

        //可能会触发两次登录
        if ($(this).text() == "登录") {
            $(".back-btn").click();
        }
        var i = 0.5;

        $(".login-bg").animate({ backgroundColor: "#000000", opacity: 0.7 })
        $(".body").css({ overflow: "hidden" })
        var timer = setInterval(function () {
            i += 1;
            if (i >= 10) { clearInterval(timer) }
            $(".body").css({
                '-webkit-filter': 'blur(' + i + 'px)',
                '-moz-filter': 'blur(' + i + 'px)',
                '-ms-filter': 'blur(' + i + 'px)',
                'filter': 'blur(' + i + 'px)',
                'filter': 'blur(' + i + 'px)'
            });
        }, 17);
        /*}*/
        $(".login-bg").show();
        $($topLoginBgClass).fadeIn(0);
        $($topLoginBgClass).animate({ marginTop: -200, opacity: 0.98 }, 500);
        $("#login input").val("");
    }


    /**
     * 判断是否已经有弹出框或者注册框
     */
    function _isAnyPopup () {
        return _anyPopup;
    }
    return {
        init               : _init,
        popupLogin         : _popupLogin,
        hidePopupLogin     : _popupLoginHide,
        isAnyPopup         : _isAnyPopup
    };
//});
})();
    