$(function () {
    $("body").css({
        "overflow": "hidden",
        "width": "auto",
        "height": "auto",
        "margin": 0,
        "position": "absolute",
        "display":"block",
        "top": 0,
        "bottom": 0,
        "left": 0,
        "right": 0
    });

    var layoutUi = function (classUi) {
        var _this = this;

        var w = $("body").width();
        var h = $("body").height();
        _this.north = classUi.ui__north || $(".ui-layout-north");
        _this.south = classUi.ui__south || $(".ui-layout-south");
        _this.west = classUi.ui__west || $(".ui-layout-west");
        _this.east = classUi.ui__east || $(".ui-layout-east");
        _this.center = classUi.ui__center || $(".ui-layout-center");


        // 上
        _this.nSizeH = classUi.north__size || 132;
        _this.nSizeW = w;

        //右
        _this.eSizeH = h - _this.nSizeH;
        _this.eSizeW = classUi.east__size || 340;

        //下
        _this.sSizeH = classUi.south__size || 80;
        _this.sSizeW = w - _this.eSizeW;


        //中间
        _this.cSizeW = classUi.center__size || w - _this.eSizeW;
        _this.cSizeH = classUi.center__size || h - _this.nSizeH - _this.sSizeH;


        // 北的高度
        _this.north.stop().animate({
            width: _this.nSizeW,
            height: _this.nSizeH,
            left: 0,
            top: 0/*,
			position:'absolute'*/
        }, 0)

        _this.south.stop().animate({
            width: _this.sSizeW,
            height: _this.sSizeH,
            left: 0,
            top: h - _this.sSizeH/*,
			position:'absolute'*/
        }, 0)

        _this.east.stop().animate({
            width: _this.eSizeW,
            height: h - _this.nSizeH,
            right: 0,
            top: _this.nSizeH/*,
			position:'absolute'*/
        }, 0)

        _this.center.stop().animate({
            width: _this.cSizeW - 50,
            height: _this.cSizeH,
            left: 50,
            top: _this.nSizeH
        }, 0);
        //        $("#playerswfObj").height(h - 212);
        $("#hidVIDEO").val(h - 212);

        function a() {

            // 上
            _this.nSizeH = classUi.north__size || 132;
            _this.nSizeW = w;

            //右
            _this.eSizeH = h - _this.nSizeH;
            _this.eSizeW = classUi.east__size || 340;

            //下
            _this.sSizeH = classUi.south__size || 80;
            _this.sSizeW = w - _this.eSizeW;


            //中间
            _this.cSizeW = classUi.center__size || w - _this.eSizeW;
            _this.cSizeH = classUi.center__size || h - _this.nSizeH - _this.sSizeH;



            // 北的高度
            _this.north.stop().animate({
                width: _this.nSizeW,
                height: _this.nSizeH,
                left: 0,
                top: 0/*,
				position:'absolute'*/
            }, 100)

            _this.south.stop().animate({
                width: _this.sSizeW,
                height: _this.sSizeH,
                left: 0,
                top: h - _this.sSizeH/*,
				position:'absolute'*/
            }, 100)

            _this.east.stop().animate({
                width: _this.eSizeW,
                height: h - _this.nSizeH,
                right: 0,
                top: _this.nSizeH/*,
				position:'absolute'*/
            }, 100)

            _this.center.stop().animate({
                width: _this.cSizeW - 50,
                height: _this.cSizeH,
                left: 50,
                top: _this.nSizeH
            }, 100);
        }
        $(window).resize(function () {
            w = $(this).width();
            h = $(this).height();
            if (w <= 1226) {
                w = 1226;
            } else {
                w = $(this).width();
            }


            a()
            var name = "cc_" + $("#h_vid").val();
//            $("embed[name='" + name + "']").height(h - 212);
            $("embed").height(h - 212);
            $("#" + name).height(h - 212);
        })
    }




    var a = new layoutUi({
        north__size: "",
        east__size: "360"
    })


    var openColse = true;
    $(".close-slide-bar span").click(function () {

        var sw = $(".ui-layout-center").width();


        if (openColse) {
            $(this).text('<<')
            $(".ui-layout-center").animate({ width: sw + 340 }, 200);
            $(".ui-layout-east").animate({ right: -340 }, 200, function () {
                layoutUi({ east__size: "20" });
                openColse = false;
            });
            return;
        }
        if (!openColse) {
            $(this).text('>>')
            $(".ui-layout-center").animate({ width: sw - 340 }, 200);
            layoutUi({ east__size: "360" });
            $(".ui-layout-east").animate({ right: 0 }, 200, function () {
                openColse = true;
            });
            return;

        }
    })

    $(".east-tab-con .tab-meun span").click(function () {
        var _index = $(this).index();
        $(this).addClass("active").siblings().removeClass("active");
        $(".east-tab-con .tab-con .show-con").eq(_index).show().siblings().hide();

    })

    /*
    var demo = function(m){
    console.log(m);
    console.log(m.a);
    }

    var mm={
    a:1,
    b:2
    };
    var q = new demo(mm);
    console.log(mm.a)
    // layoutUi()


    function demo2(a,b){
    this.a = a;
    this.b = b;


    return this;
    }

    var d2 = demo2(1,2);
    console.log("xxxxxxxxxx"+d2.a);*/



    var pfText = ['不满意', '一般般', '满意', '较满意', '非常满意'];
    var pfColor = ['不满意', '一般般', '满意', '较满意', '非常满意'];
    var sorce = 0;
    function hor(className) {
        $(className).find("ul .xinxin-icon").hover(function () {
            var index = $(this).index();
            for (var i = 0; i < index; i++) {
                $(className).find("ul .xinxin-icon").eq(i).addClass("hover");
            }
        }, function () {
            $(className).find("ul .xinxin-icon").removeClass("hover")
        });
        $(className + " " + "ul").hover(function () {
            $(className).find("ul .pf-show-end").hide()
        }, function () {
            $(className).find("ul .pf-show-end").show();
        })
        $(className + " " + "ul .xinxin-icon").click(function () {
            var index = $(this).index();
            $(className).find("ul .pf-show-end").css({
                width: 30 * index
            });
            $(className).find(".xinxin-pf-text").css({ color: '#2bb6ea' }).text(pfText[index - 1]);
            sorce = index;
        });
    }
    hor('.xinxin-pf');
    // 
    $(".course-comment").click(function () {
        $(".popBase-bg").show().animate({ opacity: 0.8 });
        $(".pop-course-comment").fadeIn()
    })
    $(".off-popComment-btn").click(function () {
        $(".popBase-bg").animate({ opacity: 0 }, function () {
            $(this).hide()
        });
        $(".pop-course-comment").fadeOut()
    })

    //提交事件
    $("#btnSubmit").on("click", function () {
        var score_1 = sorce;
        var msg_1 = $("#txtCommunion").val().toString();
        var pid_1 = $(this).attr("rel");
        if (msg_1 == null || msg_1 == undefined || $.trim(msg_1) == "" || $.trim(msg_1).length < 15) {
            alert("内容不能少于15个字");
            return;
        }
        $.ajax({
            type: "POST",
            url: "/lib/product/communion.aspx",
            data: { score: score_1, msg: msg_1, pid: pid_1 },
            dataType: "text",
            beforeSend: function () {
                $("#btnSubmit").html("正在提交...");
            },
            success: function (data) {
                if (data.indexOf("成功") != -1) {
                    $(".popBase-bg").animate({ opacity: 0 }, function () {
                        $(this).hide()
                    });
                    $(".pop-course-comment").fadeOut();
                } else {
                    alert("提交失败"); return false;
                }
            },
            complete: function () {
                $("#btnSubmit").html("提交");
            }
        });
    });

    // 
    var a = 0;
    var ba = 0;
    var aa = $(".scrollBox").height()
    var tableLen = $(".ui-layout-east .course-list table").length;
    var ddLen = $("#scrollCon-0 dl dd").length;


    $(".ui-layout-east .course-list table").each(function (i) {
        a += $(this).height();
        if (tableLen - 1 == i) {
            $(".ui-layout-east  .course-list").css({ height: a + 50 })
        }
    })
    $("#scrollCon-0 dl dd").each(function (q) {
        var cc = $(this).height();
        ba += cc;
        if (ddLen - 1 == q) {
            $(".ui-layout-east  #scrollCon-0").css({ height: ba })
        }
    })


    $(".select-gou span").click(function () {
        if ($(this).hasClass("active")) {
            $(this).removeClass("active")
            return;
        }
        $(this).addClass("active").siblings("span").removeClass("active");
        $(this).attr('onselectstart', "return false")
    })



})
