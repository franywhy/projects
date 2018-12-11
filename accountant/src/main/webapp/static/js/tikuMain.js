$(function () {
    // ========================头部导航==============================
    var navBtnW = $(".h-main-nav a").width();
    $(".h-main-nav a").each(function (i) { $(this).attr('data-idnex', i) })
    $(".h-main-nav .active").addClass("node-active")
    var backActiveIndex = $(".h-main-nav .node-active").attr("data-idnex");
    $(".h-main-nav .scroll-block").css({ left: navBtnW * backActiveIndex })
    $(".h-main-nav a").hover(function () {
        var index = $(".h-main-nav a").index(this);
        $(".h-main-nav a").removeClass("active");
        $(".h-main-nav .scroll-block").stop().animate({ left: navBtnW * index }, 200)
    }, function () {
        $(".h-main-nav .scroll-block").stop().animate({ left: navBtnW * backActiveIndex }, 200, function () {
            $(".h-main-nav a").siblings(".node-active").addClass("active")
        })
    })

    // =========================== 头部右侧导航 ===================================
    if ($(".h-main-nav a").eq(2).hasClass('active')) {
        $(".sunNav-leftMuen dl dd").eq(3).addClass("active");
        $(".sunNav-leftMuen dl dd").eq(3).stop().animate({ backgroundColor: "#2bb6ea" }, 200);
        $(".sunNav-leftMuen dl dd").eq(3).find('h2').stop().stop().animate({ marginTop: 0 }, 300);
        $(".sunNav-leftMuen dl dd").eq(3).find('i').stop().stop().animate({ marginTop: 0 }, 300);

        $(".sunNav-leftMuen dl dd").hover(function () {
            $(".sunNav-leftMuen dl dd").eq(3).removeClass("active");
            $(".sunNav-leftMuen dl dd").stop().animate({ backgroundColor: "#ffffff" }, 200);
            $(".sunNav-leftMuen dl dd").find('h2').stop().stop().animate({ marginTop: 0 }, 300);
            $(".sunNav-leftMuen dl dd").find('i').stop().stop().animate({ marginTop: 0 }, 300);
            $(this).stop().animate({ backgroundColor: "#2bb6ea" }, 200)
            $(this).find('h2').stop().animate({ marginTop: 10 }, 300)
            $(this).find('i').stop().animate({ marginTop: -5 }, 300)

        }, function () {
            $(".sunNav-leftMuen dl dd").eq(3).addClass("active");
            $(this).stop().animate({ backgroundColor: "#ffffff" }, 200);
            $(this).find('h2').stop().animate({ marginTop: 0 }, 300);
            $(this).find('i').stop().animate({ marginTop: 0 }, 300);
            $(".sunNav-leftMuen dl dd").eq(3).stop().animate({ backgroundColor: "#2bb6ea" }, 200);
            $(".sunNav-leftMuen dl dd").eq(3).find('h2').stop().stop().animate({ marginTop: 0 }, 300);
            $(".sunNav-leftMuen dl dd").eq(3).find('i').stop().stop().animate({ marginTop: 0 }, 300);
        });
    } else {

        $(".sunNav-leftMuen dl dd").hover(function () {
            $(this).stop().animate({ backgroundColor: "#2bb6ea" }, 200)
            $(this).find('h2').stop().animate({ marginTop: 10 }, 300)
            $(this).find('i').stop().animate({ marginTop: -5 }, 300)

        }, function () {
            $(this).stop().animate({ backgroundColor: "#ffffff" }, 200);
            $(this).find('h2').stop().animate({ marginTop: 0 }, 300);
            $(this).find('i').stop().animate({ marginTop: 0 }, 300);
        });

    }
    // =============================== 扫码支付 ===================================
    $(".btn-buy-course").hover(function () {
        $(this).addClass("active")
        $(".slideDonw-buyStyle").stop().slideDown(200)
    }, function () {
        $(".slideDonw-buyStyle").stop().slideUp(200, function () {
            $(".btn-buy-course").removeClass("active")
        })
    })
    $(".left-text i").hover(function () {
        $(".pop-ewm").show()
    }, function () {
        $(".pop-ewm").hide()
    })

    // =============================== 头部幻灯片 ===================================
    var flshDiv = $(".sunNav-middle .top-banner ul li");
    var liLen = flshDiv.length;
    var playIndex = 0;
    var timer;
    $(".top-banner .btn-page span").hover(function () {
        playIndex = $(this).index();
        showPlay()
    })

    $(".top-banner").hover(function () {
        clearInterval(timer);
    }, function () {
        timer = setInterval(function () {
            playIndex++;
            if (playIndex >= liLen) {
                playIndex = 0
            };
            showPlay()
        }, 5000);
    }).trigger('mouseleave');
    function showPlay() {
        flshDiv.eq(playIndex).fadeIn(800).siblings().fadeOut(800);
        $(".top-banner .btn-page span").eq(playIndex).addClass('active').siblings().removeClass('active');
    }
    var saveScueeHtml = $(".userLogin .login-scrccen").clone(); //保存登录成功的HTML
    $(function () {
        document.onkeydown = function (e) {
            var ev = document.all ? window.event : e;
            if (ev.keyCode == 13) {
                var isFocus = $("#txtQueryWord1").is(":focus");
                if (isFocus == true) {
                    var key = $("#txtQueryWord1").val();
                    if (key == '输入课程关键字搜索' || key == '') {
                        return;
                    }
                    else {
                        window.location.href = "/courselist.html?key=" + escape(key) + "";
                        ev.returnValue = false;
                    }
                } else {
                    isFocus = $("#txt_Password").is(":focus");
                    if (isFocus == true) {
                        var getPwd = $("#txt_Password").val();
                        var sMobile = $("#txt_Username").val();
                        var resPwd = /^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{1,220}$/; //密码
                        var resMobile = /^1[3|4|5|8][0-9]\d{4,8}$/; //手机号
                        var resMali = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; //邮箱
                        var url = window.location.href;
                        var timeout = 30;
                        if ($("#cb_Check").attr("class") == "checkboxBtn active") {
                            timeout = 5440;
                        }
                        $.ajax({
                            type: "POST",
                            url: "/zt/lib/loginHandler.ashx",
                            data: {
                                type: "1",
                                name: sMobile,
                                pass: getPwd,
                                timeout: timeout
                            },
                            cache: false,
                            beforeSend: function () {
                                $(".login-btn").removeClass("active").text("正在登录...");
                            },
                            success: function (result) {
                                if (result == '000') {
                                    window.location.reload();
                                }
                                else if (result == '009') {
                                    $("#hidMobile").val(sMobile);
                                    $("#hidPass").val(getPwd);
                                    $(".loginText").text('登录成功');
                                    $(".userLogin .user-login-box").hide();
                                    $(".userLogin").append("<div class='login-scrccen'><h1><i></i>恭喜您，登录成功</h1><p>为了更好的体验和账号安全，建议您绑定手机号，以便我们提供更多的免费优惠服务给您。</p><div class='other-btn'><span class='btn-change-phone'>去绑定手机</span></div></div>");
                                    $(".userLogin .login-scrccen").show();
                                    $(".btn-login").trigger("click");
                                }
                                else {
                                    $(".login-tip").html(result);
                                    $(".login-tip").stop().animate({ top: 0, opacity: 1 }, 150, function () {
                                        setTimeout(function () {
                                            $(".login-tip").stop().animate({ top: -40, opacity: 0 }, 50);
                                            $("#txt_Password").val(""); //清空登录框的密码
                                            $(".login-btn").removeClass("active").text("登录");
                                        }, 2000);
                                    });
                                }
                            }
                        });

                        ev.returnValue = true;
                    }
                    else {
                        if ($("#login .userLogin .pwd input").is(":focus")) {
                            var sMobile = $("#login .userLogin .userName input").val();
                            var sPwd = $("#login .userLogin .pwd input").val();
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
                                    $("#login .pop-login-btn").removeClass("active").text("正在登录...");
                                },
                                success: function (result) {
                                    if (result == '000') {
                                        if (getUrlParam("ReturnUrl") == null) {
                                            window.location.reload();
                                        }
                                        else {
                                            window.location.href = getUrlParam("ReturnUrl");
                                        }
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
                                        $("#login .pop-login-btn").text('立即登录');
                                        $("#login .pop-login-btn").removeClass("active");
                                        $("#login .login-tip").show().stop().animate({ top: 0 }, 200, function () {
                                            setTimeout(function () {// 设置间隔，防止连续点击登录按钮,多次发送ajax请求。
                                                $("#login .pop-login-btn").text('立即登录');
                                                $("#login .login-tip").stop().animate({ top: -56 }, 120).hide();
                                            }, 1500)
                                        })
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    });
    $(".btn-search").click(function () {
        var key = $("#txtQueryWord").val();
        if (key == '输入课程关键字搜索' || key == '') {
            return;
        }
        window.location.href = "/courselist.html?key=" + escape(key) + "";
    })
    $(".search-btn-a").click(function () {
        var key = $("#txtQueryWord1").val();
        if (key == '输入课程关键字搜索' || key == '') {
            return;
        }
        window.location.href = "/courselist.html?key=" + escape(key) + "";
    })
    $(".slideDown-list a ").live("click", function () {
        var key = $(this).attr('title');
        window.location.href = "/courselist.html?key=" + escape(key);
    });
    //搜索展开效果
	var ddD = $(".slideDown-list dl dd");
	var ddLength = ddD.length - 1;
	for (var i = -1; i <= ddLength; i++) {
		ddD.eq(i).css({ marginLeft: i * 150 + 100 })
	}
	$(".search-input input").css({ marginLeft: 100 })
	$(".kd-gn-search").on('click onfocus', function () {
		$(".header .form-search,.bg-logo").fadeIn();
		$(".search-input input").focus();
		$(".kd-gn-list").addClass("active");
		$(".search-input input").stop().animate({ marginLeft: 0 }, 400)
		for (var i = 0; i <= ddLength; i++) {
			ddD.eq(i).stop().animate({ marginLeft: 0 }, i * 100 + 100)
		}
	})
	$(".search-con span.absolute").click(function () {
		$(".header .form-search,.bg-logo").fadeOut();
		$(".kd-gn-list").removeClass("active");
		$(".search-input input").stop().animate({ marginLeft: 100 }, 400)
		for (var i = 0; i <= ddLength; i++) {
			ddD.eq(i).stop().animate({ marginLeft: i * 150 + 100 }, i * 100 + 200)
		}
	})
})