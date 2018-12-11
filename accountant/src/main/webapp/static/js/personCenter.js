$(function () {
    courseSlide();			//课程切换
    personCenterSlide();
});

function personCenterSlide() {
    $(".sc-main-box .sc-main-box-l li").on("click", function () {
        var i = $(this).index();
        var o = $(".sc-main-box-r .main-big-box");
        $(this).addClass("act").siblings().removeClass("act");
        o.eq(i).stop(true, true).fadeIn().siblings().stop(true, true).fadeOut(0);
    });

    $(".user-data-bar .add-mian").on("click", function () {
        $(".pop-2").stop(true, true).fadeIn();
    });
    $(".user-data-bar .add-cou").on("click", function () {
        $(".pop-1").stop(true, true).fadeIn();
    });
    $(".cou-pop .tit i").on("click", function () {
        $(this).parents(".black-bg").stop(true, true).fadeOut();
    });

    $(".user-data-bar .user-data-r .t-ul dd a").on("click", function () {
        var j = $(this).parent().parent().index();
        clickTab(j);
    });
};

//点击明细 切换
function clickTab(that) {
    var i = 1;
    var o = $(".sc-main-box-r .main-big-box");
    var p = $(".sc-main-box .sc-main-box-l li");

    var cont = $(".main-big-box").eq(1);
    var nav = cont.find(".main-bar-nav li");
    var box = cont.find(".main-course-box");


    p.eq(i).addClass("act").siblings().removeClass("act");
    o.eq(i).stop(true, true).fadeIn().siblings().stop(true, true).fadeOut(0);

    nav.eq(that + 2).addClass("act").siblings().removeClass("act");
    box.eq(that + 2).stop(true, true).fadeIn().siblings().stop(true, true).fadeOut(0);
};