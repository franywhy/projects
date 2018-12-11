$(function () {
//    NTKF_kf("https://looyuoms7811.looyu.com/chat/chat/p.do?_server=0&c=20001784&f=10071012&g=10068628", $(".ly-zixun-course"));
//    createLyScriptSell("https://looyuoms7811.looyu.com/chat/chat/p.do?_server=0&c=20001784&f=10071012&g=10066933", $(".ly-zixun-sell"));
});
//创建课程乐语
function createLyScriptMian() {
      var href = "http://looyuoms7811.looyu.com/chat/chat/p.do?_server=0&c=20001784&f=10068734&g=10068835";
//    btn.on("click", function () {
        if (document.documentElement.clientWidth <= 768) {
            window.location.href = href;
        }
        else {
            window.open(href,"","width=700,height=500");
        }
//    });
}
function createLyScriptWang() {
    var href = "http://looyuoms7811.looyu.com/chat/chat/p.do?_server=0&c=20001784&f=10068290&g=10066933";
//    btn.on("click", function () {
        if (document.documentElement.clientWidth <= 768) {
            window.location.href = href;
        }
        else {
            window.open(href, "", "width=700,height=500");
        }
//    });
}