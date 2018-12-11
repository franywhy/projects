/*


 */
$(function () {

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
    function move() {
        var w = $(window).width();
        var h = $(window).height();
        $(window).mousemove(function (e) {
            if (canMove != false) {
                var x = e.clientX - ox + 250;
                var y = e.clientY - oy + 200;
                //                console.log(e.clientX, e.clientY, x, y)
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
            $(".userRegister .next-btn").css({ marginTop: 0 })
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
//      $.browser.msie = /msie/.test(navigator.userAgent.toLowerCase());
        // 高丽撕模糊 
        $(clickShowPopBtn).live('click', function () {
            if ($(this).text() == "登录") {
                $(".back-btn").click();
            }
//          var i = 0.5;
//          if ($.browser.msie) {//IE不使用模糊效果
//              $(".login-bg").show().animate({ backgroundColor: "#000000", opacity: 0.7 })
//              /*	$("body").css({'overflow-x':'hidden'})
//              var timer = setInterval(function(){
//              i+=1;
//              if(i>=15){clearInterval(timer)}
//              $(".body").css({
//              'filter': 'progid:DXImageTransform.Microsoft.Blur(PixelRadius='+i+', MakeShadow=false)' 
//              });
//              },17);*/
//          } else {
//              $(".body").css({ overflow: "hidden" })
//              var timer = setInterval(function () {
//                  i += 1;
//                  if (i >= 3) { clearInterval(timer) }
//                  $(".body").css({
//                      '-webkit-filter': 'blur(' + i + 'px)',
//                      '-moz-filter': 'blur(' + i + 'px)',
//                      '-ms-filter': 'blur(' + i + 'px)',
//                      'filter': 'blur(' + i + 'px)',
//                      'filter': 'blur(' + i + 'px)'
//                  });
//              }, 17);
//          }
            $(".login-bg").show();
            $(showConClass).fadeIn(0);
            $(showConClass).animate({ marginTop: -200, opacity: 0.98 }, 500);
            $("#login input").val("");
        });

        $(clickoffPopBtn).click(function () {
            var i = 3;
            if ($.browser.msie) {
                /*		var timer = setInterval(function(){
                i-=1;
                if(i<=0){clearInterval(timer);i=0;}
                $(".body").css({
                'filter': 'progid:DXImageTransform.Microsoft.Blur(PixelRadius='+i+', MakeShadow=false)' 
                });
                },17);*/

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
                    if (i <= 0) { clearInterval(timer); $(".body").removeAttr("style").css({ overflow: "hidden" }) }
                }, 17);

            }
            $(showConClass).animate({ marginTop: -130, opacity: 0 }, 500)
            $(showConClass).fadeOut(400, function () {
                $(".login-scrccen,.bind-phone,.bind-phone-info").hide().remove()
                $(".user-login-box").show();
                $(".loginText").text("登录");
                $(".pop-login-btn").text("登录");
                $("#login input").val("");
            });
            $(".login-bg").fadeOut(400);
        });

    }
    //有3个参数 ，showPop([0 = 点击弹出登录层的按钮,1 = 弹出层,2=关闭弹出层的按钮])
    // showPop(".get-VIP a",".buy-VIP-popBox",".off-pop-btn");
    showPop(".btn-login", ".loginBox", ".btn-right");
    showPop(".btn-reget", ".loginBox", ".btn-right");


    // 切换 
    $(".btn-reget").click(function () {
        $(".go-register").click();
    });

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
    var zMaliYZ = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; //邮箱正则
    var phoneYZ = /^1[3|4|5|8][0-9]\d{4,8}$/; //手机正则
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
        //        console.log(userOK)
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


    $("#login .bitme-btn.active").live('click', function () {
        if (userOK && pwdOK) { //当条件全部成立
            var sMobile = $(phone).val();
            var sPwd = $(pword).val();
            var url = window.location.href;
            var timeout = 120;
            $.ajax({
                type: "POST",
                url: "/zt/lib/loginHandler.ashx",
                data: {
                    type: "1",
                    name: sMobile,
                    pass: sPwd,
                    timeout: timeout
                },
                cache: false,
                beforeSend: function () {
                    loginBtn.removeClass("active").text("正在登录...");
                },
                success: function (result) {
                    if (result == '000') {
                        window.location.reload();
                    }
                    else if (result == '009') {
                        setTimeout(function () {//模拟网速缓冲 实际情况删除 setTimeout 便可
                            $(".loginText").text('登录成功');
                            $(".userLogin .user-login-box").hide();
                            $(".userLogin").append(saveScueeHtml);
                            $(".userLogin .login-scrccen").show();
                        }, 2000)
                    }
                    else {
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
                    }
                }
            });
        }
    });
    var canGetCode1 = true;
    $(".getCode-btn").live('click', function () {
        var sMobile = $("#sMobile").val();
        if (sMobile.match(/^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/) && canGetCode1 == true) {
            canGetCode1 = false;
            $.ajax({
                type: "POST",
                url: "/zt/lib/loginHandler.ashx",
                data: {
                    type: "0",
                    name: sMobile
                },
                cache: false,
                success: function (result) {
                    alert(result);
                }
            });
            timera = setInterval(function () {
                k--;
                $(".getCode-btn").text(k + "秒后重新获取");
                if (k < 0) {
                    clearTimeout(timera);
                    $(".getCode-btn").removeClass("active");
                    $(".getCode-btn").text("获取验证码");
                    canGetCode1 = true;
                    k = 60;
                }
            }, 1000);
        }
        else {
            if (canGetCode1 == true) {
                alert("手机号码格式不正确");
            }
        }
    });
    $(".next-btn").live('click', function () {
        var sCode = $(pCode).val();
        var sMobile = $("#login .userRegister .userName input").val();
        $.ajax({
            type: "POST",
            url: "/zt/lib/loginHandler.ashx",
            data: {
                type: "3",
                name: sMobile,
                code: sCode
            },
            cache: false,
            beforeSend: function () {
                //                    $("#register").html("正在注册...");
            },
            success: function (result) {

                if (result == '000') {
                    $(".next-btn").text("提交中...");
                    $(".next-btn").removeClass("active");
                    $(".re-ul").animate({ marginLeft: -500 }, function () {
                        $(".next-btn").removeClass('active').text("立即注册");
                        $(".userRegister .select").hide();
                        $(".next-btn").animate({ marginTop: 28 }, 800);
                        $(".back-btn").hide();
                        $(".registerText").text("最后一步");
                    })
                }
                else {
                    alert(result);
                }
            }
        });
        // $(".next-btn").addClass("active");

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
            if (phoneYZ.test($(this).val())) {
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


    $(".now-bind-phone").live('click', function () {
        var mobile = $("#sMobile").val();
        var sCode = $("#bindCode").val();
        var sMobile = $(phone).val();
        var sPwd = $(pword).val();
        if (sMobile == "") {
            sMobile = $("#hidMobile").val();
        }
        if (sPwd == "") {
            sPwd = $("#hidPass").val();
        }
        var url = window.location.href;
        var timeout = 120;
        $.ajax({
            type: "POST",
            url: "/zt/lib/loginHandler.ashx",
            data: {
                type: "30",
                name: sMobile,
                mobile: mobile,
                code: sCode
            },
            cache: false,
            beforeSend: function () {
                $(".now-bind-phone").removeClass("active").html("正在绑定...");
            },
            success: function (result) {
                if (result == '000') {
                    window.location.href = url;
                }
                else {
                    $(".now-bind-phone").addClass("active").html("立即绑定手机");
                    alert(result);
                }
            }
        });
    })

    /*====================================
    ======================================
    注册验证
    ======================================
    ====================================*/
    function phoneCodeR() {
        var sMobile = $(phoneR).val();
        if ($(phoneR).val().length > 0) {
            $(phoneR).css({ borderColor: "#d8d8d8" });
            $(phoneR).siblings(".icon-pd").removeClass("active").hide();
            phoneOK = true;
        } else {
            $(phoneR).css({ borderColor: "#ff0000" });
            phoneOK = false;
        }
        regisOK();

    }


    var codeLodin = false;
    var lodinCode = true;
    // 
    function coCode() {
        var sCode = $(pCode).val();
        var zzNun = /^[0-9]*$/;
        if (zzNun.test(sCode) && sCode.length == 6) {
            $(".userRegister .userCode").css({ borderColor: "#d8d8d8" });
            codeOK = true; // 验证码进行ajax验证  ，成功则为 true ，失败则 return；
        } else {
            $("#login .userRegister .userCode").css({ borderColor: "#ff0000" });
            codeOK = false;
        }
        regisOK();

    }
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
        if (r_sPwd.length > 5 && r_sPwd === u_sPwd) {
            var sMobile = $("#login .userRegister .userName input").val();
            var url = window.location.href;
            var timeout = 120;
            $.ajax({
                type: "POST",
                url: "/zt/lib/loginHandler.ashx",
                data: {
                    type: "33",
                    name: sMobile,
                    pass: r_sPwd
                },
                cache: false,
                beforeSend: function () {
                    $(".next-btn").text("正在注册...");
                    //                    $("#register").html("正在注册...");
                },
                success: function (result) {

                    if (result == '000') {
                        window.location.href = url;
                    }
                    else {
                        $(".next-btn").text("立即注册");
                        alert(result);
                    }
                }
            });

        } else {
            $(rpwd).css({ borderColor: "#ff0000" });
            $(rpwd).siblings(".icon-pd").show().addClass("active");
            $(upwd).siblings(".icon-pd").show().addClass("active");
            rUPwdOK = false;
        }
        pwdShow();
    }

    // ====================================================================

    // 手机验证
    $("#login .userRegister .userName input").focus(function () {
        $(this).keyup(function (event) {
            phoneCodeR();
            $(".next-btn").addClass("active");
        })
    });

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


    var timer;
    var canDo = true;
    function pwdShow() {
        if (rUPwdOK) {
            $(".next-btn").addClass("active");
            $(".baseInput input").blur();
            $(".re-ul").animate({ marginLeft: -1000 }, 300, function () {
                setTimeout(function () {

                    $(".btn-right").click();
                }, 2000)
            })
            $(".next-btn").removeClass("active").fadeOut();
        } else {
            $(".next-btn").removeClass("active");
        }
    }




    // ====================================================================
    function regisOK() {
        if (phoneOK && codeOK) {
            $(".next-btn").addClass("active");
            //            if (!canDo) return;
            //            canDo = false;
            //            // $(".next-btn").addClass("active");
            //            timer = setTimeout(function () {//模拟网速缓冲
            //                $(".next-btn").text("提交中...");
            //                $(".next-btn").removeClass("active");
            //            }, 3500);
            //            //AJAX 成功
            //            timer = setTimeout(function () {
            //                $(".re-ul").animate({ marginLeft: -500 }, function () {
            //                    $(".next-btn").removeClass('active').text("立即注册");
            //                    $(".userRegister .select").hide();
            //                    $(".next-btn").animate({ marginTop: 28 }, 800);
            //                    $(".back-btn").hide();
            //                    $(".registerText").text("最后一步");
            //                    canDo = true
            //                })
            //            }, 5500);
        } else {//AJAX 失败
            //            $(".next-btn").removeClass("active");
        }
        if (codeOK) {
            $(".next-btn").addClass("active");
        } else {
            //            $(".next-btn").removeClass("active");
        }
    }






    // 获取验证码计时器 
    var canIn = true;
    var canGetCode = true;
    var k = 60;
    var timera;

    $(".getCode").click(function () {
        var sMobile = $(phoneR).val();
        var code = $("#userCode1").val();
        var telReg = !!sMobile.match(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/); //前端手机的验证
        regisOK();
        if (!phoneOK || telReg == false) {
            $(".userRegister .userName").addClass("animated" + " shake");
            $(phoneR).css({ borderColor: "#ff0000" });
            $(phoneR).siblings(".icon-pd").show().addClass("active");
            setTimeout(function () {
                $(".userRegister .userName").removeClass("animated" + " shake");
            }, 1000);
            return;
        }
        $(".getCode").addClass("active");
        if (canGetCode) {
            $.ajax({
                type: "POST",
                url: "/zt/lib/loginHandler.ashx",
                data: {
                    type: "0",
                    code: code,
                    name: sMobile
                },
                cache: false,
                success: function (result) {
                    alert(result);
                }
            });
            timera = setInterval(function () {
                k--;
                $(".getCode").text(k + "秒后重新获取");
                if (k < 0) {
                    clearTimeout(timera);
                    $(".getCode").removeClass("active");
                    $(".getCode").text("获取验证码");
                    canGetCode = true;
                    k = 30;
                    codeOK = false;
                }
            }, 1000);
        }
        canGetCode = false;

    });


})