/*------------------------------邀请链接-------------------*/
$(function () {
    $("#btnInvite").on("click", function () {

        var a = new Dialog2("把云网校分享给好友", "#details1", {
            "btnClose": function () { a.CloseDialog(); }
        }, false);
        a.ShowDialog();
    });

    $("#copy").on("click", function () {

        var e = document.getElementById("invitelink"); 
        e.select(); //选择对象 
        document.execCommand("Copy"); //执行浏览器复制命令
    });
});

