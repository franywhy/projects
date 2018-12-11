$(function() {


    $(".next-speet").live("click", function () {
        $(".bar-bgColor").animate({width: "66%"}, 200, function () {
            $(".regigth-seep font.r-2").addClass("active")
            $(".regigth-seep span.r-1").addClass("active")
        });

        $(".page-slide").animate({marginLeft: "-100%"})
    })


    $(".hy-btnSelect").click(function () {
        $(".hy-tab-pop,.pop-bg").fadeIn(200)
    })
    $(".kc-btnSelect").click(function () {
        $(".kc-tab-pop,.pop-bg").fadeIn(200)
    })

    $(".off-popBox").click(function () {
        $(".hy-tab-pop,.pop-bg,.kc-tab-pop").fadeOut(200);


        var html = $(this).parent().find("span.active").clone();

        $(this).parent().parent().find(".select-showCon").html(html)

    });


    function select(c, t) {
        $(c).on("click", function () {
            var s = $(t).length;
            if (s >= 2) {
                if ($(this).hasClass("active")) {
                    $(this).removeClass("active");
                }
                if (s >= 2) {
                    alert("只能选俩项");
                }
                return;
            }
            if ($(this).hasClass("active")) {
                $(this).removeClass("active");
            }
            else {
                $(this).addClass("active")
            }
        })
    }

    select(".hy-tab-pop span", ".hy-tab-pop span.active")
    select(".kc-tab-pop span", ".kc-tab-pop span.active")

    $(".user-xinxin i").click(function () {
        var index = $(this).index() - 1;
        var xx = "xinxin.png"
        var xa = "xinxin_active.png"

        $(".user-xinxin i").find("img").attr("src", "images/" + xa)
        for (var i = 0; i <= index; i++) {
            $(".user-xinxin i").eq(i).find("img").attr("src", "images/" + xx)
        }
    })

    //校验手机号码：必须以数字开头，除数字外，可含有“-”
    function isMobil(s) {
        var patrn = /^1\d{10}$/;
        if (!patrn.exec(s))
            return false;
        return true;
    }

    var canGetCode = true;
    var MAX_S = 60;
    var i = MAX_S;
    var timer;
    if (canGetCode) {
        $("#sendvercode").click(function () {
            if (i != MAX_S) {
                return;
            }
            var phone = $("#phone").val();
            if (phone == "") {
                alert("手机号不能为空");
                return;
            } else if (!isMobil(phone)) {
                alert("请填写正确的手机号码");
                return;
            }
            //发送验证码
            $.ajax({
                url: '/security_teacher_apply',// 跳转到 发送验证码
                data: {
                    "phone": phone
                },
                type: 'post',
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data.code == "1") {
                        $("#sendvercode").removeClass("active");
                        $("#sendvercode").addClass("no_active");

                        setTimeout(function () {
                            $(".code-tip").fadeOut();
                        }, 500);
                        timer = setInterval(function () {
                            $("#sendvercode").text(i + "秒后重新获取");
                            i--;
                            if (i < 0) {
                                clearTimeout(timer);
                                $("#sendvercode").text("重新获取验证码");
                                $("#sendvercode").removeClass("no_active");
                                $("#sendvercode").addClass("active");
                                i = MAX_S;
                            }
                        }, 1000);
                    } else {
                        alert(data.msg);
                    }
                },
                error: function () {
                    // view("异常！");
                    alert("网络异常！");
                }

            });
        });


    }


    function getStart()
    {

        return  $("[src='images/xinxin.png']").length;
    }

    function buidTeacherTips(fid1,fid2,sid1,sid2)
    {


      var  tipjson= [
        {
            "users_industry_tip" : [
                {
                    "industry_tip_id" : fid1
                },
                {
                    "industry_tip_id" : fid2
                }
            ],
            "industry_id" : 1111
        },
        {
            "users_industry_tip" : [
                {
                    "industry_tip_id" : sid1
                },
                {
                    "industry_tip_id" : sid2
                }
            ],
            "industry_id" : 2222
        }
    ]

    if(fid2 ===undefined)
    {
        tipjson[0].users_industry_tip.splice(1,1);
    }

        if(sid2 ===undefined)
        {
            tipjson[1].users_industry_tip.splice(1,1);
        }

   return tipjson;

    }


    $("#submitdata").click(function () {
//
//        String username = (String) item.get("username");
//        String nickname = (String) item.get("nickname");// 昵称和用户名一样啦
//        String city = (String) item.get("city");
//        String school = (String) item.get("school");
//        String star = (String) item.get("star");
//        String isPro = (String) item.get("ispro");
//        String checkcode = (String) item.get("checkcode");

        var json = {};
        json.nickname =$("#nick_name").val();
        json.username =$("#phone").val();
        json.city =$("#city").val();
        json.school =$("#school").val();
        json.checkcode =$("#checkcode").val();
        json.star =  getStart();


        var fid1,fid2,sid1,sid2;

       $("#learning-area").find(".active").each(function(i){
           if(i==0)
           {
               fid1 = $(this).attr("id");
           }
           if(i==1)
           {
               fid2 = $(this).attr("id");
           }
        });
        $("#working-area").find(".active").each(function(i){
            if(i==0)
            {
                sid1 = $(this).attr("id");
            }
            if(i==1)
            {
                sid2 = $(this).attr("id");
            }
        });


        json.insjson = buidTeacherTips(fid1,fid2,sid1,sid2);

        $.ajax({
            url: '/api_teacher_apply',// 跳转到 发送验证码
            data: {
                "json": JSON.stringify(json)
            },
            type: 'post',
            cache: false,
            dataType: 'json',
            success: function (data) {
                if (data.code == "1") {
                    alert("申请成功!您可以直接在会答教师端里面，用对应手机号找回密码，即可登录");
                } else {
                    alert(data.data);
                }
            },
            error: function () {
                // view("异常！");
                alert("网络异常！");
            }

        });


    });


});