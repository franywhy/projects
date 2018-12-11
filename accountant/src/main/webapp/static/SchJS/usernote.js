
$(".btnRqid").live("click", function () {
    var notesid = $("#hidqaid").val();
    var content = $("#txtNote2").val();
    $.ajax({
        url: "/lib/member/UserNote.ashx",
        type: "POST",
        async: false,
        data: "type=2&notesid=" + notesid + "&content=" + content + "",
        beforeSend: function () {
            $("#loading1").html("数据提交中,请稍候...");
        },
        success: function (result) {
            if (result != "000") {
                window.location.reload();
            }
            else {
                var url = window.location.href;
                window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";
            }
        }

    })

});

$("#update").live("click", function () {
    var notesid = $(this).attr('rel');

    $.ajax({
        url: "/lib/member/UserNote.ashx",
        type: "POST",
        async: false,
        data: "type=1&notesid=" + notesid + "",
        cache: false,
        beforeSend: function () {

        },
        success: function (result) {
            if (result != "") {
                var dataObj = eval("(" + result + ")");
                $("#hidqaid").val(dataObj.NOTESID);
                $("#txtNote2").html(dataObj.CONTENT);
            }
        }

    })
    var a = new Dialog2("修改笔记", "#details1", {
        "btnCancel": function () { a.CloseDialog(); }
    }, false);
    a.ShowDialog();
});


$("#delete").live("click", function () {
    var notesid = $(this).attr('rel');

    $.ajax({
        url: "/lib/member/UserNote.ashx",
        type: "POST",
        async: false,
        data: "type=3&notesid=" + notesid + "",
        cache: false,
        beforeSend: function () {

        },
        success: function (result) {
            if (result != "000") {
                window.location.reload();
            }
            else {
                var url = window.location.href;
                window.location.href = "/login.html?ReturnUrl=" + escape(url) + "";

            }
        }

    })
});